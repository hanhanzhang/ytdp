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

package com.ytdp.data.dao.org;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ytdp.data.entity.org.Resource;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ResourceMapper extends BaseMapper<Resource> {


    @Select("SELECT * FROM yt_sys_resource a INNER JOIN yt_team_resource b ON a.id = b.resource_id WHERE b.team_id = #{teamId} AND resource_type = 0")
    List<Resource> selectByTeamId(int teamId);

    @Select("SELECT * FROM yt_sys_resource a INNER JOIN yt_role_resource b ON a.id = b.resource_id WHERE b.role_id = #{roleId}")
    List<Resource> selectByRoleId(int roleId);

}
