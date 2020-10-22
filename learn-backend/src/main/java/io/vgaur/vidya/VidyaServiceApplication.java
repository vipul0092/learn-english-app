package io.vgaur.vidya;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.PolymorphicAuthDynamicFeature;
import io.dropwizard.auth.PolymorphicAuthValueFactoryProvider;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.redis.RedisClientBundle;
import io.dropwizard.redis.RedisClientFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.vgaur.vidya.auth.ApiKeyAuthenticator;
import io.vgaur.vidya.auth.BearerAuthenticator;
import io.vgaur.vidya.auth.RoleAuthorizer;
import io.vgaur.vidya.config.VidyaServiceConfiguration;
import io.vgaur.vidya.dao.AuthDao;
import io.vgaur.vidya.dao.StudentDao;
import io.vgaur.vidya.dao.TeacherDao;
import io.vgaur.vidya.dao.mappers.AuthMapper;
import io.vgaur.vidya.dao.mappers.StudentsMapper;
import io.vgaur.vidya.dao.mappers.TeachersMapper;
import io.vgaur.vidya.exceptions.UncheckedExecutionExceptionMapper;
import io.vgaur.vidya.models.auth.ApiKeyContext;
import io.vgaur.vidya.models.auth.UserContext;
import io.vgaur.vidya.models.serialization.VidyaInternalModule;
import io.vgaur.vidya.mybatis.DefaultSqlSessionFactoryProvider;
import io.vgaur.vidya.mybatis.UUIDObjectTypeHandler;
import io.vgaur.vidya.resources.AuthResource;
import io.vgaur.vidya.resources.CacheResource;
import io.vgaur.vidya.resources.StudentResource;
import io.vgaur.vidya.resources.TeachersResource;
import io.vgaur.vidya.services.AuthService;
import io.vgaur.vidya.services.AuthTokenStore;
import io.vgaur.vidya.services.StudentService;
import io.vgaur.vidya.services.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class VidyaServiceApplication extends Application<VidyaServiceConfiguration> {

    private static final Logger LOG = LoggerFactory.getLogger(VidyaServiceApplication.class);

    // Make sure this is false to run the service locally, to prevent initialization errors
    // Because the Elasticache redis instance is not accessible from local machine
    private static final boolean USE_DISTRIBUTED_CACHE = false;

    public static void main(final String[] args) throws Exception {
        new VidyaServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "Learning App Backend Service";
    }

    private final RedisClientBundle<String, String, VidyaServiceConfiguration> redis = new RedisClientBundle<>() {
        @Override
        public RedisClientFactory<String, String> getRedisClientFactory(VidyaServiceConfiguration config) {
            return config.getRedisClientFactory();
        }
    };

    @Override
    public void initialize(final Bootstrap<VidyaServiceConfiguration> bootstrap) {
        // Setup Liquibase Migrations
        bootstrap.addBundle(new MigrationsBundle<>() {
            @Override
            public DataSourceFactory getDataSourceFactory(VidyaServiceConfiguration configuration) {
                return configuration.getDatabase();
            }
        });
        // Redis client initialization
        if (USE_DISTRIBUTED_CACHE) {
            LOG.info("Adding Redis bundle...");
            bootstrap.addBundle(redis);
        }
    }

    @Override
    public void run(final VidyaServiceConfiguration configuration,
                    final Environment environment) {
        environment.jersey().register(UncheckedExecutionExceptionMapper.class);
        environment.jersey().register(JsonProcessingExceptionMapper.class);

        environment.getObjectMapper().registerModule(new VidyaInternalModule());
        environment.getObjectMapper().registerModule(new JavaTimeModule());

        var sqlSessionFactoryProvider = new DefaultSqlSessionFactoryProvider
                .Builder(environment, getName(), configuration.getDatabase())
                .register(UUID.class, UUIDObjectTypeHandler.class)
                .addMapper(TeachersMapper.class)
                .addMapper(StudentsMapper.class)
                .addMapper(AuthMapper.class)
                .build();

        var teachersDao = new TeacherDao(sqlSessionFactoryProvider);
        var studentsDao = new StudentDao(sqlSessionFactoryProvider);
        var tokensDao = new AuthDao(sqlSessionFactoryProvider);

        var teacherService = new TeacherService(teachersDao);
        var studentService = new StudentService(studentsDao);
        var authTokenStore = new AuthTokenStore(redis, configuration.getTokenTTL(),
                environment.getObjectMapper(), USE_DISTRIBUTED_CACHE);
        var authService = new AuthService(tokensDao, authTokenStore, studentService, teacherService);

        environment.jersey().register(new AuthResource(authService));
        environment.jersey().register(new TeachersResource(teacherService, studentService));
        environment.jersey().register(new StudentResource(studentService));
        environment.jersey().register(setupCacheResource(authService, studentService, teacherService));

        registerAuth(environment, authService);
    }

    private static CacheResource setupCacheResource(AuthService authService, StudentService studentService,
                                                    TeacherService teacherService) {
        var cacheMap = ImmutableMap.<CacheResource.CacheType, Runnable>builder();
        cacheMap.put(CacheResource.CacheType.APIKEYS, authService.getApiKeysCacheInvalidator());
        cacheMap.put(CacheResource.CacheType.STUDENTS, studentService.getStudentCachesInvalidator());
        cacheMap.put(CacheResource.CacheType.TEACHERS, teacherService.getTeachersCacheInvalidator());

        return new CacheResource(cacheMap.build());
    }

    private static void registerAuth(Environment environment, AuthService authService) {
        final AuthFilter<String, UserContext> bearerAuthFilter
                = new OAuthCredentialAuthFilter.Builder<UserContext>()
                .setAuthenticator(new BearerAuthenticator(authService))
                .setPrefix("bearer")
                .buildAuthFilter();
        final AuthFilter<String, ApiKeyContext> apiKeyAuthFilter
                = new OAuthCredentialAuthFilter.Builder<ApiKeyContext>()
                .setAuthenticator(new ApiKeyAuthenticator(authService))
                .setAuthorizer(new RoleAuthorizer<>())
                .setPrefix("api_key")
                .buildAuthFilter();

        final var dynamicFeature = new PolymorphicAuthDynamicFeature<>(
                ImmutableMap.of(
                        UserContext.class, bearerAuthFilter,
                        ApiKeyContext.class, apiKeyAuthFilter));
        final var binder = new PolymorphicAuthValueFactoryProvider.Binder<>(
                ImmutableSet.of(UserContext.class, ApiKeyContext.class));

        environment.jersey().register(dynamicFeature);
        environment.jersey().register(binder);
    }

}
