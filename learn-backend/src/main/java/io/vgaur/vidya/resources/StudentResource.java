package io.vgaur.vidya.resources;

import io.vgaur.vidya.models.Student;
import io.vgaur.vidya.services.StudentService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
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

    @GET
    @Path("/{id}")
    public Student getStudent(
            @PathParam("id") UUID studentId
    ) throws ExecutionException {
        return studentService.getStudent(studentId);
    }
}
