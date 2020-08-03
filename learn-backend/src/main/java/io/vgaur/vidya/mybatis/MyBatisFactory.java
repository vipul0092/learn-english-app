package io.vgaur.vidya.mybatis;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.setup.Environment;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * A factor for creating instances of MyBatis
 *
 * @author bryan
 */
public class MyBatisFactory {
    /**
     * A string to identify this instance within ibatis framework. Only here because API requires it.
     */
    private static final String ENV_NAME = "mybatis";


    /**
     * Create an instance of MyBatis.
     *
     * @param environment The dropwizard environment
     * @param config A Mybatis config object
     * @param name The name of this mybatis factory used for metrics
     * @return An instance of MyBatis.
     * @throws ClassNotFoundException
     */
    public final SqlSessionFactory build(Environment environment,
                                         MyBatisConfiguration config,
                                         String name) throws ClassNotFoundException {
        final ManagedDataSource dataSource = config.getConfig().build(environment.metrics(), name);
        return build(environment, config, dataSource, name);
    }

    /**
     * Create an instance of MyBatis.
     *
     * @param environment The dropwizard environment
     * @param config A Mybatis config object
     * @param dataSource
     * @param name The name of this mybatis factory used for metrics
     * @return An instance of MyBatis.
     */
    public final SqlSessionFactory build(Environment environment,
                                         MyBatisConfiguration config,
                                         ManagedDataSource dataSource,
                                         String name) {

        SqlSessionFactory sessionFactory = null;

        // Try to use the mybatis configuration file if it is specified and exists.
        try (InputStream inputStream = Resources.getResourceAsStream(config.getConfigFile())) {
            sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException ioe) {
            // Build session factory from configuration values given in the dropwizard config.
            TransactionFactory transactionFactory = new JdbcTransactionFactory();
            org.apache.ibatis.mapping.Environment myBatisEnvironment =
                    new org.apache.ibatis.mapping.Environment(ENV_NAME, transactionFactory, dataSource);
            Configuration mybatisConfiguration = new Configuration(myBatisEnvironment);
            sessionFactory = new SqlSessionFactoryBuilder().build(mybatisConfiguration);
        }

        environment.lifecycle().manage(dataSource);
        environment.healthChecks().register(name,
                new MyBatisHealthCheck(sessionFactory, config.getConfig().getValidationQuery().get()));

        return sessionFactory;
    }

    /**
     * Create an instance of MyBatis.
     *
     * @param environment The dropwizard environment
     * @param configuration The data source factory/configuration
     * @param dataSource The datasource you want to use.
     * @param name The name of this mybatis factory used for metrics
     * @return An instance of MyBatis.
     */
    public final SqlSessionFactory build(Environment environment,
                                         DataSourceFactory configuration,
                                         ManagedDataSource dataSource,
                                         String name) {
        // Initialize validation query
        final String validationQuery = configuration.getValidationQuery().get();

        // Build mybatis session factory
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        org.apache.ibatis.mapping.Environment myBatisEnvironment =
                new org.apache.ibatis.mapping.Environment(ENV_NAME, transactionFactory, dataSource);
        Configuration mybatisConfiguration = new Configuration(myBatisEnvironment);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(mybatisConfiguration);

        // Setup managed data source and health checks
        environment.lifecycle().manage(dataSource);
        environment.healthChecks().register(name, new MyBatisHealthCheck(sessionFactory, validationQuery));

        return sessionFactory;
    }
}
