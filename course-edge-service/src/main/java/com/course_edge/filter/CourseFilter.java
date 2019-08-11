/**
 * @Project: micro-service
 * @Package com.courses.filter
 * @author : zzc
 * @date Date : 2019年08月05日 下午9:34
 * @version V1.0
 */


package com.course_edge.filter;

import com.thrift.user.dto.UserDTO;
import com.user.client.LoginFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CourseFilter extends LoginFilter {
    @Override
    protected void login(HttpServletRequest request, HttpServletResponse response, UserDTO userDTO) {
        request.setAttribute("user", userDTO);
    }
}
