/*
 Navicat Premium Data Transfer

 Source Server         : localDocker
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : library

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 20/04/2024 15:21:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- --------------------------------
-- Table structure for table `book`
-- --------------------------------

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book` (
  `isbn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图书编号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '作者',
  `publisher` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '出版社',
  `left_number` bigint NOT NULL COMMENT '剩余数量',
  `total_number` bigint NOT NULL COMMENT '总共数量',
  PRIMARY KEY (`isbn`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

-- ----------------------------
-- Dumping data for table `book`
-- ----------------------------

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES ('20211880115','十万个为什么','苏格与','西安电子科技大学出版社',3,6),('20211880119','五万个为什么','聂家辉','西安电子科技大学出版社',2,10),('20211880106','一万个为什么','丁奕中','西安电子科技大学出版社',4,5),('20211880114','计算机操作系统','凌浩晨','西安电子科技大学出版社',1,5),('20211880118','伊索寓言','沈益威','西安电子科技大学出版社',2,3),('20211880109','格林童话','张超祥','西安电子科技大学出版社',10,14),('20211880100','图书管理系统','聂家辉','西安电子科技大学出版社',3,7),('20211880128','和另一个自己谈谈心','\r\n武志红','中国友谊出版公司',6,9),('20211880111','全国计算机技术与软件专业技术资格(水平)考试. 系统分析师考试大纲','全国计算机技术与软件专业技术资格(水平)考试办公室','清华大学出版社',6,15),('20211880145','简单减肥餐, 好吃不反弹','\r\n萨巴蒂娜','中国轻工业出版社',4,10),('20211880155','其实她真的好喜欢你','\r\n莫妮打','时代文艺出版社',6,11),('20211880156','导游业务. 第7版','\r\n全国导游人员资格考试教材编写组','旅游教育出版社',1,8),('20211880166','法医秦明. 幸存者','\r\n法医秦明','北京联合出版公司',5,10),('20211880177','中国书法史','\r\n徐建融','浙江人民美术出版社',4,10),('20211880199','实用百草治百病','\r\n宋纬文','福建科学技术出版社',5,7),('20211880176','烈火芳菲','赵方新','北京十月文艺出版社',2,12);
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

-- ----------------------------------------
-- Table structure for table `bookwithuser`
-- ---------------------------------------

DROP TABLE IF EXISTS `bookwithuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookwithuser` (
  `id` bigint NOT NULL COMMENT '读者id',
  `isbn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图书编号',
  `book_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图书名',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '读者姓名',
  `lendtime` datetime DEFAULT NULL COMMENT '借阅时间',
  `deadtime` datetime DEFAULT NULL COMMENT '应归还时间',
  `prolong` int DEFAULT NULL COMMENT '续借次数',
  PRIMARY KEY (`book_name`) USING BTREE,
  KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookwithuser`
--

LOCK TABLES `bookwithuser` WRITE;
/*!40000 ALTER TABLE `bookwithuser` DISABLE KEYS */;
INSERT INTO `bookwithuser` VALUES (21,'20211880119','五万个为什么','db','2023-01-01 13:38:36','2023-01-31 13:38:36',1),(28,'20211880115','十万个为什么','123','2024-04-16 13:53:48','2024-05-16 13:53:48',1),(27,'20211880100','图书管理系统','12','2022-12-29 08:48:36','2023-01-28 08:48:36',2);
/*!40000 ALTER TABLE `bookwithuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lend_record`
--

DROP TABLE IF EXISTS `lend_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lend_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `reader_id` bigint NOT NULL COMMENT '读者id',
  `isbn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图书编号',
  `bookname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图书名',
  `lend_time` datetime DEFAULT NULL COMMENT '借书日期',
  `return_time` datetime DEFAULT NULL COMMENT '还书日期',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '0：未归还 1：已归还',
  `borrownum` int NOT NULL COMMENT '此书被借阅次数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2030043138 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lend_record`
--

LOCK TABLES `lend_record` WRITE;
/*!40000 ALTER TABLE `lend_record` DISABLE KEYS */;
INSERT INTO `lend_record` VALUES (-2048151551,21,'20211880119','五万个为什么','2023-01-01 13:38:36',NULL,'0',19),(-2046758911,28,'20211880115','十万个为什么','2024-04-16 13:53:48',NULL,'0',19),(-1859469311,21,'20211880114','计算机操作系统','2022-12-26 17:31:05','2023-01-01 13:38:22','1',4),(-1851039742,21,'20211880100','图书管理系统','2022-12-23 19:30:59','2022-12-23 19:32:00','1',13),(-1691627519,27,'20211880100','图书管理系统','2022-12-29 08:48:36',NULL,'0',16),(-1230368766,21,'20211880115','十万个为什么','2022-12-24 15:10:31','2022-12-24 15:17:51','1',13),(-1171603454,21,'20211880106','一万个为什么','2021-12-26 17:31:01','2022-12-31 15:58:52','1',12),(-1007984639,21,'20211880115','十万个为什么','2022-12-23 19:34:28','2022-12-23 19:34:33','1',12),(-785686526,21,'20211880115','十万个为什么','2022-12-23 19:31:28','2022-12-23 19:32:05','1',11),(-705966078,28,'20211880115','十万个为什么','2022-12-29 09:22:08','2024-04-16 13:49:52','1',18),(-693452798,21,'20211880100','图书管理系统','2022-12-26 17:31:07','2022-12-27 13:16:54','1',15),(-630497279,21,'20211880109','格林童话','2022-12-23 19:31:31','2022-12-23 19:32:10','1',2),(-483696639,21,'20211880118','伊索寓言','2022-12-23 19:31:29','2022-12-23 19:32:11','1',9),(-391421951,21,'20211880128','和另一个自己谈谈心','2022-12-23 19:35:41','2022-12-23 19:35:46','1',11),(-177598462,21,'20211880115','十万个为什么','2022-12-24 15:23:24','2022-12-24 15:23:33','1',15),(-18128895,21,'20211880111','全国计算机技术与软件专业技术资格(水平)考试. 系统分析师考试大纲','2022-12-23 19:31:35','2022-12-23 19:32:15','1',1),(19632129,21,'20211880106','一万个为什么','2022-12-31 16:05:24','2023-01-01 13:38:29','1',13),(262901761,21,'20211880109','格林童话','2022-12-31 15:58:56','2023-01-01 13:38:24','1',3),(611016705,21,'20211880119','五万个为什么','2022-12-23 19:31:28','2022-12-23 19:31:57','1',18),(749342722,21,'20211880115','十万个为什么','2022-12-24 15:26:33','2022-12-24 15:29:49','1',16),(761970690,21,'20211880118','伊索寓言','2022-12-26 17:31:02','2023-01-01 13:38:28','1',12),(901836802,28,'20211880106','一万个为什么','2024-04-16 13:50:02','2024-04-16 13:50:17','1',14),(1001086978,21,'20211880118','伊索寓言','2022-12-23 19:33:59','2022-12-23 19:34:02','1',10),(1017778177,21,'20211880115','十万个为什么','2022-12-24 15:19:35','2022-12-24 15:19:50','1',14),(1172967426,21,'20211880100','图书管理系统','2022-12-24 15:28:35','2022-12-24 15:30:12','1',14),(1181442050,21,'20211880106','一万个为什么','2022-12-23 19:31:29','2022-12-23 19:32:06','1',11),(1290493954,21,'20211880118','伊索寓言','2022-12-23 19:35:39','2022-12-23 19:35:44','1',11),(1412087809,21,'20211880128','和另一个自己谈谈心','2022-12-26 17:31:04','2023-01-01 13:38:26','1',12),(1428905986,21,'20211880114','计算机操作系统','2022-12-23 19:31:30','2022-12-23 19:32:08','1',3),(1659592705,21,'20211880119','五万个为什么','2022-12-23 19:30:59','2022-12-23 19:31:18','1',17),(1735090177,21,'20211880118','和另一个自己谈谈心','2022-12-23 19:33:57','2022-12-23 19:34:03','1',2),(1756061697,21,'20211880118','和另一个自己谈谈心','2022-12-23 19:31:33','2022-12-23 19:32:13','1',1);
/*!40000 ALTER TABLE `lend_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话号码',
  `sex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地址',
  `role` int NOT NULL COMMENT '角色、1：管理员 2：普通用户',
  `alow` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否允许借阅',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'SysAdmin','123','B5Sys','17326788058','男','温州大学南校区A12-417室',1,'1'),(21,'admin','123','管理员','17326788058','男','温州大学计算机与人工智能学院',2,'1'),(29,'huo','123','huo','13289711132','男',NULL,2,NULL),(30,'huo2','123','111','13289711131','男',NULL,3,NULL),(31,'admin2','123456','admin2','13289711132',NULL,'陕西',2,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `specificBook`
--

DROP TABLE IF EXISTS `specificBook`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `specificBook` (
  `id` INT(8) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `isbn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图书编号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1' COMMENT '0：未归还 1：已归还',
  `location` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '存放位置',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='具体书籍表';

--
-- Dumping data for table `specificBook`
--

LOCK TABLES `specificBook` WRITE;
/*!40000 ALTER TABLE `specificBook` DISABLE KEYS */;
INSERT INTO `specificBook` VALUES (00000009,'20211880115','十万个为什么','0','01-04-22'),(00000010,'20211880119','五万个为什么','0','02-28-23'),(00000011,'20211880106','一万个为什么','1','03-15-21'),(00000012,'20211880114','计算机操作系统','1','08-31-17'),(00000013,'20211880118','伊索寓言','1','12-18-22'),(00000015,'20211880109','格林童话','1','09-16-25'),(00000016,'20211880100','图书管理系统','0','05-20-20'),(000000017,'20211880128','和另一个自己谈谈心','1','04-12-24'),(00000018,'20211880111','全国计算机技术与软件专业技术资格(水平)考试. 系统分析师考试大纲','1','11-09-23'),(00000019,'20211880145','简单减肥餐, 好吃不反弹','1','12-18-22'),(00000020,'20211880155','其实她真的好喜欢你','1','10-15-26'),(00000021,'20211880156','导游业务. 第7版','1','02-27-29'),(00000022,'20211880166','法医秦明. 幸存者','1','05-09-16'),(00000023,'20211880177','中国书法史','1','02-27-29'),(00000024,'20211880199','实用百草治百病','1','12-04-25'),(00000025,'20211880176','烈火芳菲','1','06-28-19');
/*!40000 ALTER TABLE `specificBook` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `codeInfo`
--

DROP TABLE IF EXISTS `codeInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `codeInfo` (
  `id` INT(8) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `isbn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图书编号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '作者',
  `publisher` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '出版社',
  `create_time` date DEFAULT NULL COMMENT '出版时间',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1' COMMENT '0：未归还 1：已归还',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='条形码信息表';

--
-- Dumping data for table `codeInfo`
--

LOCK TABLES `codeInfo` WRITE;
/*!40000 ALTER TABLE `codeInfo` DISABLE KEYS */;
INSERT INTO `codeInfo` VALUES (00000009,'20211880115','十万个为什么','苏格与','西安电子科技大学出版社','2022-12-05','0'),(00000010,'20211880119','五万个为什么','聂家辉','西安电子科技大学出版社','2022-12-01','0'),(00000011,'20211880106','一万个为什么','丁奕中','西安电子科技大学出版社','2022-12-02','1'),(00000012,'20211880114','计算机操作系统','凌浩晨','西安电子科技大学出版社','2022-12-03','1'),(00000013,'20211880118','伊索寓言','沈益威','西安电子科技大学出版社','2022-12-04','1'),(00000015,'20211880109','格林童话','张超祥','西安电子科技大学出版社','2022-12-05','1'),(00000016,'20211880100','图书管理系统','聂家辉','西安电子科技大学出版社','2022-08-10','0'),(00000017,'20211880128','和另一个自己谈谈心','\r\n武志红','中国友谊出版公司','2021-01-01','1'),(00000018,'20211880111','全国计算机技术与软件专业技术资格(水平)考试. 系统分析师考试大纲','全国计算机技术与软件专业技术资格(水平)考试办公室','清华大学出版社','2004-05-22','1'),(00000019,'20211880145','简单减肥餐, 好吃不反弹','\r\n萨巴蒂娜','中国轻工业出版社','2022-07-07','1'),(00000020,'20211880155','其实她真的好喜欢你','\r\n莫妮打','时代文艺出版社','2022-02-02','1'),(00000021,'20211880156','导游业务. 第7版','\r\n全国导游人员资格考试教材编写组','旅游教育出版社','2022-08-22','1'),(00000022,'20211880166','法医秦明. 幸存者','\r\n法医秦明','北京联合出版公司','2022-02-22','1'),(00000023,'20211880177','中国书法史','\r\n徐建融','浙江人民美术出版社','2021-11-01','1'),(00000024,'20211880199','实用百草治百病','\r\n宋纬文','福建科学技术出版社','2021-09-22','1'),(00000025,'20211880176','烈火芳菲','赵方新','北京十月文艺出版社','2022-01-22','1');
/*!40000 ALTER TABLE `codeInfo` ENABLE KEYS */;
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
-- Dump completed on 2024-04-20 17:45:59
