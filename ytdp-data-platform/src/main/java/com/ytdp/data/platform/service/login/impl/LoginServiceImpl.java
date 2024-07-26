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

package com.ytdp.data.platform.service.login.impl;

import com.ytdp.data.platform.annotations.AuditLogRecord;
import com.ytdp.data.platform.audit.OperationChannel;
import com.ytdp.data.platform.audit.OperationType;
import com.ytdp.data.platform.pojo.vo.Response;
import com.ytdp.data.platform.pojo.vo.login.LoginResult;
import com.ytdp.data.platform.redis.RedisCache;
import com.ytdp.data.platform.service.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private RedisCache redisCache;

    @Override
    @AuditLogRecord(channel = OperationChannel.LOGIN, operationType = OperationType.NONE, descriptor = "登录系统")
    public Response<LoginResult> login(String username, String password) {
        return null;
    }

}
