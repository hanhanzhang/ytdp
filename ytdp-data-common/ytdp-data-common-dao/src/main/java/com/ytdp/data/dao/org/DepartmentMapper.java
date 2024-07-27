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
import com.ytdp.data.entity.org.Department;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;

import java.io.Serializable;

public interface DepartmentMapper extends BaseMapper<Department> {

    @Select("SELECT * FROM yt_org_dept WHERE id = #{id}")
    @Results(
            id = "departmentResults",
            value = {
                    @Result(
                            column = "id",
                            property = "deptId"
                    ),
                    @Result(
                            column = "id",
                            property = "childDepartments",
                            many = @Many(
                                    select = "com.ytdp.data.dao.org.DepartmentMapper.selectByParentId",
                                    fetchType = FetchType.EAGER
                            )
                    )
            })
    @Override
    Department selectById(Serializable id);

    @Select("SELECT * FROM yt_org_dept WHERE dept_parent_id = #{id}")
    @ResultMap("departmentResults")
    Department selectByParentId(Serializable parentId);


    @Select("SELECT * FROM yt_org_dept a INNER JOIN yt_tenant_dept b ON a.id = b.dept_id WHERE b.tenant_id = #{tenantId}")
    @ResultMap("departmentResults")
    Department selectByTenantId(int tenantId);

    @Select("SELECT * FROM yt_org_dept a INNER JOIN yt_user_dept b ON a.id = b.dept_id WHERE b.user_id = #{userId}")
    Department selectByUserId(int userId);
}
