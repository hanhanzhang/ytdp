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

import com.ytdp.data.platform.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ytdp.data.platform.utils.JwtUtils.USER_DETAILS;

/**
 * 1. 用户登录成功后携带登录的token, 自定义Jwt Token认证过滤器
 * 2. OncePerRequestFilter确保每个请求指被过滤一次, 避免多次过滤
 * */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final String TOKEN = "token";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 请求中获取token
        String token = request.getHeader(TOKEN);

        // 2. token空则直接放行
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 解析token
        Claims claims = JwtUtils.parseJWT(token);
        UserDetailsWithOrg userDetails = claims.get(USER_DETAILS, UserDetailsWithOrg.class);


        // 用户认证保存到SecurityContextHolder, 用于后续访问控制和授权操作
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
