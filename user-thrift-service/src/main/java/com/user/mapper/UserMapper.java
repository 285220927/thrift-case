/**
 * @Project: micro-service
 * @Package com.user.mapper
 * @author : zzc
 * @date Date : 2019年08月04日 上午10:27
 * @version V1.0
 */


package com.user.mapper;

import com.thrift.user.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where id = #{id}")
    UserInfo getUserById(int id);

    @Select("select * from user where username = #{username}")
    UserInfo getUserByUsername(String username);

    @Insert("insert into user(username, password, realName, mobile, email) values" +
            "(#{u.username}, #{u.password}, #{u.realName}, #{u.mobile}, #{u.email})")
    void registerUser(@Param("u") UserInfo userInfo);
}
