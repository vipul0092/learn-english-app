package io.vgaur.vidya.services;

import com.google.common.cache.CacheLoader;
import io.vgaur.vidya.dao.TeacherDao;
import io.vgaur.vidya.models.Teacher;
import io.vgaur.vidya.services.cache.CacheProvider;
import io.vgaur.vidya.services.cache.GuavaCache;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.WebApplicationException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Service class dealing with Teachers
 * Created by vgaur created on 01/08/20
 */
public class TeacherService {

    private final TeacherDao teacherDao;
    private final CacheProvider<UUID, Teacher> teacherCache = new GuavaCache<>(new CacheLoader<>() {
        @Override
        public Teacher load(UUID teacherId) {
            return getTeacherInternal(teacherId);
        }
    });

    public TeacherService(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    public Runnable getTeachersCacheInvalidator() {
        return teacherCache::removeAll;
    }

    /**
     * Create a teacher record in db for the given teacher information
     */
    public Teacher createTeacher(Teacher teacher) {
        teacherDao.addTeacher(teacher);
        teacherCache.set(teacher.id(), teacher);
        return teacher;
    }

    /**
     * Get teacher for the given id
     */
    public Teacher getTeacher(UUID teacherId) throws ExecutionException {
        return teacherCache.get(teacherId).get();
    }

    private Teacher getTeacherInternal(UUID teacherId) {
        var teacher = teacherDao.getTeacher(teacherId);
        return teacher.orElseThrow(() -> new WebApplicationException("Teacher not found", HttpStatus.NOT_FOUND_404));
    }
}
