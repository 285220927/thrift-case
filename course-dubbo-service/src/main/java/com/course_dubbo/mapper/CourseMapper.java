/**
 * @Project: micro-service
 * @Package com.courses.mapper
 * @author : zzc
 * @date Date : 2019年08月04日 下午6:47
 * @version V1.0
 */


package com.course_dubbo.mapper;

import com.courses.dto.CourseDTO;
import com.courses.dto.TeacherDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseMapper {

    @Select("select * from course")
    List<CourseDTO> courseList();

    @Select("select * from user where id = (select teacher_id from course where id = #{course_id})")
    TeacherDTO findTeacherByCourseId(int courseId);
}
