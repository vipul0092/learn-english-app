package io.vgaur.vidya.resources;

import io.dropwizard.auth.Auth;
import io.vgaur.vidya.models.Student;
import io.vgaur.vidya.models.auth.ApiKeyContext;
import io.vgaur.vidya.models.auth.UserContext;
import io.vgaur.vidya.services.StudentService;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Student related endpoints
 * Created by vgaur created on 03/08/20
 */
@Produces("application/json")
@Consumes("application/json")
@Path("/students")
public class StudentResource {

    private final StudentService studentService;

    public StudentResource(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Gets the detail of the student for the user context
     */
    @GET
    public Student getStudent(
            @Auth UserContext userContext
    ) throws ExecutionException {
        return studentService.getStudent(userContext.token().id());
    }

    /**
     * Gets the detail of the student by student id
     */
    @RolesAllowed("ADMIN")
    @GET
    @Path("/{id}")
    public Student getStudentById(
            @PathParam("id") UUID studentId,
            @Auth ApiKeyContext apiKeyContext
            ) throws ExecutionException {
        return studentService.getStudent(studentId);
    }
}
