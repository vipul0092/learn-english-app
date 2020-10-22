package io.vgaur.vidya.services;

import com.google.common.cache.CacheLoader;
import io.vgaur.vidya.dao.StudentDao;
import io.vgaur.vidya.models.ImmutableStudent;
import io.vgaur.vidya.models.Student;
import io.vgaur.vidya.models.StudentRequest;
import io.vgaur.vidya.services.cache.CacheProvider;
import io.vgaur.vidya.services.cache.GuavaCache;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.WebApplicationException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Service class dealing with Students data
 * Created by vgaur created on 01/08/20
 */
public class StudentService {

    private final StudentDao studentDao;
    private final CacheProvider<String, Student> studentByEmailCache = new GuavaCache<>(new CacheLoader<>() {
        @Override
        public Student load(String email) {
            return getStudentByEmailInternal(email);
        }
    });
    private final CacheProvider<UUID, Student> studentByIdCache = new GuavaCache<>(new CacheLoader<>() {
        @Override
        public Student load(UUID id) {
            return getStudentInternal(id);
        }
    });

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public Runnable getStudentCachesInvalidator() {
        return () -> {
            studentByEmailCache.removeAll();
            studentByIdCache.removeAll();
        };
    }

    /**
     * Create student record in db for the given student request for the given teacher id
     */
    public Student createStudent(StudentRequest studentRequest, UUID teacherId) {
        Student incomingStudent = studentRequest.student();
        int days = studentRequest.period().getDays();
        incomingStudent = ImmutableStudent.builder().from(incomingStudent)
                .teacherId(teacherId)
                .validUntil(LocalDateTime.now().plusDays(days))
                .build();

        studentDao.addStudent(incomingStudent);
        addStudentToCaches(incomingStudent);
        return incomingStudent;
    }

    /**
     * Get student for the given student id
     */
    public Student getStudent(UUID studentId) throws ExecutionException {
        return studentByIdCache.get(studentId).get();
    }

    /**
     * Get student with the given email address
     */
    public Student getStudentByEmail(String email) throws ExecutionException {
        return studentByEmailCache.get(email).get();
    }

    private Student getStudentInternal(UUID studentId) {
        var student = studentDao.getStudent(studentId);
        return student.orElseThrow(() -> new WebApplicationException("Student not found", HttpStatus.NOT_FOUND_404));
    }

    private Student getStudentByEmailInternal(String email) {
        var student = studentDao.getStudentByEmail(email.toLowerCase());
        return student.orElseThrow(() ->
                new WebApplicationException("Student email record incorrect", HttpStatus.NOT_FOUND_404));
    }

    private void addStudentToCaches(Student student) {
        studentByIdCache.set(student.id(), student);
        studentByEmailCache.set(student.email().toLowerCase(), student);
    }
}
