/**
 * @Project: micro-service
 * @Package com.courses.service
 * @author : zzc
 * @date Date : 2019年08月04日 下午6:47
 * @version V1.0
 */


package com.course_dubbo.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.courses.dto.CourseDTO;
import com.courses.dto.TeacherDTO;
import com.courses.service.CourseService;
import com.course_dubbo.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<CourseDTO> courseList() {
        List<CourseDTO> courseDTOS = courseMapper.courseList();
        for (CourseDTO courseDTO : courseDTOS) {
            TeacherDTO teacherDTO = findTeacherByCourseId(courseDTO.getId());
            teacherDTO.setPassword(null);
            courseDTO.setTeacher(teacherDTO);
        }
        return courseDTOS;
    }

    @Override
    public TeacherDTO findTeacherByCourseId(int courseId) {
        return courseMapper.findTeacherByCourseId(courseId);
    }
}
