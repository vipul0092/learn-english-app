package io.vgaur.vidya.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.vgaur.vidya.dao.TokenDao;
import io.vgaur.vidya.models.ImmutableStudentToken;
import io.vgaur.vidya.models.Student;
import io.vgaur.vidya.models.StudentToken;
import io.vgaur.vidya.models.Teacher;
import io.vgaur.vidya.models.TokenRequest;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.WebApplicationException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Created by vgaur created on 02/08/20
 */
public class AuthService {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final TokenDao tokenDao;

    private final LoadingCache<UUID, StudentToken> tokensCache = CacheBuilder.newBuilder()
            .build(new CacheLoader<>() {
                @Override
                public StudentToken load(UUID tokenId) {
                    return getTokenInternal(tokenId);
                }
            });

    public AuthService(TokenDao tokenDao, StudentService studentService, TeacherService teacherService) {
        this.tokenDao = tokenDao;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    public StudentToken generateToken(TokenRequest tokenRequest) throws ExecutionException {
        Student student = studentService.getStudentByEmail(tokenRequest.email());

        if (!student.pass().equals(tokenRequest.pass())
            || !verifyStudentDetails(student)) {
            throw new WebApplicationException("Invalid Student Data", HttpStatus.UNAUTHORIZED_401);
        }

        UUID tokenId = UUID.randomUUID();
        var studentToken = ImmutableStudentToken.builder()
                .tokenId(tokenId)
                .email(student.email())
                .id(student.id())
                .teacherId(student.teacherId())
                .build();

        tokenDao.saveToken(studentToken);
        tokensCache.put(tokenId, studentToken);
        return studentToken;
    }

    public StudentToken verifyToken(UUID tokenId) throws ExecutionException {
        var token = tokensCache.get(tokenId);
        Student student = studentService.getStudent(token.id());
        if (!verifyStudentDetails(student)) {
            // Remove the token
            tokenDao.deleteToken(tokenId);
            tokensCache.invalidate(tokenId);

            throw new WebApplicationException("Invalid Student Data", HttpStatus.UNAUTHORIZED_401);
        }
        return token;
    }

    private boolean verifyStudentDetails(Student student) throws ExecutionException {
        var currentDateTime = LocalDateTime.now();
        if (currentDateTime.isAfter(student.validUntil())) {
            return false;
        }
        Teacher teacher = teacherService.getTeacher(student.teacherId());
        return teacher.active();
    }

    private StudentToken getTokenInternal(UUID tokenId) {
        var token = tokenDao.getToken(tokenId);
        if (token == null) {
            throw new WebApplicationException("Token not found", HttpStatus.NOT_FOUND_404);
        }
        return token;
    }
}
