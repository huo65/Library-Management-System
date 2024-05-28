/*
 Navicat Premium Data Transfer

 Source Server         : devLib
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : lib

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 28/05/2024 09:09:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `isbn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图书编号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '作者',
  `publisher` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '出版社',
  `left_number` bigint(0) NOT NULL COMMENT '剩余数量',
  `total_number` bigint(0) NOT NULL COMMENT '总共数量',
  `borrownum` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`isbn`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES ('20211880100', '图书管理系统', '聂家辉', '西安电子科技大学出版社', 7, 7, NULL);
INSERT INTO `book` VALUES ('20211880106', '一万个为什么', '丁奕中', '西安电子科技大学出版社', 5, 5, NULL);
INSERT INTO `book` VALUES ('20211880109', '格林童话', '张超祥', '西安电子科技大学出版社', 14, 14, NULL);
INSERT INTO `book` VALUES ('20211880111', '全国计算机技术与软件专业技术资格(水平)考试. 系统分析师考试大纲', '全国计算机技术与软件专业技术资格(水平)考试办公室', '清华大学出版社', 15, 15, NULL);
INSERT INTO `book` VALUES ('20211880114', '计算机操作系统', '凌浩晨', '西安电子科技大学出版社', 5, 5, NULL);
INSERT INTO `book` VALUES ('20211880115', '十万个为什么', '苏格与', '西安电子科技大学出版社', 6, 6, NULL);
INSERT INTO `book` VALUES ('20211880118', '伊索寓言', '沈益威', '西安电子科技大学出版社', 3, 3, NULL);
INSERT INTO `book` VALUES ('20211880119', '五万个为什么', '聂家辉', '西安电子科技大学出版社', 10, 10, NULL);
INSERT INTO `book` VALUES ('20211880128', '和另一个自己谈谈心', '\r\n武志红', '中国友谊出版公司', 9, 9, NULL);
INSERT INTO `book` VALUES ('20211880145', '简单减肥餐, 好吃不反弹', '\r\n萨巴蒂娜', '中国轻工业出版社', 4, 10, NULL);
INSERT INTO `book` VALUES ('20211880155', '其实她真的好喜欢你', '\r\n莫妮打', '时代文艺出版社', 6, 11, NULL);
INSERT INTO `book` VALUES ('20211880156', '导游业务. 第7版', '\r\n全国导游人员资格考试教材编写组', '旅游教育出版社', 1, 8, NULL);
INSERT INTO `book` VALUES ('20211880166', '法医秦明. 幸存者', '\r\n法医秦明', '北京联合出版公司', 5, 10, NULL);
INSERT INTO `book` VALUES ('20211880176', '烈火芳菲', '赵方新', '北京十月文艺出版社', 2, 12, NULL);
INSERT INTO `book` VALUES ('20211880177', '中国书法史', '\r\n徐建融', '浙江人民美术出版社', 4, 10, NULL);
INSERT INTO `book` VALUES ('20211880184', '高等数学', '同济大学数学系', '高等教育出版社', 4, 15, NULL);
INSERT INTO `book` VALUES ('20211880192', '平凡的世界', '路遥', '北京十月文艺出版社', 3, 10, NULL);
INSERT INTO `book` VALUES ('20211880199', '实用百草治百病', '\r\n宋纬文', '福建科学技术出版社', 5, 7, NULL);

-- ----------------------------
-- Table structure for bookwithuser
-- ----------------------------
DROP TABLE IF EXISTS `bookwithuser`;
CREATE TABLE `bookwithuser`  (
  `id` bigint(0) NOT NULL COMMENT '读者id',
  `isbn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图书种类编号',
  `book_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图书名',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '读者姓名',
  `lendtime` datetime(0) NULL DEFAULT NULL COMMENT '借阅时间',
  `deadtime` datetime(0) NULL DEFAULT NULL COMMENT '应归还时间',
  `prolong` int(0) NULL DEFAULT NULL COMMENT '续借次数',
  `book_id` bigint(0) NULL DEFAULT NULL COMMENT '图书具体编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bookwithuser
-- ----------------------------

-- ----------------------------
-- Table structure for codeinfo
-- ----------------------------
DROP TABLE IF EXISTS `codeinfo`;
CREATE TABLE `codeinfo`  (
  `id` int(8) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `isbn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图书编号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '作者',
  `publisher` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '出版社',
  `create_time` date NULL DEFAULT NULL COMMENT '出版时间',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1' COMMENT '0：未归还 1：已归还',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '条形码信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of codeinfo
-- ----------------------------
INSERT INTO `codeinfo` VALUES (00000009, '20211880115', '十万个为什么', '苏格与', '西安电子科技大学出版社', '2022-12-05', '0');
INSERT INTO `codeinfo` VALUES (00000010, '20211880119', '五万个为什么', '聂家辉', '西安电子科技大学出版社', '2022-12-01', '0');
INSERT INTO `codeinfo` VALUES (00000011, '20211880106', '一万个为什么', '丁奕中', '西安电子科技大学出版社', '2022-12-02', '1');
INSERT INTO `codeinfo` VALUES (00000012, '20211880114', '计算机操作系统', '凌浩晨', '西安电子科技大学出版社', '2022-12-03', '1');
INSERT INTO `codeinfo` VALUES (00000013, '20211880118', '伊索寓言', '沈益威', '西安电子科技大学出版社', '2022-12-04', '1');
INSERT INTO `codeinfo` VALUES (00000015, '20211880109', '格林童话', '张超祥', '西安电子科技大学出版社', '2022-12-05', '1');
INSERT INTO `codeinfo` VALUES (00000016, '20211880100', '图书管理系统', '聂家辉', '西安电子科技大学出版社', '2022-08-10', '0');
INSERT INTO `codeinfo` VALUES (00000017, '20211880128', '和另一个自己谈谈心', '\r\n武志红', '中国友谊出版公司', '2021-01-01', '1');
INSERT INTO `codeinfo` VALUES (00000018, '20211880111', '全国计算机技术与软件专业技术资格(水平)考试. 系统分析师考试大纲', '全国计算机技术与软件专业技术资格(水平)考试办公室', '清华大学出版社', '2004-05-22', '1');
INSERT INTO `codeinfo` VALUES (00000019, '20211880145', '简单减肥餐, 好吃不反弹', '\r\n萨巴蒂娜', '中国轻工业出版社', '2022-07-07', '1');
INSERT INTO `codeinfo` VALUES (00000020, '20211880155', '其实她真的好喜欢你', '\r\n莫妮打', '时代文艺出版社', '2022-02-02', '1');
INSERT INTO `codeinfo` VALUES (00000021, '20211880156', '导游业务. 第7版', '\r\n全国导游人员资格考试教材编写组', '旅游教育出版社', '2022-08-22', '1');
INSERT INTO `codeinfo` VALUES (00000022, '20211880166', '法医秦明. 幸存者', '\r\n法医秦明', '北京联合出版公司', '2022-02-22', '1');
INSERT INTO `codeinfo` VALUES (00000023, '20211880177', '中国书法史', '\r\n徐建融', '浙江人民美术出版社', '2021-11-01', '1');
INSERT INTO `codeinfo` VALUES (00000024, '20211880199', '实用百草治百病', '\r\n宋纬文', '福建科学技术出版社', '2021-09-22', '1');
INSERT INTO `codeinfo` VALUES (00000025, '20211880176', '烈火芳菲', '赵方新', '北京十月文艺出版社', '2022-01-22', '1');
INSERT INTO `codeinfo` VALUES (00000026, '20211880184', '高等数学', '同济大学数学系', '高等教育出版社', '2021-07-07', '0');
INSERT INTO `codeinfo` VALUES (00000027, '20211880192', '平凡的世界', '路遥', '北京十月文艺出版社', '2021-06-10', '1');

-- ----------------------------
-- Table structure for lend_record
-- ----------------------------
DROP TABLE IF EXISTS `lend_record`;
CREATE TABLE `lend_record`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `reader_id` bigint(0) NOT NULL COMMENT '读者id',
  `isbn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图书种类编号',
  `bookname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图书名',
  `lend_time` datetime(0) NULL DEFAULT NULL COMMENT '借书日期',
  `return_time` datetime(0) NULL DEFAULT NULL COMMENT '还书日期',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '0：未归还 1：已归还',
  `borrownum` int(0) NOT NULL COMMENT '此书被借阅次数',
  `book_id` bigint(0) NULL DEFAULT NULL COMMENT '图书具体编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2030043138 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lend_record
-- ----------------------------

-- ----------------------------
-- Table structure for specificbook
-- ----------------------------
DROP TABLE IF EXISTS `specificbook`;
CREATE TABLE `specificbook`  (
  `id` int(8) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `isbn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图书编号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1' COMMENT '0：未归还 1：已归还',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '存放位置',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '具体书籍表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of specificbook
-- ----------------------------
INSERT INTO `specificbook` VALUES (00000009, '20211880115', '十万个为什么', '0', '01-04-22');
INSERT INTO `specificbook` VALUES (00000010, '20211880119', '五万个为什么', '0', '02-28-23');
INSERT INTO `specificbook` VALUES (00000011, '20211880106', '一万个为什么', '1', '03-15-21');
INSERT INTO `specificbook` VALUES (00000012, '20211880114', '计算机操作系统', '1', '08-31-17');
INSERT INTO `specificbook` VALUES (00000013, '20211880118', '伊索寓言', '1', '12-18-22');
INSERT INTO `specificbook` VALUES (00000015, '20211880109', '格林童话', '1', '09-16-25');
INSERT INTO `specificbook` VALUES (00000016, '20211880100', '图书管理系统', '0', '05-20-20');
INSERT INTO `specificbook` VALUES (00000017, '20211880128', '和另一个自己谈谈心', '1', '04-12-24');
INSERT INTO `specificbook` VALUES (00000018, '20211880111', '全国计算机技术与软件专业技术资格(水平)考试. 系统分析师考试大纲', '1', '11-09-23');
INSERT INTO `specificbook` VALUES (00000019, '20211880145', '简单减肥餐, 好吃不反弹', '1', '12-18-22');
INSERT INTO `specificbook` VALUES (00000020, '20211880155', '其实她真的好喜欢你', '1', '10-15-26');
INSERT INTO `specificbook` VALUES (00000021, '20211880156', '导游业务. 第7版', '1', '02-27-29');
INSERT INTO `specificbook` VALUES (00000022, '20211880166', '法医秦明. 幸存者', '1', '05-09-16');
INSERT INTO `specificbook` VALUES (00000023, '20211880177', '中国书法史', '1', '02-27-29');
INSERT INTO `specificbook` VALUES (00000024, '20211880199', '实用百草治百病', '1', '12-04-25');
INSERT INTO `specificbook` VALUES (00000025, '20211880176', '烈火芳菲', '1', '06-28-19');
INSERT INTO `specificbook` VALUES (00000026, '20211880184', '高等数学', '0', '07-12-14');
INSERT INTO `specificbook` VALUES (00000027, '20211880192', '平凡的世界', '1', '06-14-11');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话号码',
  `sex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  `role` int(0) NOT NULL COMMENT '角色、1：管理员 2：普通用户',
  `alow` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否允许借阅',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'SysAdmin', '123', 'B5Sys', '17326788058', '男', '西电南校区A12-417室', 1, '1');
INSERT INTO `user` VALUES (21, 'admin', '123', '管理员', '17326788058', '男', '西电', 2, '1');
INSERT INTO `user` VALUES (31, 'admin2', '123456', 'admin2', '13289711132', NULL, '陕西', 2, '1');

SET FOREIGN_KEY_CHECKS = 1;
