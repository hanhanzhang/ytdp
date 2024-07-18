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

package com.ytdp.data.dao.api;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ytdp.data.entity.api.DataApp;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;

import java.io.Serializable;

public interface DataAppMapper extends BaseMapper<DataApp> {

    @Override
    @Select("SELECT * FROM yt_data_app WHERE id = #{id}")
    @Results({
            @Result(
                    column = "id",
                    property = "allocatedApis",
                    many = @Many(
                            select = "com.ytdp.data.dao.api.DataAppAllocatedApiMapper.selectByAppId",
                            fetchType = FetchType.LAZY
                    )
            )
    })
    DataApp selectById(Serializable id);
}
