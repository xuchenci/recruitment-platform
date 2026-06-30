SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

UPDATE enterprise SET company_name = '腾讯科技(深圳)有限公司' WHERE id = 1;
UPDATE enterprise SET company_name = '阿里巴巴集团控股有限公司' WHERE id = 2;
UPDATE enterprise SET company_name = '百度在线网络技术(北京)有限公司' WHERE id = 3;
UPDATE enterprise SET company_name = '京东集团股份有限公司' WHERE id = 4;
UPDATE enterprise SET company_name = '美团' WHERE id = 5;
UPDATE enterprise SET company_name = '字节跳动有限公司' WHERE id = 6;
UPDATE enterprise SET company_name = '网易(杭州)网络有限公司' WHERE id = 7;
UPDATE enterprise SET company_name = '小米科技有限责任公司' WHERE id = 8;
UPDATE enterprise SET company_name = '华为技术有限公司' WHERE id = 9;
UPDATE enterprise SET company_name = '滴滴出行科技有限公司' WHERE id = 10;

UPDATE job SET job_title = 'Java开发工程师' WHERE id IN (1,2,3,7,8,13,19,20,25,26,31,32,37,38,43,44,49,50,55,56,57);
UPDATE job SET job_title = '前端开发工程师' WHERE id IN (4,5,9,10,14,22,27,33,39,45,51,58);
UPDATE job SET job_title = '产品经理' WHERE id IN (6,11,15,24,29,35,41,47,53,59);
UPDATE job SET job_title = 'UI设计师' WHERE id IN (16,40,54);
UPDATE job SET job_title = '测试工程师' WHERE id IN (17,21,23,36,42,48,52,60);

-- 修复职位分类表乱码
UPDATE job_category SET category_name = '技术类' WHERE id = 1;
UPDATE job_category SET category_name = '产品类' WHERE id = 2;
UPDATE job_category SET category_name = '设计类' WHERE id = 3;
UPDATE job_category SET category_name = '市场类' WHERE id = 4;
UPDATE job_category SET category_name = '运营类' WHERE id = 5;
UPDATE job_category SET category_name = '职能类' WHERE id = 6;
UPDATE job_category SET category_name = 'Java开发' WHERE id = 7;
UPDATE job_category SET category_name = '前端开发' WHERE id = 8;
UPDATE job_category SET category_name = 'Python开发' WHERE id = 9;
UPDATE job_category SET category_name = '数据分析' WHERE id = 10;
UPDATE job_category SET category_name = 'AI/机器学习' WHERE id = 11;
UPDATE job_category SET category_name = '产品经理' WHERE id = 12;
UPDATE job_category SET category_name = 'UI设计' WHERE id = 13;
UPDATE job_category SET category_name = 'UX设计' WHERE id = 14;

SELECT '数据更新完成' AS result;
