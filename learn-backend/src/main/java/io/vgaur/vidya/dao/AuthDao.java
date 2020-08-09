package io.vgaur.vidya.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vgaur.vidya.dao.mappers.AuthMapper;
import io.vgaur.vidya.models.auth.ApiKeyData;
import io.vgaur.vidya.models.auth.StudentToken;
import io.vgaur.vidya.mybatis.DefaultSqlSessionFactoryProvider;
import org.apache.commons.lang3.StringUtils;
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
    private final ObjectMapper objectMapper;

    public AuthDao(ObjectMapper objectMapper, DefaultSqlSessionFactoryProvider sessionFactoryProvider) {
        this.objectMapper = objectMapper;
        this.sessionFactoryProvider = sessionFactoryProvider;
    }

    /**
     * Save token
     */
    public void saveToken(StudentToken studentToken) {
        SqlSessionFactory sessionFactory = sessionFactoryProvider.getSqlSessionFactory();
        try (SqlSession session = sessionFactory.openSession(true)) {
            var mapper = session.getMapper(AuthMapper.class);
            mapper.saveToken(studentToken.tokenId(), objectMapper.writeValueAsString(studentToken));
        } catch (Exception e) {
            LOG.error("Failed to add token, {}", studentToken, e);
            throw new WebApplicationException("Failed to add token");
        }
    }

    /**
     * Get token
     */
    public Optional<StudentToken> getToken(UUID tokenId) {
        SqlSessionFactory sessionFactory = sessionFactoryProvider.getSqlSessionFactory();
        try (SqlSession session = sessionFactory.openSession(true)) {
            var mapper = session.getMapper(AuthMapper.class);
            String tokenMetadata = mapper.getToken(tokenId);
            return StringUtils.isBlank(tokenMetadata)
                    ? Optional.empty()
                    : Optional.of(objectMapper.readValue(tokenMetadata, StudentToken.class));
        } catch (Exception e) {
            LOG.error("Failed to get token with id: {}", tokenId, e);
            throw new WebApplicationException("Failed to get token");
        }
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

    /**
     * Delete token
     */
    public void deleteToken(UUID tokenId) {
        SqlSessionFactory sessionFactory = sessionFactoryProvider.getSqlSessionFactory();
        try (SqlSession session = sessionFactory.openSession(true)) {
            var mapper = session.getMapper(AuthMapper.class);
            mapper.deleteToken(tokenId);
        } catch (Exception e) {
            LOG.error("Failed to delete token, tokenId: {}", tokenId, e);
            throw new WebApplicationException("Failed to delete token");
        }
    }

}
