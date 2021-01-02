/*
 Navicat Premium Data Transfer

 Source Server         : 本地MySQL-5.7
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : shiro

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 02/01/2021 13:55:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_permission`;
CREATE TABLE `tb_permission`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限字符串',
  `resource_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对应可以操作的资源',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `permission_name`(`permission_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_permission
-- ----------------------------
INSERT INTO `tb_permission` VALUES (1, 'product:read', NULL);
INSERT INTO `tb_permission` VALUES (2, 'product:update', NULL);
INSERT INTO `tb_permission` VALUES (3, 'product:delete', NULL);
INSERT INTO `tb_permission` VALUES (4, 'product:add', NULL);
INSERT INTO `tb_permission` VALUES (6, 'product:*', NULL);

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`role_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` VALUES (1, 'admin');
INSERT INTO `tb_role` VALUES (2, 'super');
INSERT INTO `tb_role` VALUES (3, 'visitor');

-- ----------------------------
-- Table structure for tb_role_perm
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_perm`;
CREATE TABLE `tb_role_perm`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `perm_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `tb_role_perm_ibfk_1`(`role_id`) USING BTREE,
  INDEX `tb_role_perm_ibfk_2`(`perm_id`) USING BTREE,
  CONSTRAINT `tb_role_perm_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `tb_role` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `tb_role_perm_ibfk_2` FOREIGN KEY (`perm_id`) REFERENCES `tb_permission` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role_perm
-- ----------------------------
INSERT INTO `tb_role_perm` VALUES (1, 1, 1);
INSERT INTO `tb_role_perm` VALUES (2, 1, 2);
INSERT INTO `tb_role_perm` VALUES (3, 3, 1);
INSERT INTO `tb_role_perm` VALUES (4, 2, 2);
INSERT INTO `tb_role_perm` VALUES (5, 1, 3);

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键',
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户登录密码',
  `salt` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码加密的随机盐',
  `jwt_secret` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生成jwt的随机盐',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, 'ysq', '5ff409c0b931ee0a8d07ee7326d6c5a5', 'i#CviS^y', '015i4@+I');
INSERT INTO `tb_user` VALUES (3, 'wxm', '3d7891d864cc190d45917282113e3d9e', '7ZhyDevw', NULL);
INSERT INTO `tb_user` VALUES (4, 'test1', 'adc79fef4d1d3de9458fa5d9660020cd', 'yIB&gMM2', NULL);
INSERT INTO `tb_user` VALUES (5, 'test', 'dd5f71aa0855592e045c055c6b61c921', 'chP^t=R&', NULL);

-- ----------------------------
-- Table structure for tb_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_role`;
CREATE TABLE `tb_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `tb_user_role_ibfk_1`(`user_id`) USING BTREE,
  INDEX `tb_user_role_ibfk_2`(`role_id`) USING BTREE,
  CONSTRAINT `tb_user_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `tb_user_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `tb_role` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_role
-- ----------------------------
INSERT INTO `tb_user_role` VALUES (1, 1, 1);
INSERT INTO `tb_user_role` VALUES (2, 3, 3);
INSERT INTO `tb_user_role` VALUES (3, 3, 2);

SET FOREIGN_KEY_CHECKS = 1;
