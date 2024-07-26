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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ytdp.data.entity.org.User;
import com.ytdp.data.platform.pojo.vo.CodeEnum;
import com.ytdp.data.platform.pojo.vo.Response;
import com.ytdp.data.platform.pojo.vo.login.LoginSuccessResult;
import com.ytdp.data.platform.utils.JwtUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.String.format;

@Component
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {

    public static final String TOKEN = "bear ";
    private static final String HEADER_AUTHENTICATION = "Authorization";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getDetails();
        String token = JwtUtils.createJWT(user);

        String attachToken = format("%s%s", TOKEN, token);
        response.setCharacterEncoding("utf-8");
        response.setHeader(HEADER_AUTHENTICATION, attachToken);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(
                response.getWriter(),
                Response.of(
                        CodeEnum.SUCCESS.getValue(),
                        new LoginSuccessResult(attachToken)
                )
        );
    }
}
