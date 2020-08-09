package io.vgaur.vidya.dao;

import io.vgaur.vidya.dao.mappers.TeachersMapper;
import io.vgaur.vidya.models.Teacher;
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
public class TeacherDao {
    private static final Logger LOG = LoggerFactory.getLogger(TeacherDao.class);
    private final DefaultSqlSessionFactoryProvider sessionFactoryProvider;

    public TeacherDao(DefaultSqlSessionFactoryProvider sessionFactoryProvider) {
        this.sessionFactoryProvider = sessionFactoryProvider;
    }

    /**
     * Add teacher
     */
    public void addTeacher(Teacher teacher) {
        SqlSessionFactory sessionFactory = sessionFactoryProvider.getSqlSessionFactory();
        try (SqlSession session = sessionFactory.openSession(true)) {
            var mapper = session.getMapper(TeachersMapper.class);
            mapper.addTeacher(teacher);
        } catch (Exception e) {
            LOG.error("Failed to add teacher", e);
            throw new WebApplicationException("Failed to add teacher");
        }
    }

    /**
     * Get teacher
     */
    public Optional<Teacher> getTeacher(UUID id) {
        SqlSessionFactory sessionFactory = sessionFactoryProvider.getSqlSessionFactory();
        try (SqlSession session = sessionFactory.openSession(true)) {
            var mapper = session.getMapper(TeachersMapper.class);
            return Optional.ofNullable(mapper.getTeacher(id));
        } catch (Exception e) {
            LOG.error("Failed to get teacher with id: {}", id, e);
            throw new WebApplicationException("Failed to get teacher");
        }
    }
}
