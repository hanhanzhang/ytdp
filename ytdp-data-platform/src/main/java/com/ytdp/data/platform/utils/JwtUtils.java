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

package com.ytdp.data.platform.utils;

import com.ytdp.data.platform.security.UserDetailsWithOrg;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    // 秘钥必须保留在服务端, 不能暴露出去, 否则sign可以被伪造
    private static final String JWT_SECRET_KEY = "ytdp";
    // 设置token过期时间(ms)
    private static final long JWT_TTL = 60 * 60 * 1000L;
    //
    public static final String USER_DETAILS = "userDetails";

    public JwtUtils() { }

    public static String createJWT(UserDetailsWithOrg userDetails) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put(USER_DETAILS, userDetails);
        return Jwts
                .builder()
                // 设置秘钥
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                .claims(claims)
                // 设置过期时间
                .expiration(new Date(System.currentTimeMillis() + JWT_TTL))
                .compact();
    }

    public static Claims parseJWT(String token) {
        try {
            // 生成HMAC秘钥
            SecretKey secretKey = Keys
                    .hmacShaKeyFor(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            JwtParser parser = Jwts
                    .parser()
                    // 设置签名秘钥
                    .verifyWith(secretKey)
                    .build();
            return parser
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new RuntimeException("非法token", e);
        }
    }
}
