-- data api
CREATE TABLE IF NOT EXISTS yt_data_api (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `api_name` VARCHAR(32) NOT NULL COMMENT 'API接口名',
    `api_owner` VARCHAR(32) NOT NULL COMMENT 'API接口负责人',
    `api_status` TINYINT NOT NULL COMMENT 'API接口状态, 0: 在线, 1: 下线',
    `api_url` VARCHAR(128) NOT NULL COMMENT 'API请求URL',
    `api_method` VARCHAR(16) NOT NULL COMMENT 'API请求方式, eg: GET、POST',
    `api_desc` VARCHAR(256) NOT NULL COMMENT 'API接口描述',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE INDEX api_url_index (`api_url`),
    INDEX api_owner_index (`api_owner`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS yt_data_api_request_cfg (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `api_id` INT(11) NOT NULL COMMENT '数据API接口ID',
    `api_request_param_name` VARCHAR(32) NOT NULL COMMENT '请求参数名',
    `api_request_param_value` VARCHAR(32) NOT NULL COMMENT '请求参数值',
    `api_request_param_required` TINYINT NOT NULL COMMENT '请求参数是否必填, 0: 必填, 1: 非必填',
    `api_request_param_desc` VARCHAR(128) NOT NULL COMMENT '请求参数描述',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    INDEX api_id_index (`api_id`),
    UNIQUE INDEX api_param_name_index (`api_request_param_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS yt_data_api_cal_cfg (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `api_id` INT(11) NOT NULL COMMENT '数据API接口ID',
    `api_calc_type` TINYINT NOT NULL COMMENT '数据API接口计算类型, eg: SQL',
    `api_calc_cfg` VARCHAR(512) NOT NULL COMMENT '数据API接口配置, json格式',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE INDEX api_id_index (`api_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- API接口响应配置
CREATE TABLE IF NOT EXISTS yt_data_api_response_cfg (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `api_id` INT(11) NOT NULL COMMENT '数据API接口ID',
    `api_response_field_name` VARCHAR(32) NOT NULL COMMENT '响应字段名',
    `api_response_field_encrypt` TINYINT NOT NULL COMMENT '响应字段名是否加密, 0: 加密, 1: 非加密',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    INDEX api_id_index (`api_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- API接口安全配置
CREATE TABLE IF NOT EXISTS yt_data_api_security_cfg (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `api_id` INT(11) NOT NULL COMMENT '数据API接口ID',
    `api_request_limit_period` VARCHAR(32) NOT NULL COMMENT '数据接口访问限流周期, eg: 60sec',
    `api_request_limit_frequency` INT(11) NOT NULL COMMENT '数据接口访问限流频次',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE INDEX api_id_index (`api_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据应用
CREATE TABLE IF NOT EXISTS yt_data_app (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `app_name` VARCHAR(32) NOT NULL COMMENT '数据应用名',
    `app_owner` VARCHAR(64) NOT NULL COMMENT '数据应用负责人',
    `app_status` TINYINT NOT NULL COMMENT '应用状态, 0: 在线, 1: 下线',
    `app_key` VARCHAR(128) NOT NULL COMMENT '数据应用唯一标识符',
    `app_secret_key` VARCHAR(128) NOT NULL COMMENT '数据应用秘钥',
    `app_desc` VARCHAR(256) COMMENT '数据应用描述',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE INDEX app_name_index (`app_name`),
    UNIQUE INDEX app_key_index (`app_key`),
    INDEX owner_index (`app_owner`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据应用已分配的数据API
CREATE TABLE IF NOT EXISTS yt_data_app_allocated_api (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `app_id` INT(11) NOT NULL COMMENT '数据App ID',
    `api_id` INT(11) NOT NULL COMMENT '数据API ID',
    `create_time` DATETIME NOT NULL COMMENT '分配时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    INDEX app_id_index (`app_id`),
    INDEX api_id_index (`api_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;