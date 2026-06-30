-- ============================================
-- 导入历史记录表
-- ============================================

CREATE TABLE IF NOT EXISTS `import_history` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '导入记录ID',
    `file_name` VARCHAR(255) COMMENT '文件名',
    `file_size` BIGINT COMMENT '文件大小（字节）',
    `total_count` INT COMMENT '总条数',
    `success_count` INT COMMENT '成功条数',
    `fail_count` INT COMMENT '失败条数',
    `operator_id` BIGINT COMMENT '操作人ID',
    `operator` VARCHAR(100) COMMENT '操作人姓名',
    `import_type` VARCHAR(50) COMMENT '导入类型（student-学生导入）',
    `error_details` TEXT COMMENT '错误详情（JSON格式）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_at` DATETIME COMMENT '删除时间（软删除）',
    INDEX `idx_operator_id` (`operator_id`),
    INDEX `idx_import_type` (`import_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='导入历史记录表';
