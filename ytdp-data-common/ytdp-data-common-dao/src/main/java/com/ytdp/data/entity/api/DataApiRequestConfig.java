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

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("yt_data_api_request_cfg")
public class DataApiRequestConfig extends Entity {

    @TableId(value = "id", type = IdType.AUTO)
    private int apiRequestConfigId;

    @TableField("api_id")
    private int apiId;

    @TableField("api_request_param_name")
    private String apiRequestParamName;

    @TableField("api_request_param_value")
    private String apiRequestParamValue;

    @TableField("api_request_param_required")
    private int apiRequestParamRequired;

    @TableField("api_request_param_desc")
    private String apiRequestParamDesc;

}
