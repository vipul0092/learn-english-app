package io.vgaur.vidya.dao.mappers;

import io.vgaur.vidya.models.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.exceptions.PersistenceException;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by vgaur created on 03/08/20
 */
public interface TeachersMapper {
    void addTeacher(@Param("teacher") Teacher teacher) throws SQLException, PersistenceException;

    Teacher getTeacher(@Param("teacherId") UUID teacherId)  throws SQLException, PersistenceException;
}
