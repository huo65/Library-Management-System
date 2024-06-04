/*
 Navicat Premium Data Transfer

 Source Server         : library
 Source Server Type    : MySQL
 Source Server Version : 80300
 Source Host           : 116.198.233.53:3306
 Source Schema         : library

 Target Server Type    : MySQL
 Target Server Version : 80300
 File Encoding         : 65001

 Date: 04/06/2024 09:35:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for Fine
-- ----------------------------
DROP TABLE IF EXISTS `Fine`;
CREATE TABLE `Fine`  (
  `readerId` int(0) NOT NULL COMMENT '读者id',
  `book_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图书id',
  `number` int(0) NULL DEFAULT NULL COMMENT '金额',
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `readerName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '冗余存储读者姓名',
  `bookName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int(0) NULL DEFAULT NULL COMMENT '0未支付，已支付',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Fine_pk`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 127 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
