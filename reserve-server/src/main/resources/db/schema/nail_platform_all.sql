-- =============================================
-- 美甲预约平台完整14张基础表建表脚本
-- MySQL 8.0.46 适配 | 字符集utf8mb4 支持emoji
-- 规范：雪花ID主键、逻辑删除deleted、create_time/update_time自动维护、ext_json扩展字段
-- 无物理外键，代码层关联，适配高并发预约场景
-- =============================================

DROP DATABASE IF EXISTS nail_platform;
CREATE DATABASE nail_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE nail_platform;

-- --------------------------
-- 1.顾客用户表 user
-- --------------------------
CREATE TABLE `user` (
  `id` bigint NOT NULL COMMENT '雪花主键ID',
  `phone` varchar(20) NOT NULL COMMENT '手机号，唯一登录',
  `nickname` varchar(50) DEFAULT '' COMMENT '昵称',
  `avatar` varchar(255) DEFAULT '' COMMENT '头像OSS地址',
  `gender` tinyint DEFAULT 0 COMMENT '0未知 1女 2男',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1正常',
  `ext_json` json DEFAULT NULL COMMENT '扩展字段：肤质、美甲偏好等',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除 0未删 1已删',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_phone_deleted` (`phone`,`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='顾客用户表';

-- --------------------------
-- 2.品牌美甲门店表 shop
-- --------------------------
CREATE TABLE `shop` (
  `id` bigint NOT NULL COMMENT '门店ID',
  `shop_name` varchar(100) NOT NULL COMMENT '门店名称',
  `cover` varchar(255) DEFAULT '' COMMENT '门店封面图',
  `address` varchar(255) NOT NULL COMMENT '详细地址',
  `longitude` decimal(10,6) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10,6) DEFAULT NULL COMMENT '纬度',
  `contact_phone` varchar(20) NOT NULL COMMENT '门店联系电话',
  `business_license` varchar(255) DEFAULT '' COMMENT '营业执照图片',
  `business_hours` json DEFAULT NULL COMMENT '门店通用营业时间',
  `score` decimal(2,1) DEFAULT 5.0 COMMENT '综合评分1-5',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '0停业 1营业',
  `ext_json` json DEFAULT NULL COMMENT '门店配置扩展',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_status_score` (`status`,`score`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='品牌美甲门店表';

-- --------------------------
-- 3.美甲师表 artist（独立美甲师/门店美甲师通用）
-- --------------------------
CREATE TABLE `artist` (
  `id` bigint NOT NULL COMMENT '美甲师ID',
  `shop_id` bigint DEFAULT NULL COMMENT '门店ID，NULL=独立美甲师',
  `name` varchar(30) NOT NULL COMMENT '美甲师姓名',
  `avatar` varchar(255) DEFAULT '' COMMENT '头像',
  `work_year` tinyint DEFAULT 0 COMMENT '从业年限',
  `intro` varchar(1000) DEFAULT '' COMMENT '个人简介',
  `tags` varchar(200) DEFAULT '' COMMENT '擅长标签，逗号分隔',
  `score` decimal(2,1) DEFAULT 5.0 COMMENT '个人评分',
  `max_order_per_day` int DEFAULT 20 COMMENT '每日最大接单量',
  `work_status` tinyint NOT NULL DEFAULT 1 COMMENT '0休息 1可接单',
  `phone` varchar(20) NOT NULL COMMENT '登录手机号',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '0禁用 1正常',
  `ext_json` json DEFAULT NULL COMMENT '个人扩展配置',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_phone_deleted` (`phone`,`deleted`),
  KEY `idx_shop_id` (`shop_id`),
  KEY `idx_work_status_score` (`work_status`,`score`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='美甲师信息表';

-- --------------------------
-- 4.服务一级分类 service_category
-- --------------------------
CREATE TABLE `service_category` (
  `id` bigint NOT NULL COMMENT '分类ID',
  `cat_name` varchar(50) NOT NULL COMMENT '分类名称：美甲/美睫/手部护理',
  `icon` varchar(255) DEFAULT '' COMMENT '分类图标',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序权重',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '0下架 1上架',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_sort_status` (`sort`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='服务一级分类';

-- --------------------------
-- 5.标准服务项目 service_item
-- --------------------------
CREATE TABLE `service_item` (
  `id` bigint NOT NULL COMMENT '项目ID',
  `cat_id` bigint NOT NULL COMMENT '分类ID',
  `item_name` varchar(100) NOT NULL COMMENT '项目名称',
  `original_price` decimal(8,2) NOT NULL COMMENT '原价',
  `sale_price` decimal(8,2) NOT NULL COMMENT '售价',
  `duration` int NOT NULL COMMENT '服务时长，单位分钟',
  `cover_img` varchar(255) DEFAULT '' COMMENT '项目主图',
  `detail` varchar(2000) DEFAULT '' COMMENT '项目详情',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '0下架 1上架',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_cat_status` (`cat_id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标准服务项目';

-- --------------------------
-- 6.美甲师-服务多对多关联 artist_service
-- --------------------------
CREATE TABLE `artist_service` (
  `id` bigint NOT NULL,
  `artist_id` bigint NOT NULL COMMENT '美甲师ID',
  `item_id` bigint NOT NULL COMMENT '服务项目ID',
  `custom_price` decimal(8,2) DEFAULT NULL COMMENT '自定义定价，NULL使用标准售价',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '0不提供 1上架可预约',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_artist_item_deleted` (`artist_id`,`item_id`,`deleted`),
  KEY `idx_item_id` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='美甲师可提供服务关联';

-- --------------------------
-- 7.美甲师周固定排班 work_schedule
-- --------------------------
CREATE TABLE `work_schedule` (
  `id` bigint NOT NULL,
  `artist_id` bigint NOT NULL COMMENT '美甲师ID',
  `week_num` tinyint NOT NULL COMMENT '1周一~7周日',
  `start_time` time NOT NULL COMMENT '当日开始接单时间',
  `end_time` time NOT NULL COMMENT '当日停止接单时间',
  `is_rest` tinyint NOT NULL DEFAULT 0 COMMENT '0上班 1全天休息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_artist_week_deleted` (`artist_id`,`week_num`,`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='美甲师周固定排班';

-- --------------------------
-- 8.预约时段库存表 time_slot（防超卖核心表）
-- --------------------------
CREATE TABLE `time_slot` (
  `id` bigint NOT NULL COMMENT '时段唯一ID',
  `artist_id` bigint NOT NULL COMMENT '美甲师',
  `slot_date` date NOT NULL COMMENT '预约日期',
  `slot_start` datetime NOT NULL COMMENT '时段开始时间',
  `slot_end` datetime NOT NULL COMMENT '时段结束时间',
  `order_id` bigint DEFAULT NULL COMMENT '已预约则存订单ID',
  `slot_status` tinyint NOT NULL DEFAULT 1 COMMENT '1可预约 2已占用 3锁定 4过期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_artist_slot_start_deleted` (`artist_id`,`slot_start`,`deleted`),
  KEY `idx_artist_date_status` (`artist_id`,`slot_date`,`slot_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预约时段库存表';

-- --------------------------
-- 9.预约订单主表 order
-- --------------------------
CREATE TABLE `order` (
  `id` bigint NOT NULL COMMENT '订单雪花ID',
  `order_no` varchar(32) NOT NULL COMMENT '订单编号，唯一',
  `user_id` bigint NOT NULL COMMENT '顾客ID',
  `artist_id` bigint NOT NULL COMMENT '美甲师ID',
  `shop_id` bigint DEFAULT NULL COMMENT '所属门店，独立美甲师为NULL',
  `reserve_date` date NOT NULL COMMENT '预约日期',
  `reserve_start` datetime NOT NULL COMMENT '预约开始时段',
  `reserve_end` datetime NOT NULL COMMENT '预约结束时段',
  `total_amount` decimal(8,2) NOT NULL COMMENT '实付总金额',
  `order_status` tinyint NOT NULL DEFAULT 1 COMMENT '1待到店 2已完成 3已取消 4已退款',
  `pay_status` tinyint NOT NULL DEFAULT 0 COMMENT '0未支付 1已支付 2已退款',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `user_remark` varchar(500) DEFAULT '' COMMENT '顾客备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_status` (`user_id`,`order_status`),
  KEY `idx_artist_date` (`artist_id`,`reserve_date`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预约订单主表';

-- --------------------------
-- 10.订单服务明细表 order_item
-- --------------------------
CREATE TABLE `order_item` (
  `id` bigint NOT NULL,
  `order_id` bigint NOT NULL COMMENT '订单主表ID',
  `item_id` bigint NOT NULL COMMENT '服务项目ID',
  `price` decimal(8,2) NOT NULL COMMENT '下单单价',
  `num` int NOT NULL DEFAULT 1 COMMENT '数量',
  `sub_total` decimal(8,2) NOT NULL COMMENT '小计金额',
  `item_remark` varchar(300) DEFAULT '' COMMENT '单项备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单服务明细';

-- --------------------------
-- 11.优惠券模板 coupon
-- --------------------------
CREATE TABLE `coupon` (
  `id` bigint NOT NULL,
  `coupon_name` varchar(100) NOT NULL COMMENT '券名称',
  `denomination` decimal(8,2) NOT NULL COMMENT '减免金额',
  `full_limit` decimal(8,2) NOT NULL DEFAULT 0 COMMENT '满减门槛，0无门槛',
  `valid_start` date NOT NULL COMMENT '有效期开始',
  `valid_end` date NOT NULL COMMENT '有效期结束',
  `scope_type` tinyint NOT NULL COMMENT '1平台通用 2指定门店 3指定美甲师',
  `total_count` int NOT NULL DEFAULT 0 COMMENT '发放总量',
  `remain_count` int NOT NULL DEFAULT 0 COMMENT '剩余可领数量',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '0失效 1正常',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_scope_status` (`scope_type`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='优惠券模板';

-- --------------------------
-- 12.用户持有优惠券 user_coupon
-- --------------------------
CREATE TABLE `user_coupon` (
  `id` bigint NOT NULL,
  `user_id` bigint NOT NULL COMMENT '顾客ID',
  `coupon_id` bigint NOT NULL COMMENT '优惠券模板ID',
  `get_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
  `use_status` tinyint NOT NULL DEFAULT 0 COMMENT '0未使用 1已使用 2已过期',
  `use_order_id` bigint DEFAULT NULL COMMENT '核销订单ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_user_status` (`user_id`,`use_status`),
  KEY `idx_coupon_id` (`coupon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户持有优惠券';

-- --------------------------
-- 13.订单评价表 review
-- --------------------------
CREATE TABLE `review` (
  `id` bigint NOT NULL,
  `order_id` bigint NOT NULL COMMENT '关联订单ID',
  `user_id` bigint NOT NULL COMMENT '评价顾客',
  `artist_id` bigint NOT NULL COMMENT '被评价美甲师',
  `score` tinyint NOT NULL COMMENT '评分1-5星',
  `content` varchar(1000) DEFAULT '' COMMENT '评价文字',
  `img_list` varchar(1000) DEFAULT '' COMMENT '图片逗号分隔',
  `shop_reply` varchar(1000) DEFAULT '' COMMENT '商家回复',
  `review_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_deleted` (`order_id`,`deleted`),
  KEY `idx_artist_score` (`artist_id`,`score`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单评价';

-- --------------------------
-- 14.站内消息通知 notice
-- --------------------------
CREATE TABLE `notice` (
  `id` bigint NOT NULL,
  `receive_uid` bigint NOT NULL COMMENT '接收人ID',
  `user_type` tinyint NOT NULL COMMENT '1顾客 2美甲师 3门店管理员',
  `title` varchar(100) NOT NULL COMMENT '消息标题',
  `content` varchar(2000) NOT NULL COMMENT '消息内容',
  `msg_type` tinyint NOT NULL COMMENT '1预约提醒 2订单变更 3活动营销 4系统通知',
  `is_read` tinyint NOT NULL DEFAULT 0 COMMENT '0未读 1已读',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_receive_read` (`receive_uid`,`is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='站内消息通知';

-- --------------------------
-- 15.通用操作日志表 review
-- --------------------------
CREATE TABLE `sys_operation_log` (
   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
   `operator_id` bigint DEFAULT NULL COMMENT '操作人ID（管理员/用户openid映射ID）',
   `operator_name` varchar(64) DEFAULT NULL COMMENT '操作人名称',
   `module` varchar(64) NOT NULL COMMENT '操作模块（用户管理、预约管理、短信管理）',
   `operation` varchar(64) NOT NULL COMMENT '操作类型（新增/修改/删除/查询/导出）',
   `description` varchar(512) DEFAULT NULL COMMENT '操作详细描述',
   `request_url` varchar(255) NOT NULL COMMENT '请求接口地址',
   `request_method` varchar(16) NOT NULL COMMENT '请求方式 GET/POST/PUT/DELETE',
   `request_ip` varchar(64) DEFAULT NULL COMMENT '操作人IP地址',
   `request_params` text DEFAULT NULL COMMENT '接口请求入参',
   `response_result` text DEFAULT NULL COMMENT '接口返回结果',
   `cost_time` bigint DEFAULT 0 COMMENT '接口执行耗时(ms)',
   `log_level` varchar(16) NOT NULL DEFAULT 'INFO' COMMENT '日志级别 DEBUG/INFO/WARN/ERROR',
   `exception_info` text DEFAULT NULL COMMENT '异常堆栈信息（报错时记录）',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
   PRIMARY KEY (`id`),
   KEY idx_operator (`operator_id`),
   KEY idx_module_time (`module`, `create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统操作审计日志表';
-- =============================================
-- 执行完成说明：
-- 1.自动创建库 nail_platform，无需手动建库
-- 2.主键统一使用后端雪花算法生成，禁止自增id
-- 3.查询所有表统一条件 WHERE deleted = 0
-- 4.热点表 time_slot、order 已做联合索引优化，支持高并发预约
-- 5.ext_json 用于临时扩展属性，避免频繁ALTER TABLE
-- =============================================