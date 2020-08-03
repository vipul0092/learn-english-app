package io.vgaur.vidya.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vgaur.vidya.dao.mappers.TokensMapper;
import io.vgaur.vidya.models.StudentToken;
import io.vgaur.vidya.mybatis.DefaultSqlSessionFactoryProvider;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import java.util.UUID;

/**
 * Created by vgaur created on 03/08/20
 */
public class TokenDao {

    private static final Logger LOG = LoggerFactory.getLogger(TokenDao.class);
    private final DefaultSqlSessionFactoryProvider sessionFactoryProvider;
    private final ObjectMapper objectMapper;

    public TokenDao(ObjectMapper objectMapper, DefaultSqlSessionFactoryProvider sessionFactoryProvider) {
        this.objectMapper = objectMapper;
        this.sessionFactoryProvider = sessionFactoryProvider;
    }

    /**
     * Save token
     */
    public void saveToken(StudentToken studentToken) {
        SqlSessionFactory sessionFactory = sessionFactoryProvider.getSqlSessionFactory();
        try (SqlSession session = sessionFactory.openSession(true)) {
            var mapper = session.getMapper(TokensMapper.class);
            mapper.saveToken(studentToken.tokenId(), objectMapper.writeValueAsString(studentToken));
        } catch (Exception e) {
            LOG.error("Failed to add token, {}", studentToken, e);
            throw new WebApplicationException("Failed to add token");
        }
    }

    /**
     * Get token
     */
    public StudentToken getToken(UUID tokenId) {
        SqlSessionFactory sessionFactory = sessionFactoryProvider.getSqlSessionFactory();
        try (SqlSession session = sessionFactory.openSession(true)) {
            var mapper = session.getMapper(TokensMapper.class);
            String tokenMetadata = mapper.getToken(tokenId);
            return objectMapper.readValue(tokenMetadata, StudentToken.class);
        } catch (Exception e) {
            LOG.error("Failed to get token with id: {}", tokenId, e);
            throw new WebApplicationException("Failed to get token");
        }
    }

    /**
     * Delete token
     */
    public void deleteToken(UUID tokenId) {
        SqlSessionFactory sessionFactory = sessionFactoryProvider.getSqlSessionFactory();
        try (SqlSession session = sessionFactory.openSession(true)) {
            var mapper = session.getMapper(TokensMapper.class);
            mapper.deleteToken(tokenId);
        } catch (Exception e) {
            LOG.error("Failed to delete token, tokenId: {}", tokenId, e);
            throw new WebApplicationException("Failed to delete token");
        }
    }

}
