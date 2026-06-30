USE recruitment_platform;

ALTER TABLE `enterprise` 
ADD COLUMN IF NOT EXISTS `credit_code` VARCHAR(50) COMMENT '统一社会信用代码' AFTER `industry`,
ADD COLUMN IF NOT EXISTS `financing_stage` VARCHAR(50) COMMENT '融资阶段' AFTER `credit_code`,
ADD COLUMN IF NOT EXISTS `city` VARCHAR(50) COMMENT '所在城市' AFTER `financing_stage`,
ADD COLUMN IF NOT EXISTS `business_license_url` VARCHAR(500) COMMENT '营业执照URL' AFTER `business_license`,
ADD COLUMN IF NOT EXISTS `other_cert_url` VARCHAR(500) COMMENT '其他资质文件URL' AFTER `business_license_url`,
ADD COLUMN IF NOT EXISTS `contact_position` VARCHAR(50) COMMENT '联系人职位' AFTER `contact_name`,
ADD COLUMN IF NOT EXISTS `verify_status` TINYINT DEFAULT 0 COMMENT '认证状态：0-未提交，1-审核中，2-已通过，3-已拒绝' AFTER `verify_time`,
ADD COLUMN IF NOT EXISTS `reject_reason` VARCHAR(500) COMMENT '拒绝原因' AFTER `verify_status`,
ADD COLUMN IF NOT EXISTS `deleted_at` DATETIME COMMENT '删除时间（软删除）' AFTER `updated_at`;
