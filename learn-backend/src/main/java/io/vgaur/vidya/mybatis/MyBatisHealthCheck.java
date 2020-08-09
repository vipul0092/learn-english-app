package io.vgaur.vidya.mybatis;

import com.codahale.metrics.health.HealthCheck;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.CallableStatement;

/**
 * A standard MyBatis healthcheck
 */
public class MyBatisHealthCheck extends HealthCheck {
    /**
     * The MyBatis instance to check
     */
    private final SqlSessionFactory sessionFactory;
    /**
     * The validation SQL query to run
     */
    private final String validationQuery;

    /**
     * Create a new dropwizard healthcheck
     *
     * @param sessionFactory    The MyBatis instance we want to check
     * @param validationQuery   The validation SQL query to run
     */
    public MyBatisHealthCheck(SqlSessionFactory sessionFactory, String validationQuery) {
        this.sessionFactory = sessionFactory;
        this.validationQuery = validationQuery;
    }

    @Override
    protected HealthCheck.Result check() throws Exception {
        try (SqlSession session = sessionFactory.openSession();
                CallableStatement callableStatement = session.getConnection().prepareCall(validationQuery)) {
            callableStatement.execute();
        }
        return Result.healthy();
    }
}
