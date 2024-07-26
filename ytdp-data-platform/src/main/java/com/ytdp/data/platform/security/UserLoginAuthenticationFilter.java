/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ytdp.data.platform.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ytdp.data.dao.org.UserMapper;
import com.ytdp.data.entity.org.User;
import com.ytdp.data.enums.UserStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String SECURITY_USERNAME = "username";
    public static final String SECURITY_PASSWORD = "password";

    @Autowired
    private UserMapper userMapper;

    public UserLoginAuthenticationFilter() {
        super(new AntPathRequestMatcher("/login", "POST"));
    }

    @Autowired
    protected void authenticationManager(AuthenticationManager authenticationManager) throws Exception {
        setAuthenticationManager(authenticationManager);
    }

    @Autowired
    protected void successAuthenticationHandler(UserLoginSuccessHandler handler) {
        setAuthenticationSuccessHandler(handler);
    }

    @Autowired
    protected void failureAuthenticationHandler(UserLoginFailureHandler handler) {
        setAuthenticationFailureHandler(handler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String username = request.getParameter(SECURITY_USERNAME);
        String password = request.getParameter(SECURITY_PASSWORD);
        checkParameter(username, "Authentication request username empty.");
        checkParameter(password, "Authentication request password empty.");

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName, username);
        wrapper.eq(User::getUserPassword, password);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new UsernameNotFoundException("user not exist");
        }
        if (user.getUserStatus() == UserStatusEnum.OFF_POST) {
            throw new DisabledException("user already off post.");
        }

        // 用户角色
        List<SimpleGrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, password, authorities);
        authenticationToken.setDetails(user);

        return authenticationToken;
    }


    private static void checkParameter(String parameter, String reason) {
        if (parameter == null || parameter.isEmpty()) {
            throw new AuthenticationServiceException(reason);
        }
    }
}
