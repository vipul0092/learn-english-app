package io.vgaur.vidya.dao;

import io.vgaur.vidya.dao.mappers.AuthMapper;
import io.vgaur.vidya.models.auth.ApiKeyData;
import io.vgaur.vidya.mybatis.DefaultSqlSessionFactoryProvider;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by vgaur created on 03/08/20
 */
public class AuthDao {

    private static final Logger LOG = LoggerFactory.getLogger(AuthDao.class);
    private final DefaultSqlSessionFactoryProvider sessionFactoryProvider;

    public AuthDao(DefaultSqlSessionFactoryProvider sessionFactoryProvider) {
        this.sessionFactoryProvider = sessionFactoryProvider;
    }

    /**
     * Get Api Key
     */
    public Optional<ApiKeyData> getApiKey(UUID apiKey) {
        SqlSessionFactory sessionFactory = sessionFactoryProvider.getSqlSessionFactory();
        try (SqlSession session = sessionFactory.openSession(true)) {
            var mapper = session.getMapper(AuthMapper.class);
            return Optional.ofNullable(mapper.getApiKey(apiKey));
        } catch (Exception e) {
            LOG.error("Failed to get api key with id: {}", apiKey, e);
            throw new WebApplicationException("Failed to get api key");
        }
    }
}
