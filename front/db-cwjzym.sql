

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_captcha
-- ----------------------------
DROP TABLE IF EXISTS `sys_captcha`;
CREATE TABLE `sys_captcha` (
  `uuid` char(36) NOT NULL COMMENT 'uuid',
  `code` varchar(6) NOT NULL COMMENT '验证码',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统验证码';

-- ----------------------------
-- Records of sys_captcha
-- ----------------------------
BEGIN;
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('0154d0dd-a34e-462f-84da-ae0aa6985502', 'yppxc', '2025-03-06 11:49:21');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('02092117-38e7-4dd9-8015-65b71b2909f0', '86xx7', '2025-02-19 15:33:49');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('0789112f-da1f-4c3f-89f9-673d93c85aef', '7dndn', '2025-02-19 15:22:45');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('08d70c92-fc51-48ac-8ae2-f264ba556b16', '7pw6n', '2025-03-06 21:10:48');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('0a4376e2-e86d-483a-8c3d-20381fe6f226', 'd5n8f', '2025-03-06 11:35:23');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('0b6dee59-f0c1-4196-8037-cb4b29a6bfd2', 'cb7df', '2025-02-19 16:18:15');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('0c80fe13-3b60-49b9-8118-4c67a58a011b', '5bnxx', '2025-02-19 15:23:10');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('0ca4672a-75b5-4174-87d2-79252adbf09e', 'cedbe', '2025-03-06 11:41:31');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('10253008-fcb3-4962-8218-7ceb873cf642', '776yd', '2025-03-06 13:10:51');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('143dc4aa-0fb2-4515-8240-2fd95e23ec41', 'dnbxx', '2025-03-06 13:04:29');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('1568e688-e96b-4ef9-85a7-603f83a266bb', '8gfy7', '2025-02-19 15:07:29');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('1de621f1-7000-4a55-8c88-dbb61d736e48', '7gngw', '2025-03-06 13:27:46');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('203c638a-faf3-4691-8742-a402db5d21f5', '7emb4', '2025-03-06 13:10:51');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('205c1d44-c760-4338-8a7f-f131d3b5b5e4', '5pbpb', '2025-03-06 11:59:38');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('32c0501c-9e69-48be-8e94-763650ce34bc', '66n5x', '2025-02-19 15:14:15');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('344d8d8d-2abf-45f6-8e5f-51eb271189d4', 'e88fb', '2025-02-19 15:23:11');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('3861d4a5-e7fb-4550-8aab-f6b47961cb5c', 'bme5c', '2025-03-06 11:43:20');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('3aca0107-26d0-4147-8465-b4ddee64f020', 'nybpf', '2025-03-06 17:44:56');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('482302df-f34c-4544-85f8-ddb8b2582180', '73cny', '2025-03-06 12:11:20');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('4abe52c5-d530-4132-8d5c-104d7a4a674f', '883fm', '2025-03-06 11:49:21');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('4d01a446-d418-4b38-8228-e54b60ed9961', 'bedfp', '2025-03-06 11:43:20');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('4f91cd00-c70e-412d-8a14-82a8576edc58', 'gn4y7', '2025-03-06 11:35:23');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('50daaf7c-f0ea-4306-8579-3d8fee73d021', '553yb', '2025-03-06 17:46:00');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('544c26d2-ccc0-46f7-8e42-a55548bec1d6', 'eaca5', '2025-03-06 11:43:20');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('55d1513b-5acd-4e02-824b-8a9195b5fc3d', 'cnwde', '2025-02-19 15:13:57');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('58a1a188-b4cf-4859-878c-8fed714a0c91', 'ffamf', '2025-02-19 15:13:56');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('5d59c734-e69c-4962-87e0-2f6e2e30da69', '5na37', '2025-03-14 13:41:25');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('5d943728-eac9-4c2c-833d-35bfa9c1a461', 'mcf88', '2025-03-14 13:41:58');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('5e1a6e3f-4e8d-4b81-8701-8b5eb05c21d2', 'f2d5c', '2025-03-06 21:10:35');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('5fd4af3c-c21e-44c3-8624-81c43037b6f7', '4anb5', '2025-03-06 17:46:00');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('679a38f5-1434-45e0-8f5e-f10f14c0156d', 'dnpm5', '2025-02-19 15:07:29');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('681b59ea-a6d6-422f-84b0-4bda89313ee6', '5fcdg', '2025-03-14 13:33:46');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('6b3ee055-860b-4071-88c3-4c70e904b47a', 'dad4e', '2025-03-06 13:27:46');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('733f504e-3841-40db-8b74-1ab29264041c', 'b3854', '2025-03-06 13:04:29');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('73d3b976-4ff9-4c68-8108-25e2820d7a64', '562xf', '2025-03-14 13:37:54');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('79621eeb-b2e1-4f68-8f1e-8517c921fa76', '7npdx', '2025-02-19 15:13:56');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('79ee66b7-6b17-48f0-8b52-ae6141cbda84', '3d3g4', '2025-03-03 01:32:40');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('84d068fb-7611-4baf-869d-ee5a19ac42e4', '8pn25', '2025-03-06 17:44:56');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('87e1b8b2-cc30-477c-80be-f4607130a3ee', 'm23f5', '2025-02-19 16:56:17');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('8985c2eb-15e4-46a6-86fb-b8e45ae8d2d7', 'axyfm', '2025-02-19 16:28:22');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('8bb6fb25-2604-4087-86a3-783b6af098e3', '3mxc2', '2025-02-19 15:01:24');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('90f91916-5f2a-4c4e-8f37-813fa13f0c91', '75cgg', '2025-03-06 17:44:53');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('922f8b4a-f24b-4de3-893e-2609700ed186', '3c65a', '2025-03-14 13:39:11');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('952b0d6b-c19d-48b2-8d60-e3493395a11b', 'afdm2', '2025-02-19 15:14:14');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('9755bc5f-3072-44c3-843a-b50dbc34bcf6', '8nn8e', '2025-02-19 16:18:15');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('98406bc4-d43f-43e3-8527-562574abc952', '7ndpd', '2025-02-19 14:56:40');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('9a28498f-874e-48ed-8a3a-c14710717c6f', 'wde8b', '2025-03-06 11:41:31');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('9e745139-0831-4bb9-84ab-48cfbbc6fddd', 'cpyx8', '2025-03-06 11:59:38');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('a8f9cbd0-c3da-461b-848d-6b23b1ab727c', '7fm2m', '2025-03-06 13:26:49');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('ab34cf1b-0b1d-4508-8ecd-2aa3e453fbee', 'xgnmn', '2025-03-06 17:45:19');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('ad15d503-c096-4d70-8da3-8e2b1cb57cfa', '68gne', '2025-03-06 11:59:38');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('b0474ec0-3d4f-4698-8272-449fec13ad09', 'p3feb', '2025-03-14 13:39:11');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('b37ae793-a650-4e59-8d5a-cec5052b318d', 'dgcfg', '2025-02-19 15:33:50');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('b7bb4cd1-5013-4f46-867b-193237c52d6f', 'nd63m', '2025-02-19 15:22:45');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('b883d1a1-db8c-4c76-838b-5a42acce270b', '6wfb6', '2025-03-06 17:45:19');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('c13fff12-aa15-4dd8-8579-3c17b8824499', 'b2764', '2025-03-06 13:26:49');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('c3b32c43-c9ff-46a2-876e-5e18b6517c78', 'mxfwn', '2025-03-06 12:11:21');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('c52c72a4-8cc0-4df9-891a-7a3161ee467c', 'ng2fp', '2025-02-19 16:56:06');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('d14549c2-44df-4c24-8b7f-8843c15726e5', 'pamax', '2025-02-19 16:28:22');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('d63a7225-9475-41c1-8a85-7b64e4c537b8', 'n2p5w', '2025-03-06 15:23:55');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('d849b9b1-b7c8-4483-82a5-dc45b0680de1', '72f5x', '2025-03-14 13:39:11');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('de7d1194-5d3e-4536-8643-422b3d136233', '24egf', '2025-03-06 11:49:21');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('e164c638-1fb2-4c78-8009-6e1bffa89b7e', 'nd2ca', '2025-03-06 12:02:16');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('e55e21c1-ee0b-4105-8b17-611b53a9a3bc', 'm6n35', '2025-03-06 15:19:02');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('e9108da1-2ce3-4c43-8829-385d0587006f', '82w35', '2025-03-06 12:11:21');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('eb1d9e95-95f6-448e-8570-1392a50c4b4d', 'nw8dn', '2025-03-06 11:35:23');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('ebfe9ad2-5e42-4b1e-8113-f869fe7242c5', '3wfng', '2025-02-19 12:56:06');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('f13bb03c-6052-42e5-8349-9e79ce498a51', '555xx', '2025-02-19 15:14:15');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('f7d88771-04c1-4f19-8d00-e1bed49a8898', 'gxxbf', '2025-02-19 14:57:24');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('f7f5674e-07e2-43d9-8f81-7d2a73c4fcf1', 'mn68x', '2025-03-06 11:41:31');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('fcf3f2cf-4f7b-4c8e-8191-10980323f08b', 'ew434', '2025-03-06 17:45:19');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('fdaab908-7d7b-4619-8048-a4a54adce122', 'p5wn7', '2025-03-06 14:51:52');
INSERT INTO `sys_captcha` (`uuid`, `code`, `expire_time`) VALUES ('feb7bd51-521c-4860-8865-0fe92a0216f6', 'pce3w', '2025-03-06 17:46:00');
COMMIT;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `param_key` varchar(50) DEFAULT NULL COMMENT 'key',
  `param_value` varchar(2000) DEFAULT NULL COMMENT 'value',
  `status` tinyint DEFAULT '1' COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `param_key` (`param_key`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统配置信息表';

-- ----------------------------
-- Records of sys_config
-- ----------------------------
BEGIN;
INSERT INTO `sys_config` (`id`, `param_key`, `param_value`, `status`, `remark`) VALUES (1, 'CLOUD_STORAGE_CONFIG_KEY', '{\n    \"aliyunAccessKeyId\": \"LTAI5tCrhEHskSewGovxUqTF\",\n    \"aliyunAccessKeySecret\": \"BPAEImr9MsiZj0XV2xFFWGEuvAyeZg\",\n    \"aliyunBucketName\": \"996cwp\",\n    \"aliyunDomain\": \"https://996cwp.oss-cn-beijing.aliyuncs.com\",\n    \"aliyunEndPoint\": \"oss-cn-beijing.aliyuncs.com\",\n    \"aliyunPrefix\": \"\",\n    \"qcloudBucketName\": \"\",\n    \"qcloudDomain\": \"\",\n    \"qcloudPrefix\": \"\",\n    \"qcloudSecretId\": \"\",\n    \"qcloudSecretKey\": \"\",\n    \"qiniuAccessKey\": \"NrgMfABZxWLo5B-YYSjoE8-AZ1EISdi1Z3ubLOeZ\",\n    \"qiniuBucketName\": \"ios-app\",\n    \"qiniuDomain\": \"http://7xqbwh.dl1.z0.glb.clouddn.com\",\n    \"qiniuPrefix\": \"upload\",\n    \"qiniuSecretKey\": \"uIwJHevMRWU0VLxFvgy0tAcOdGqasdtVlJkdy6vV\",\n    \"type\": 2\n}', 0, '云存储配置信息');
COMMIT;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) DEFAULT NULL COMMENT '请求参数',
  `time` bigint NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统日志';

-- ----------------------------
-- Records of sys_log
-- ----------------------------
BEGIN;
INSERT INTO `sys_log` (`id`, `username`, `operation`, `method`, `params`, `time`, `ip`, `create_date`) VALUES (7, 'admin', '保存角色', 'io.renren.modules.sys.controller.SysRoleController.save()', '[{\"roleId\":1,\"roleName\":\"普通用户\",\"remark\":\"普通用户\",\"createUserId\":1,\"menuIdList\":[-666666],\"createTime\":\"Mar 6, 2025 12:46:42 PM\"}]', 85, '0:0:0:0:0:0:0:1', '2025-03-06 12:46:42');
INSERT INTO `sys_log` (`id`, `username`, `operation`, `method`, `params`, `time`, `ip`, `create_date`) VALUES (8, 'admin', '保存角色', 'io.renren.modules.sys.controller.SysRoleController.save()', '[{\"roleId\":2,\"roleName\":\"普通用户\",\"remark\":\"普通用户\",\"createUserId\":1,\"menuIdList\":[-666666],\"createTime\":\"Mar 6, 2025 12:46:42 PM\"}]', 18, '0:0:0:0:0:0:0:1', '2025-03-06 12:46:43');
INSERT INTO `sys_log` (`id`, `username`, `operation`, `method`, `params`, `time`, `ip`, `create_date`) VALUES (9, 'admin', 'Delete角色', 'io.renren.modules.sys.controller.SysRoleController.delete()', '[[2]]', 17, '0:0:0:0:0:0:0:1', '2025-03-06 12:46:47');
INSERT INTO `sys_log` (`id`, `username`, `operation`, `method`, `params`, `time`, `ip`, `create_date`) VALUES (10, 'admin', 'Edit菜单', 'io.renren.modules.sys.controller.SysMenuController.update()', '[{\"menuId\":2,\"parentId\":1,\"name\":\"用户列表\",\"url\":\"sys/user\",\"type\":1,\"icon\":\"admin\",\"orderNum\":1,\"list\":[]}]', 20, '0:0:0:0:0:0:0:1', '2025-03-06 21:01:54');
INSERT INTO `sys_log` (`id`, `username`, `operation`, `method`, `params`, `time`, `ip`, `create_date`) VALUES (11, 'admin', 'Edit菜单', 'io.renren.modules.sys.controller.SysMenuController.update()', '[{\"menuId\":2133,\"parentId\":0,\"name\":\"比赛活动管理\",\"url\":\"app/competition\",\"type\":1,\"icon\":\"config\",\"orderNum\":6,\"list\":[]}]', 3, '0:0:0:0:0:0:0:1', '2025-03-06 21:02:08');
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint DEFAULT NULL COMMENT '父菜单ID，First Menu为0',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2138 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单管理';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (1, 0, '系统管理', NULL, NULL, 0, 'system', 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (2, 1, '用户列表', 'sys/user', NULL, 1, 'admin', 1);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (3, 1, '角色管理', 'sys/role', NULL, 1, 'role', 2);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (4, 1, '菜单管理', 'sys/menu', NULL, 1, 'menu', 3);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (15, 2, '查看', NULL, 'sys:user:list,sys:user:info', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (16, 2, 'Add', NULL, 'sys:user:save,sys:role:select', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (17, 2, 'Edit', NULL, 'sys:user:update,sys:role:select', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (18, 2, 'Delete', NULL, 'sys:user:delete', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (19, 3, '查看', NULL, 'sys:role:list,sys:role:info', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (20, 3, 'Add', NULL, 'sys:role:save,sys:menu:list', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (21, 3, 'Edit', NULL, 'sys:role:update,sys:menu:list', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (22, 3, 'Delete', NULL, 'sys:role:delete', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (23, 4, '查看', NULL, 'sys:menu:list,sys:menu:info', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (24, 4, 'Add', NULL, 'sys:menu:save,sys:menu:select', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (25, 4, 'Edit', NULL, 'sys:menu:update,sys:menu:select', 2, NULL, 0);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (26, 4, 'Delete', NULL, 'sys:menu:delete', 2, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_oss
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss`;
CREATE TABLE `sys_oss` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `url` varchar(200) DEFAULT NULL COMMENT 'URL地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文件上传';

-- ----------------------------
-- Records of sys_oss
-- ----------------------------
BEGIN;
INSERT INTO `sys_oss` (`id`, `url`, `create_date`) VALUES (1, 'https://996cwp.oss-cn-beijing.aliyuncs.com/20250306/00feaabd7eda4f248e780cf99b0977fc.jpeg', '2025-03-06 13:12:51');
INSERT INTO `sys_oss` (`id`, `url`, `create_date`) VALUES (2, 'https://996cwp.oss-cn-beijing.aliyuncs.com/20250306/68f254c822f44caebf501ae69eac5567.jpeg', '2025-03-06 14:49:44');
INSERT INTO `sys_oss` (`id`, `url`, `create_date`) VALUES (3, 'https://996cwp.oss-cn-beijing.aliyuncs.com/20250306/02e2053dd84f45e5a1b3e93edcb8b5d0.png', '2025-03-06 18:57:01');
INSERT INTO `sys_oss` (`id`, `url`, `create_date`) VALUES (4, 'https://996cwp.oss-cn-beijing.aliyuncs.com/20250306/6023998b65934948acf38295e6874c52.jpeg', '2025-03-06 19:29:30');
INSERT INTO `sys_oss` (`id`, `url`, `create_date`) VALUES (5, 'https://996cwp.oss-cn-beijing.aliyuncs.com/20250306/796ffc7bde88487f9fd4021276c240a5.png', '2025-03-06 19:29:42');
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` (`role_id`, `role_name`, `remark`, `create_user_id`, `create_time`) VALUES (2, '普通用户', '普通用户', 1, '2025-03-06 12:46:42');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色与菜单对应关系';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (1, 1, -666666);
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) DEFAULT NULL COMMENT '盐',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `status` tinyint DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`user_id`, `username`, `password`, `salt`, `email`, `mobile`, `status`, `create_user_id`, `create_time`) VALUES (1, 'admin', '9ec9750e709431dad22365cabc5c625482e574c74adaebba7dd02f1129e4ce1d', 'YzcmCZNvbXocrsz9dm8e', 'r123@qq.com', '18709462554', 1, 1, '2025-01-11 11:11:11');
INSERT INTO `sys_user` (`user_id`, `username`, `password`, `salt`, `email`, `mobile`, `status`, `create_user_id`, `create_time`) VALUES (2, 'cwp123', '89c821a9357e2e79edcafe33ce3212f33af408c2002c6a9601ba77476b5277c1', 'jGLq213wmglW95r0Ui6o', '123@qq.com', NULL, 1, NULL, '2025-03-06 12:06:36');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户与角色对应关系';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token` (
  `user_id` bigint NOT NULL,
  `token` varchar(100) NOT NULL COMMENT 'token',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统用户Token';

-- ----------------------------
-- Records of sys_user_token
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_token` (`user_id`, `token`, `expire_time`, `update_time`) VALUES (1, '1912b2b8f117e7f7787c9fe61a93ae2f', '2025-03-07 03:49:10', '2025-03-06 15:49:10');
COMMIT;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `mobile` varchar(20) NOT NULL COMMENT '手机号',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户';

-- ----------------------------
-- Records of tb_user
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
