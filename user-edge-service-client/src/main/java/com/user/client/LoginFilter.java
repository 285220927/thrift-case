/**
 * @Project: micro-service
 * @Package com.user.client
 * @author : zzc
 * @date Date : 2019年08月04日 下午3:55
 * @version V1.0
 */


package com.user.client;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.thrift.user.dto.UserDTO;
import com.user.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public abstract class LoginFilter implements Filter {

    private static Cache<String, UserDTO> cache = CacheBuilder.newBuilder().maximumSize(10000).
            expireAfterWrite(3, TimeUnit.MINUTES).build();

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = request.getParameter("token");
        if (StringUtils.isBlank(token)) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        token = cookie.getValue();
                    }
                }
            }
        }
        UserDTO user = null;
        if (StringUtils.isNotBlank(token)) {
            // 先从缓存里面取,如果没有,再进行远程调用
            user = cache.getIfPresent(token);
            if (user == null) {
                user = requestUserInfo(token);
                if (user != null) {
                    // 远程调用完成之后,添加到缓存中
                    cache.put(token, user);
                }
            }
        }
        if (user == null) {
            response.sendRedirect("http://localhost:8080/user/login");
            return;
        }
        login(request, response, user);

        filterChain.doFilter(request, response);
    }

    // 调用者调用登录验证之后实现这个方法
    protected abstract void login(HttpServletRequest request, HttpServletResponse response, UserDTO userDTO);

    private UserDTO requestUserInfo(String token) {
        String url = "http://user-edge-service:8082/user/authentication";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        InputStream inputStream = null;
        post.setHeader("token", token);
        try {
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                System.out.println(response.getStatusLine().getStatusCode());
                throw new RuntimeException("request user info failed! StatusLine:" + response.getStatusLine());
            }
            inputStream = response.getEntity().getContent();
            byte[] bytes = new byte[1024];
            int len = 0;
            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, len));
            }
            return JsonUtils.parse(sb.toString(), UserDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void destroy() {

    }
}
