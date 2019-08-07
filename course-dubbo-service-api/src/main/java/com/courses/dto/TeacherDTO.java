/**
 * @Project: micro-service
 * @Package com.courses.dto
 * @author : zzc
 * @date Date : 2019年08月04日 下午6:39
 * @version V1.0
 */


package com.courses.dto;

import com.thrift.user.dto.UserDTO;

import java.io.Serializable;

public class TeacherDTO extends UserDTO implements Serializable {
    private String introduce;
    private int stars;

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}
