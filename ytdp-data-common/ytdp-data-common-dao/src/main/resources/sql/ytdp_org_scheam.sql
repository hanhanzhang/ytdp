-- 部门组织
CREATE TABLE IF NOT EXISTS yt_org_dept (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `dept_parent_id` INT(11) DEFAULT 0 COMMENT '父部门',
    `dept_name` VARCHAR(64) NOT NULL COMMENT '部门名称',
    `dept_code` VARCHAR(32) NOT NULL COMMENT '部门编码',
    `dept_sort` INT DEFAULT 1 COMMENT '部门排序值, 从小到大排序',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE INDEX dept_parent_id_index (`dept_parent_id`),
    UNIQUE INDEX dept_name_index (`dept_name`),
    UNIQUE INDEX dept_code_index (`dept_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 职位
CREATE TABLE IF NOT EXISTS yt_org_job_group (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `job_group_name` VARCHAR(64) NOT NULL COMMENT '簇名称',
    `job_group_code` VARCHAR(32) NOT NULL COMMENT '簇编码',
    `job_group_desc` VARCHAR(128) COMMENT '簇描述',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE INDEX job_group_name_index (`job_group_name`),
    UNIQUE INDEX job_group_code_index (`job_group_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS yt_org_job (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `job_group_id` INT(11) NOT NULL COMMENT '簇组',
    `job_name` VARCHAR(64) NOT NULL COMMENT '职位名成',
    `job_code` VARCHAR(32) NOT NULL COMMENT '职位编码',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE INDEX job_name_index (`job_name`),
    UNIQUE INDEX job_code_index (`job_code`),
    INDEX job_group_id_index (`job_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用户
-- 组织关系看: 用户属于某个部门的某个岗位
-- 开发关系看: 用户可属于多个项目组，项目组和租户相对应
--           注意:
--               1. 一个租户属于一个部门, 一个部门有多个租户
--               2. 租户下可有多个项目组, 一个项目组只能属于一个租户
--               3. 项目组是分配物理资源(计算队列 & 数据存储)和系统资源权限的基本单位
-- 权限控制看: 用户可分配多个角色, 拥有角色对应的系统资源权限, 若用户是开发人员, 归属某个项目组, 拥有项目组的系统资源权限

-- 租户
CREATE TABLE IF NOT EXISTS yt_dep_tenant (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `tenant_name` VARCHAR(32) NOT NULL COMMENT '租户名称',
    `tenant_account` VARCHAR(32) NOT NULL COMMENT '租户账号, 创建相应的Hadoop集群账号',
    `tenant_desc` VARCHAR(128) COMMENT '租户描述',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE INDEX tenant_name_index (`tenant_name`),
    UNIQUE INDEX tenant_account_index (`tenant_account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS yt_tenant_dept (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `tenant_id` INT(11) NOT NULL COMMENT '',
    `dept_id` INT(11) NOT NULL COMMENT '',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE INDEX tenant_id_index (`tenant_id`),
    INDEX dept_id_index (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 项目组
CREATE TABLE IF NOT EXISTS yt_dep_team (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `team_name` VARCHAR(32) NOT NULL COMMENT '项目组名称',
    `team_desc` VARCHAR(128) COMMENT '项目组描述',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE INDEX team_name_index (`team_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS yt_team_tenant (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `team_id` INT(11) NOT NULL COMMENT '项目组ID',
    `tenant_id` INT(11) NOT NULL COMMENT '租户ID',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    INDEX team_id_index (`team_id`),
    INDEX tenant_id_index (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS yt_team_resource (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `resource_type` TINYINT NOT NULL COMMENT '分配资源类型, 0: 系统资源, 1: 计算资源, 2: 存储资源',
    `team_id` INT(11) NOT NULL COMMENT '项目组标识',
    `resource_id` INT(11) NOT NULL COMMENT '资源标识',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    INDEX resource_type_index (`resource_type`),
    INDEX resource_id_index (`resource_id`),
    INDEX team_id_index (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 系统资源
CREATE TABLE IF NOT EXISTS yt_sys_resource (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `resource_name` VARCHAR(64) COMMENT '资源名称',
    `resource_parent_id` INT(11) DEFAULT -1 COMMENT '父资源',
    `resource_type` TINYINT NOT NULL COMMENT '资源类型, 0: 菜单, 1: 菜单页面, 2: 菜单页面动作资源',
    `resource_path` VARCHAR(128) NOT NULL COMMENT '资源地址',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE INDEX resource_name_index (`resource_name`),
    UNIQUE INDEX resource_path_index (`resource_path`),
    INDEX resource_parent_id_index (`resource_parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 角色
CREATE TABLE IF NOT EXISTS yt_sys_role (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `role_name` VARCHAR(32) NOT NULL COMMENT '角色名称',
    `role_sort` TINYINT DEFAULT 1 COMMENT '角色排序值, 默认值从小到大排序',
    `role_status` TINYINT NOT NULL COMMENT '角色状态, 0: 可用, 1: 禁用',
    `role_creator` VARCHAR(32) NOT NULL COMMENT '角色创建者',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE INDEX role_name_index (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS yt_role_resource (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `role_id` INT(11) NOT NULL COMMENT '角色ID',
    `resource_id` INT(11) NOT NULL COMMENT '系统资源ID',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    INDEX role_id_index (`role_id`),
    INDEX resource_id_index (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用户
CREATE TABLE IF NOT EXISTS yt_org_user (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `user_name` VARCHAR(32) NOT NULL COMMENT '用户名称',
    `user_password` VARCHAR(128) NOT NULL COMMENT '用户密码',
    `user_status` TINYINT DEFAULT 0 COMMENT '用户状态, 0: 在职, 1: 离职',
    `user_email` VARCHAR(128) COMMENT '用户邮箱',
    `user_phone` INT(11) COMMENT '用户电话',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE INDEX user_name_index (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS yt_user_dept (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT(11) NOT NULL COMMENT '',
    `dept_id` INT(11) NOT NULL COMMENT '',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE INDEX user_id_index (`user_id`),
    INDEX dept_id_index (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS yt_user_team (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT(11) NOT NULL COMMENT '用户ID',
    `team_id` INT(11) NOT NULL COMMENT '项目组ID',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    INDEX user_id_index (`user_id`),
    INDEX team_id_index (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS yt_user_role (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT(11) NOT NULL COMMENT '用户ID',
    `role_id` INT(11) NOT NULL COMMENT '角色ID',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    INDEX user_id_index (`user_id`),
    INDEX role_id_index (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;