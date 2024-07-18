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

package com.ytdp.data.platform.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AuditLogWriteAspect {

    // 切面定义, 所关注的某个事件入口
    @Pointcut("@annotation(com.ytdp.data.platform.annotations.AuditLogRecord)")
    public void operateLog() {

    }

    //
    @AfterReturning(pointcut = "operateLog()", returning = "result")
    public void afterReturning(JoinPoint point, Object result) {
        log.info("execute method {} success", point.getSignature().getName());
    }

    @AfterThrowing(pointcut = "operateLog()", throwing = "ex")
    public void afterThrowing(JoinPoint point, Throwable ex) {

    }
}
