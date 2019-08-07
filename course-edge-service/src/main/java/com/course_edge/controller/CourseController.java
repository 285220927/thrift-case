/**
 * @Project: micro-service
 * @Package com.courses.controller
 * @author : zzc
 * @date Date : 2019年08月05日 下午9:32
 * @version V1.0
 */


package com.course_edge.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.courses.dto.CourseDTO;
import com.courses.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/course")
public class CourseController {

    @Reference
    private CourseService courseService;

    @RequestMapping(value = "/courseList", method = RequestMethod.GET)
    @ResponseBody
    public List<CourseDTO> getCourseList() {
        return courseService.courseList();
    }
}
