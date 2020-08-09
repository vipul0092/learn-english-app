package io.vgaur.vidya.resources;

import io.dropwizard.auth.Auth;
import io.vgaur.vidya.models.Student;
import io.vgaur.vidya.models.StudentRequest;
import io.vgaur.vidya.models.Teacher;
import io.vgaur.vidya.models.auth.ApiKeyContext;
import io.vgaur.vidya.models.auth.UserContext;
import io.vgaur.vidya.services.StudentService;
import io.vgaur.vidya.services.TeacherService;
import org.eclipse.jetty.http.HttpStatus;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Teacher related endpoints
 * Created by vgaur created on 03/08/20
 */
@Produces("application/json")
@Consumes("application/json")
@Path("/teachers")
public class TeachersResource {

    private final TeacherService teacherService;
    private final StudentService studentService;

    public TeachersResource(TeacherService teacherService, StudentService studentService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    @RolesAllowed("ADMIN")
    @POST
    public Response createTeacher(
            @NotNull Teacher teacher,
            @Auth ApiKeyContext apiKeyContext
    ) {
        teacher = teacherService.createTeacher(teacher);
        return Response.status(HttpStatus.CREATED_201).entity(teacher).build();
    }

    /**
     * Gets the detail of the teacher for the user context
     */
    @GET
    public Teacher getTeacher(
            @Auth UserContext userContext
    ) throws ExecutionException {
        return teacherService.getTeacher(userContext.token().teacherId());
    }

    /**
     * Gets the detail of the teacher for thee given teacher id
     */
    @RolesAllowed("ADMIN")
    @GET
    @Path("/{id}")
    public Teacher getTeacherById(
            @PathParam("id") UUID teacherId,
            @Auth ApiKeyContext apiKeyContext
    ) throws ExecutionException {
        return teacherService.getTeacher(teacherId);
    }

    @RolesAllowed("ADMIN")
    @POST
    @Path("/{id}/student")
    public Response createStudent(
            @PathParam("id") UUID teacherId,
            @NotNull StudentRequest studentRequest,
            @Auth ApiKeyContext apiKeyContext
    ) throws ExecutionException {
        teacherService.getTeacher(teacherId); // To check that the teacher with that id exists
        Student student = studentService.createStudent(studentRequest, teacherId);
        return Response.status(HttpStatus.CREATED_201).entity(student).build();
    }
}
