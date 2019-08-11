/**
 * @Project: micro-service
 * @Package com.user.controller
 * @author : zzc
 * @date Date : 2019年08月04日 上午11:24
 * @version V1.0
 */


package com.user.controller;

import com.thrift.user.UserInfo;
import com.thrift.user.dto.UserDTO;
import com.user.redis.RedisClient;
import com.user.response.LoginResponse;
import com.user.response.Response;
import com.user.thrift.ServiceProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Random;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private ServiceProvider serviceProvider;

    @Autowired
    private RedisClient redisClient;

    @Value("${timeout.mobile}")
    private int mobileTimeout;

    @Value("${timeout.email}")
    private int emailTimeout;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Response login(@RequestParam("username") String username, @RequestParam("password") String password) {
        UserInfo user = null;
        // 1.校验用户名和密码
        try {
            user = serviceProvider.getUserService().getUserByName(username);
        } catch (TException e) {
            e.printStackTrace();
            return Response.USERNAME_OR_PASSWORD_INVALID;
        }
        if (user == null || !user.getPassword().equalsIgnoreCase(md5(password))) {
            return Response.USERNAME_OR_PASSWORD_INVALID;
        }

        // 2.单点登录,生成token
        String token = getToken();

        // 3.缓存用户
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        redisClient.set(token, userDTO, 3600);
        return new LoginResponse(token);
    }

    private String getToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String md5(String password) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte[] digest = md5.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexUtils.toHexString(digest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/verifyCode", method = RequestMethod.POST)
    @ResponseBody
    public Response verifyCode(@RequestParam(value = "mobile", required = false) String mobile, @RequestParam(value = "email", required = false) String email) {
        String message = "your verify code is: ";
        String verifyCode =  getVerifyCode(6);
        Boolean sendResult = null;
        if (!StringUtils.isBlank(mobile)) {
            try {
                sendResult = serviceProvider.getMessageService().sendMobileMessage(mobile, message + verifyCode);
            } catch (TException e) {
                e.printStackTrace();
                return Response.SEND_MOBILE_VERIFY_CODE_FAILED;
            }
            if (!sendResult) {
                return Response.SEND_MOBILE_VERIFY_CODE_FAILED;
            }
            redisClient.set("mobile_verify_" + mobile, verifyCode, mobileTimeout);
            return new Response();
        } else if (!StringUtils.isBlank(email)) {
            try {
                sendResult = serviceProvider.getMessageService().sendEmailMessage(email, message + verifyCode);
            } catch (TException e) {
                e.printStackTrace();
                return Response.SEND_EMAIL_VERIFY_CODE_FAILED;
            }
            if (!sendResult) {
                return Response.SEND_EMAIL_VERIFY_CODE_FAILED;
            }
            redisClient.set("email_verify_" + email, verifyCode, emailTimeout);
            return new Response();
        } else {
            return Response.MOBILE_OR_EMAIL_IS_REQUIRED;
        }
    }

    private String getVerifyCode(int verifyCodeSize) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < verifyCodeSize; i++) {
            int num = random.nextInt(10);
            sb.append(num);
        }
        return String.valueOf(sb);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Response register(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("realName") String realName,
                             @RequestParam(value = "mobile", required = false) String mobile,
                             @RequestParam(value = "email", required = false) String email,
                             @RequestParam("verifyCode") String verifyCode) {

        // 校验参数过程省略

        if (!StringUtils.isBlank(mobile)) {
            String realMobileVerifyCode = redisClient.get("mobile_verify_" + mobile);
            if (!verifyCode.equals(realMobileVerifyCode)) {
                return Response.MOBILE_VERIFY_CODE_INVALID;
            }
            redisClient.delete("mobile_verify_" + mobile);
        }
        if (!StringUtils.isBlank(email)) {
            String realEmailVerifyCode = redisClient.get("email_verify_" + email);
            if (!verifyCode.equals(realEmailVerifyCode)) {
                return Response.EMAIL_VERIFY_CODE_INVALID;
            }
            redisClient.delete("email_verify_" + email);
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(md5(password));
        userInfo.setRealName(realName);
        userInfo.setMobile(mobile);
        userInfo.setEmail(email);
        try {
            serviceProvider.getUserService().registerUser(userInfo);
        } catch (TException e) {
            e.printStackTrace();
            return Response.USER_INFO_VALID;
        }
        return new Response();
    }

    @RequestMapping(value = "/authentication", method = RequestMethod.POST)
    @ResponseBody
    public UserDTO authenticationUser(HttpServletRequest request) {
        String token = request.getHeader("token");
        return redisClient.get(token);
    }
}
