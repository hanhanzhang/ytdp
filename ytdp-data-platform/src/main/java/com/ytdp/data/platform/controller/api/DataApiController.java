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

package com.ytdp.data.platform.controller.api;

import com.ytdp.data.platform.annotations.AuditLogRecord;
import com.ytdp.data.platform.audit.OperationType;
import com.ytdp.data.platform.audit.SystemResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataApiController {

    @GetMapping("/dataApi/test")
    @AuditLogRecord(resource = SystemResource.DATA_API, operationType = OperationType.SELECT, descriptor = "测试")
    public String test() {
        return "hello";
    }

}