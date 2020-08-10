package io.vgaur.vidya.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.vgaur.vidya.dao.AuthDao;
import io.vgaur.vidya.models.Student;
import io.vgaur.vidya.models.Teacher;
import io.vgaur.vidya.models.auth.ApiKeyContext;
import io.vgaur.vidya.models.auth.ApiKeyData;
import io.vgaur.vidya.models.auth.ImmutableStudentToken;
import io.vgaur.vidya.models.auth.StudentToken;
import io.vgaur.vidya.models.auth.TokenRequest;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.WebApplicationException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Service class dealing with tokens and api keys
 * Created by vgaur created on 02/08/20
 */
public class AuthService {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final AuthDao authDao;

    private final LoadingCache<UUID, StudentToken> tokensCache = CacheBuilder.newBuilder()
            .build(new CacheLoader<>() {
                @Override
                public StudentToken load(UUID tokenId) {
                    return getTokenInternal(tokenId);
                }
            });

    private final LoadingCache<UUID, ApiKeyData> apiKeyCache = CacheBuilder.newBuilder()
            .build(new CacheLoader<>() {
                @Override
                public ApiKeyData load(UUID apiKey) {
                    return getApiKeyInternal(apiKey);
                }
            });

    public AuthService(AuthDao authDao, StudentService studentService, TeacherService teacherService) {
        this.authDao = authDao;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    /**
     * Generate token for the given request after verifying the information
     */
    public StudentToken generateToken(TokenRequest tokenRequest, ApiKeyContext apiKeyContext)
            throws ExecutionException {
        Student student = studentService.getStudentByEmail(tokenRequest.email());

        if (!student.pass().equals(tokenRequest.pass())
                || !verifyStudentDetails(student)) {
            throw new WebApplicationException("Invalid Student Data", HttpStatus.UNAUTHORIZED_401);
        }

        UUID tokenId = UUID.randomUUID();
        var studentToken = ImmutableStudentToken.builder()
                .tokenId(tokenId)
                .createdWithApiKey(apiKeyContext.apiKey())
                .email(student.email())
                .id(student.id())
                .teacherId(student.teacherId())
                .build();

        authDao.saveToken(studentToken);
        tokensCache.put(tokenId, studentToken);
        return studentToken;
    }

    /**
     * Verify if the token is still valid
     */
    public StudentToken verifyToken(UUID tokenId) throws ExecutionException {
        var token = tokensCache.get(tokenId);
        Student student = studentService.getStudent(token.id());
        if (!verifyStudentDetails(student)) {
            // Remove the token
            deleteTokenInternal(tokenId);
            throw new WebApplicationException("Invalid Student Data", HttpStatus.UNAUTHORIZED_401);
        }
        return token;
    }

    /**
     * Delete the token with the given id
     */
    public void deleteToken(ApiKeyContext apiKeyContext, UUID tokenId) throws ExecutionException {
        StudentToken token = tokensCache.get(tokenId); // This will check that the token actually exists
        // To delete a token, either the api key should have the admin role
        // or the token should have been created with the same api key
        if (apiKeyContext.isAdmin() || token.createdWithApiKey().equals(apiKeyContext.apiKey())) {
            deleteTokenInternal(tokenId);
        } else {
            throw new WebApplicationException("Cant delete token", HttpStatus.UNAUTHORIZED_401);
        }
    }

    /**
     * Get api key details for the given api key
     */
    public ApiKeyData getApiKeyData(UUID apiKey) throws ExecutionException {
        return apiKeyCache.get(apiKey);
    }

    private boolean verifyStudentDetails(Student student) throws ExecutionException {
        var currentDateTime = LocalDateTime.now();
        if (currentDateTime.isAfter(student.validUntil())) {
            return false;
        }
        Teacher teacher = teacherService.getTeacher(student.teacherId());
        return teacher.active();
    }

    private void deleteTokenInternal(UUID tokenId) {
        authDao.deleteToken(tokenId);
        tokensCache.invalidate(tokenId);
    }

    private StudentToken getTokenInternal(UUID tokenId) {
        var token = authDao.getToken(tokenId);
        return token.orElseThrow(() -> new WebApplicationException("Token not found", HttpStatus.NOT_FOUND_404));
    }

    private ApiKeyData getApiKeyInternal(UUID apiKey) {
        var apiKeyData = authDao.getApiKey(apiKey);
        return apiKeyData.orElseThrow(() -> new WebApplicationException("Api key not found", HttpStatus.NOT_FOUND_404));
    }
}
