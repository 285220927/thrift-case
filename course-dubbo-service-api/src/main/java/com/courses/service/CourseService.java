/**
 * @Project: micro-service
 * @Package com.courses.service
 * @author : zzc
 * @date Date : 2019年08月04日 下午6:37
 * @version V1.0
 */


package com.courses.service;

import com.courses.dto.CourseDTO;
import com.courses.dto.TeacherDTO;

import java.util.List;

public interface CourseService {
    List<CourseDTO> courseList();

    TeacherDTO findTeacherByCourseId(int courseId);
}
