-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: recruitment_platform
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `recruitment_platform`
--

/*!40000 DROP DATABASE IF EXISTS `recruitment_platform`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `recruitment_platform` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `recruitment_platform`;

--
-- Table structure for table `ai_favorite`
--

DROP TABLE IF EXISTS `ai_favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_favorite` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'optimize' COMMENT 'optimize/match/score',
  `title` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_favorite`
--

LOCK TABLES `ai_favorite` WRITE;
/*!40000 ALTER TABLE `ai_favorite` DISABLE KEYS */;
INSERT INTO `ai_favorite` VALUES (4,11,'这是一段测试内容，包含中文特殊字符','optimize','简历优化测试','2026-06-29 19:54:08','2026-06-29 19:54:08'),(5,11,'简历评分：77分（及格）\n\n完整性：30/30 - 基本信息已填写\n相关性：30/30 - 有明确的求职意向\n专业性：10/20 - 有工作经历描述\n格式规范性：4/10 - 格式基本规范\n亮点突出：3/10 - 个人亮点不够突出\n\n综合建议：建议优先完善基本信息和工作经历，明确求职意向，用数据量化成果来提升简历竞争力。','score','简历评分 77分','2026-06-29 23:52:54','2026-06-29 23:52:54'),(6,11,'张！看到你扎实的Java后端技术栈和清晰的项目实践脉——尤其在大完成基于Spring Cloud了RocketMQ（简历中误写为Active网关路由，这已远超同龄人的“广州+5K–10K+Backend”的明确诉求，我们为你精准匹配**推荐企业：广州极有限公司（专注S）****正扩建广州研发中心，急需熟悉SpringBoot3+MySQL+栈协作的应届生参与其「轻量级B2B订单协同平台」二期开发——划分（商品/订单/账户服务网关、短信网易背景工程师带队，能延续你此前在网易的测试经验形成“测-。**薪资与7.5K–（含餐补6月+季度绩效），每周5天坐班（弹性1小时），核心减幂等性改造及对接微信小程序订单API。**最佳简历修改建议**：① 删除冗余空行与乱码（如“姓 班为15911@qq.com（避免sunqi@test.com造成混淆）；② 将“”升级为《粗项目名），MQ消息重试机制设计率降低至0.量化结果；单列“微acos配置中心+**棠下村（距公司步行12分钟），整租800–2200元/月；地铁10号线（在建）开通前，可乘8:45两班）。**特别提示**：该公司采用“导师制双周迭代”，入职首月需完成Docker容器化部署训练；另提醒你携带证书（蓝湾未来实习证明）——他们该经历是隐藏加分项。','match','人岗匹配方案','2026-06-30 14:24:50','2026-06-30 14:24:50');
/*!40000 ALTER TABLE `ai_favorite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `education_experience`
--

DROP TABLE IF EXISTS `education_experience`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `education_experience` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'š╗ĆňÄćID',
  `resume_id` bigint NOT NULL COMMENT 'š«ÇňÄćID',
  `school_name` varchar(100) NOT NULL COMMENT 'ňşŽŠáíňÉŹšž░',
  `major` varchar(100) NOT NULL COMMENT 'ńŞôńŞÜ',
  `degree` varchar(50) NOT NULL COMMENT 'ňşŽňÄć',
  `start_date` date NOT NULL COMMENT 'ň╝ÇňžőŠŚÂÚŚ┤',
  `end_date` date DEFAULT NULL COMMENT 'š╗ôŠŁčŠŚÂÚŚ┤´╝łńŞ║šę║ŔíĘšĄ║Ŕç│ń╗Ő´╝ë',
  `description` text COMMENT 'ŠĆĆŔ┐░',
  `sort_order` int DEFAULT '0' COMMENT 'ŠÄĺň║Ć',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'ňłŤň╗║ŠŚÂÚŚ┤',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ŠŤ┤Šľ░ŠŚÂÚŚ┤',
  PRIMARY KEY (`id`),
  KEY `idx_resume_id` (`resume_id`),
  CONSTRAINT `education_experience_ibfk_1` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='ŠĽÖŔé▓š╗ĆňÄćŔíĘ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `education_experience`
--

LOCK TABLES `education_experience` WRITE;
/*!40000 ALTER TABLE `education_experience` DISABLE KEYS */;
/*!40000 ALTER TABLE `education_experience` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enterprise`
--

DROP TABLE IF EXISTS `enterprise`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enterprise` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ń╝üńŞÜID',
  `user_id` bigint NOT NULL COMMENT 'šöĘŠłĚID',
  `company_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ňůČňĆŞňÉŹšž░',
  `company_size` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ňůČňĆŞŔžäŠĘí',
  `industry` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ŠëÇň▒×ŔíîńŞÜ',
  `credit_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????????',
  `financing_stage` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `city` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `business_license` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ŔÉąńŞÜŠëžšůžňĆĚ',
  `business_license_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????URL',
  `other_cert_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??????URL',
  `logo` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ňůČňĆŞLOGO',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT 'ňůČňĆŞš«Çń╗ő',
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ňůČňĆŞňť░ňŁÇ',
  `website` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ňůČňĆŞň«śšŻĹ',
  `contact_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Ŕüöš│╗ń║║ňžôňÉŹ',
  `contact_position` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '?????',
  `contact_phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Ŕüöš│╗ń║║šöÁŔ»Ł',
  `contact_email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Ŕüöš│╗ń║║Úé«š«▒',
  `verified` tinyint DEFAULT '0' COMMENT 'Šś»ňÉŽŔ«ĄŔ»ü´╝Ü0-Šť¬Ŕ«ĄŔ»ü´╝î1-ňĚ▓Ŕ«ĄŔ»ü',
  `verify_time` datetime DEFAULT NULL COMMENT 'Ŕ«ĄŔ»üŠŚÂÚŚ┤',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'ňłŤň╗║ŠŚÂÚŚ┤',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ŠŤ┤Šľ░ŠŚÂÚŚ┤',
  `deleted_at` datetime DEFAULT NULL,
  `verify_status` int DEFAULT '0' COMMENT '?????0-????1-????2-????3-???',
  `reject_reason` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  KEY `idx_company_name` (`company_name`),
  KEY `idx_industry` (`industry`),
  KEY `idx_verified` (`verified`),
  CONSTRAINT `enterprise_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ń╝üńŞÜŔíĘ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enterprise`
--

LOCK TABLES `enterprise` WRITE;
/*!40000 ALTER TABLE `enterprise` DISABLE KEYS */;
INSERT INTO `enterprise` VALUES (1,1,'腾讯科技(深圳)有限公司','50-150','it','91110000MA01234567','a','??','91440300MA5H4U1L7T','data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAAAAAAAD/','','https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=tech%20company%20logo%20blue%20modern%20minimal&image_size=square','????????','??????xxx?xxx?','https://www.tencent.com','??','HR??','13800138000','hr@test.com',1,'2026-06-25 15:40:54','2026-06-16 16:44:35','2026-06-30 00:41:38',NULL,2,NULL),(2,2,'阿里巴巴集团控股有限公司','10000人以上','电子商务',NULL,NULL,NULL,NULL,NULL,NULL,'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=ecommerce%20company%20logo%20orange%20modern%20minimal&image_size=square','阿里巴巴是全球领先的电子商务平台，业务涵盖电商、云计算、数字媒体等领域。','杭州市西湖区文三路电子商务大厦','https://www.alibaba.com','张勇',NULL,'13800138002','contact@alibaba.com',0,NULL,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL,0,NULL),(3,3,'百度在线网络技术(北京)有限公司','10000人以上','互联网',NULL,NULL,NULL,NULL,NULL,NULL,'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=tech%20company%20logo%20red%20modern%20minimal&image_size=square','字节跳动是一家全球领先的科技公司，旗下产品包括抖音、今日头条等。','北京市海淀区中关村科技园8号楼','https://www.bytedance.com','张一鸣',NULL,'13800138003','contact@bytedance.com',0,NULL,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL,0,NULL),(4,4,'京东集团股份有限公司','10000人以上','通信设备',NULL,NULL,NULL,NULL,NULL,NULL,'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=telecom%20company%20logo%20red%20sun%20modern&image_size=square','华为是全球领先的ICT基础设施和智能终端提供商。','深圳市南山区科技园南区','https://www.huawei.com','任正非',NULL,'13800138004','contact@huawei.com',0,NULL,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL,0,NULL),(5,5,'美团','10000人以上','生活服务',NULL,NULL,NULL,NULL,NULL,NULL,'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=food%20delivery%20company%20logo%20yellow%20modern&image_size=square','美团是中国领先的生活服务平台，提供外卖、餐饮、旅游等服务。','北京市海淀区中关村科技园','https://www.meituan.com','王兴',NULL,'13800138005','contact@meituan.com',0,NULL,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL,0,NULL),(6,6,'字节跳动有限公司','10000人以上','电子商务',NULL,NULL,NULL,NULL,NULL,NULL,'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=ecommerce%20company%20logo%20red%20modern%20box&image_size=square','京东是中国领先的自营式电商平台。','北京市大兴区亦庄经济开发区','https://www.jd.com','刘强东',NULL,'13800138006','contact@jd.com',0,NULL,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL,0,NULL),(7,7,'网易(杭州)网络有限公司','1000-9999人','互联网',NULL,NULL,NULL,NULL,NULL,NULL,'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=smartphone%20company%20logo%20orange%20modern%20minimal&image_size=square','小米是一家以智能手机、IoT设备为核心的互联网公司。','北京市海淀区中关村科技园','https://www.mi.com','雷军',NULL,'13800138007','contact@xiaomi.com',0,NULL,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL,0,NULL),(8,8,'小米科技有限责任公司','1000-9999人','电子设备',NULL,NULL,NULL,NULL,NULL,NULL,'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=computer%20company%20logo%20blue%20modern%20tech&image_size=square','联想是全球领先的个人电脑制造商。','北京市海淀区中关村科技园','https://www.lenovo.com','杨元庆',NULL,'13800138008','contact@lenovo.com',0,NULL,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL,0,NULL),(9,9,'华为技术有限公司','1000-9999人','出行服务',NULL,NULL,NULL,NULL,NULL,NULL,'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=ride%20sharing%20company%20logo%20orange%20modern%20car&image_size=square','滴滴出行是全球领先的一站式出行平台。','北京市海淀区中关村科技园','https://www.didiglobal.com','程维',NULL,'13800138009','contact@didi.com',0,NULL,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL,0,NULL),(10,10,'滴滴出行科技有限公司','1000-9999人','游戏',NULL,NULL,NULL,NULL,NULL,NULL,'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=game%20company%20logo%20purple%20modern%20fantasy&image_size=square','网易是中国领先的互联网技术公司，业务涵盖游戏、邮箱、音乐等领域。','杭州市滨江区网商路','https://www.163.com','丁磊',NULL,'13800138010','contact@netease.com',0,NULL,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL,0,NULL);
/*!40000 ALTER TABLE `enterprise` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `import_history`
--

DROP TABLE IF EXISTS `import_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `import_history` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ň»╝ňůąŔ«░ňŻĽID',
  `file_name` varchar(255) DEFAULT NULL COMMENT 'Šľçń╗ÂňÉŹ',
  `file_size` bigint DEFAULT NULL COMMENT 'Šľçń╗ÂňĄžň░Ć´╝łňşŚŔŐé´╝ë',
  `total_count` int DEFAULT NULL COMMENT 'ŠÇ╗ŠŁíŠĽ░',
  `success_count` int DEFAULT NULL COMMENT 'ŠłÉňŐčŠŁíŠĽ░',
  `fail_count` int DEFAULT NULL COMMENT 'ňĄ▒Ŕ┤ąŠŁíŠĽ░',
  `operator_id` bigint DEFAULT NULL COMMENT 'ŠôŹńŻťń║║ID',
  `operator` varchar(100) DEFAULT NULL COMMENT 'ŠôŹńŻťń║║ňžôňÉŹ',
  `import_type` varchar(50) DEFAULT NULL COMMENT 'ň»╝ňůąš▒╗ň×ő´╝łstudent-ňşŽšöčň»╝ňůą´╝ë',
  `error_details` text COMMENT 'ÚöÖŔ»»Ŕ»ŽŠâů´╝łJSONŠá╝ň╝Ć´╝ë',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'ňłŤň╗║ŠŚÂÚŚ┤',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ŠŤ┤Šľ░ŠŚÂÚŚ┤',
  `deleted_at` datetime DEFAULT NULL COMMENT 'ňłáÚÖĄŠŚÂÚŚ┤´╝łŔŻ»ňłáÚÖĄ´╝ë',
  PRIMARY KEY (`id`),
  KEY `idx_operator_id` (`operator_id`),
  KEY `idx_import_type` (`import_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='ň»╝ňůąňÄćňĆ▓Ŕ«░ňŻĽŔíĘ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `import_history`
--

LOCK TABLES `import_history` WRITE;
/*!40000 ALTER TABLE `import_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `import_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `interview`
--

DROP TABLE IF EXISTS `interview`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `interview` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ÚŁóŔ»ĽID',
  `application_id` bigint NOT NULL COMMENT 'šö│Ŕ»ĚID',
  `interview_type` tinyint DEFAULT '1' COMMENT 'ÚŁóŔ»Ľš▒╗ň×ő´╝Ü1-ňłŁŔ»Ľ´╝î2-ňĄŹŔ»Ľ´╝î3-š╗łÚŁó',
  `interview_mode` tinyint DEFAULT '1' COMMENT 'ÚŁóŔ»ĽŠľ╣ň╝Ć´╝Ü1-šÄ░ňť║´╝î2-ŔžćÚóĹ´╝î3-šöÁŔ»Ł',
  `scheduled_time` datetime NOT NULL COMMENT 'ÚŁóŔ»ĽŠŚÂÚŚ┤',
  `duration` int DEFAULT '60' COMMENT 'ÚŁóŔ»ĽŠŚÂÚĽ┐´╝łňłćÚĺč´╝ë',
  `location` varchar(255) DEFAULT NULL COMMENT 'ÚŁóŔ»Ľňť░šé╣/ń╝ÜŔ««ÚôżŠÄą',
  `interviewer` varchar(100) DEFAULT NULL COMMENT 'ÚŁóŔ»Ľň«ś',
  `status` tinyint DEFAULT '1' COMMENT 'šŐÂŠÇü´╝Ü1-ňżůÚŁóŔ»Ľ´╝î2-ňĚ▓ň«îŠłÉ´╝î3-ňĚ▓ňĆľŠÂł',
  `result` tinyint DEFAULT NULL COMMENT 'š╗ôŠ×ť´╝Ü1-ÚÇÜŔ┐ç´╝î2-Šť¬ÚÇÜŔ┐ç´╝î3-ňżůň«Ü',
  `feedback` text COMMENT 'ÚŁóŔ»ĽňĆŹÚŽł',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'ňłŤň╗║ŠŚÂÚŚ┤',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ŠŤ┤Šľ░ŠŚÂÚŚ┤',
  PRIMARY KEY (`id`),
  KEY `idx_application_id` (`application_id`),
  KEY `idx_scheduled_time` (`scheduled_time`),
  KEY `idx_status` (`status`),
  CONSTRAINT `interview_ibfk_1` FOREIGN KEY (`application_id`) REFERENCES `job_application` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='ÚŁóŔ»ĽŔíĘ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `interview`
--

LOCK TABLES `interview` WRITE;
/*!40000 ALTER TABLE `interview` DISABLE KEYS */;
/*!40000 ALTER TABLE `interview` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job`
--

DROP TABLE IF EXISTS `job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ŔüîńŻŹID',
  `enterprise_id` bigint NOT NULL COMMENT 'ń╝üńŞÜID',
  `category_id` bigint DEFAULT NULL COMMENT 'ŔüîńŻŹňłćš▒╗ID',
  `job_title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ŔüîńŻŹňÉŹšž░',
  `job_type` tinyint DEFAULT NULL COMMENT 'ňĚąńŻťš▒╗ň×ő´╝Ü1-ňůĘŔüî´╝î2-ň«×ń╣á´╝î3-ňů╝Ŕüî',
  `salary_min` int DEFAULT NULL COMMENT 'ŠťÇńŻÄŔľ¬ŔÁä´╝łňůâ/Šťł´╝ë',
  `salary_max` int DEFAULT NULL COMMENT 'ŠťÇÚźśŔľ¬ŔÁä´╝łňůâ/Šťł´╝ë',
  `city` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ňĚąńŻťňčÄňŞé',
  `district` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ňĚąńŻťňî║ňčč',
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Ŕ»Žš╗ćňť░ňŁÇ',
  `experience_requirement` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'š╗ĆÚ¬îŔŽüŠ▒é',
  `education_requirement` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ňşŽňÄćŔŽüŠ▒é',
  `skill_requirements` json DEFAULT NULL COMMENT 'ŠŐÇŔâŻŔŽüŠ▒é´╝łJSONŠĽ░š╗ä´╝ë',
  `job_description` text COLLATE utf8mb4_unicode_ci COMMENT 'ŔüîńŻŹŠĆĆŔ┐░',
  `job_responsibility` text COLLATE utf8mb4_unicode_ci COMMENT 'ňĚąńŻťŔüîŔ┤ú',
  `benefits` json DEFAULT NULL COMMENT 'šŽĆňłęňżůÚüç´╝łJSONŠĽ░š╗ä´╝ë',
  `status` tinyint DEFAULT '1' COMMENT 'šŐÂŠÇü´╝Ü0-ńŞőŠ×Â´╝î1-ŠőŤŔüśńŞş´╝î2-ŠÜéňüť',
  `publish_time` datetime DEFAULT NULL COMMENT 'ňĆĹňŞâŠŚÂÚŚ┤',
  `expire_time` datetime DEFAULT NULL COMMENT 'Ŕ┐çŠťčŠŚÂÚŚ┤',
  `view_count` int DEFAULT '0' COMMENT 'ŠÁĆŔžłŠČíŠĽ░',
  `apply_count` int DEFAULT '0' COMMENT 'šö│Ŕ»ĚŠČíŠĽ░',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'ňłŤň╗║ŠŚÂÚŚ┤',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ŠŤ┤Šľ░ŠŚÂÚŚ┤',
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_enterprise_id` (`enterprise_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_city` (`city`),
  KEY `idx_salary` (`salary_min`,`salary_max`),
  KEY `idx_status` (`status`),
  KEY `idx_publish_time` (`publish_time`),
  FULLTEXT KEY `ft_job_title` (`job_title`,`job_description`),
  CONSTRAINT `job_ibfk_1` FOREIGN KEY (`enterprise_id`) REFERENCES `enterprise` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ŔüîńŻŹŔíĘ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job`
--

LOCK TABLES `job` WRITE;
/*!40000 ALTER TABLE `job` DISABLE KEYS */;
INSERT INTO `job` VALUES (1,1,NULL,'Java开发工程师',1,45,60,'深圳',NULL,'深圳市南山区科技园南区','5-10年','本科','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]','负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。','本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。','[\"五险一金\", \"年终奖\", \"带薪年假\", \"餐补\", \"交通补贴\"]',1,'2026-06-16 16:44:35',NULL,426,35,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(2,1,NULL,'Java开发工程师',1,30,45,'深圳',NULL,'深圳市南山区科技园南区','3-5年','本科','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]','负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。','本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。','[\"五险一金\", \"年终奖\", \"带薪年假\", \"餐补\", \"交通补贴\"]',1,'2026-06-16 16:44:35',NULL,581,48,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(3,1,NULL,'Java开发工程师',1,15,25,'深圳',NULL,'深圳市南山区科技园南区','1-3年','本科','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]','负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。','本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。','[\"五险一金\", \"股票期权\", \"年终双薪\", \"补充公积金\", \"年度体检\"]',1,'2026-06-16 16:44:35',NULL,721,65,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(4,1,NULL,'前端开发工程师',1,40,55,'深圳',NULL,'深圳市南山区科技园南区','5-10年','本科','[\"Vue.js\", \"React\", \"TypeScript\", \"Webpack\", \"CSS3\"]','负责前端页面的开发与维护，实现高质量的用户界面。与设计师协作，将设计稿转化为可交互的网页应用。优化前端性能，提升用户体验。','本科及以上学历，计算机相关专业。熟练掌握Vue.js或React框架。熟悉TypeScript、Webpack等前端技术。优秀的UI设计审美能力。','[\"五险一金\", \"年终奖\", \"带薪年假\", \"餐补\", \"交通补贴\"]',1,'2026-06-16 16:44:35',NULL,380,30,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(5,1,NULL,'前端开发工程师',1,25,38,'深圳',NULL,'深圳市南山区科技园南区','3-5年','本科','[\"Vue.js\", \"React\", \"TypeScript\", \"Webpack\", \"CSS3\"]','负责前端页面的开发与维护，实现高质量的用户界面。与设计师协作，将设计稿转化为可交互的网页应用。优化前端性能，提升用户体验。','本科及以上学历，计算机相关专业。熟练掌握Vue.js或React框架。熟悉TypeScript、Webpack等前端技术。优秀的UI设计审美能力。','[\"五险一金\", \"年终奖\", \"带薪年假\", \"餐补\", \"交通补贴\"]',1,'2026-06-16 16:44:35',NULL,520,42,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(6,1,NULL,'产品经理',1,35,50,'深圳',NULL,'深圳市南山区科技园南区','3-5年','本科','[\"产品设计\", \"需求分析\", \"原型设计\", \"数据分析\", \"用户研究\"]','进行产品规划、需求分析和用户研究，推动产品迭代优化。设计产品原型和用户界面，提升用户体验。与开发团队协作，确保产品按时交付。','本科及以上学历，产品管理、市场营销等相关专业。优秀的需求分析和产品规划能力。熟练使用Axure、Figma等工具。良好的沟通和协调能力。','[\"五险一金\", \"股票期权\", \"年终双薪\", \"补充公积金\", \"年度体检\"]',1,'2026-06-16 16:44:35',NULL,450,38,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(7,2,NULL,'Java开发工程师',1,50,70,'杭州',NULL,'杭州市西湖区文三路电子商务大厦','5-10年','本科','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]','负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。','本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。','[\"五险一金\", \"股票期权\", \"年终双薪\", \"补充公积金\", \"年度体检\"]',1,'2026-06-16 16:44:35',NULL,550,42,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(8,2,NULL,'Java开发工程师',1,32,50,'杭州',NULL,'杭州市西湖区文三路电子商务大厦','3-5年','本科','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]','负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。','本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。','[\"五险一金\", \"股票期权\", \"年终双薪\", \"补充公积金\", \"年度体检\"]',1,'2026-06-16 16:44:35',NULL,680,55,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(9,2,NULL,'前端开发工程师',1,42,58,'杭州',NULL,'杭州市西湖区文三路电子商务大厦','5-10年','本科','[\"Vue.js\", \"React\", \"TypeScript\", \"Webpack\", \"CSS3\"]','负责前端页面的开发与维护，实现高质量的用户界面。与设计师协作，将设计稿转化为可交互的网页应用。优化前端性能，提升用户体验。','本科及以上学历，计算机相关专业。熟练掌握Vue.js或React框架。熟悉TypeScript、Webpack等前端技术。优秀的UI设计审美能力。','[\"五险一金\", \"股票期权\", \"年终双薪\", \"补充公积金\", \"年度体检\"]',1,'2026-06-16 16:44:35',NULL,420,35,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(10,2,NULL,'前端开发工程师',1,38,52,'杭州',NULL,'杭州市西湖区文三路电子商务大厦','3-5年','本科','[\"SQL\", \"Python\", \"数据分析\", \"Excel\", \"可视化\"]','分析业务数据，提供数据驱动的决策支持。构建数据分析模型，挖掘数据价值。制作数据可视化报表，向管理层汇报。','本科及以上学历，统计学、数学等相关专业。熟练使用SQL、Python等数据分析工具。良好的数据可视化能力。优秀的逻辑思维能力。','[\"五险一金\", \"绩效奖金\", \"节日福利\", \"培训机会\", \"团建活动\"]',1,'2026-06-16 16:44:35',NULL,480,40,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(11,2,NULL,'产品经理',1,40,55,'杭州',NULL,'杭州市西湖区文三路电子商务大厦','3-5年','本科','[\"产品设计\", \"需求分析\", \"原型设计\", \"数据分析\", \"用户研究\"]','进行产品规划、需求分析和用户研究，推动产品迭代优化。设计产品原型和用户界面，提升用户体验。与开发团队协作，确保产品按时交付。','本科及以上学历，产品管理、市场营销等相关专业。优秀的需求分析和产品规划能力。熟练使用Axure、Figma等工具。良好的沟通和协调能力。','[\"五险一金\", \"股票期权\", \"年终双薪\", \"补充公积金\", \"年度体检\"]',1,'2026-06-16 16:44:35',NULL,520,45,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(12,2,NULL,'运营专员',1,12,18,'杭州',NULL,'杭州市西湖区文三路电子商务大厦','1-3年','本科','[\"运营策划\", \"用户增长\", \"活动运营\", \"内容运营\", \"数据分析\"]','负责用户运营和增长策略，提升用户活跃度和留存率。策划和执行运营活动，推动业务目标达成。分析运营数据，优化运营策略。','本科及以上学历，市场营销、广告等相关专业。优秀的活动策划和执行能力。良好的数据分析和文案写作能力。积极主动，有责任心。','[\"五险一金\", \"绩效奖金\", \"节日福利\", \"培训机会\", \"团建活动\"]',1,'2026-06-16 16:44:35',NULL,650,58,'2026-06-16 16:44:35','2026-06-16 16:44:35',NULL),(13,3,NULL,'Java开发工程师',1,48,68,'北京',NULL,'北京市海淀区中关村科技园8号楼','5-10年','本科','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]','负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。','本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。','[\"五险一金\", \"项目奖金\", \"弹性工作\", \"远程办公\", \"员工宿舍\"]',1,'2026-06-16 16:44:35',NULL,520,40,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(14,3,NULL,'前端开发工程师',1,45,62,'北京',NULL,'北京市海淀区中关村科技园8号楼','5-10年','本科','[\"Vue.js\", \"React\", \"TypeScript\", \"Webpack\", \"CSS3\"]','负责前端页面的开发与维护，实现高质量的用户界面。与设计师协作，将设计稿转化为可交互的网页应用。优化前端性能，提升用户体验。','本科及以上学历，计算机相关专业。熟练掌握Vue.js或React框架。熟悉TypeScript、Webpack等前端技术。优秀的UI设计审美能力。','[\"五险一金\", \"项目奖金\", \"弹性工作\", \"远程办公\", \"员工宿舍\"]',1,'2026-06-16 16:44:35',NULL,480,38,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(15,3,NULL,'产品经理',1,38,55,'北京',NULL,'北京市海淀区中关村科技园8号楼','3-5年','本科','[\"产品设计\", \"需求分析\", \"原型设计\", \"数据分析\", \"用户研究\"]','进行产品规划、需求分析和用户研究，推动产品迭代优化。设计产品原型和用户界面，提升用户体验。与开发团队协作，确保产品按时交付。','本科及以上学历，产品管理、市场营销等相关专业。优秀的需求分析和产品规划能力。熟练使用Axure、Figma等工具。良好的沟通和协调能力。','[\"五险一金\", \"项目奖金\", \"弹性工作\", \"远程办公\", \"员工宿舍\"]',1,'2026-06-16 16:44:35',NULL,460,42,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(16,3,NULL,'UI设计师',1,32,48,'北京',NULL,'北京市海淀区中关村科技园8号楼','3-5年','本科','[\"Photoshop\", \"Sketch\", \"Figma\", \"交互设计\", \"UI设计\"]','设计产品界面和用户体验，制作高质量的UI设计稿。与产品经理和开发团队协作，确保设计方案的实施。持续关注行业趋势，提升设计水平。','本科及以上学历，设计相关专业。熟练使用Photoshop、Sketch、Figma等设计工具。优秀的UI设计和交互设计能力。良好的团队协作精神。','[\"五险一金\", \"年终奖金\", \"带薪病假\", \"商业保险\", \"生日福利\"]',1,'2026-06-16 16:44:35',NULL,380,32,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(17,3,NULL,'测试工程师',1,25,38,'北京',NULL,'北京市海淀区中关村科技园8号楼','3-5年','本科','[\"Python\", \"自动化测试\", \"JUnit\", \"Selenium\", \"性能测试\"]','编写测试用例，执行功能测试和性能测试。定位和跟踪软件缺陷，确保产品质量。与开发团队协作，优化测试流程。','本科及以上学历，计算机相关专业。熟悉软件测试流程和方法。熟练使用JUnit、Selenium等测试工具。细心、耐心，具有良好的质量意识。','[\"五险一金\", \"年终奖金\", \"带薪病假\", \"商业保险\", \"生日福利\"]',1,'2026-06-16 16:44:35',NULL,420,35,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(18,3,NULL,'数据分析工程师',1,35,50,'北京',NULL,'北京市海淀区中关村科技园8号楼','3-5年','本科','[\"SQL\", \"Python\", \"数据分析\", \"Excel\", \"可视化\"]','分析业务数据，提供数据驱动的决策支持。构建数据分析模型，挖掘数据价值。制作数据可视化报表，向管理层汇报。','本科及以上学历，统计学、数学等相关专业。熟练使用SQL、Python等数据分析工具。良好的数据可视化能力。优秀的逻辑思维能力。','[\"五险一金\", \"项目奖金\", \"弹性工作\", \"远程办公\", \"员工宿舍\"]',1,'2026-06-16 16:44:35',NULL,450,38,'2026-06-16 16:44:35','2026-06-16 16:44:35',NULL),(19,4,NULL,'Java开发工程师',1,55,80,'深圳',NULL,'深圳市南山区科技园南区','5-10年','本科','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]','负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。','本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。','[\"五险一金\", \"年终奖\", \"带薪年假\", \"餐补\", \"交通补贴\"]',1,'2026-06-16 16:44:35',NULL,620,48,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(20,4,NULL,'Java开发工程师',1,35,55,'深圳',NULL,'深圳市南山区科技园南区','3-5年','本科','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]','负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。','本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。','[\"五险一金\", \"年终奖\", \"带薪年假\", \"餐补\", \"交通补贴\"]',1,'2026-06-16 16:44:35',NULL,758,62,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(21,4,NULL,'测试工程师',1,45,65,'深圳',NULL,'深圳市南山区科技园南区','5-10年','本科','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]','负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。','本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。','[\"五险一金\", \"年终奖\", \"带薪年假\", \"餐补\", \"交通补贴\"]',1,'2026-06-16 16:44:35',NULL,480,35,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(22,4,NULL,'前端开发工程师',1,42,58,'深圳',NULL,'深圳市南山区科技园南区','3-5年','本科','[\"Vue.js\", \"React\", \"TypeScript\", \"Webpack\", \"CSS3\"]','负责前端页面的开发与维护，实现高质量的用户界面。与设计师协作，将设计稿转化为可交互的网页应用。优化前端性能，提升用户体验。','本科及以上学历，计算机相关专业。熟练掌握Vue.js或React框架。熟悉TypeScript、Webpack等前端技术。优秀的UI设计审美能力。','[\"五险一金\", \"股票期权\", \"年终双薪\", \"补充公积金\", \"年度体检\"]',1,'2026-06-16 16:44:35',NULL,520,42,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(23,4,NULL,'测试工程师',1,28,42,'深圳',NULL,'深圳市南山区科技园南区','3-5年','本科','[\"Python\", \"自动化测试\", \"JUnit\", \"Selenium\", \"性能测试\"]','编写测试用例，执行功能测试和性能测试。定位和跟踪软件缺陷，确保产品质量。与开发团队协作，优化测试流程。','本科及以上学历，计算机相关专业。熟悉软件测试流程和方法。熟练使用JUnit、Selenium等测试工具。细心、耐心，具有良好的质量意识。','[\"五险一金\", \"绩效奖金\", \"节日福利\", \"培训机会\", \"团建活动\"]',1,'2026-06-16 16:44:35',NULL,480,40,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(24,4,NULL,'产品经理',1,42,58,'深圳',NULL,'深圳市南山区科技园南区','3-5年','本科','[\"产品设计\", \"需求分析\", \"原型设计\", \"数据分析\", \"用户研究\"]','进行产品规划、需求分析和用户研究，推动产品迭代优化。设计产品原型和用户界面，提升用户体验。与开发团队协作，确保产品按时交付。','本科及以上学历，产品管理、市场营销等相关专业。优秀的需求分析和产品规划能力。熟练使用Axure、Figma等工具。良好的沟通和协调能力。','[\"五险一金\", \"股票期权\", \"年终双薪\", \"补充公积金\", \"年度体检\"]',1,'2026-06-16 16:44:35',NULL,460,38,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(25,5,NULL,'Java开发工程师',1,42,58,'北京',NULL,'北京市海淀区中关村科技园','5-10年','本科','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]','负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。','本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。','[\"五险一金\", \"绩效奖金\", \"节日福利\", \"培训机会\", \"团建活动\"]',1,'2026-06-16 16:44:35',NULL,480,38,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(26,5,NULL,'Java开发工程师',1,28,42,'北京',NULL,'北京市海淀区中关村科技园','3-5年','本科','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]','负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。','本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。','[\"五险一金\", \"绩效奖金\", \"节日福利\", \"培训机会\", \"团建活动\"]',1,'2026-06-16 16:44:35',NULL,620,52,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(27,5,NULL,'前端开发工程师',1,38,52,'北京',NULL,'北京市海淀区中关村科技园','3-5年','本科','[\"Vue.js\", \"React\", \"TypeScript\", \"Webpack\", \"CSS3\"]','负责前端页面的开发与维护，实现高质量的用户界面。与设计师协作，将设计稿转化为可交互的网页应用。优化前端性能，提升用户体验。','本科及以上学历，计算机相关专业。熟练掌握Vue.js或React框架。熟悉TypeScript、Webpack等前端技术。优秀的UI设计审美能力。','[\"五险一金\", \"绩效奖金\", \"节日福利\", \"培训机会\", \"团建活动\"]',1,'2026-06-16 16:44:35',NULL,550,45,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(28,5,NULL,'运营专员',1,15,22,'北京',NULL,'北京市海淀区中关村科技园','1-3年','本科','[\"运营策划\", \"用户增长\", \"活动运营\", \"内容运营\", \"数据分析\"]','负责用户运营和增长策略，提升用户活跃度和留存率。策划和执行运营活动，推动业务目标达成。分析运营数据，优化运营策略。','本科及以上学历，市场营销、广告等相关专业。优秀的活动策划和执行能力。良好的数据分析和文案写作能力。积极主动，有责任心。','[\"五险一金\", \"项目奖金\", \"弹性工作\", \"远程办公\", \"员工宿舍\"]',1,'2026-06-16 16:44:35',NULL,788,68,'2026-06-16 16:44:35','2026-06-16 16:44:35',NULL),(29,5,NULL,'产品经理',1,35,50,'北京',NULL,'北京市海淀区中关村科技园','3-5年','本科','[\"产品设计\", \"需求分析\", \"原型设计\", \"数据分析\", \"用户研究\"]','进行产品规划、需求分析和用户研究，推动产品迭代优化。设计产品原型和用户界面，提升用户体验。与开发团队协作，确保产品按时交付。','本科及以上学历，产品管理、市场营销等相关专业。优秀的需求分析和产品规划能力。熟练使用Axure、Figma等工具。良好的沟通和协调能力。','[\"五险一金\", \"绩效奖金\", \"节日福利\", \"培训机会\", \"团建活动\"]',1,'2026-06-16 16:44:35',NULL,490,40,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(30,5,NULL,'数据分析工程师',1,32,45,'北京',NULL,'北京市海淀区中关村科技园','3-5年','本科','[\"SQL\", \"Python\", \"数据分析\", \"Excel\", \"可视化\"]','分析业务数据，提供数据驱动的决策支持。构建数据分析模型，挖掘数据价值。制作数据可视化报表，向管理层汇报。','本科及以上学历，统计学、数学等相关专业。熟练使用SQL、Python等数据分析工具。良好的数据可视化能力。优秀的逻辑思维能力。','[\"五险一金\", \"项目奖金\", \"弹性工作\", \"远程办公\", \"员工宿舍\"]',1,'2026-06-16 16:44:35',NULL,460,38,'2026-06-16 16:44:35','2026-06-16 16:44:35',NULL),(31,6,NULL,'Java开发工程师',1,45,62,'北京',NULL,'北京市大兴区亦庄经济开发区','5-10年','本科','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]','负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。','本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。','[\"五险一金\", \"股票期权\", \"年终双薪\", \"补充公积金\", \"年度体检\"]',1,'2026-06-16 16:44:35',NULL,520,42,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(32,6,NULL,'Java开发工程师',1,30,45,'北京',NULL,'北京市大兴区亦庄经济开发区','3-5年','本科','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]','负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。','本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。','[\"五险一金\", \"股票期权\", \"年终双薪\", \"补充公积金\", \"年度体检\"]',1,'2026-06-16 16:44:35',NULL,680,55,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(33,6,NULL,'前端开发工程师',1,40,55,'北京',NULL,'北京市大兴区亦庄经济开发区','3-5年','本科','[\"Vue.js\", \"React\", \"TypeScript\", \"Webpack\", \"CSS3\"]','负责前端页面的开发与维护，实现高质量的用户界面。与设计师协作，将设计稿转化为可交互的网页应用。优化前端性能，提升用户体验。','本科及以上学历，计算机相关专业。熟练掌握Vue.js或React框架。熟悉TypeScript、Webpack等前端技术。优秀的UI设计审美能力。','[\"五险一金\", \"股票期权\", \"年终双薪\", \"补充公积金\", \"年度体检\"]',1,'2026-06-16 16:44:35',NULL,550,45,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(34,6,NULL,'产品经理',1,38,52,'北京',NULL,'北京市大兴区亦庄经济开发区','3-5年','本科','[\"产品设计\", \"需求分析\", \"原型设计\", \"数据分析\", \"用户研究\"]','进行产品规划、需求分析和用户研究，推动产品迭代优化。设计产品原型和用户界面，提升用户体验。与开发团队协作，确保产品按时交付。','本科及以上学历，产品管理、市场营销等相关专业。优秀的需求分析和产品规划能力。熟练使用Axure、Figma等工具。良好的沟通和协调能力。','[\"五险一金\", \"绩效奖金\", \"节日福利\", \"培训机会\", \"团建活动\"]',1,'2026-06-16 16:44:35',NULL,480,40,'2026-06-16 16:44:35','2026-06-16 16:44:35',NULL),(35,6,NULL,'产品经理',1,14,20,'北京',NULL,'北京市大兴区亦庄经济开发区','1-3年','本科','[\"运营策划\", \"用户增长\", \"活动运营\", \"内容运营\", \"数据分析\"]','负责用户运营和增长策略，提升用户活跃度和留存率。策划和执行运营活动，推动业务目标达成。分析运营数据，优化运营策略。','本科及以上学历，市场营销、广告等相关专业。优秀的活动策划和执行能力。良好的数据分析和文案写作能力。积极主动，有责任心。','[\"五险一金\", \"项目奖金\", \"弹性工作\", \"远程办公\", \"员工宿舍\"]',1,'2026-06-16 16:44:35',NULL,720,62,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(36,6,NULL,'测试工程师',1,26,38,'北京',NULL,'北京市大兴区亦庄经济开发区','3-5年','本科','[\"Python\", \"自动化测试\", \"JUnit\", \"Selenium\", \"性能测试\"]','编写测试用例，执行功能测试和性能测试。定位和跟踪软件缺陷，确保产品质量。与开发团队协作，优化测试流程。','本科及以上学历，计算机相关专业。熟悉软件测试流程和方法。熟练使用JUnit、Selenium等测试工具。细心、耐心，具有良好的质量意识。','[\"五险一金\", \"绩效奖金\", \"节日福利\", \"培训机会\", \"团建活动\"]',1,'2026-06-16 16:44:35',NULL,490,42,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(37,7,NULL,'Java开发工程师',1,38,55,'北京',NULL,'北京市海淀区中关村科技园','5-10年','本科','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]','负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。','本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。','[\"五险一金\", \"年终奖金\", \"带薪病假\", \"商业保险\", \"生日福利\"]',1,'2026-06-16 16:44:35',NULL,450,35,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(38,7,NULL,'Java开发工程师',1,25,38,'北京',NULL,'北京市海淀区中关村科技园','3-5年','本科','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]','负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。','本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。','[\"五险一金\", \"年终奖金\", \"带薪病假\", \"商业保险\", \"生日福利\"]',1,'2026-06-16 16:44:35',NULL,580,48,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(39,7,NULL,'前端开发工程师',1,35,48,'北京',NULL,'北京市海淀区中关村科技园','3-5年','本科','[\"Vue.js\", \"React\", \"TypeScript\", \"Webpack\", \"CSS3\"]','负责前端页面的开发与维护，实现高质量的用户界面。与设计师协作，将设计稿转化为可交互的网页应用。优化前端性能，提升用户体验。','本科及以上学历，计算机相关专业。熟练掌握Vue.js或React框架。熟悉TypeScript、Webpack等前端技术。优秀的UI设计审美能力。','[\"五险一金\", \"年终奖金\", \"带薪病假\", \"商业保险\", \"生日福利\"]',1,'2026-06-16 16:44:35',NULL,520,42,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(40,7,NULL,'UI设计师',1,28,42,'北京',NULL,'北京市海淀区中关村科技园','3-5年','本科','[\"Photoshop\", \"Sketch\", \"Figma\", \"交互设计\", \"UI设计\"]','设计产品界面和用户体验，制作高质量的UI设计稿。与产品经理和开发团队协作，确保设计方案的实施。持续关注行业趋势，提升设计水平。','本科及以上学历，设计相关专业。熟练使用Photoshop、Sketch、Figma等设计工具。优秀的UI设计和交互设计能力。良好的团队协作精神。','[\"五险一金\", \"季度奖金\", \"年度旅游\", \"健身房\", \"下午茶\"]',1,'2026-06-16 16:44:35',NULL,420,35,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(41,7,NULL,'产品经理',1,32,45,'北京',NULL,'北京市海淀区中关村科技园','3-5年','本科','[\"产品设计\", \"需求分析\", \"原型设计\", \"数据分析\", \"用户研究\"]','进行产品规划、需求分析和用户研究，推动产品迭代优化。设计产品原型和用户界面，提升用户体验。与开发团队协作，确保产品按时交付。','本科及以上学历，产品管理、市场营销等相关专业。优秀的需求分析和产品规划能力。熟练使用Axure、Figma等工具。良好的沟通和协调能力。','[\"五险一金\", \"年终奖金\", \"带薪病假\", \"商业保险\", \"生日福利\"]',1,'2026-06-16 16:44:35',NULL,460,38,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(42,7,NULL,'测试工程师',1,22,35,'北京',NULL,'北京市海淀区中关村科技园','1-3年','本科','[\"Python\", \"自动化测试\", \"JUnit\", \"Selenium\", \"性能测试\"]','编写测试用例，执行功能测试和性能测试。定位和跟踪软件缺陷，确保产品质量。与开发团队协作，优化测试流程。','本科及以上学历，计算机相关专业。熟悉软件测试流程和方法。熟练使用JUnit、Selenium等测试工具。细心、耐心，具有良好的质量意识。','[\"五险一金\", \"季度奖金\", \"年度旅游\", \"健身房\", \"下午茶\"]',1,'2026-06-16 16:44:35',NULL,482,40,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(43,8,NULL,'Java开发工程师',1,35,50,'北京',NULL,'北京市海淀区中关村科技园','5-10年','本科','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]','负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。','本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。','[\"五险一金\", \"绩效奖金\", \"节日福利\", \"培训机会\", \"团建活动\"]',1,'2026-06-16 16:44:35',NULL,420,32,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(44,8,NULL,'Java开发工程师',1,22,35,'北京',NULL,'北京市海淀区中关村科技园','3-5年','本科','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]','负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。','本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。','[\"五险一金\", \"绩效奖金\", \"节日福利\", \"培训机会\", \"团建活动\"]',1,'2026-06-16 16:44:35',NULL,550,45,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(45,8,NULL,'前端开发工程师',1,32,45,'北京',NULL,'北京市海淀区中关村科技园','3-5年','本科','[\"Vue.js\", \"React\", \"TypeScript\", \"Webpack\", \"CSS3\"]','负责前端页面的开发与维护，实现高质量的用户界面。与设计师协作，将设计稿转化为可交互的网页应用。优化前端性能，提升用户体验。','本科及以上学历，计算机相关专业。熟练掌握Vue.js或React框架。熟悉TypeScript、Webpack等前端技术。优秀的UI设计审美能力。','[\"五险一金\", \"绩效奖金\", \"节日福利\", \"培训机会\", \"团建活动\"]',1,'2026-06-16 16:44:35',NULL,480,38,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(46,8,NULL,'产品经理',1,30,42,'北京',NULL,'北京市海淀区中关村科技园','3-5年','本科','[\"产品设计\", \"需求分析\", \"原型设计\", \"数据分析\", \"用户研究\"]','进行产品规划、需求分析和用户研究，推动产品迭代优化。设计产品原型和用户界面，提升用户体验。与开发团队协作，确保产品按时交付。','本科及以上学历，产品管理、市场营销等相关专业。优秀的需求分析和产品规划能力。熟练使用Axure、Figma等工具。良好的沟通和协调能力。','[\"五险一金\", \"项目奖金\", \"弹性工作\", \"远程办公\", \"员工宿舍\"]',1,'2026-06-16 16:44:35',NULL,440,35,'2026-06-16 16:44:35','2026-06-16 16:44:35',NULL),(47,8,NULL,'产品经理',1,28,40,'北京',NULL,'北京市海淀区中关村科技园','3-5年','本科','[\"SQL\", \"Python\", \"数据分析\", \"Excel\", \"可视化\"]','分析业务数据，提供数据驱动的决策支持。构建数据分析模型，挖掘数据价值。制作数据可视化报表，向管理层汇报。','本科及以上学历，统计学、数学等相关专业。熟练使用SQL、Python等数据分析工具。良好的数据可视化能力。优秀的逻辑思维能力。','[\"五险一金\", \"项目奖金\", \"弹性工作\", \"远程办公\", \"员工宿舍\"]',1,'2026-06-16 16:44:35',NULL,460,38,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(48,8,NULL,'测试工程师',1,20,32,'北京',NULL,'北京市海淀区中关村科技园','1-3年','本科','[\"Python\", \"自动化测试\", \"JUnit\", \"Selenium\", \"性能测试\"]','编写测试用例，执行功能测试和性能测试。定位和跟踪软件缺陷，确保产品质量。与开发团队协作，优化测试流程。','本科及以上学历，计算机相关专业。熟悉软件测试流程和方法。熟练使用JUnit、Selenium等测试工具。细心、耐心，具有良好的质量意识。','[\"五险一金\", \"年终奖金\", \"带薪病假\", \"商业保险\", \"生日福利\"]',1,'2026-06-16 16:44:35',NULL,450,38,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(49,9,NULL,'Java开发工程师',1,40,55,'北京',NULL,'北京市海淀区中关村科技园','5-10年','本科','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]','负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。','本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。','[\"五险一金\", \"项目奖金\", \"弹性工作\", \"远程办公\", \"员工宿舍\"]',1,'2026-06-16 16:44:35',NULL,460,36,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(50,9,NULL,'Java开发工程师',1,26,40,'北京',NULL,'北京市海淀区中关村科技园','3-5年','本科','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]','负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。','本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。','[\"五险一金\", \"项目奖金\", \"弹性工作\", \"远程办公\", \"员工宿舍\"]',1,'2026-06-16 16:44:35',NULL,590,48,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(51,9,NULL,'前端开发工程师',1,36,50,'北京',NULL,'北京市海淀区中关村科技园','3-5年','本科','[\"Vue.js\", \"React\", \"TypeScript\", \"Webpack\", \"CSS3\"]','负责前端页面的开发与维护，实现高质量的用户界面。与设计师协作，将设计稿转化为可交互的网页应用。优化前端性能，提升用户体验。','本科及以上学历，计算机相关专业。熟练掌握Vue.js或React框架。熟悉TypeScript、Webpack等前端技术。优秀的UI设计审美能力。','[\"五险一金\", \"项目奖金\", \"弹性工作\", \"远程办公\", \"员工宿舍\"]',1,'2026-06-16 16:44:35',NULL,520,42,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(52,9,NULL,'测试工程师',1,33,46,'北京',NULL,'北京市海淀区中关村科技园','3-5年','本科','[\"产品设计\", \"需求分析\", \"原型设计\", \"数据分析\", \"用户研究\"]','进行产品规划、需求分析和用户研究，推动产品迭代优化。设计产品原型和用户界面，提升用户体验。与开发团队协作，确保产品按时交付。','本科及以上学历，产品管理、市场营销等相关专业。优秀的需求分析和产品规划能力。熟练使用Axure、Figma等工具。良好的沟通和协调能力。','[\"五险一金\", \"年终奖金\", \"带薪病假\", \"商业保险\", \"生日福利\"]',1,'2026-06-16 16:44:35',NULL,450,38,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(53,9,NULL,'产品经理',1,12,18,'北京',NULL,'北京市海淀区中关村科技园','1-3年','本科','[\"运营策划\", \"用户增长\", \"活动运营\", \"内容运营\", \"数据分析\"]','负责用户运营和增长策略，提升用户活跃度和留存率。策划和执行运营活动，推动业务目标达成。分析运营数据，优化运营策略。','本科及以上学历，市场营销、广告等相关专业。优秀的活动策划和执行能力。良好的数据分析和文案写作能力。积极主动，有责任心。','[\"五险一金\", \"年终奖金\", \"带薪病假\", \"商业保险\", \"生日福利\"]',1,'2026-06-16 16:44:35',NULL,680,58,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(54,9,NULL,'UI设计师',1,25,38,'北京',NULL,'北京市海淀区中关村科技园','1-3年','本科','[\"Photoshop\", \"Sketch\", \"Figma\", \"交互设计\", \"UI设计\"]','设计产品界面和用户体验，制作高质量的UI设计稿。与产品经理和开发团队协作，确保设计方案的实施。持续关注行业趋势，提升设计水平。','本科及以上学历，设计相关专业。熟练使用Photoshop、Sketch、Figma等设计工具。优秀的UI设计和交互设计能力。良好的团队协作精神。','[\"五险一金\", \"季度奖金\", \"年度旅游\", \"健身房\", \"下午茶\"]',1,'2026-06-16 16:44:35',NULL,421,35,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(55,10,NULL,'Java开发工程师',1,55,75,'杭州',NULL,'杭州市滨江区网商路','5-10年','本科','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]','负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。','本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。','[\"五险一金\", \"季度奖金\", \"年度旅游\", \"健身房\", \"下午茶\"]',1,'2026-06-16 16:44:35',NULL,520,40,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(56,10,NULL,'Java开发工程师',1,32,48,'杭州',NULL,'杭州市滨江区网商路','3-5年','本科','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]','负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。','本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。','[\"五险一金\", \"季度奖金\", \"年度旅游\", \"健身房\", \"下午茶\"]',1,'2026-06-16 16:44:35',NULL,655,52,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(57,10,NULL,'Java开发工程师',1,28,45,'杭州',NULL,'杭州市滨江区网商路','1-3年','本科','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]','负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。','本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。','[\"五险一金\", \"季度奖金\", \"年度旅游\", \"健身房\", \"下午茶\"]',1,'2026-06-16 16:44:35',NULL,722,58,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(58,10,NULL,'前端开发工程师',1,26,38,'杭州',NULL,'杭州市滨江区网商路','1-3年','本科','[\"Python\", \"自动化测试\", \"JUnit\", \"Selenium\", \"性能测试\"]','编写测试用例，执行功能测试和性能测试。定位和跟踪软件缺陷，确保产品质量。与开发团队协作，优化测试流程。','本科及以上学历，计算机相关专业。熟悉软件测试流程和方法。熟练使用JUnit、Selenium等测试工具。细心、耐心，具有良好的质量意识。','[\"五险一金\", \"年终奖金\", \"带薪病假\", \"商业保险\", \"生日福利\"]',1,'2026-06-16 16:44:35',NULL,480,40,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(59,10,NULL,'产品经理',1,10,16,'杭州',NULL,'杭州市滨江区网商路','不限','大专','[\"运营策划\", \"用户增长\", \"活动运营\", \"内容运营\", \"数据分析\"]','负责用户运营和增长策略，提升用户活跃度和留存率。策划和执行运营活动，推动业务目标达成。分析运营数据，优化运营策略。','本科及以上学历，市场营销、广告等相关专业。优秀的活动策划和执行能力。良好的数据分析和文案写作能力。积极主动，有责任心。','[\"五险一金\", \"年终奖金\", \"带薪病假\", \"商业保险\", \"生日福利\"]',1,'2026-06-16 16:44:35',NULL,620,55,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL),(60,10,NULL,'测试工程师',1,30,45,'杭州',NULL,'杭州市滨江区网商路','1-3年','本科','[\"产品设计\", \"需求分析\", \"原型设计\", \"数据分析\", \"用户研究\"]','进行产品规划、需求分析和用户研究，推动产品迭代优化。设计产品原型和用户界面，提升用户体验。与开发团队协作，确保产品按时交付。','本科及以上学历，产品管理、市场营销等相关专业。优秀的需求分析和产品规划能力。熟练使用Axure、Figma等工具。良好的沟通和协调能力。','[\"五险一金\", \"季度奖金\", \"年度旅游\", \"健身房\", \"下午茶\"]',1,'2026-06-16 16:44:35',NULL,472,39,'2026-06-16 16:44:35','2026-06-30 00:41:38',NULL);
/*!40000 ALTER TABLE `job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_application`
--

DROP TABLE IF EXISTS `job_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_application` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'šö│Ŕ»ĚID',
  `user_id` bigint DEFAULT NULL,
  `job_id` bigint NOT NULL COMMENT 'ŔüîńŻŹID',
  `student_id` bigint DEFAULT NULL,
  `resume_id` bigint DEFAULT NULL,
  `status` tinyint DEFAULT '1' COMMENT 'šŐÂŠÇü´╝Ü1-ňĚ▓ŠŐĽÚÇĺ´╝î2-ňĚ▓Ščąšťő´╝î3-ÚŁóŔ»ĽńŞş´╝î4-ňĚ▓ňŻĽšöĘ´╝î5-ňĚ▓Šőĺš╗Ł´╝î6-ňĚ▓ňĆľŠÂł',
  `cover_letter` text COMMENT 'Š▒éŔüîń┐í',
  `apply_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'šö│Ŕ»ĚŠŚÂÚŚ┤',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ŠŤ┤Šľ░ŠŚÂÚŚ┤',
  `interview_time` datetime DEFAULT NULL COMMENT 'ÚŁóŔ»ĽŠŚÂÚŚ┤',
  `interview_location` varchar(255) DEFAULT NULL COMMENT 'ÚŁóŔ»Ľňť░šé╣',
  `interview_contact` varchar(100) DEFAULT NULL COMMENT 'ÚŁóŔ»ĽŔüöš│╗ń║║',
  `feedback` text COMMENT 'ňĆŹÚŽłŠäĆŔžü',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'ňłŤň╗║ŠŚÂÚŚ┤',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ŠŤ┤Šľ░ŠŚÂÚŚ┤',
  `deleted_at` timestamp NULL DEFAULT NULL,
  `enterprise_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_job_student` (`job_id`,`student_id`),
  KEY `resume_id` (`resume_id`),
  KEY `idx_job_id` (`job_id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_status` (`status`),
  KEY `idx_apply_time` (`apply_time`),
  CONSTRAINT `job_application_ibfk_1` FOREIGN KEY (`job_id`) REFERENCES `job` (`id`),
  CONSTRAINT `job_application_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
  CONSTRAINT `job_application_ibfk_3` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='ŔüîńŻŹšö│Ŕ»ĚŔíĘ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_application`
--

LOCK TABLES `job_application` WRITE;
/*!40000 ALTER TABLE `job_application` DISABLE KEYS */;
INSERT INTO `job_application` VALUES (7,11,1,1,NULL,2,'test','2026-06-16 17:39:08','2026-06-30 11:24:54','2026-06-15 10:00:00','Guangzhou',NULL,NULL,'2026-06-16 17:39:08','2026-06-16 17:39:08',NULL,1),(8,11,2,1,NULL,3,'test','2026-06-16 17:39:08','2026-06-30 11:27:17','2026-06-14 14:30:00','Online',NULL,NULL,'2026-06-16 17:39:08','2026-06-16 17:39:08',NULL,1),(9,11,3,1,NULL,3,'test','2026-06-16 17:39:08','2026-06-30 11:32:08','2026-06-16 09:00:00','-',NULL,NULL,'2026-06-16 17:39:08','2026-06-16 17:39:08',NULL,1),(10,11,4,1,NULL,2,'test','2026-06-16 17:39:08','2026-06-30 14:01:39','2026-06-13 15:00:00','Guangzhou',NULL,NULL,'2026-06-16 17:39:08','2026-06-16 17:39:08',NULL,1),(11,11,5,1,NULL,3,'test','2026-06-16 17:39:08','2026-06-16 17:39:08','2026-06-12 10:30:00','Online',NULL,NULL,'2026-06-16 17:39:08','2026-06-16 17:39:08',NULL,1),(12,11,6,1,NULL,2,'test','2026-06-16 17:39:08','2026-06-16 17:39:08','2026-06-17 11:00:00','Guangzhou',NULL,NULL,'2026-06-16 17:39:08','2026-06-16 17:39:08',NULL,1),(13,12,1,2,8,1,'????????','2026-06-28 00:43:36','2026-06-28 00:43:36',NULL,NULL,NULL,NULL,'2026-06-28 00:43:36','2026-06-28 00:43:36',NULL,1),(14,12,2,2,8,2,'??Java???????','2026-06-28 00:43:36','2026-06-28 00:43:36',NULL,NULL,NULL,NULL,'2026-06-28 00:43:36','2026-06-28 00:43:36',NULL,1),(15,12,3,2,8,3,'??????','2026-06-28 00:43:36','2026-06-28 00:43:36','2026-07-01 10:00:00','??????',NULL,NULL,'2026-06-28 00:43:36','2026-06-28 00:43:36',NULL,1),(16,12,4,2,8,4,'????????','2026-06-28 00:43:36','2026-06-28 00:43:36','2026-06-28 14:00:00','??????',NULL,'????','2026-06-28 00:43:36','2026-06-28 00:43:36',NULL,1),(17,12,5,2,8,5,'??????','2026-06-28 00:43:36','2026-06-28 00:43:36',NULL,NULL,NULL,'?????','2026-06-28 00:43:36','2026-06-28 00:43:36',NULL,1),(18,11,60,1,NULL,1,'','2026-06-30 10:28:26','2026-06-30 10:28:26',NULL,NULL,NULL,NULL,'2026-06-30 10:28:26','2026-06-30 10:28:26',NULL,10);
/*!40000 ALTER TABLE `job_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_category`
--

DROP TABLE IF EXISTS `job_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ňłćš▒╗ID',
  `category_name` varchar(50) NOT NULL COMMENT 'ňłćš▒╗ňÉŹšž░',
  `parent_id` bigint DEFAULT '0' COMMENT 'šłÂňłćš▒╗ID´╝ł0ŔíĘšĄ║ńŞÇš║žňłćš▒╗´╝ë',
  `sort_order` int DEFAULT '0' COMMENT 'ŠÄĺň║Ć',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'ňłŤň╗║ŠŚÂÚŚ┤',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ŠŤ┤Šľ░ŠŚÂÚŚ┤',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='ŔüîńŻŹňłćš▒╗ŔíĘ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_category`
--

LOCK TABLES `job_category` WRITE;
/*!40000 ALTER TABLE `job_category` DISABLE KEYS */;
INSERT INTO `job_category` VALUES (1,'技术类',0,1,'2026-06-13 16:33:43','2026-06-30 08:53:02'),(2,'产品类',0,2,'2026-06-13 16:33:43','2026-06-30 08:53:02'),(3,'设计类',0,3,'2026-06-13 16:33:43','2026-06-30 08:53:02'),(4,'市场类',0,4,'2026-06-13 16:33:43','2026-06-30 08:53:02'),(5,'运营类',0,5,'2026-06-13 16:33:43','2026-06-30 08:53:02'),(6,'职能类',0,6,'2026-06-13 16:33:43','2026-06-30 08:53:02'),(7,'Java开发',1,1,'2026-06-13 16:33:43','2026-06-30 08:53:02'),(8,'前端开发',1,2,'2026-06-13 16:33:43','2026-06-30 08:53:02'),(9,'Python开发',1,3,'2026-06-13 16:33:43','2026-06-30 08:53:02'),(10,'数据分析',1,4,'2026-06-13 16:33:43','2026-06-30 08:53:02'),(11,'AI/机器学习',1,5,'2026-06-13 16:33:43','2026-06-30 08:53:02'),(12,'产品经理',2,1,'2026-06-13 16:33:43','2026-06-30 08:53:02'),(13,'UI设计',3,1,'2026-06-13 16:33:43','2026-06-30 08:53:02'),(14,'UX设计',3,2,'2026-06-13 16:33:43','2026-06-30 08:53:02');
/*!40000 ALTER TABLE `job_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_favorite`
--

DROP TABLE IF EXISTS `job_favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_favorite` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint NOT NULL,
  `job_id` bigint NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_job` (`student_id`,`job_id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_job_id` (`job_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_favorite`
--

LOCK TABLES `job_favorite` WRITE;
/*!40000 ALTER TABLE `job_favorite` DISABLE KEYS */;
INSERT INTO `job_favorite` VALUES (1,11,60,'2026-06-30 10:28:28');
/*!40000 ALTER TABLE `job_favorite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ÚÇÜščąID',
  `receiver_id` bigint NOT NULL COMMENT 'ŠÄąŠöÂŔÇůID',
  `sender_id` bigint DEFAULT NULL COMMENT 'ňĆĹÚÇüŔÇůID',
  `type` tinyint NOT NULL COMMENT 'ÚÇÜščąš▒╗ň×ő´╝Ü1-š│╗š╗čÚÇÜščą´╝î2-šö│Ŕ»ĚšŐÂŠÇüŠŤ┤Šľ░´╝î3-ÚŁóŔ»ĽÚÇÜščą´╝î4-Šľ░ŔüîńŻŹŠÄĘŔŹÉ',
  `title` varchar(200) NOT NULL COMMENT 'ÚÇÜščąŠáçÚóś',
  `content` text COMMENT 'ÚÇÜščąňćůň«╣',
  `is_read` tinyint DEFAULT '0' COMMENT 'Šś»ňÉŽňĚ▓Ŕ»╗´╝Ü0-Šť¬Ŕ»╗´╝î1-ňĚ▓Ŕ»╗',
  `read_time` datetime DEFAULT NULL COMMENT 'Ŕ»╗ňĆľŠŚÂÚŚ┤',
  `related_id` bigint DEFAULT NULL COMMENT 'ňů│ŔüöID´╝łňŽéšö│Ŕ»ĚIDŃÇüŔüîńŻŹIDšşë´╝ë',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'ňłŤň╗║ŠŚÂÚŚ┤',
  PRIMARY KEY (`id`),
  KEY `idx_receiver_id` (`receiver_id`),
  KEY `idx_is_read` (`is_read`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='ÚÇÜščąŠÂłŠü»ŔíĘ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1,11,NULL,1,'投递状态更新','您投递的「Java开发工程师」状态更新为：已安排面试',1,NULL,8,'2026-06-30 11:27:16'),(2,11,NULL,1,'投递状态更新','您投递的「Java开发工程师」状态更新为：已安排面试',1,NULL,9,'2026-06-30 11:32:08'),(3,11,NULL,1,'投递状态更新','您投递的「前端开发工程师」状态更新为：通过初筛',1,NULL,10,'2026-06-30 14:01:39');
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operation_log`
--

DROP TABLE IF EXISTS `operation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `operation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ŠŚąň┐ŚID',
  `user_id` bigint DEFAULT NULL COMMENT 'šöĘŠłĚID',
  `operation` varchar(100) DEFAULT NULL COMMENT 'ŠôŹńŻťŠĆĆŔ┐░',
  `method` varchar(10) DEFAULT NULL COMMENT 'Ŕ»ĚŠ▒éŠľ╣Š│Ľ',
  `url` varchar(255) DEFAULT NULL COMMENT 'Ŕ»ĚŠ▒éURL',
  `ip` varchar(50) DEFAULT NULL COMMENT 'IPňť░ňŁÇ',
  `user_agent` varchar(500) DEFAULT NULL COMMENT 'User Agent',
  `request_params` json DEFAULT NULL COMMENT 'Ŕ»ĚŠ▒éňĆéŠĽ░',
  `response_data` json DEFAULT NULL COMMENT 'ňôŹň║öŠĽ░ŠŹ«',
  `status` tinyint DEFAULT '1' COMMENT 'šŐÂŠÇü´╝Ü1-ŠłÉňŐč´╝î0-ňĄ▒Ŕ┤ą',
  `error_message` text COMMENT 'ÚöÖŔ»»ń┐íŠü»',
  `execution_time` int DEFAULT NULL COMMENT 'ŠëžŔíîŠŚÂÚŚ┤´╝łŠ»źšžĺ´╝ë',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'ňłŤň╗║ŠŚÂÚŚ┤',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='ŠôŹńŻťŠŚąň┐ŚŔíĘ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operation_log`
--

LOCK TABLES `operation_log` WRITE;
/*!40000 ALTER TABLE `operation_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `operation_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_experience`
--

DROP TABLE IF EXISTS `project_experience`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project_experience` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Úí╣šŤ«ID',
  `resume_id` bigint NOT NULL COMMENT 'š«ÇňÄćID',
  `project_name` varchar(100) NOT NULL COMMENT 'Úí╣šŤ«ňÉŹšž░',
  `role` varchar(100) DEFAULT NULL COMMENT 'Šőůń╗╗ŔžĺŔë▓',
  `start_date` date NOT NULL COMMENT 'ň╝ÇňžőŠŚÂÚŚ┤',
  `end_date` date DEFAULT NULL COMMENT 'š╗ôŠŁčŠŚÂÚŚ┤',
  `description` text COMMENT 'Úí╣šŤ«ŠĆĆŔ┐░',
  `responsibility` text COMMENT 'Ŕ┤úń╗╗ŠĆĆŔ┐░',
  `sort_order` int DEFAULT '0' COMMENT 'ŠÄĺň║Ć',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'ňłŤň╗║ŠŚÂÚŚ┤',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ŠŤ┤Šľ░ŠŚÂÚŚ┤',
  PRIMARY KEY (`id`),
  KEY `idx_resume_id` (`resume_id`),
  CONSTRAINT `project_experience_ibfk_1` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Úí╣šŤ«š╗ĆÚ¬îŔíĘ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_experience`
--

LOCK TABLES `project_experience` WRITE;
/*!40000 ALTER TABLE `project_experience` DISABLE KEYS */;
/*!40000 ALTER TABLE `project_experience` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resume`
--

DROP TABLE IF EXISTS `resume`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resume` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'š«ÇňÄćID',
  `student_id` bigint NOT NULL COMMENT 'ňşŽšöčID',
  `resume_name` varchar(100) NOT NULL DEFAULT 'ŠłĹšÜäš«ÇňÄć' COMMENT 'š«ÇňÄćňÉŹšž░',
  `is_default` tinyint DEFAULT '0' COMMENT 'Šś»ňÉŽÚ╗śŔ«Ąš«ÇňÄć´╝Ü0-ňÉŽ´╝î1-Šś»',
  `is_public` tinyint DEFAULT '1' COMMENT 'Šś»ňÉŽňůČň╝Ç´╝Ü0-ňÉŽ´╝î1-Šś»',
  `completeness` int DEFAULT '0' COMMENT 'ň«îŠĽ┤ň║Ž´╝łšÖżňłćŠ»ö´╝ë',
  `view_count` int DEFAULT '0' COMMENT 'ŔóźŠčąšťőŠČíŠĽ░',
  `download_count` int DEFAULT '0' COMMENT 'ŔóźńŞőŔŻŻŠČíŠĽ░',
  `file_url` varchar(255) DEFAULT NULL COMMENT 'š«ÇňÄćŠľçń╗ÂURL´╝łPDF´╝ë',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'ňłŤň╗║ŠŚÂÚŚ┤',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ŠŤ┤Šľ░ŠŚÂÚŚ┤',
  `real_name` varchar(50) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `birth_date` varchar(20) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `summary` text,
  `expected_position` varchar(100) DEFAULT NULL,
  `expected_city` varchar(50) DEFAULT NULL,
  `expected_salary` varchar(50) DEFAULT NULL,
  `educations` text,
  `experiences` text,
  `projects` text,
  `skills` text,
  `languages` text,
  `certificates` text,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_is_default` (`is_default`),
  KEY `idx_is_public` (`is_public`),
  CONSTRAINT `resume_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='š«ÇňÄćŔíĘ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resume`
--

LOCK TABLES `resume` WRITE;
/*!40000 ALTER TABLE `resume` DISABLE KEYS */;
INSERT INTO `resume` VALUES (2,1,'张榕琛(1).pdf',1,1,100,1,0,'/uploads/resumes/f7097d093f184193938cd7923eaad498.pdf','2026-06-16 02:17:04','2026-06-30 14:24:13','张榕琛','male','2001-03-20','13900139001','sunqi@test.com','上海','姓 \r\n班\r\n啊\r\n名：张榕琛 \r\n级：软件241 \r\n出生年月：20060408 \r\n电 话：15913983191 \r\n邮 件：947386424@qq.com \r\n住 址：汕头市澄海金冠园 \r\n求职意向  \r\n工作经验  \r\n项目经验  \r\n* \r\n \r\n \r\n \r\n⚫ Java 开发工程师 \r\n \r\n \r\n⚫ 2024.09-2027.01 人工智能学院 软件工程 \r\n \r\n \r\n⚫ 2025.07~2025.08 蓝湾未来有限公司 主教 教大疆无人机，RoboMastere,简单\r\n的python编写 \r\n \r\n \r\n⚫ 2026.4~2026.5 自学电商订单系统 \r\n【开发环境】：jdk17、springboot3、mysql、svn; \r\n【开发工具】：idea、nginx、FinalShell、VMware \r\n【开发技术】：前端：Vue + Element UI + Mint UI + MUI \r\n买家端（移动端）：Vue + Mint UI + MUI \r\n卖家端（PC 端）：Vue + Element UI \r\n后端：Spring Boot + Spring Cloud Alibaba + MyBatis Plus + MySQL + Rocket MQ + Redis + Gateway + \r\n第三方短信服务接口 \r\n \r\n【项目简介】：主要为基于互联网技术，整合电商全流程业务的信息化管理软件，通过数字化重构传统商\r\n业流程，帮助企业实现商品、订单、库存、财务等全链路的高效管理。是由SpringBoot+Spring Cloud搭\r\n建的SSM微服务系统。 \r\n主要分为： \r\n后台服务系统：下单Product-service商品服务，Order-service订单服务。 \r\n用户服务系统： Account-service账户服务。 \r\n \r\n【责 任 描 述】： \r\n教育背景  \r\n \r\nRESUME \r\n个人简历  \r\n技能特长  \r\n自我评价  \r\n1、登录模块：负责登录与权限的控制。 \r\n2、订单模块：ActiveMQ来进行项目中所有订单的处理 \r\n3、网关模块：Gateway。 \r\n \r\n【技 术 描 述】： \r\n 基于SpringBoot进行微服务开发，基础框架是Mybatis，Spring ， SpringMVC. \r\n 使用Mybatis 的通用Mapper插件自动生成SQL语句. \r\n 使用redis 存储商品详细信息，用户信息 \r\n 使用jwt生成token保存到cookie以及在redis中保存用户登录状态实现单点登陆。 \r\n 使用ActiveMQ信息队列，实现系统的异步消息管理并实现分布式事务 \r\n  \r\n \r\n \r\n \r\n◆ 熟练掌握javaSE核心语法，熟悉集合、反射、泛型、多线程、IO、枚举等技术的使用。熟悉面向对象、面向接口、面向\r\n切面编程等编程思想。 \r\n◆ 熟悉主流Spring、SpringBoot、SpringMVC、MyBatis，SpringCloudAlibaba,SpringAI等框架 \r\n◆ 熟练使用MySQL关系型数据库以及非关系型数据库 Redis \r\n◆ 熟练运用 Tomcat、Jetty、Nginx 应用服务器 \r\n◆ 熟练使用PyChram、Idea、PL/SQL、Postman等开发工具 \r\n◆ 熟练使用html、css、js、vue等前端技术 \r\n◆ 熟悉kafaka+zookeeper（分布式服务框架）、Springcloud，MQ，Nacos \r\n◆ 安装部署，Deepseek,阿里百炼本地部署在idea \r\n \r\n \r\n◼  有较强的自学能力和团队协作精神，做事踏实负责，能吃苦 \r\n◼ 热爱互联网行业，能快速的适应自身周边的环境 \r\n◼ 有积极进取的工作精神和实际动手能力，对新知识，新技术有着强烈的求知欲与良好的接受能力 \r\n◼ 具有良好的抗压能力，能够对外界压力保持较好的态度面对 \r\n','backend','广州','5K-10K','[{\"major\":\"软件工程\",\"startDate\":\"2019-09\",\"degree\":\"本科\",\"endDate\":\"2023-06\",\"school\":\"广东轻工职业技术大学\"}]','[{\"position\":\"软件工程\",\"startDate\":\"2023-07\",\"company\":\"网易\",\"description\":\"测试\",\"endDate\":\"2024-12\"}]','[]','[{\"level\":\"精通\",\"name\":\"Axure\"},\"Java\"]','[{\"name\":\"Java\",\"level\":\"fluent\"}]','[{\"name\":\"软考证书\",\"issuer\":\"工信部\"}]',NULL),(3,1,'??????',1,1,100,0,0,NULL,'2026-06-16 02:19:02','2026-06-16 02:19:02','???','?','2000-01-15','13900139001','student1@test.com','??','??????????????????????????','Java?????','??','15K','[{\"school\":\"????\",\"major\":\"????????\",\"degree\":\"??\",\"startDate\":\"2021-09\",\"endDate\":\"2025-06\"}]','[{\"company\":\"??\",\"position\":\"Java?????\",\"startDate\":\"2024-06\",\"endDate\":\"2024-09\",\"description\":\"?????????????????????\"}]','[{\"name\":\"??????\",\"description\":\"??Spring Boot????????????????????????\"}]','[{\"name\":\"Java\",\"level\":\"??\"},{\"name\":\"Spring Boot\",\"level\":\"??\"},{\"name\":\"MySQL\",\"level\":\"??\"},{\"name\":\"Redis\",\"level\":\"??\"}]','[{\"name\":\"??\",\"level\":\"CET-6\"}]','[{\"name\":\"?????\",\"date\":\"2022-03\"}]',NULL),(4,1,'张同学的简历',1,1,100,0,0,NULL,'2026-06-16 02:19:50','2026-06-16 02:19:50','张同学','男','2000-01-15','13900139001','student1@test.com','北京','热爱编程，积极进取，有较强的学习能力和团队协作精神。','Java开发工程师','北京','15K','[{\"school\":\"北京大学\",\"major\":\"计算机科学与技术\",\"degree\":\"本科\",\"startDate\":\"2021-09\",\"endDate\":\"2025-06\"}]','[{\"company\":\"腾讯\",\"position\":\"Java开发实习生\",\"startDate\":\"2024-06\",\"endDate\":\"2024-09\",\"description\":\"参与微信支付后端开发，负责核心交易流程优化\"}]','[{\"name\":\"在线商城系统\",\"description\":\"基于Spring Boot开发的电商平台，包含商品管理、订单系统、支付接口\"}]','[{\"name\":\"Java\",\"level\":\"熟练\"},{\"name\":\"Spring Boot\",\"level\":\"熟练\"},{\"name\":\"MySQL\",\"level\":\"熟练\"},{\"name\":\"Redis\",\"level\":\"熟悉\"}]','[{\"name\":\"英语\",\"level\":\"CET-6\"}]','[{\"name\":\"计算机二级\",\"date\":\"2022-03\"}]',NULL),(5,1,'张同学的简历',1,1,100,0,0,NULL,'2026-06-16 16:44:35','2026-06-16 16:44:35','张同学','男','2000-01-15','13900139001','student1@test.com','北京','热爱编程，积极进取，有较强的学习能力和团队协作精神。','Java开发工程师','北京','15K','[{\"school\":\"北京大学\",\"major\":\"计算机科学与技术\",\"degree\":\"本科\",\"startDate\":\"2021-09\",\"endDate\":\"2025-06\"}]','[{\"company\":\"腾讯\",\"position\":\"Java开发实习生\",\"startDate\":\"2024-06\",\"endDate\":\"2024-09\",\"description\":\"参与微信支付后端开发，负责核心交易流程优化\"}]','[{\"name\":\"在线商城系统\",\"description\":\"基于Spring Boot开发的电商平台，包含商品管理、订单系统、支付接口\"}]','[{\"name\":\"Java\",\"level\":\"熟练\"},{\"name\":\"Spring Boot\",\"level\":\"熟练\"},{\"name\":\"MySQL\",\"level\":\"熟练\"},{\"name\":\"Redis\",\"level\":\"熟悉\"}]','[{\"name\":\"英语\",\"level\":\"CET-6\"}]','[{\"name\":\"计算机二级\",\"date\":\"2022-03\"}]',NULL),(8,2,'??的简历',1,1,100,0,0,NULL,'2026-06-27 16:57:21','2026-06-27 16:57:21','??','male','2000-01-15','13900139002','zhangsan@test.com','??','??','backend','??','10K-15K','[{\"school\":\"????\",\"major\":\"???\",\"degree\":\"bachelor\",\"startDate\":\"2018-09\",\"endDate\":\"2022-06\"}]','[{\"company\":\"????\",\"position\":\"??\",\"startDate\":\"2022-07\",\"endDate\":\"2024-06\",\"description\":\"??\"}]','[{\"name\":\"????\",\"role\":\"??\",\"description\":\"??\"}]','[\"Java\",\"Python\"]','[{\"name\":\"??\",\"level\":\"native\"}]','[{\"name\":\"??\",\"issuer\":\"??\"}]',NULL),(9,1,'ŠłĹšÜäš«ÇňÄć',1,1,100,0,0,NULL,'2026-06-29 20:56:11','2026-06-29 20:56:11','??',NULL,NULL,'13900139002',NULL,'??',NULL,'????',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(10,1,'ŠłĹšÜäš«ÇňÄć',1,1,100,0,0,NULL,'2026-06-29 20:56:37','2026-06-29 20:56:37','王五',NULL,NULL,'13900139003',NULL,'深圳',NULL,'后端开发',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `resume` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `school`
--

DROP TABLE IF EXISTS `school`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `school` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ňşŽŠáíID',
  `school_name` varchar(100) NOT NULL COMMENT 'ňşŽŠáíňÉŹšž░',
  `school_code` varchar(50) DEFAULT NULL COMMENT 'ňşŽŠáíń╗úšáü',
  `province` varchar(50) DEFAULT NULL COMMENT 'šťüń╗Ż',
  `city` varchar(50) DEFAULT NULL COMMENT 'ňčÄňŞé',
  `address` varchar(255) DEFAULT NULL COMMENT 'Ŕ»Žš╗ćňť░ňŁÇ',
  `logo` varchar(255) DEFAULT NULL COMMENT 'ňşŽŠáíLOGO',
  `description` text COMMENT 'ňşŽŠáíš«Çń╗ő',
  `website` varchar(255) DEFAULT NULL COMMENT 'ňşŽŠáíň«śšŻĹ',
  `contact_name` varchar(50) DEFAULT NULL COMMENT 'Ŕüöš│╗ń║║',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT 'Ŕüöš│╗šöÁŔ»Ł',
  `verify_status` tinyint DEFAULT '0' COMMENT 'ň«íŠáŞšŐÂŠÇü´╝Ü0-Šť¬ň«íŠáŞ´╝î1-ňĚ▓ň«íŠáŞ',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'ňłŤň╗║ŠŚÂÚŚ┤',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ŠŤ┤Šľ░ŠŚÂÚŚ┤',
  PRIMARY KEY (`id`),
  UNIQUE KEY `school_code` (`school_code`),
  KEY `idx_school_name` (`school_name`),
  KEY `idx_city` (`city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='ňşŽŠáíŔíĘ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `school`
--

LOCK TABLES `school` WRITE;
/*!40000 ALTER TABLE `school` DISABLE KEYS */;
/*!40000 ALTER TABLE `school` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_verification`
--

DROP TABLE IF EXISTS `sms_verification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sms_verification` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `phone` varchar(20) NOT NULL COMMENT 'ŠëőŠť║ňĆĚ',
  `code` varchar(10) NOT NULL COMMENT 'Ú¬îŔ»üšáü',
  `type` tinyint NOT NULL COMMENT 'š▒╗ň×ő´╝Ü1-Š│Ęňćî´╝î2-šÖ╗ňŻĽ´╝î3-ÚçŹšŻ«ň»ćšáü´╝î4-š╗Ĺň«ÜŠëőŠť║',
  `status` tinyint DEFAULT '0' COMMENT 'šŐÂŠÇü´╝Ü0-Šť¬ńŻ┐šöĘ´╝î1-ňĚ▓ńŻ┐šöĘ´╝î2-ňĚ▓Ŕ┐çŠťč',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IPňť░ňŁÇ',
  `attempts` int DEFAULT '0' COMMENT 'ň░ŁŔ»ĽŠČíŠĽ░',
  `expire_time` datetime NOT NULL COMMENT 'Ŕ┐çŠťčŠŚÂÚŚ┤',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'ňłŤň╗║ŠŚÂÚŚ┤',
  PRIMARY KEY (`id`),
  KEY `idx_phone` (`phone`),
  KEY `idx_code` (`code`),
  KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='ščşń┐íÚ¬îŔ»üšáüŔíĘ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_verification`
--

LOCK TABLES `sms_verification` WRITE;
/*!40000 ALTER TABLE `sms_verification` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_verification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ňşŽšöčID',
  `user_id` bigint NOT NULL COMMENT 'šöĘŠłĚID',
  `student_no` varchar(50) DEFAULT NULL COMMENT 'ňşŽňĆĚ',
  `school_id` bigint DEFAULT NULL COMMENT 'ňşŽŠáíID',
  `college` varchar(100) DEFAULT NULL COMMENT 'ňşŽÚÖó',
  `major` varchar(100) DEFAULT NULL COMMENT 'ńŞôńŞÜ',
  `degree` varchar(50) DEFAULT NULL COMMENT 'ňşŽňÄć´╝ÜŠťČšžĹŃÇüšíĽňúźŃÇüňŹÜňúź',
  `enrollment_year` int DEFAULT NULL COMMENT 'ňůąňşŽň╣┤ń╗Ż',
  `graduation_year` int DEFAULT NULL COMMENT 'Š»ĽńŞÜň╣┤ń╗Ż',
  `gpa` decimal(3,2) DEFAULT NULL COMMENT 'GPA',
  `gender` tinyint DEFAULT NULL COMMENT 'ŠÇžňłź´╝Ü0-Šť¬ščą´╝î1-šöĚ´╝î2-ňą│',
  `birthday` date DEFAULT NULL COMMENT 'šöčŠŚą',
  `bio` text COMMENT 'ńŞ¬ń║║š«Çń╗ő',
  `skills` json DEFAULT NULL COMMENT 'ŠŐÇŔâŻŠáçšşż',
  `expectation_salary` int DEFAULT NULL COMMENT 'ŠťčŠťŤŔľ¬ŔÁä´╝łňůâ/Šťł´╝ë',
  `expectation_city` varchar(50) DEFAULT NULL COMMENT 'ŠťčŠťŤňčÄňŞé',
  `expectation_industry` varchar(100) DEFAULT NULL COMMENT 'ŠťčŠťŤŔíîńŞÜ',
  `expectation_position` varchar(100) DEFAULT NULL COMMENT 'ŠťčŠťŤŔüîńŻŹ',
  `resume_score` int DEFAULT '0' COMMENT 'š«ÇňÄćň«îŠĽ┤ň║ŽŔ»äňłć',
  `job_status` tinyint DEFAULT '1' COMMENT 'Š▒éŔüîšŐÂŠÇü´╝Ü1-šž»Š×üŠ▒éŔüî´╝î2-ŔžéŠťŤŠť║ń╝Ü´╝î3-ńŞŹŔÇâŔÖĹ',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'ňłŤň╗║ŠŚÂÚŚ┤',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ŠŤ┤Šľ░ŠŚÂÚŚ┤',
  `deleted_at` datetime DEFAULT NULL,
  `real_name` varchar(50) DEFAULT NULL COMMENT '????',
  `id_card_number` varchar(18) DEFAULT NULL COMMENT '????',
  `verify_status` int DEFAULT '0' COMMENT '?????0-????1-????2-????3-???',
  `verify_time` datetime DEFAULT NULL COMMENT '????',
  `reject_reason` text COMMENT '????',
  `id_card_front_url` text,
  `id_card_back_url` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  UNIQUE KEY `student_no` (`student_no`),
  KEY `idx_school_id` (`school_id`),
  KEY `idx_major` (`major`),
  KEY `idx_graduation_year` (`graduation_year`),
  KEY `idx_job_status` (`job_status`),
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='ňşŽšöčŔíĘ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (1,11,NULL,NULL,'北京大学','计算机科学与技术','本科',NULL,2025,NULL,1,'2006-04-08','热爱编程，积极进取，有较强的学习能力和团队协作精神。','[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\"]',15,NULL,NULL,'Java开发工程师',0,1,'2026-06-16 16:44:35','2026-06-16 16:44:35',NULL,'??','440515200604080014',2,'2026-06-25 15:29:00','????????,?????','data:image/jpeg;base64,test','data:image/jpeg;base64,test'),(2,12,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'2000-01-15',NULL,NULL,NULL,NULL,NULL,NULL,0,1,'2026-06-27 16:29:32','2026-06-27 16:29:32',NULL,'??','110101199001011234',2,'2026-06-27 16:37:50',NULL,'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==','data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg=='),(3,15,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,'2026-06-30 09:44:41','2026-06-30 09:44:41',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL),(4,16,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,'2026-06-30 10:11:42','2026-06-30 10:11:42',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_config`
--

DROP TABLE IF EXISTS `system_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ÚůŹšŻ«ID',
  `config_key` varchar(100) NOT NULL COMMENT 'ÚůŹšŻ«Úö«',
  `config_value` text COMMENT 'ÚůŹšŻ«ňÇ╝',
  `config_type` varchar(50) DEFAULT NULL COMMENT 'ÚůŹšŻ«š▒╗ň×ő',
  `description` varchar(255) DEFAULT NULL COMMENT 'ŠĆĆŔ┐░',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'ňłŤň╗║ŠŚÂÚŚ┤',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ŠŤ┤Šľ░ŠŚÂÚŚ┤',
  PRIMARY KEY (`id`),
  UNIQUE KEY `config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='š│╗š╗čÚůŹšŻ«ŔíĘ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_config`
--

LOCK TABLES `system_config` WRITE;
/*!40000 ALTER TABLE `system_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'šöĘŠłĚID',
  `username` varchar(50) NOT NULL COMMENT 'šöĘŠłĚňÉŹ',
  `password` varchar(255) NOT NULL COMMENT 'ň»ćšáü´╝łňŐáň»ć´╝ë',
  `phone` varchar(20) NOT NULL COMMENT 'ŠëőŠť║ňĆĚ',
  `email` varchar(100) DEFAULT NULL COMMENT 'Úé«š«▒',
  `real_name` varchar(50) DEFAULT NULL COMMENT 'šťčň«×ňžôňÉŹ',
  `avatar` varchar(255) DEFAULT NULL COMMENT 'ňĄ┤ňâĆURL',
  `user_type` tinyint NOT NULL DEFAULT '1' COMMENT 'šöĘŠłĚš▒╗ň×ő´╝Ü1-ňşŽšöč´╝î2-ń╝üńŞÜ´╝î3-ňşŽŠáí´╝î4-š«íšÉćňĹś',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT 'šŐÂŠÇü´╝Ü0-šŽüšöĘ´╝î1-ŠşúňŞŞ´╝î2-ňżůň«íŠáŞ',
  `last_login_time` datetime DEFAULT NULL COMMENT 'ŠťÇňÉÄšÖ╗ňŻĽŠŚÂÚŚ┤',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT 'ŠťÇňÉÄšÖ╗ňŻĽIP',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'ňłŤň╗║ŠŚÂÚŚ┤',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ŠŤ┤Šľ░ŠŚÂÚŚ┤',
  `deleted_at` datetime DEFAULT NULL COMMENT 'ňłáÚÖĄŠŚÂÚŚ┤´╝łŔŻ»ňłáÚÖĄ´╝ë',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `email` (`email`),
  KEY `idx_phone` (`phone`),
  KEY `idx_user_type` (`user_type`),
  KEY `idx_status` (`status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='šöĘŠłĚŔíĘ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'enterprise1','123456','13800138001','contact@tencent.com','马化腾',NULL,2,1,NULL,NULL,'2026-06-16 16:44:35','2026-06-16 16:44:35',NULL),(2,'enterprise2','123456','13800138002','contact@alibaba.com','张勇',NULL,2,1,NULL,NULL,'2026-06-16 16:44:35','2026-06-16 16:44:35',NULL),(3,'enterprise3','123456','13800138003','contact@bytedance.com','张一鸣',NULL,2,1,NULL,NULL,'2026-06-16 16:44:35','2026-06-16 16:44:35',NULL),(4,'enterprise4','123456','13800138004','contact@huawei.com','任正非',NULL,2,1,NULL,NULL,'2026-06-16 16:44:35','2026-06-16 16:44:35',NULL),(5,'enterprise5','123456','13800138005','contact@meituan.com','王兴',NULL,2,1,NULL,NULL,'2026-06-16 16:44:35','2026-06-29 17:13:51',NULL),(6,'enterprise6','123456','13800138006','contact@jd.com','刘强东',NULL,2,1,NULL,NULL,'2026-06-16 16:44:35','2026-06-29 17:13:51',NULL),(7,'enterprise7','123456','13800138007','contact@xiaomi.com','雷军',NULL,2,0,NULL,NULL,'2026-06-16 16:44:35','2026-06-30 14:04:09',NULL),(8,'enterprise8','123456','13800138008','contact@lenovo.com','杨元庆',NULL,2,0,NULL,NULL,'2026-06-16 16:44:35','2026-06-30 14:04:09',NULL),(9,'enterprise9','123456','13800138009','contact@didi.com','程维',NULL,2,0,NULL,NULL,'2026-06-16 16:44:35','2026-06-30 14:04:09',NULL),(10,'enterprise10','123456','13800138010','contact@netease.com','丁磊',NULL,2,0,NULL,NULL,'2026-06-16 16:44:35','2026-06-30 14:04:09',NULL),(11,'student1','$2a$10$GueJlxQd9DKYvmxYuzCVbOoKYr9zwJXidSS6OQpUIKLuR1etyz0TK','13900139001','student1@test.com','张同学',NULL,1,0,NULL,NULL,'2026-06-16 16:44:35','2026-06-30 14:04:09',NULL),(12,'student2','$2a$10$EbBtst103ScOmkPkwtNrEejtv7/J4RnulOCP5GKM24ILqmzvyNFSq','13900139002',NULL,NULL,NULL,3,0,NULL,NULL,'2026-06-27 15:56:54','2026-06-30 14:04:09',NULL),(13,'testadmin','$2a$10$.cs.dL9jS9kb9xZb6kLgJeNKTdr2uIXulrHS.YBX5z7KhKcGLbe3q','13888888888',NULL,'???????',NULL,4,0,NULL,NULL,'2026-06-29 16:37:33','2026-06-30 14:04:09',NULL),(14,'admin','123456','13800000000','admin@recruitment.com','系统管理员',NULL,4,0,NULL,NULL,'2026-06-29 16:44:26','2026-06-30 14:04:09',NULL),(15,'Teacher3','$2a$10$sSy3ed5vFbVFLjcZreG7.eMF1XhP/SPijHfLX6RJqg/jKGu5ohW9C','13726542998',NULL,NULL,NULL,1,0,NULL,NULL,'2026-06-30 09:44:24','2026-06-30 14:04:09',NULL),(16,'yy','$2a$10$pLQmth8COkF.C4uuZf8qWeh3GxifLsmWgB/ov2AVmi0YuguJG2qOi','15875795106',NULL,NULL,NULL,3,0,NULL,NULL,'2026-06-30 09:45:06','2026-06-30 14:04:09',NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `work_experience`
--

DROP TABLE IF EXISTS `work_experience`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_experience` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'š╗ĆňÄćID',
  `resume_id` bigint NOT NULL COMMENT 'š«ÇňÄćID',
  `company_name` varchar(100) NOT NULL COMMENT 'ňůČňĆŞňÉŹšž░',
  `position` varchar(100) NOT NULL COMMENT 'ŔüîńŻŹ',
  `start_date` date NOT NULL COMMENT 'ň╝ÇňžőŠŚÂÚŚ┤',
  `end_date` date DEFAULT NULL COMMENT 'š╗ôŠŁčŠŚÂÚŚ┤´╝łńŞ║šę║ŔíĘšĄ║Ŕç│ń╗Ő´╝ë',
  `description` text COMMENT 'ňĚąńŻťŠĆĆŔ┐░',
  `achievement` text COMMENT 'ňĚąńŻťŠłÉň░▒',
  `sort_order` int DEFAULT '0' COMMENT 'ŠÄĺň║Ć',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'ňłŤň╗║ŠŚÂÚŚ┤',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ŠŤ┤Šľ░ŠŚÂÚŚ┤',
  PRIMARY KEY (`id`),
  KEY `idx_resume_id` (`resume_id`),
  CONSTRAINT `work_experience_ibfk_1` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='ňĚąńŻťš╗ĆňÄćŔíĘ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `work_experience`
--

LOCK TABLES `work_experience` WRITE;
/*!40000 ALTER TABLE `work_experience` DISABLE KEYS */;
/*!40000 ALTER TABLE `work_experience` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'recruitment_platform'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-30 14:28:07
