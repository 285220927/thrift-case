/**
 * @Project: micro-service
 * @Package com.courses
 * @author : zzc
 * @date Date : 2019年08月04日 下午7:09
 * @version V1.0
 */


package com.course_dubbo;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class ServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }
}
