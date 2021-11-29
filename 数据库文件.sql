-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: tefs
-- ------------------------------------------------------
-- Server version	5.7.29-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `Admin_ID` int(11) NOT NULL AUTO_INCREMENT,
  `User_Name` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `Password` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`Admin_ID`),
  UNIQUE KEY `AK_Key_2` (`User_Name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'wzl','123456');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `edu_videosource`
--

DROP TABLE IF EXISTS `edu_videosource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `edu_videosource` (
  `videoId` int(11) NOT NULL AUTO_INCREMENT COMMENT '视频id',
  `name` varchar(200) DEFAULT NULL COMMENT '视频名称',
  `videoSize` varchar(200) DEFAULT NULL COMMENT '视频原片大小',
  `addTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '上传时间',
  `status` varchar(200) DEFAULT 'init' COMMENT '转码状态 (init：待转码 progress：转码中 finish：转码成功 error：转码失败 delete : 已回收)',
  `videoLength` varchar(11) DEFAULT '0' COMMENT '转码后大小',
  `idVarchar` varchar(200) DEFAULT NULL COMMENT '视频播放码',
  `videoDuration` int(11) DEFAULT '0' COMMENT '视频时长（s）',
  `imageUrl` varchar(255) DEFAULT NULL COMMENT '封面图片地址',
  `fileType` int(2) DEFAULT '1' COMMENT '播放文件类型 1：视频 2：音频',
  `initType` int(2) DEFAULT '1' COMMENT '源文件类型 1：视频 2：音频',
  `uploadUserId` int(11) DEFAULT '0' COMMENT '上传用户id',
  `videoType` varchar(20) DEFAULT NULL COMMENT '视频类型(ninetySixkoo96刻....)',
  PRIMARY KEY (`videoId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='录播视频表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `edu_videosource`
--

LOCK TABLES `edu_videosource` WRITE;
/*!40000 ALTER TABLE `edu_videosource` DISABLE KEYS */;
/*!40000 ALTER TABLE `edu_videosource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `login_name` varchar(20) NOT NULL DEFAULT '' COMMENT '登录名',
  `login_pwd` varchar(32) NOT NULL DEFAULT '' COMMENT '登录密码',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户真实姓名名',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态.0: 正常,1:冻结,2：删除',
  `last_login_time` timestamp NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(20) DEFAULT NULL COMMENT '最后登录ip',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `email` varchar(50) DEFAULT NULL COMMENT '邮件地址',
  `tel` varchar(12) DEFAULT NULL COMMENT '手机号码',
  `role_id` int(11) DEFAULT '0' COMMENT '所属角色id',
  `bindingRole` int(11) DEFAULT NULL COMMENT '绑定角色（暂用于绑定老师 绑定分校）',
  `uuid` varchar(20) DEFAULT NULL COMMENT '随机字符串',
  `is_examine` int(2) DEFAULT NULL COMMENT '审核状态1:已审核 0:未审核',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `User_ID` int(11) NOT NULL AUTO_INCREMENT,
  `User_Name` varchar(20) COLLATE utf8_bin NOT NULL,
  `User_RealName` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `Password` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `Phone` varchar(11) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`User_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'yxy','杨昕语','123456','19817176579'),(2,'Mike','麦克','123456','11111111111'),(3,'Tom','汤姆','666666','99999999999');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `video`
--

DROP TABLE IF EXISTS `video`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `video` (
  `Video_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '视频id',
  `Video_Name` varchar(200) DEFAULT NULL COMMENT '视频名称',
  `Video_Url` varchar(200) DEFAULT NULL COMMENT '视频地址',
  `Video_Size` varchar(200) DEFAULT NULL COMMENT '视频原片大小',
  `Video_Addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '上传时间',
  `Video_Status` varchar(200) DEFAULT 'init' COMMENT '转码状态 (init：待转码 progress：转码中 finish：转码成功 error：转码失败 delete : 已回收)',
  `Video_Length` varchar(11) DEFAULT '0' COMMENT '转码后大小',
  `Video_Duration` int(11) DEFAULT '0' COMMENT '视频时长（s）',
  `Video_Poster` varchar(255) DEFAULT NULL COMMENT '封面图片地址',
  `User_ID` int(11) DEFAULT '0' COMMENT '上传用户id',
  `Video_Type` varchar(20) DEFAULT NULL COMMENT '视频类型',
  PRIMARY KEY (`Video_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='录播视频表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video`
--

LOCK TABLES `video` WRITE;
/*!40000 ALTER TABLE `video` DISABLE KEYS */;
INSERT INTO `video` VALUES (1,'java教程','http://localhost:8181/api/file/java教程.mp4',NULL,'2021-02-21 13:31:09','init','0',0,NULL,1,NULL),(2,'恭喜发财','http://localhost:8181/api/file/恭喜发财.mp4',NULL,'2021-02-21 13:31:09','init','0',0,NULL,1,NULL),(11,'mike1','http://localhost:8181/api/file/1614012057818.mp4',NULL,'2021-02-22 16:41:02','init','0',0,NULL,2,NULL),(12,'Mike2','http://localhost:8181/api/file/1614012211274.mp4',NULL,'2021-02-22 16:43:43','init','0',0,NULL,2,NULL),(13,'Mike3','http://localhost:8181/api/file/1614012321427.mp4',NULL,'2021-02-22 16:45:26','init','0',0,NULL,2,NULL),(14,'钢琴曲','http://localhost:8181/api/file/1614138796056.mp4',NULL,'2021-02-24 04:47:41','init','0',0,NULL,1,NULL),(15,'测试1','http://localhost:8181/api/file/1614185412417.mp4',NULL,'2021-02-24 16:50:13','init','0',0,NULL,1,NULL),(16,'测试2','http://localhost:8181/api/file/1614185424787.mp4',NULL,'2021-02-24 16:50:25','init','0',0,NULL,1,NULL),(17,'测试3','http://localhost:8181/api/file/1614185437376.mp4',NULL,'2021-02-24 16:50:38','init','0',0,NULL,1,NULL);
/*!40000 ALTER TABLE `video` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-02-27 18:39:38
