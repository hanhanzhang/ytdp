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

package com.ytdp.data.entity.api;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ytdp.data.entity.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("yt_data_api")
public class DataApi extends Entity {

    @TableId(value = "id", type = IdType.AUTO)
    private int apiId;

    @TableField("api_name")
    private String apiName;

    @TableField("api_owner")
    private String apiOwner;

    @TableField("api_status")
    private int apiStatus;

    @TableField("api_url")
    private String apiUrl;

    @TableField("api_method")
    private String apiMethod;

    @TableField("api_desc")
    private String apiDesc;

    @TableField(exist = false)
    private List<DataApiRequestConfig> requestConfigs;

    @TableField(exist = false)
    private DataApiCalConfig calConfig;

    @TableField(exist = false)
    private List<DataApiResponseConfig> responseConfigs;

    @TableField(exist = false)
    private DataApiSecurityConfig securityConfig;
}
