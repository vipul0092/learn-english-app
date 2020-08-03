package io.vgaur.vidya.dao.mappers;

import io.vgaur.vidya.models.Student;
import io.vgaur.vidya.models.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.exceptions.PersistenceException;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by vgaur created on 03/08/20
 */
public interface StudentsMapper {
    void addStudent(@Param("student") Student student) throws SQLException, PersistenceException;

    Student getStudent(@Param("studentId") UUID studentId)  throws SQLException, PersistenceException;

    Student getStudentByEmail(@Param("email") String email)  throws SQLException, PersistenceException;
}
