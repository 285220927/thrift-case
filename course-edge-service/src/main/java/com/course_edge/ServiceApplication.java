/**
 * @Project: micro-service
 * @Package com
 * @author : zzc
 * @date Date : 2019年08月05日 下午9:34
 * @version V1.0
 */


package com.course_edge;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.course_edge.filter.CourseFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableDubbo
public class ServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        CourseFilter courseFilter = new CourseFilter();
        filterRegistrationBean.setFilter(courseFilter);
        List<String> filterUrlList = new ArrayList<>();
        filterUrlList.add("/course/*");
        filterRegistrationBean.setUrlPatterns(filterUrlList);
        return filterRegistrationBean;
    }
}
