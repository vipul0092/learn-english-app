package io.vgaur.vidya.dao;

import io.vgaur.vidya.dao.mappers.StudentsMapper;
import io.vgaur.vidya.models.Student;
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
public class StudentDao {
    private static final Logger LOG = LoggerFactory.getLogger(StudentDao.class);
    private final DefaultSqlSessionFactoryProvider sessionFactoryProvider;

    public StudentDao(DefaultSqlSessionFactoryProvider sessionFactoryProvider) {
        this.sessionFactoryProvider = sessionFactoryProvider;
    }

    /**
     * Add student
     */
    public void addStudent(Student student) {
        SqlSessionFactory sessionFactory = sessionFactoryProvider.getSqlSessionFactory();
        try (SqlSession session = sessionFactory.openSession(true)) {
            var mapper = session.getMapper(StudentsMapper.class);
            mapper.addStudent(student);
        } catch (Exception e) {
            LOG.error("Failed to add student", e);
            throw new WebApplicationException("Failed to add student");
        }
    }

    /**
     * Get student
     */
    public Optional<Student> getStudent(UUID id) {
        SqlSessionFactory sessionFactory = sessionFactoryProvider.getSqlSessionFactory();
        try (SqlSession session = sessionFactory.openSession(true)) {
            var mapper = session.getMapper(StudentsMapper.class);
            return Optional.ofNullable(mapper.getStudent(id));
        } catch (Exception e) {
            LOG.error("Failed to get student with id: {}", id, e);
            throw new WebApplicationException("Failed to get student");
        }
    }

    /**
     * Get student by email
     */
    public Optional<Student> getStudentByEmail(String email) {
        SqlSessionFactory sessionFactory = sessionFactoryProvider.getSqlSessionFactory();
        try (SqlSession session = sessionFactory.openSession(true)) {
            var mapper = session.getMapper(StudentsMapper.class);
            return Optional.ofNullable(mapper.getStudentByEmail(email));
        } catch (Exception e) {
            LOG.error("Failed to get student with email: {}", email, e);
            throw new WebApplicationException("Failed to get student by email");
        }
    }
}
