/**
 * @Project: micro-service
 * @Package com.user
 * @author : zzc
 * @date Date : 2019年08月04日 上午10:01
 * @version V1.0
 */


package com.user.service;

import com.thrift.user.UserInfo;
import com.user.mapper.UserMapper;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements com.thrift.user.UserService.Iface {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserInfo getUserById(int id) throws TException {
        return userMapper.getUserById(id);
    }

    @Override
    public UserInfo getUserByName(String username) throws TException {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public UserInfo getTeacherById(int id) throws TException {
        return userMapper.getUserById(id);
    }

    @Override
    public void registerUser(UserInfo userinfo) throws TException {
        userMapper.registerUser(userinfo);
    }
}
