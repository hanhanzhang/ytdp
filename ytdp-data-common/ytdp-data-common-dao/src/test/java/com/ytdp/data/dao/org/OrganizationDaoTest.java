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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.ytdp.data.entity.org.Department;
import com.ytdp.data.entity.org.Post;
import com.ytdp.data.entity.org.PostGroup;
import com.ytdp.data.entity.org.Resource;
import com.ytdp.data.entity.org.Role;
import com.ytdp.data.entity.org.RoleResource;
import com.ytdp.data.entity.org.Team;
import com.ytdp.data.entity.org.TeamResource;
import com.ytdp.data.entity.org.TeamTenant;
import com.ytdp.data.entity.org.Tenant;
import com.ytdp.data.entity.org.TenantDepartment;
import com.ytdp.data.enums.PermissionTypeEnum;
import com.ytdp.data.enums.RoleStatusEnum;
import com.ytdp.data.enums.ResourceTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@Slf4j
@MybatisPlusTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrganizationDaoTest {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private PostGroupMapper postGroupMapper;
    @Autowired
    private PostMapper postMapper;


    @Autowired
    private TenantMapper tenantMapper;
    @Autowired
    private TenantDepartmentMapper tenantDepartmentMapper;
    @Autowired
    private TeamMapper teamMapper;
    @Autowired
    private TeamTenantMapper teamTenantMapper;


    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleResourceMapper roleResourceMapper;
    @Autowired
    private TeamResourceMapper teamResourceMapper;

    @Test
    @Rollback(value = true)
    public void testDepartmentMapper() {
        Department parent = new Department();
        parent.setDeptParentId(0);
        parent.setDeptName("集团");
        parent.setDeptCode("bloc");
        parent.setDeptSort(1);
        departmentMapper.insert(parent);

        Department child = new Department();
        child.setDeptParentId(parent.getDeptId());
        child.setDeptName("基础机构技术部");
        child.setDeptCode("infra");
        child.setDeptSort(1);
        departmentMapper.insert(child);

        Department selectedDept = departmentMapper.selectById(parent.getDeptId());
        Assertions.assertEquals(parent.getDeptName(), selectedDept.getDeptName());
        Assertions.assertEquals(1, selectedDept.getChildDepartments().size());

        Department selectedChildDept = departmentMapper.selectByParentId(parent.getDeptId());
        Assertions.assertEquals(child.getDeptName(), selectedChildDept.getDeptName());
    }


    @Test
    @Rollback(value = true)
    public void testPostMapper() {
        PostGroup postGroup = new PostGroup();
        postGroup.setPostGroupName("数据簇");
        postGroup.setPostGroupCode("data");
        postGroup.setPostGroupDesc("数据通道");
        postGroupMapper.insert(postGroup);

        Post post = new Post();
        post.setPostGroupId(postGroup.getPostGroupId());
        post.setPostName("数据开发");
        post.setPostCode("de");
        postMapper.insert(post);

        PostGroup selectedPostGroup = postGroupMapper.selectById(postGroup.getPostGroupId());
        Assertions.assertEquals(postGroup.getPostGroupName(), selectedPostGroup.getPostGroupName());

        Post selectedPost = postMapper.selectByPostGroupId(postGroup.getPostGroupId());
        Assertions.assertEquals(post.getPostName(), selectedPost.getPostName());
    }

    @Test
    @Rollback(value = true)
    public void testTeamMapper() {
        // 租户隶属部门
        testDepartmentMapper();
        LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Department::getDeptCode, "infra");
        Department department = departmentMapper.selectOne(queryWrapper);

        Tenant tenant = new Tenant();
        tenant.setTenantAccount("infra");
        tenant.setTenantName("基础架构数据组");
        tenant.setTenantDesc("基础架构数据团队租户");
        tenantMapper.insert(tenant);

        TenantDepartment tenantDepartment = new TenantDepartment();
        tenantDepartment.setDepartmentId(department.getDeptId());
        tenantDepartment.setTenantId(tenant.getTenantId());
        tenantDepartmentMapper.insert(tenantDepartment);

        Team team = new Team();
        team.setTeamName("infra_data");
        team.setTeamDesc("基础架构数据工程组");
        teamMapper.insert(team);

        TeamTenant teamTenant = new TeamTenant();
        teamTenant.setTeamId(team.getTeamId());
        teamTenant.setTenantId(tenant.getTenantId());
        teamTenantMapper.insert(teamTenant);

        // 按照租户查询部门
        Department selectedDept = departmentMapper.selectByTenantId(tenant.getTenantId());
        Assertions.assertEquals(department.getDeptName(), selectedDept.getDeptName());
        // 按照租户查询项目组
        List<Team> selectedTeams = teamMapper.selectByTenantId(tenant.getTenantId());
        Assertions.assertEquals(1, selectedTeams.size());
        Assertions.assertEquals(team.getTeamName(), selectedTeams.get(0).getTeamName());

        // 查询租户
        Tenant selectedTenant = tenantMapper.selectById(tenant.getTenantId());
        Assertions.assertEquals(tenant.getTenantAccount(), selectedTenant.getTenantAccount());
    }

    @Test
    public void testSecurityMapper() {
        // 角色
        Role role = new Role();
        role.setRoleName("系统管理员");
        role.setRoleCreator("张韩");
        role.setRoleStatus(RoleStatusEnum.ENABLE);
        role.setRoleSort(1);
        roleMapper.insert(role);

        // 菜单资源
        Resource menuResource = new Resource();
        menuResource.setResourceName("系统管理");
        menuResource.setResourcePath("/system");
        menuResource.setPermissionType(PermissionTypeEnum.MENU);
        menuResource.setResourceParentId(0);
        resourceMapper.insert(menuResource);

        // 菜单页面资源
        Resource menuPageResource = new Resource();
        menuPageResource.setResourceName("系统管理页面");
        menuPageResource.setResourcePath("/system/page");
        menuPageResource.setPermissionType(PermissionTypeEnum.MENU_PAGE);
        menuPageResource.setResourceParentId(menuResource.getResourceId());
        resourceMapper.insert(menuPageResource);

        // 角色分配资源
        RoleResource roleResource = new RoleResource();
        roleResource.setRoleId(role.getRoleId());
        roleResource.setResourceId(menuResource.getResourceId());

        RoleResource roleResource1 = new RoleResource();
        roleResource1.setRoleId(role.getRoleId());
        roleResource1.setResourceId(menuPageResource.getResourceId());
        roleResourceMapper.insert(Lists.list(roleResource, roleResource1));

        // 查看角色
        Role selectedRole = roleMapper.selectById(role.getRoleId());
        Assertions.assertEquals(role.getRoleName(), selectedRole.getRoleName());

        // 项目组分配角色
        testTeamMapper();
        LambdaQueryWrapper<Team> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Team::getTeamName, "infra_data");
        Team team = teamMapper.selectOne(queryWrapper);

        TeamResource teamResource = new TeamResource();
        teamResource.setTeamId(team.getTeamId());
        teamResource.setResourceId(menuResource.getResourceId());
        teamResource.setResourceType(ResourceTypeEnum.SYSTEM);

        TeamResource teamResource1 = new TeamResource();
        teamResource1.setTeamId(team.getTeamId());
        teamResource1.setResourceId(menuPageResource.getResourceId());
        teamResource1.setResourceType(ResourceTypeEnum.SYSTEM);
        teamResourceMapper.insert(teamResource);
        teamResourceMapper.insert(teamResource1);


        // 查询项目组资源
        List<Resource> resources = resourceMapper.selectByTeamId(team.getTeamId());
        Assertions.assertEquals(2, resources.size());

    }

    @Test
    public void testUserMapper() {

    }
}
