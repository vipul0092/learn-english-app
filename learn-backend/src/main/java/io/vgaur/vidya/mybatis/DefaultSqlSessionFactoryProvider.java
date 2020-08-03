package io.vgaur.vidya.mybatis;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.setup.Environment;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.vgaur.vidya.Constants.ENVIRONMENT;

/**
 * This is the base SqlSessionFactoryProvider class to manage SQL session initialization for all environments specified
 * in the configuration on startup. It also manages new session creation during runtime for new environments not
 * available in the existing session factories collection.
 *
 * @author Brent Ryan, Nikhil Bhagwat, zhuang
 */
public final class DefaultSqlSessionFactoryProvider {

    private final SqlSessionFactory sessionFactory;
    private final DataSourceFactory dataSourceFactory;
    private final Environment dropwizardEnvironment;
    private final String applicationName;
    private final List<Class<?>> typeHandlers;
    private final List<Class<?>> sqlMappers;
    private final Map<Class<?>, Class<?>> typeClassToTypeHandlerClassMap;
    private final Map<String, Class<?>> typeToAliasClassMap;
    private final ObjectFactory objectFactory;
    private final Map<String, Object> mybatisConfigurationSettings;

    /**
     * Create a new provider instance
     */
    private DefaultSqlSessionFactoryProvider(Environment dropwizardEnvironment,
                                             String applicationName,
                                             DataSourceFactory dataSourceFactory,
                                             List<Class<?>> typeHandlers,
                                             List<Class<?>> sqlMappers,
                                             Map<Class<?>, Class<?>> typeClassToTypeHandlerClassMap,
                                             Map<String, Class<?>> typeToAliasClassMap,
                                             ObjectFactory objectFactory,
                                             Map<String, Object> mybatisConfigurationSettingsMap) {
        this.dropwizardEnvironment = dropwizardEnvironment;
        this.applicationName = applicationName;
        this.dataSourceFactory = dataSourceFactory;
        this.typeHandlers = typeHandlers;
        this.sqlMappers = sqlMappers;
        this.typeClassToTypeHandlerClassMap = typeClassToTypeHandlerClassMap;
        this.typeToAliasClassMap = typeToAliasClassMap;
        this.objectFactory = objectFactory;
        this.mybatisConfigurationSettings = mybatisConfigurationSettingsMap;
        sessionFactory = buildSessionFactory(dataSourceFactory, ENVIRONMENT);
    }

    /**
     * A simple builder allowing us to customize the underlying session factory provider
     */
    public static class Builder {

        private final List<Class<?>> typeHandlers = new ArrayList<>();
        private final List<Class<?>> sqlMappers = new ArrayList<>();
        private final Map<Class<?>, Class<?>> typeClassToTypeHandlerClassMap = new HashMap<>();
        private final Map<String, Class<?>> typeToAliasClassMap = new HashMap<>();
        private final DataSourceFactory dataSourceFactory;
        private final Environment dropwizardEnvironment;
        private final String applicationName;
        private ObjectFactory objectFactory;
        private final Map<String, Object> mybatisConfigurationSettingsMap = new HashMap<>();

        /**
         * A new Builder
         */
        public Builder(Environment dropwizardEnvironment, String applicationName,
                       DataSourceFactory dataSourceFactory) {
            this.dropwizardEnvironment = dropwizardEnvironment;
            this.applicationName = applicationName;
            this.dataSourceFactory = dataSourceFactory;
        }

        /**
         * Register a new type handler to be used with this sql session provider
         *
         * @param typeHandler
         * @return
         */
        public Builder register(Class<?> typeHandler) {
            typeHandlers.add(typeHandler);
            return this;
        }

        /**
         * Register a new type and type handler association to be used with this sql session provider
         *
         * @param javaTypeClass
         * @param typeHandlerClass
         * @return
         */
        public Builder register(Class<?> javaTypeClass, Class<?> typeHandlerClass) {
            typeClassToTypeHandlerClassMap.put(javaTypeClass, typeHandlerClass);
            return this;
        }

        /**
         * Register a new alias to be used with this sql session provider
         *
         * @param alias
         * @param value
         * @return
         */
        public Builder registerAlias(String alias, Class<?> value) {
            typeToAliasClassMap.put(alias, value);
            return this;
        }

        /**
         * Register a new sql mapper to be used with this sql session provider
         *
         * @param sqlMapper
         * @return
         */
        public Builder addMapper(Class<?> sqlMapper) {
            sqlMappers.add(sqlMapper);
            return this;
        }

        /**
         * Add an object factory to the builder
         *
         * @param factory
         * @return
         */
        public Builder objectFactory(ObjectFactory factory) {
            this.objectFactory = factory;
            return this;
        }

        /**
         * Add a new MyBatis Configuration Setting.
         *
         * @param configName
         * @param configSettingObject
         * @return
         */
        public Builder addConfigurationSettings(String configName, Object configSettingObject) {
            this.mybatisConfigurationSettingsMap.put(configName, configSettingObject);
            return this;
        }

        /**
         * Create a new SqlSessionFactoryProvider based on the attributes that have been added to this builder
         *
         * @return a new instance of SqlSessionFactoryProvider
         */
        public DefaultSqlSessionFactoryProvider build() {
            return new DefaultSqlSessionFactoryProvider(dropwizardEnvironment,
                    applicationName,
                    dataSourceFactory,
                    typeHandlers,
                    sqlMappers,
                    typeClassToTypeHandlerClassMap,
                    typeToAliasClassMap,
                    objectFactory,
                    mybatisConfigurationSettingsMap);
        }

    }

    /**
     * Build a new sql session factory.  This method is NOT threadsafe so this internal class must take care to
     * synchronize on sessionFactories.
     */
    private SqlSessionFactory buildSessionFactory(DataSourceFactory dataSource,
                                                  String environmentName) {
        String dataSourceName = String.format("%s-%s-sql", applicationName, environmentName);

        ManagedDataSource ds = dataSource.build(dropwizardEnvironment.metrics(), dataSourceName);
        SqlSessionFactory sessionFactoryObj = new MyBatisFactory()
                .build(dropwizardEnvironment, dataSource, ds, dataSourceName);

        for (Map.Entry<Class<?>, Class<?>> typeClassToTypeHandlerClassEntry :
                typeClassToTypeHandlerClassMap.entrySet()) {
            sessionFactoryObj.getConfiguration().getTypeHandlerRegistry().register(
                    typeClassToTypeHandlerClassEntry.getKey(),
                    typeClassToTypeHandlerClassEntry.getValue());
        }

        for (Map.Entry<String, Class<?>> typeToAliasClassEntry :
                typeToAliasClassMap.entrySet()) {
            sessionFactoryObj.getConfiguration().getTypeAliasRegistry().registerAlias(
                    typeToAliasClassEntry.getKey(),
                    typeToAliasClassEntry.getValue());
        }

        for (Class<?> typeHandler : typeHandlers) {
            sessionFactoryObj.getConfiguration().getTypeHandlerRegistry().register(typeHandler);
        }

        for (Class<?> sqlMapper : sqlMappers) {
            sessionFactoryObj.getConfiguration().addMapper(sqlMapper);
        }

        if (objectFactory != null) {
            sessionFactoryObj.getConfiguration().setObjectFactory(objectFactory);
        }

        mybatisConfigurationSettings.forEach((settingName, configSettingObject) -> {
            try {
                Field field = sessionFactoryObj.getConfiguration().getClass().getDeclaredField(settingName);
                field.setAccessible(true);
                field.set(sessionFactoryObj.getConfiguration(), configSettingObject);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        return sessionFactoryObj;
    }

    /**
     * Get SQL session by environment name, create a new session if requested environment is not found
     */
    public SqlSessionFactory getSqlSessionFactory() {
        return sessionFactory;
    }

}

