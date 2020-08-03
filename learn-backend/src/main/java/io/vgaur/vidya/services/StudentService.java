package io.vgaur.vidya.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.vgaur.vidya.dao.StudentDao;
import io.vgaur.vidya.models.ImmutableStudent;
import io.vgaur.vidya.models.Student;
import io.vgaur.vidya.models.StudentRequest;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.WebApplicationException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Created by vgaur created on 01/08/20
 */
public class StudentService {

    private final StudentDao studentDao;
    private final LoadingCache<String, Student> studentByEmailCache = CacheBuilder.newBuilder()
            .build(new CacheLoader<>() {
                @Override
                public Student load(String email) {
                    return getStudentByEmailInternal(email);
                }
            });
    private final LoadingCache<UUID, Student> studentByIdCache = CacheBuilder.newBuilder()
            .build(new CacheLoader<>() {
                @Override
                public Student load(UUID id) {
                    return getStudentInternal(id);
                }
            });

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

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

    public Student getStudent(UUID studentId) throws ExecutionException {
        return studentByIdCache.get(studentId);
    }


    public Student getStudentByEmail(String email) throws ExecutionException {
        return studentByEmailCache.get(email);
    }

    private Student getStudentInternal(UUID studentId) {
        var student = studentDao.getStudent(studentId);
        if (student == null) {
            throw new WebApplicationException("Student not found", HttpStatus.NOT_FOUND_404);
        }
        return student;
    }

    private Student getStudentByEmailInternal(String email) {
        var student = studentDao.getStudentByEmail(email.toLowerCase());
        if (student == null) {
            throw new WebApplicationException("Student email record incorrect", HttpStatus.NOT_FOUND_404);
        }
        return student;
    }

    private void addStudentToCaches(Student student) {
        studentByIdCache.put(student.id(), student);
        studentByEmailCache.put(student.email().toLowerCase(), student);
    }
}
