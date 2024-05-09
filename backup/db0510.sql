-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: db_shoe_store
-- ------------------------------------------------------
-- Server version	8.0.35

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
-- Table structure for table `account_role`
--

DROP TABLE IF EXISTS `account_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account_role` (
  `account_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  KEY `FKlxcbsh4odc9s9spjfw4jgxyqw` (`role_id`),
  KEY `FKne5hjaf6i1wun5wnxlxvtl616` (`account_id`),
  CONSTRAINT `FKlxcbsh4odc9s9spjfw4jgxyqw` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FKne5hjaf6i1wun5wnxlxvtl616` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_role`
--

LOCK TABLES `account_role` WRITE;
/*!40000 ALTER TABLE `account_role` DISABLE KEYS */;
INSERT INTO `account_role` VALUES (2,2),(3,2),(4,3),(5,3),(6,3),(7,3),(8,3),(9,3),(10,3),(11,3),(1,1);
/*!40000 ALTER TABLE `account_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `is_account_non_locked` bit(1) DEFAULT NULL,
  `is_enabled` bit(1) DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `verification_code` varchar(6) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `verification_code_expiration_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (1,'admin@gmail.com',_binary '',_binary '','$2a$10$UGIU0FEBs2d0PMnhnFO91.cZYxACm1XDVDBBhjkk8FBdkuRPufOSO',NULL,NULL),(2,'nhanvien@gmail.com',_binary '',_binary '','$2a$10$FO/wibVt31M8DaXcfPKdMuRox1p.qFD.BneUlTGesD3axWx0KGxAS',NULL,NULL),(3,'nhanvien2@gmail.com',_binary '',_binary '','$2a$10$QqE18BY8FfTy4BaOysHl7ujpJ/NBxr0s8hF0VG9i/0b9YPyh35Mx2',NULL,NULL),(4,'manonguyen123@gmail.com',_binary '',_binary '','$2a$10$2sNtEMUy74HVLsaJT1Yr8ej76gC3dg.0OscoPblBm0uMIjE/SErC6',NULL,NULL),(5,'khachhang@gmail.com',_binary '',_binary '','$2a$10$ujDwItBxHovVgiQ4AxBFeu8n8riJa7xUQ84xVYB5lo.CN4djaoYQi',NULL,NULL),(6,'khachhang2@gmail.com',_binary '',_binary '','$2a$10$6TE.0G8ibs5A0HJYpvx.vuJnJAlXNWCbqHF5liFWBnuB.IFnz8u3G',NULL,NULL),(7,'thuphuongtran767@gmail.com',_binary '',_binary '','$2a$10$q3dhV3/G8m06XqoZHf6VpubZK6LPZe4XLK2IVCzE2KthBQDe/gILK',NULL,NULL),(8,'domaihoa2001bv@gmail.com',_binary '',_binary '','$2a$10$5yCGVZQ6CD2BB4qCfxyCJOkoQy.ztPQ6qs0hYTlsbxknttmKxyI8S',NULL,NULL),(9,'khachhang3@gmail.com',_binary '',_binary '','$2a$10$K2KWCZ5mC8t27Vct8h2wf.6gF4bxDF2FHOjCBT/KEySAYFi8zQxme',NULL,NULL),(10,'tupham1120@gmail.com',_binary '',_binary '','$2a$10$fRotr0OBug.DhitsJQesluiDTZ7YzMfBc1lsdGAj0/x2e8dm0BhqC',NULL,NULL),(11,'manhnv291201@gmail.com',_binary '',_binary '','$2a$10$z1c.8iRbJopbILqKOWhCJuH.KgetgaY1rI2x/VumQs/WYWCxJldou',NULL,NULL);
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `brands`
--

DROP TABLE IF EXISTS `brands`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `brands` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `slug` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `thumbnail` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `brands`
--

LOCK TABLES `brands` WRITE;
/*!40000 ALTER TABLE `brands` DISABLE KEYS */;
INSERT INTO `brands` VALUES (1,'2024-05-01 17:15:15.926000','2024-05-01 17:15:15.926000',_binary '','Nike','nike','4f2d1a49-5517-469a-a850-2e15f00223d5.jpg'),(2,'2024-05-01 17:15:29.956000','2024-05-01 17:15:29.956000',_binary '','Adidas','adidas','eed4c487-58e1-4d58-829e-bc788355edde.webp'),(3,'2024-05-01 17:18:46.360000','2024-05-01 17:18:46.360000',_binary '','Puma','puma','bc07e238-bb61-4a41-95a6-f15196a304f1.jpg'),(4,'2024-05-01 17:19:15.794000','2024-05-01 17:19:15.794000',_binary '','Under Armour','under-armour','02f2f549-46ab-4cf9-871a-e37360261b37.jpg'),(5,'2024-05-01 17:19:34.558000','2024-05-01 17:19:34.558000',_binary '','Reebok','reebok','5504b3f2-a5c3-472a-838c-9b4bb21439d2.webp'),(6,'2024-05-01 17:19:55.009000','2024-05-01 17:19:55.009000',_binary '','Converse','converse','dd50da89-a65d-4792-8042-61c91ca6db2a.png'),(7,'2024-05-01 17:20:20.269000','2024-05-01 17:20:20.269000',_binary '','Vans','vans','e8313fdf-c57b-4c29-9aea-f5f7eb4bc7b1.jpg'),(8,'2024-05-01 17:20:56.277000','2024-05-01 17:20:56.277000',_binary '','New Balance','new-balance','49c5ba59-ddd4-4dc1-b7c1-f2702e0975f7.png'),(9,'2024-05-01 17:21:11.937000','2024-05-01 17:21:11.937000',_binary '','Gucci','gucci','1c968560-00cd-40cd-aae4-bd4898c25e25.png'),(10,'2024-05-01 17:21:30.988000','2024-05-01 17:21:30.988000',_binary '','Balenciaga','balenciaga','d9d8b99f-c351-475b-ab44-86241ab40922.webp'),(11,'2024-05-01 17:22:04.141000','2024-05-01 17:22:04.141000',_binary '','Louis Vuitton','louis-vuitton','64796c7d-6879-45b9-ba3a-1ad5c4431a8e.webp');
/*!40000 ALTER TABLE `brands` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_details`
--

DROP TABLE IF EXISTS `cart_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `quantity` int NOT NULL,
  `cart_id` bigint NOT NULL,
  `product_details_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkcochhsa891wv0s9wrtf36wgt` (`cart_id`),
  KEY `FKtibhs55lvuicxc2r84wqiynt` (`product_details_id`),
  CONSTRAINT `FKkcochhsa891wv0s9wrtf36wgt` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`),
  CONSTRAINT `FKtibhs55lvuicxc2r84wqiynt` FOREIGN KEY (`product_details_id`) REFERENCES `product_details` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_details`
--

LOCK TABLES `cart_details` WRITE;
/*!40000 ALTER TABLE `cart_details` DISABLE KEYS */;
INSERT INTO `cart_details` VALUES (17,'2024-05-02 02:32:29.104000','2024-05-02 02:32:29.104000',1,6,695),(18,'2024-05-02 02:32:39.258000','2024-05-02 02:32:39.258000',2,6,721),(19,'2024-05-02 02:32:56.524000','2024-05-02 02:32:56.524000',2,6,479),(21,'2024-05-02 02:43:40.683000','2024-05-02 02:43:40.683000',1,8,696),(25,'2024-05-04 03:52:33.803000','2024-05-04 03:52:33.803000',1,1,490),(26,'2024-05-04 04:11:56.625000','2024-05-04 04:11:56.625000',1,1,666),(27,'2024-05-04 04:36:15.815000','2024-05-04 04:36:15.815000',2,1,422),(28,'2024-05-04 14:27:33.166000','2024-05-04 14:27:33.166000',1,8,705);
/*!40000 ALTER TABLE `cart_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carts`
--

DROP TABLE IF EXISTS `carts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8ba3sryid5k8a9kidpkvqipyt` (`customer_id`),
  CONSTRAINT `FK8ba3sryid5k8a9kidpkvqipyt` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carts`
--

LOCK TABLES `carts` WRITE;
/*!40000 ALTER TABLE `carts` DISABLE KEYS */;
INSERT INTO `carts` VALUES (1,'2024-05-02 01:05:16.135000','2024-05-02 01:05:16.135000',1714586716116),(2,'2024-05-02 01:05:51.988000','2024-05-02 01:05:51.988000',1714586751986),(3,'2024-05-02 01:06:21.125000','2024-05-02 01:06:21.125000',1714586781123),(4,'2024-05-02 01:06:49.953000','2024-05-02 01:06:49.953000',1714586809953),(5,'2024-05-02 01:08:06.902000','2024-05-02 01:08:06.902000',1714586886902),(6,'2024-05-02 01:08:53.382000','2024-05-02 01:08:53.382000',1714586933374),(7,'2024-05-02 01:22:57.542000','2024-05-02 01:22:57.542000',1714587777426),(8,'2024-05-02 01:51:11.008000','2024-05-02 01:51:11.008000',1714589470989);
/*!40000 ALTER TABLE `carts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `slug` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'2024-05-01 18:03:42.498000','2024-05-01 18:03:42.498000',_binary '','Nam','nam'),(2,'2024-05-01 18:03:47.618000','2024-05-01 18:03:47.618000',_binary '','Nữ','nu'),(3,'2024-05-01 18:03:54.931000','2024-05-01 18:03:54.931000',_binary '','Unisex','unisex'),(4,'2024-05-01 18:04:02.545000','2024-05-01 18:04:02.545000',_binary '','Bé trai','be-trai'),(5,'2024-05-01 18:04:08.310000','2024-05-01 18:04:08.310000',_binary '','Bé gái','be-gai');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `id` bigint NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `address_detail` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `city` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `district` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `ward` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `account_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKor0fx9fttvasr4grtaqnltyrl` (`account_id`),
  CONSTRAINT `FKor0fx9fttvasr4grtaqnltyrl` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1714586716116,'2024-05-02 01:05:16.123000','2024-05-02 01:10:20.442000','Số 14, Ngách 415/2, Ngõ 415','Hà Nội','Huyện Gia Lâm','Manh Nguyen Van','0359124586','Xã phù đổng',4),(1714586751986,'2024-05-02 01:05:51.988000','2024-05-02 02:15:33.361000','Số 19, Đường ABC','Hải Dương','Huyện Tứ Kỳ','Dịu Thanh','0356124565','Xã Đại Sơn',5),(1714586781123,'2024-05-02 01:06:21.125000','2024-05-02 02:19:19.481000','Xóm Dạ Sơn','Nghệ An','Huyện Yên Thành','Linh Thái','0359446664','Xã đô thành',6),(1714586809953,'2024-05-02 01:06:49.953000','2024-05-02 02:12:23.603000','Số 100','Nam Định','Huyện Hải Hậu','Thu Phương','0345678914','Xã hải bắc',7),(1714586886902,'2024-05-02 01:08:06.902000','2024-05-02 02:09:02.835000','Số 2, Đường D','Hà Nội','Huyện Ba Vì','Mai Hi','0559452699','Xã khánh thượng',8),(1714586933374,'2024-05-02 01:08:53.382000','2024-05-02 02:31:43.483000','Số 64, Đường Y','Hồ Chí Minh','Huyện Bình Chánh','Bạn Hoa','0386124586','Xã vĩnh lộc a',9),(1714587777426,'2024-05-02 01:22:57.491000','2024-05-02 01:22:57.559000',NULL,NULL,NULL,'Việt Tú',NULL,NULL,10),(1714589470989,'2024-05-02 01:51:10.998000','2024-05-02 02:37:43.038000','Số 64, Đường Y','Bà Rịa - Vũng Tàu','Huyện Châu Đức','Bạn Mạnh','0359799499','Xã cù bị',11),(7777777777777,'2024-05-02 01:51:10.998000','2024-05-02 01:51:10.998000','Số 97','Hà Nội','Quận Đống Đa','Khách Lẻ','0777777777','Đặng Văn Ngữ',NULL);
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `id` bigint NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `address_detail` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `avatar` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `city` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `district` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `gender` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `status` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `ward` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `account_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8dwhb0qmor08fl06pde1bef3c` (`account_id`),
  CONSTRAINT `FK8dwhb0qmor08fl06pde1bef3c` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (1111111111111,'2024-05-01 17:41:04.525000','2024-05-06 09:27:59.281000',NULL,NULL,NULL,NULL,NULL,NULL,'Nguyễn Văn Mạnh',NULL,NULL,NULL,1),(1714561478492,'2024-05-01 18:04:38.517000','2024-05-01 18:04:38.517000','Số 14, Ngách 415/2, Ngõ 415','e28af8a2-4a1b-4b7e-98eb-f58057c07a0d.jpg','2001-12-29','Thành phố Hà Nội','Quận Long Biên','Nam','Mạnh Văn','0959124586','Đang làm việc','Phường Thượng Thanh',2),(1714561577522,'2024-05-01 18:06:17.537000','2024-05-01 18:06:17.537000','Số 64, Đường Y','8e0070b3-a96f-4be5-bc27-72452fed6aca.jpg','2001-09-08','Tỉnh Bắc Ninh','Thành phố Bắc Ninh','Nam','Tiến Nguyễn','0386124586','Đang làm việc','Phường Vũ Ninh',3);
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_details`
--

DROP TABLE IF EXISTS `order_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `price` bigint NOT NULL,
  `quantity` int NOT NULL,
  `sale_price` bigint DEFAULT NULL,
  `order_id` bigint NOT NULL,
  `product_details_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjyu2qbqt8gnvno9oe9j2s2ldk` (`order_id`),
  KEY `FKos61972xo18nahkjujx4ck9ys` (`product_details_id`),
  CONSTRAINT `FKjyu2qbqt8gnvno9oe9j2s2ldk` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKos61972xo18nahkjujx4ck9ys` FOREIGN KEY (`product_details_id`) REFERENCES `product_details` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_details`
--

LOCK TABLES `order_details` WRITE;
/*!40000 ALTER TABLE `order_details` DISABLE KEYS */;
INSERT INTO `order_details` VALUES (1,336000,4,NULL,1,622),(2,737000,2,NULL,2,655),(3,413000,2,NULL,3,707),(4,575000,3,NULL,3,695),(5,393000,2,334050,4,38),(6,1669000,3,1418650,4,6),(7,1669000,1,1418650,5,6),(8,393000,3,334050,5,48),(9,575000,2,NULL,6,695),(10,609000,3,NULL,7,691),(11,860000,2,NULL,8,292),(12,981000,2,NULL,9,680),(13,1019000,1,NULL,10,434),(14,1256000,2,NULL,10,469),(15,1502000,2,NULL,10,479),(16,814000,2,NULL,11,401),(17,300000,2,NULL,12,256),(18,128000,2,NULL,12,25),(19,447000,3,NULL,13,79),(20,609000,3,NULL,14,691),(21,413000,4,NULL,15,710),(22,687000,3,NULL,16,336),(23,972000,2,NULL,17,490),(24,221000,3,NULL,18,223),(25,415000,2,NULL,19,147),(26,447000,2,NULL,20,531),(27,447000,2,NULL,20,542),(28,1669000,3,1418650,21,8),(29,737000,2,NULL,22,654),(30,668000,3,NULL,22,456),(31,349000,2,NULL,23,364),(32,1256000,3,NULL,24,466),(33,972000,2,NULL,25,488),(34,860000,2,NULL,26,293),(35,336000,1,NULL,27,596),(36,499000,3,NULL,28,576),(37,167000,4,NULL,29,123),(38,972000,6,NULL,30,490),(39,299000,7,NULL,31,553),(40,432000,5,NULL,32,59),(41,1669000,2,1418650,33,7),(42,1669000,2,1418650,34,7),(43,737000,2,NULL,35,665),(44,737000,2,NULL,36,665),(45,575000,2,NULL,37,695),(46,575000,2,NULL,38,695),(47,814000,2,NULL,39,406),(48,814000,2,NULL,40,406),(49,609000,1,NULL,41,691),(50,609000,1,NULL,42,691),(51,756000,1,NULL,43,720),(52,737000,1,NULL,44,654),(53,221000,1,NULL,45,221),(54,447000,2,NULL,46,538),(55,413000,2,NULL,47,705),(56,668000,2,NULL,48,459),(57,981000,1,NULL,49,679),(58,981000,1,NULL,50,679),(59,499000,2,NULL,51,573),(60,299000,1,NULL,52,553),(61,432000,1,NULL,53,343),(64,1418650,1,1418650,56,6),(65,1418650,1,1418650,57,7),(66,447000,2,NULL,57,78),(67,737000,2,663300,58,666),(68,1418650,2,1418650,59,8),(69,1502000,2,1351800,60,479),(70,1418650,2,1418650,61,8),(71,1418650,2,1418650,62,8),(72,1418650,1,1418650,63,8),(73,981000,2,NULL,64,678),(74,981000,2,NULL,65,679),(75,972000,1,NULL,66,490),(76,981000,3,NULL,67,679),(77,1669000,2,NULL,68,9);
/*!40000 ALTER TABLE `order_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `cancel_date` datetime(6) DEFAULT NULL,
  `cancel_reason` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `carrier_logo` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `carrier_name` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `completed_date` datetime(6) DEFAULT NULL,
  `confirm_date` datetime(6) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `delivery_date` datetime(6) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `note` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `order_status` int NOT NULL,
  `order_type` bit(1) DEFAULT NULL,
  `payment_date` datetime(6) DEFAULT NULL,
  `payment_method` int NOT NULL,
  `payment_status` bit(1) NOT NULL,
  `phone` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `return_date` datetime(6) DEFAULT NULL,
  `service` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `total_fee` bigint DEFAULT NULL,
  `transaction_id` bigint DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  `employee_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpxtb8awmi0dk6smoh2vp1litg` (`customer_id`),
  KEY `FKfhl8bv0xn3sj33q2f3scf1bq6` (`employee_id`),
  CONSTRAINT `FKfhl8bv0xn3sj33q2f3scf1bq6` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`),
  CONSTRAINT `FKpxtb8awmi0dk6smoh2vp1litg` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_58_00_1c812f1a13f8f5441837990ee16e5fcf.jpg','Giao Hàng Tiết Kiệm','2024-02-02 02:48:06.878000','2024-05-02 02:48:02.341000','2024-05-01 01:11:35.856000','2024-05-02 02:48:04.658000','Manh Nguyen Van','',3,_binary '','2024-05-02 02:48:06.878000',0,_binary '','0359124586','2024-05-02 02:48:06.878000','Nhanh',37750,NULL,1714586716116,1111111111111),(2,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)','2024-01-02 02:48:16.779000','2024-05-02 02:48:11.909000','2024-04-02 01:11:49.214000','2024-05-02 02:48:14.869000','Manh Nguyen Van','',3,_binary '','2024-05-02 01:13:07.000000',1,_binary '','0359124586','2024-05-02 02:48:16.779000','Nhanh',24100,14397599,1714586716116,1111111111111),(3,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2022_06_13_12_05_02_badccf2a4495cd91623f437e2c29fe8d.png','Best Express','2024-05-02 09:52:10.632000','2024-05-02 09:51:20.504000','2024-03-02 01:13:56.965000','2024-05-02 09:51:47.037000','Manh Nguyen Van','',3,_binary '','2024-05-02 09:52:10.632000',0,_binary '','0359124586','2024-05-02 09:52:10.632000','Nhanh',13600,NULL,1714586716116,1111111111111),(4,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)',NULL,NULL,'2024-02-02 01:14:41.511000',NULL,'Manh Nguyen Van','',0,_binary '','2024-05-02 01:14:57.000000',1,_binary '','0359124586',NULL,'Nhanh',24100,14397601,1714586716116,NULL),(5,'Số 2, Đường D, Xã khánh thượng, Huyện Ba Vì, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_58_00_1c812f1a13f8f5441837990ee16e5fcf.jpg','Giao Hàng Tiết Kiệm','2024-05-02 02:47:49.366000','2024-05-02 02:47:45.221000','2024-05-02 02:09:17.245000','2024-05-02 02:47:47.175000','Mai Hi','',3,_binary '','2024-05-02 02:47:49.366000',0,_binary '','0559452699','2024-05-02 02:47:49.366000','Nhanh',37750,NULL,1714586886902,1111111111111),(6,'Số 2, Đường D, Xã khánh thượng, Huyện Ba Vì, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_58_00_1c812f1a13f8f5441837990ee16e5fcf.jpg','Giao Hàng Tiết Kiệm',NULL,'2024-05-02 02:51:04.259000','2024-01-02 02:09:32.816000','2024-05-02 02:51:06.368000','Mai Hi','',2,_binary '',NULL,0,_binary '\0','0559452699',NULL,'Nhanh',37750,NULL,1714586886902,1111111111111),(7,'Số 2, Đường D, Xã khánh thượng, Huyện Ba Vì, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2022_06_13_12_05_02_badccf2a4495cd91623f437e2c29fe8d.png','Best Express','2024-03-02 02:47:39.063000','2024-05-02 02:47:33.793000','2024-04-02 02:09:45.284000','2024-05-02 02:47:35.854000','Mai Hi','',3,_binary '','2024-05-02 02:47:39.063000',0,_binary '','0559452699','2024-05-02 02:47:39.063000','Nhanh',13600,NULL,1714586886902,1111111111111),(8,'Số 2, Đường D, Xã khánh thượng, Huyện Ba Vì, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)','2024-04-02 02:51:38.546000','2024-05-02 02:51:34.114000','2024-05-01 02:10:02.622000','2024-05-02 02:51:36.308000','Mai Hi','',3,_binary '','2024-05-02 02:51:38.546000',0,_binary '','0559452699','2024-05-02 02:51:38.546000','Nhanh',24100,NULL,1714586886902,1111111111111),(9,'Số 2, Đường D, Xã khánh thượng, Huyện Ba Vì, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_58_00_1c812f1a13f8f5441837990ee16e5fcf.jpg','Giao Hàng Tiết Kiệm',NULL,'2024-05-02 02:51:14.758000','2024-04-02 02:10:14.514000',NULL,'Mai Hi','',1,_binary '',NULL,0,_binary '\0','0559452699',NULL,'Nhanh',37750,NULL,1714586886902,1111111111111),(10,'Số 100, Xã hải bắc, Huyện Hải Hậu, Nam Định',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2022_06_13_12_05_02_badccf2a4495cd91623f437e2c29fe8d.png','Best Express',NULL,NULL,'2024-05-02 02:12:25.624000',NULL,'Thu Phương','',0,_binary '',NULL,0,_binary '\0','0345678914',NULL,'Nhanh',13600,NULL,1714586809953,NULL),(11,'Số 100, Xã hải bắc, Huyện Hải Hậu, Nam Định',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2022_06_13_12_05_02_badccf2a4495cd91623f437e2c29fe8d.png','Best Express',NULL,'2024-05-02 02:51:23.628000','2024-05-02 02:12:43.368000','2024-05-02 02:51:27.054000','Thu Phương','',2,_binary '',NULL,0,_binary '\0','0345678914',NULL,'Nhanh',13600,NULL,1714586809953,1111111111111),(12,'Số 100, Xã hải bắc, Huyện Hải Hậu, Nam Định',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)','2024-04-02 02:51:50.389000','2024-05-02 02:51:45.948000','2024-05-02 02:13:08.966000','2024-05-02 02:51:48.337000','Thu Phương','',3,_binary '','2024-05-02 02:51:50.389000',0,_binary '','0345678914','2024-05-02 02:51:50.389000','Nhanh',24100,NULL,1714586809953,1111111111111),(13,'Số 100, Xã hải bắc, Huyện Hải Hậu, Nam Định',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)','2024-03-02 02:46:44.958000','2024-05-02 02:46:40.948000','2024-05-02 02:13:31.077000','2024-05-02 02:46:42.948000','Thu Phương','',3,_binary '','2024-05-02 02:46:44.958000',0,_binary '','0345678914','2024-05-02 02:46:44.958000','Nhanh',24100,NULL,1714586809953,1111111111111),(14,'Số 19, Đường ABC, Xã Đại Sơn, Huyện Tứ Kỳ, Hải Dương',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_58_00_1c812f1a13f8f5441837990ee16e5fcf.jpg','Giao Hàng Tiết Kiệm','2024-02-02 02:47:04.999000','2024-05-02 02:47:01.153000','2024-05-02 02:15:39.515000','2024-05-02 02:47:02.877000','Dịu Thanh','',3,_binary '','2024-05-02 02:47:04.999000',0,_binary '','0356124565','2024-05-02 02:47:04.999000','Nhanh',37750,NULL,1714586751986,1111111111111),(15,'Số 19, Đường ABC, Xã Đại Sơn, Huyện Tứ Kỳ, Hải Dương',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2022_06_13_12_05_02_badccf2a4495cd91623f437e2c29fe8d.png','Best Express','2024-01-02 02:46:54.243000','2024-05-02 02:46:49.827000','2024-05-02 02:15:59.124000','2024-05-02 02:46:52.045000','Dịu Thanh','',3,_binary '','2024-05-02 02:16:10.000000',1,_binary '','0356124565','2024-05-02 02:46:54.243000','Nhanh',13600,14397630,1714586751986,1111111111111),(16,'Số 19, Đường ABC, Xã Đại Sơn, Huyện Tứ Kỳ, Hải Dương',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)','2024-05-02 02:46:31.165000','2024-05-02 02:46:25.933000','2024-05-02 02:17:07.053000','2024-05-02 02:46:29.033000','Dịu Thanh','',3,_binary '','2024-05-02 02:46:31.165000',0,_binary '','0356124565','2024-05-02 02:46:31.165000','Nhanh',24100,NULL,1714586751986,1111111111111),(17,'Số 19, Đường ABC, Xã Đại Sơn, Huyện Tứ Kỳ, Hải Dương',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_58_00_1c812f1a13f8f5441837990ee16e5fcf.jpg','Giao Hàng Tiết Kiệm','2024-02-02 02:47:25.059000','2024-05-02 02:47:12.191000','2024-05-02 02:18:02.632000','2024-05-02 02:47:14.333000','Dịu Thanh','',3,_binary '','2024-05-02 02:47:25.059000',0,_binary '','0356124565','2024-05-02 02:47:25.059000','Nhanh',37750,NULL,1714586751986,1111111111111),(18,'Xóm Dạ Sơn, Xã đô thành, Huyện Yên Thành, Nghệ An','2024-05-02 02:49:21.663000','Lý do cá nhân','http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)',NULL,NULL,'2024-05-02 02:19:22.872000',NULL,'Linh Thái','',4,_binary '',NULL,0,_binary '\0','0359446664',NULL,'Nhanh',30400,NULL,1714586781123,1111111111111),(19,'Xóm Dạ Sơn, Xã đô thành, Huyện Yên Thành, Nghệ An',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_58_00_1c812f1a13f8f5441837990ee16e5fcf.jpg','Giao Hàng Tiết Kiệm','2024-05-02 02:48:51.842000','2024-05-02 02:48:47.398000','2024-05-02 02:19:47.567000','2024-05-02 02:48:49.342000','Linh Thái','',3,_binary '','2024-05-02 02:48:51.842000',0,_binary '','0359446664','2024-05-02 02:48:51.842000','Nhanh',37750,NULL,1714586781123,1111111111111),(20,'Xóm Dạ Sơn, Xã đô thành, Huyện Yên Thành, Nghệ An',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)','2024-05-02 02:48:41.114000','2024-05-02 02:48:36.303000','2023-05-02 02:20:31.765000','2024-05-02 02:48:38.243000','Linh Thái','',3,_binary '','2024-05-02 02:48:41.114000',0,_binary '','0359446664','2024-05-02 02:48:41.114000','Nhanh',24100,NULL,1714586781123,1111111111111),(21,'Xóm Dạ Sơn, Xã đô thành, Huyện Yên Thành, Nghệ An','2024-05-02 02:49:00.429000','Thiếu hàng hoặc sản phẩm không đủ chất lượng','http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)',NULL,NULL,'2022-05-02 02:20:54.892000',NULL,'Linh Thái','',4,_binary '',NULL,0,_binary '\0','0359446664',NULL,'Nhanh',24100,NULL,1714586781123,1111111111111),(22,'Số 64, Đường Y, Xã vĩnh lộc a, Huyện Bình Chánh, Hồ Chí Minh',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_58_00_1c812f1a13f8f5441837990ee16e5fcf.jpg','Giao Hàng Tiết Kiệm','2024-04-02 02:48:30.690000','2024-05-02 02:48:23.368000','2023-05-02 02:31:47.251000','2024-05-02 02:48:27.010000','Bạn Hoa','',3,_binary '','2024-05-02 02:48:30.690000',0,_binary '','0386124586','2024-05-02 02:48:30.690000','Nhanh',47200,NULL,1714586933374,1111111111111),(23,'Số 64, Đường Y, Xã vĩnh lộc a, Huyện Bình Chánh, Hồ Chí Minh',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_58_00_1c812f1a13f8f5441837990ee16e5fcf.jpg','Giao Hàng Tiết Kiệm','2024-01-02 02:50:19.633000','2024-05-02 02:50:12.849000','2024-05-02 02:32:21.741000','2024-05-02 02:50:15.242000','Bạn Hoa','',3,_binary '','2024-05-02 02:50:19.633000',0,_binary '','0386124586','2024-05-02 02:50:19.633000','Nhanh',37750,NULL,1714586933374,1111111111111),(24,'Số 64, Đường Y, Xã vĩnh lộc a, Huyện Bình Chánh, Hồ Chí Minh',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_58_00_1c812f1a13f8f5441837990ee16e5fcf.jpg','Giao Hàng Tiết Kiệm','2024-03-02 02:52:01.877000','2024-05-02 02:51:58.321000','2024-05-02 02:33:21.230000','2024-05-02 02:52:00.234000','Bạn Hoa','',3,_binary '','2024-05-02 02:52:01.877000',0,_binary '','0386124586','2024-05-02 02:52:01.877000','Nhanh',37750,NULL,1714586933374,1111111111111),(25,'Số 64, Đường Y, Xã vĩnh lộc a, Huyện Bình Chánh, Hồ Chí Minh',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2022_06_13_12_05_02_badccf2a4495cd91623f437e2c29fe8d.png','Best Express','2024-05-02 02:49:37.211000','2024-05-02 02:49:32.332000','2024-05-02 02:33:35.818000','2024-05-02 02:49:34.632000','Bạn Hoa','',3,_binary '','2024-05-02 02:49:37.211000',0,_binary '','0386124586','2024-05-02 02:49:37.211000','Nhanh',13600,NULL,1714586933374,1111111111111),(26,'Số 14, Ngách 415/2, Ngõ 415, Thị trấn ba tri, Huyện Ba Tri, Bến Tre',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_58_00_1c812f1a13f8f5441837990ee16e5fcf.jpg','Giao Hàng Tiết Kiệm',NULL,'2024-05-02 02:50:47.153000','2024-05-02 02:35:43.083000',NULL,'Bạn Mạnh','',1,_binary '',NULL,0,_binary '\0','0359124586',NULL,'Nhanh',53500,NULL,1714589470989,1111111111111),(27,'Số 64, Đường Y, Xã cù bị, Huyện Châu Đức, Bà Rịa - Vũng Tàu',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_56_18_0f3ab6c35e79e48cdf10acdb4022f6c2.png','J&T Express','2024-05-02 02:50:39.469000','2024-05-02 02:50:34.916000','2024-05-02 02:41:43.539000','2024-05-02 02:50:37.028000','Bạn Mạnh','',3,_binary '','2024-05-02 02:50:39.469000',0,_binary '','0359799499','2024-05-02 02:50:39.469000','Nhanh',13600,NULL,1714589470989,1111111111111),(28,'Số 64, Đường Y, Xã cù bị, Huyện Châu Đức, Bà Rịa - Vũng Tàu',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2022_06_13_12_05_02_badccf2a4495cd91623f437e2c29fe8d.png','Best Express','2024-05-02 02:46:16.646000','2024-05-02 02:46:10.532000','2021-05-02 02:42:06.750000','2024-05-02 02:46:12.769000','Bạn Mạnh','',3,_binary '','2024-05-02 02:46:16.646000',0,_binary '','0359799499','2024-05-02 02:46:16.646000','Nhanh',13600,NULL,1714589470989,1111111111111),(29,'Số 64, Đường Y, Xã cù bị, Huyện Châu Đức, Bà Rịa - Vũng Tàu',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_58_00_1c812f1a13f8f5441837990ee16e5fcf.jpg','Giao Hàng Tiết Kiệm','2024-05-02 02:45:49.174000','2024-05-02 02:45:43.219000','2024-05-02 02:42:27.867000','2024-05-02 02:45:46.456000','Bạn Mạnh','',3,_binary '','2024-05-02 02:45:49.174000',0,_binary '','0359799499','2024-05-02 02:45:49.174000','Nhanh',37750,NULL,1714589470989,1111111111111),(30,'Số 64, Đường Y, Xã cù bị, Huyện Châu Đức, Bà Rịa - Vũng Tàu',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)','2024-05-02 02:46:03.955000','2024-05-02 02:45:59.995000','2022-05-02 02:42:44.583000','2024-05-02 02:46:01.776000','Bạn Mạnh','',3,_binary '','2024-05-02 02:46:03.955000',0,_binary '','0359799499','2024-05-02 02:46:03.955000','Nhanh',24100,NULL,1714589470989,1111111111111),(31,'Số 64, Đường Y, Xã cù bị, Huyện Châu Đức, Bà Rịa - Vũng Tàu',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_58_00_1c812f1a13f8f5441837990ee16e5fcf.jpg','Giao Hàng Tiết Kiệm','2024-05-02 09:44:12.084000','2024-05-02 09:44:04.018000','2022-05-02 02:43:16.158000','2024-05-02 09:44:07.706000','Bạn Mạnh','',3,_binary '','2024-05-02 09:44:12.084000',0,_binary '','0359799499','2024-05-02 09:44:12.084000','Nhanh',37750,NULL,1714589470989,1111111111111),(32,'Số 64, Đường Y, Xã cù bị, Huyện Châu Đức, Bà Rịa - Vũng Tàu',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)','2024-05-02 02:45:36.892000','2024-05-02 02:45:32.675000','2024-05-02 02:43:34.803000','2024-05-02 02:45:34.699000','Bạn Mạnh','',3,_binary '','2024-05-02 02:45:36.892000',0,_binary '','0359799499','2024-05-02 02:45:36.892000','Nhanh',24100,NULL,1714589470989,1111111111111),(33,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)','2024-05-03 23:53:45.402000','2024-05-03 23:53:40.715000','2024-05-02 17:32:26.426000','2024-05-03 23:53:43.044000','Manh Nguyen Van','',3,_binary '','2024-05-03 23:53:45.402000',0,_binary '','0359124586','2024-05-03 23:53:45.402000','Nhanh',24100,NULL,1714586716116,1111111111111),(34,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)',NULL,NULL,'2024-05-02 17:34:52.927000',NULL,'Manh Nguyen Van','',0,_binary '',NULL,0,_binary '\0','0359124586',NULL,'Nhanh',24100,NULL,1714586716116,NULL),(35,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)','2024-05-03 02:32:04.661000','2024-05-03 02:31:56.849000','2024-05-02 17:35:29.902000','2024-05-03 02:32:02.331000','Manh Nguyen Van','',3,_binary '','2024-05-03 02:32:04.661000',0,_binary '','0359124586','2024-05-03 02:32:04.661000','Nhanh',24100,NULL,1714586716116,1111111111111),(36,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)',NULL,NULL,'2024-05-02 17:35:43.204000',NULL,'Manh Nguyen Van','',0,_binary '',NULL,0,_binary '\0','0359124586',NULL,'Nhanh',24100,NULL,1714586716116,NULL),(37,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_56_18_0f3ab6c35e79e48cdf10acdb4022f6c2.png','J&T Express',NULL,NULL,'2024-05-02 17:49:29.640000',NULL,'Manh Nguyen Van','',0,_binary '',NULL,0,_binary '\0','0359124586',NULL,'Nhanh',13600,NULL,1714586716116,NULL),(38,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_56_18_0f3ab6c35e79e48cdf10acdb4022f6c2.png','J&T Express',NULL,NULL,'2024-05-02 17:49:43.942000',NULL,'Manh Nguyen Van','',0,_binary '',NULL,0,_binary '\0','0359124586',NULL,'Nhanh',13600,NULL,1714586716116,NULL),(39,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_56_18_0f3ab6c35e79e48cdf10acdb4022f6c2.png','J&T Express',NULL,NULL,'2024-05-02 17:58:52.438000',NULL,'Manh Nguyen Van','',0,_binary '',NULL,0,_binary '\0','0359124586',NULL,'Nhanh',13600,NULL,1714586716116,NULL),(40,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_56_18_0f3ab6c35e79e48cdf10acdb4022f6c2.png','J&T Express','2024-05-04 13:53:23.786000','2024-05-04 13:53:15.247000','2024-05-02 17:59:07.138000','2024-05-04 13:53:21.355000','Manh Nguyen Van','',3,_binary '','2024-05-04 13:53:23.786000',0,_binary '','0359124586','2024-05-04 13:53:23.786000','Nhanh',13600,NULL,1714586716116,1714561478492),(41,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_56_18_0f3ab6c35e79e48cdf10acdb4022f6c2.png','J&T Express',NULL,NULL,'2024-05-02 18:04:22.527000',NULL,'Manh Nguyen Van','',0,_binary '',NULL,0,_binary '\0','0359124586',NULL,'Nhanh',13600,NULL,1714586716116,NULL),(42,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_56_18_0f3ab6c35e79e48cdf10acdb4022f6c2.png','J&T Express',NULL,NULL,'2024-05-02 18:10:33.115000',NULL,'Manh Nguyen Van','',0,_binary '',NULL,0,_binary '\0','0359124586',NULL,'Nhanh',13600,NULL,1714586716116,NULL),(43,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)',NULL,NULL,'2024-05-02 18:12:24.497000',NULL,'Manh Nguyen Van','',0,_binary '',NULL,0,_binary '\0','0359124586',NULL,'Nhanh',24100,NULL,1714586716116,NULL),(44,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)',NULL,NULL,'2024-05-02 18:59:34.522000',NULL,'Manh Nguyen Van','',0,_binary '',NULL,0,_binary '\0','0359124586',NULL,'Nhanh',24100,NULL,1714586716116,NULL),(45,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_56_18_0f3ab6c35e79e48cdf10acdb4022f6c2.png','J&T Express',NULL,NULL,'2024-05-02 19:53:17.942000',NULL,'Manh Nguyen Van','',0,_binary '',NULL,0,_binary '\0','0359124586',NULL,'Nhanh',13600,NULL,1714586716116,NULL),(46,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)',NULL,NULL,'2024-05-02 19:55:42.127000',NULL,'Manh Nguyen Van','',0,_binary '',NULL,0,_binary '\0','0359124586',NULL,'Nhanh',24100,NULL,1714586716116,NULL),(47,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)',NULL,NULL,'2024-05-02 20:01:40.821000',NULL,'Manh Nguyen Van','',0,_binary '',NULL,0,_binary '\0','0359124586',NULL,'Nhanh',24100,NULL,1714586716116,NULL),(48,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)',NULL,NULL,'2024-05-02 20:13:23.303000',NULL,'Manh Nguyen Van','',0,_binary '',NULL,0,_binary '\0','0359124586',NULL,'Nhanh',24100,NULL,1714586716116,NULL),(49,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_56_18_0f3ab6c35e79e48cdf10acdb4022f6c2.png','J&T Express',NULL,NULL,'2024-05-02 20:18:30.207000',NULL,'Manh Nguyen Van','',0,_binary '',NULL,0,_binary '\0','0359124586',NULL,'Nhanh',13600,NULL,1714586716116,NULL),(50,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_56_18_0f3ab6c35e79e48cdf10acdb4022f6c2.png','J&T Express',NULL,NULL,'2024-05-02 20:18:33.160000',NULL,'Manh Nguyen Van','',0,_binary '',NULL,0,_binary '\0','0359124586',NULL,'Nhanh',13600,NULL,1714586716116,NULL),(51,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội','2024-05-03 22:29:09.401000','Thủ tục thanh toán quá rắc rối','http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)',NULL,NULL,'2024-05-02 20:20:36.919000',NULL,'Manh Nguyen Van','',4,_binary '',NULL,0,_binary '\0','0359124586',NULL,'Nhanh',24100,NULL,1714586716116,NULL),(52,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội','2024-05-03 22:23:04.686000','Muốn thay đổi địa chỉ giao hàng','http://sandbox.goship.io/storage/images/carriers/2022_06_13_12_05_02_badccf2a4495cd91623f437e2c29fe8d.png','Best Express',NULL,NULL,'2024-05-02 20:51:19.996000',NULL,'Manh Nguyen Van','',4,_binary '',NULL,0,_binary '\0','0359124586',NULL,'Nhanh',13600,NULL,1714586716116,NULL),(53,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)',NULL,'2024-05-03 02:33:11.511000','2024-05-02 21:04:04.595000','2024-05-03 02:33:14.069000','Manh Nguyen Van','',2,_binary '',NULL,0,_binary '\0','0359124586',NULL,'Nhanh',24100,NULL,1714586716116,1111111111111),(56,'Số 97, Đặng Văn Ngữ, Quận Đống Đa, Hà Nội',NULL,NULL,NULL,NULL,'2024-05-03 21:47:09.946000','2024-05-03 21:47:09.946000','2024-05-03 21:47:09.953000','2024-05-03 21:47:09.946000','Bạn Tiến',NULL,3,_binary '\0','2024-05-03 21:47:09.946000',0,_binary '','0311111111','2024-05-03 21:47:09.946000',NULL,0,NULL,7777777777777,1111111111111),(57,'Số 97, Đặng Văn Ngữ, Quận Đống Đa, Hà Nội',NULL,NULL,NULL,NULL,'2024-05-03 21:47:09.946000','2024-05-03 21:47:09.946000','2024-05-03 21:55:14.448000','2024-05-03 21:47:09.946000','Bạn Mạnh',NULL,3,_binary '\0','2024-05-03 21:47:09.946000',0,_binary '','0359124586',NULL,NULL,NULL,NULL,1714586716116,1111111111111),(58,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội','2024-05-03 22:28:44.172000','Muốn thay đổi sản phẩm trong đơn hàng (màu sắc, kích cỡ, số lượng,...)','http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_58_00_1c812f1a13f8f5441837990ee16e5fcf.jpg','Giao Hàng Tiết Kiệm',NULL,NULL,'2024-05-03 22:25:13.206000',NULL,'Manh Nguyen Van','',4,_binary '','2024-05-03 22:25:23.000000',1,_binary '','0359124586',NULL,'Nhanh',37750,14401070,1714586716116,NULL),(59,'Số 97, Đặng Văn Ngữ, Quận Đống Đa, Hà Nội',NULL,NULL,NULL,NULL,'2024-05-03 23:04:09.790000','2024-05-03 23:04:09.790000','2024-05-03 23:04:09.888000','2024-05-03 23:04:09.790000','Bạn Tiến',NULL,3,_binary '\0','2024-05-03 23:04:09.790000',0,_binary '','0333333333',NULL,NULL,0,NULL,7777777777777,1111111111111),(60,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_56_18_0f3ab6c35e79e48cdf10acdb4022f6c2.png','J&T Express',NULL,NULL,'2024-05-03 23:13:31.462000',NULL,'Manh Nguyen Van','',0,_binary '',NULL,0,_binary '\0','0359124586',NULL,'Nhanh',13600,NULL,1714586716116,NULL),(61,'Số 97, Đặng Văn Ngữ, Quận Đống Đa, Hà Nội',NULL,NULL,NULL,NULL,'2024-05-03 23:14:41.452000','2024-05-03 23:14:41.452000','2024-05-03 23:14:41.452000','2024-05-03 23:14:41.452000','Bạn Tiến',NULL,3,_binary '\0','2024-05-03 23:14:41.452000',0,_binary '','0333333333',NULL,NULL,0,NULL,7777777777777,1111111111111),(62,'Số 97, Đặng Văn Ngữ, Quận Đống Đa, Hà Nội',NULL,NULL,NULL,NULL,'2024-05-03 23:17:37.402000','2024-05-03 23:17:37.402000','2024-05-03 23:17:37.413000','2024-05-03 23:17:37.402000','Bạn Linh',NULL,3,_binary '\0','2024-05-03 23:17:37.402000',0,_binary '','0388888888',NULL,NULL,0,NULL,7777777777777,1111111111111),(63,'Số 97, Đặng Văn Ngữ, Quận Đống Đa, Hà Nội',NULL,NULL,NULL,NULL,'2024-05-03 23:20:05.537000','2024-05-03 23:20:05.537000','2024-05-03 23:20:05.545000','2024-05-03 23:20:05.537000','Bạn Mai Hi',NULL,3,_binary '\0','2024-05-03 23:20:05.537000',0,_binary '','0399999999',NULL,NULL,0,NULL,7777777777777,1111111111111),(64,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2022_06_13_12_05_02_badccf2a4495cd91623f437e2c29fe8d.png','Best Express',NULL,NULL,'2024-05-03 23:48:59.946000',NULL,'Manh Nguyen Van','',0,_binary '',NULL,0,_binary '\0','0359124586',NULL,'Nhanh',13600,NULL,1714586716116,NULL),(65,'Số 14, Ngách 415/2, Ngõ 415, Xã phù đổng, Huyện Gia Lâm, Hà Nội','2024-05-04 02:21:23.842000','Muốn thay đổi sản phẩm trong đơn hàng (màu sắc, kích cỡ, số lượng,...)','http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_58_00_1c812f1a13f8f5441837990ee16e5fcf.jpg','Giao Hàng Tiết Kiệm',NULL,NULL,'2024-05-04 02:03:16.516000',NULL,'Manh Nguyen Van','',4,_binary '','2024-05-04 02:03:58.000000',1,_binary '','0359124586',NULL,'Nhanh',37750,14401239,1714586716116,NULL),(66,'Số 64, Đường Y, Xã cù bị, Huyện Châu Đức, Bà Rịa - Vũng Tàu',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_57_30_79b2958823cce2533ecbf3bb780a7b20.jpg','Giao Hàng Nhanh (v3)','2024-05-04 13:52:35.133000','2024-05-04 13:52:25.309000','2024-05-04 03:53:15.626000','2024-05-04 13:52:32.348000','Bạn Mạnh','',3,_binary '','2024-05-04 13:52:35.133000',0,_binary '','0359799499','2024-05-04 13:52:35.133000','Nhanh',24100,NULL,1714589470989,1111111111111),(67,'Số 64, Đường Y, Xã tắc vân, Thành phố Cà Mau, Cà Mau',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_58_00_1c812f1a13f8f5441837990ee16e5fcf.jpg','Giao Hàng Tiết Kiệm',NULL,NULL,'2024-05-04 13:50:56.545000',NULL,'Bạn Mạnh','',0,_binary '','2024-05-04 13:51:08.000000',1,_binary '','0359799499',NULL,'Nhanh',37750,14401688,1714589470989,NULL),(68,'Số 64, Đường Y, Xã cù bị, Huyện Châu Đức, Bà Rịa - Vũng Tàu',NULL,NULL,'http://sandbox.goship.io/storage/images/carriers/2023_11_21_13_56_18_0f3ab6c35e79e48cdf10acdb4022f6c2.png','J&T Express','2024-05-04 17:14:52.537000','2024-05-04 17:14:25.980000','2024-05-04 17:12:51.929000','2024-05-04 17:14:49.380000','Bạn Mạnh','',3,_binary '','2024-05-04 17:13:11.000000',1,_binary '','0359799499','2024-05-04 17:14:52.537000','Nhanh',13600,14402087,1714589470989,1714561478492);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_colors`
--

DROP TABLE IF EXISTS `product_colors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_colors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `color` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqhu7cqni31911lmvx4fqmiw65` (`product_id`),
  CONSTRAINT `FKqhu7cqni31911lmvx4fqmiw65` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_colors`
--

LOCK TABLES `product_colors` WRITE;
/*!40000 ALTER TABLE `product_colors` DISABLE KEYS */;
INSERT INTO `product_colors` VALUES (1,'Đen',1),(2,'Cam',2),(3,'Xanh dương',2),(4,'Đen',3),(5,'Xanh da trời',3),(6,'Kem',4),(7,'Đen',4),(8,'Hồng',5),(9,'Trắng',5),(10,'Kem xám',6),(11,'Đen xám',6),(12,'Đỏ',7),(13,'Xanh dương',7),(14,'Xanh lam',8),(15,'Hồng',8),(16,'Hồng',9),(17,'Vàng',9),(18,'Đen',10),(19,'Kem',10),(20,'Xanh lam',10),(21,'Trắng',11),(22,'Xanh dương',11),(23,'Xám',12),(24,'Rêu',12),(25,'Trắng',13),(26,'Đen',13),(27,'Nâu',14),(28,'Đen',14),(29,'Cam',15),(30,'Đỏ',15),(31,'Xanh lá mạ',15),(32,'Cam',16),(33,'Xanh dương',16),(34,'Cam',17),(35,'Xanh dương',17),(36,'Đen',17),(37,'Rêu',18),(38,'Đỏ',18),(39,'Tím',19),(40,'Trắng cam',19),(41,'Nâu',20),(42,'Đen',20),(43,'Đỏ',21),(44,'Đỏ',22),(45,'Đen',23),(46,'Trắng',23),(47,'Kem',24),(48,'Xanh lam',24),(49,'Kem',25),(50,'Đen',25),(51,'Xanh lá mạ',26),(52,'Đỏ',26),(53,'Đen',27),(54,'Trắng',27),(55,'Đen',28),(56,'Hồng',28),(57,'Kem',28),(58,'Kem',29),(59,'Đen',29),(60,'Trắng xám',30),(61,'Tím',30),(62,'Đen',31),(63,'Đen',32),(64,'Vàng',33),(65,'Trắng',34),(66,'Đen',35);
/*!40000 ALTER TABLE `product_colors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_details`
--

DROP TABLE IF EXISTS `product_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` int DEFAULT NULL,
  `size` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `product_color_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK438v073yw6qfr5nmpblo5ee7w` (`product_color_id`),
  CONSTRAINT `FK438v073yw6qfr5nmpblo5ee7w` FOREIGN KEY (`product_color_id`) REFERENCES `product_colors` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=727 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_details`
--

LOCK TABLES `product_details` WRITE;
/*!40000 ALTER TABLE `product_details` DISABLE KEYS */;
INSERT INTO `product_details` VALUES (1,0,'34',1),(2,0,'35',1),(3,0,'36',1),(4,0,'37',1),(5,0,'38',1),(6,0,'39',1),(7,0,'40',1),(8,1,'41',1),(9,2,'42',1),(10,18,'43',1),(11,0,'44',1),(12,5,'34',2),(13,8,'35',2),(14,10,'36',2),(15,0,'37',2),(16,0,'38',2),(17,0,'39',2),(18,0,'40',2),(19,0,'41',2),(20,0,'42',2),(21,0,'43',2),(22,0,'44',2),(23,5,'34',3),(24,10,'35',3),(25,8,'36',3),(26,25,'37',3),(27,0,'38',3),(28,0,'39',3),(29,0,'40',3),(30,0,'41',3),(31,0,'42',3),(32,0,'43',3),(33,0,'44',3),(34,0,'34',4),(35,0,'35',4),(36,5,'36',4),(37,5,'37',4),(38,3,'38',4),(39,0,'39',4),(40,0,'40',4),(41,0,'41',4),(42,0,'42',4),(43,0,'43',4),(44,0,'44',4),(45,0,'34',5),(46,0,'35',5),(47,0,'36',5),(48,3,'37',5),(49,0,'38',5),(50,2,'39',5),(51,5,'40',5),(52,0,'41',5),(53,0,'42',5),(54,0,'43',5),(55,0,'44',5),(56,0,'34',6),(57,0,'35',6),(58,5,'36',6),(59,0,'37',6),(60,3,'38',6),(61,7,'39',6),(62,0,'40',6),(63,0,'41',6),(64,0,'42',6),(65,0,'43',6),(66,0,'44',6),(67,0,'34',7),(68,0,'35',7),(69,5,'36',7),(70,0,'37',7),(71,3,'38',7),(72,0,'39',7),(73,0,'40',7),(74,0,'41',7),(75,0,'42',7),(76,0,'43',7),(77,0,'44',7),(78,3,'34',8),(79,0,'35',8),(80,2,'36',8),(81,5,'37',8),(82,0,'38',8),(83,0,'39',8),(84,0,'40',8),(85,0,'41',8),(86,0,'42',8),(87,0,'43',8),(88,0,'44',8),(89,0,'34',9),(90,0,'35',9),(91,0,'36',9),(92,0,'37',9),(93,0,'38',9),(94,0,'39',9),(95,0,'40',9),(96,0,'41',9),(97,0,'42',9),(98,0,'43',9),(99,0,'44',9),(100,0,'34',10),(101,0,'35',10),(102,0,'36',10),(103,0,'37',10),(104,0,'38',10),(105,0,'39',10),(106,0,'40',10),(107,0,'41',10),(108,0,'42',10),(109,0,'43',10),(110,0,'44',10),(111,0,'34',11),(112,0,'35',11),(113,0,'36',11),(114,0,'37',11),(115,0,'38',11),(116,0,'39',11),(117,0,'40',11),(118,0,'41',11),(119,0,'42',11),(120,0,'43',11),(121,0,'44',11),(122,25,'34',12),(123,2,'35',12),(124,10,'36',12),(125,0,'37',12),(126,25,'38',12),(127,0,'39',12),(128,0,'40',12),(129,0,'41',12),(130,0,'42',12),(131,0,'43',12),(132,0,'44',12),(133,6,'34',13),(134,30,'35',13),(135,9,'36',13),(136,7,'37',13),(137,0,'38',13),(138,0,'39',13),(139,0,'40',13),(140,0,'41',13),(141,0,'42',13),(142,0,'43',13),(143,0,'44',13),(144,5,'34',14),(145,3,'35',14),(146,22,'36',14),(147,0,'37',14),(148,20,'38',14),(149,0,'39',14),(150,0,'40',14),(151,0,'41',14),(152,0,'42',14),(153,0,'43',14),(154,0,'44',14),(155,5,'34',15),(156,0,'35',15),(157,0,'36',15),(158,2,'37',15),(159,2,'38',15),(160,0,'39',15),(161,0,'40',15),(162,0,'41',15),(163,0,'42',15),(164,0,'43',15),(165,0,'44',15),(166,5,'34',16),(167,2,'35',16),(168,2,'36',16),(169,0,'37',16),(170,0,'38',16),(171,0,'39',16),(172,0,'40',16),(173,0,'41',16),(174,0,'42',16),(175,0,'43',16),(176,0,'44',16),(177,2,'34',17),(178,0,'35',17),(179,0,'36',17),(180,0,'37',17),(181,22,'38',17),(182,5,'39',17),(183,0,'40',17),(184,0,'41',17),(185,0,'42',17),(186,0,'43',17),(187,0,'44',17),(188,20,'34',18),(189,0,'35',18),(190,5,'36',18),(191,10,'37',18),(192,36,'38',18),(193,40,'39',18),(194,0,'40',18),(195,0,'41',18),(196,6,'42',18),(197,40,'43',18),(198,0,'44',18),(199,26,'34',19),(200,0,'35',19),(201,0,'36',19),(202,0,'37',19),(203,7,'38',19),(204,0,'39',19),(205,0,'40',19),(206,5,'41',19),(207,0,'42',19),(208,5,'43',19),(209,0,'44',19),(210,20,'34',20),(211,0,'35',20),(212,5,'36',20),(213,0,'37',20),(214,0,'38',20),(215,0,'39',20),(216,0,'40',20),(217,0,'41',20),(218,5,'42',20),(219,0,'43',20),(220,20,'44',20),(221,4,'34',21),(222,3,'35',21),(223,23,'36',21),(224,5,'37',21),(225,5,'38',21),(226,6,'39',21),(227,0,'40',21),(228,0,'41',21),(229,0,'42',21),(230,0,'43',21),(231,0,'44',21),(232,5,'34',22),(233,0,'35',22),(234,0,'36',22),(235,0,'37',22),(236,3,'38',22),(237,5,'39',22),(238,0,'40',22),(239,0,'41',22),(240,0,'42',22),(241,0,'43',22),(242,0,'44',22),(243,65,'34',23),(244,0,'35',23),(245,0,'36',23),(246,7,'37',23),(247,5,'38',23),(248,10,'39',23),(249,0,'40',23),(250,0,'41',23),(251,13,'42',23),(252,0,'43',23),(253,8,'44',23),(254,30,'34',24),(255,0,'35',24),(256,10,'36',24),(257,0,'37',24),(258,0,'38',24),(259,9,'39',24),(260,8,'40',24),(261,0,'41',24),(262,0,'42',24),(263,7,'43',24),(264,0,'44',24),(265,0,'34',25),(266,0,'35',25),(267,10,'36',25),(268,5,'37',25),(269,0,'38',25),(270,15,'39',25),(271,5,'40',25),(272,0,'41',25),(273,0,'42',25),(274,0,'43',25),(275,0,'44',25),(276,0,'34',26),(277,0,'35',26),(278,0,'36',26),(279,0,'37',26),(280,10,'38',26),(281,15,'39',26),(282,0,'40',26),(283,0,'41',26),(284,0,'42',26),(285,0,'43',26),(286,0,'44',26),(287,0,'34',27),(288,0,'35',27),(289,0,'36',27),(290,0,'37',27),(291,0,'38',27),(292,3,'39',27),(293,0,'40',27),(294,0,'41',27),(295,0,'42',27),(296,3,'43',27),(297,0,'44',27),(298,0,'34',28),(299,0,'35',28),(300,0,'36',28),(301,0,'37',28),(302,0,'38',28),(303,0,'39',28),(304,0,'40',28),(305,0,'41',28),(306,3,'42',28),(307,0,'43',28),(308,3,'44',28),(309,0,'34',29),(310,0,'35',29),(311,0,'36',29),(312,0,'37',29),(313,5,'38',29),(314,5,'39',29),(315,0,'40',29),(316,0,'41',29),(317,0,'42',29),(318,5,'43',29),(319,0,'44',29),(320,0,'34',30),(321,0,'35',30),(322,0,'36',30),(323,0,'37',30),(324,0,'38',30),(325,0,'39',30),(326,0,'40',30),(327,20,'41',30),(328,0,'42',30),(329,0,'43',30),(330,0,'44',30),(331,0,'34',31),(332,0,'35',31),(333,0,'36',31),(334,0,'37',31),(335,15,'38',31),(336,9,'39',31),(337,0,'40',31),(338,5,'41',31),(339,0,'42',31),(340,0,'43',31),(341,0,'44',31),(342,5,'34',32),(343,9,'35',32),(344,10,'36',32),(345,0,'37',32),(346,0,'38',32),(347,0,'39',32),(348,0,'40',32),(349,0,'41',32),(350,0,'42',32),(351,0,'43',32),(352,0,'44',32),(353,5,'34',33),(354,9,'35',33),(355,5,'36',33),(356,0,'37',33),(357,20,'38',33),(358,0,'39',33),(359,0,'40',33),(360,0,'41',33),(361,0,'42',33),(362,0,'43',33),(363,0,'44',33),(364,23,'34',34),(365,0,'35',34),(366,6,'36',34),(367,0,'37',34),(368,0,'38',34),(369,7,'39',34),(370,0,'40',34),(371,0,'41',34),(372,8,'42',34),(373,10,'43',34),(374,0,'44',34),(375,1,'34',35),(376,10,'35',35),(377,0,'36',35),(378,0,'37',35),(379,12,'38',35),(380,0,'39',35),(381,0,'40',35),(382,9,'41',35),(383,0,'42',35),(384,5,'43',35),(385,0,'44',35),(386,0,'34',36),(387,30,'35',36),(388,0,'36',36),(389,10,'37',36),(390,0,'38',36),(391,4,'39',36),(392,8,'40',36),(393,0,'41',36),(394,0,'42',36),(395,0,'43',36),(396,0,'44',36),(397,0,'34',37),(398,0,'35',37),(399,0,'36',37),(400,0,'37',37),(401,8,'38',37),(402,10,'39',37),(403,10,'40',37),(404,9,'41',37),(405,0,'42',37),(406,16,'43',37),(407,0,'44',37),(408,0,'34',38),(409,0,'35',38),(410,0,'36',38),(411,0,'37',38),(412,0,'38',38),(413,0,'39',38),(414,0,'40',38),(415,0,'41',38),(416,9,'42',38),(417,9,'43',38),(418,0,'44',38),(419,0,'34',39),(420,0,'35',39),(421,8,'36',39),(422,10,'37',39),(423,5,'38',39),(424,6,'39',39),(425,6,'40',39),(426,0,'41',39),(427,0,'42',39),(428,0,'43',39),(429,0,'44',39),(430,0,'34',40),(431,0,'35',40),(432,7,'36',40),(433,7,'37',40),(434,9,'38',40),(435,0,'39',40),(436,0,'40',40),(437,0,'41',40),(438,0,'42',40),(439,0,'43',40),(440,0,'44',40),(441,15,'34',41),(442,0,'35',41),(443,0,'36',41),(444,20,'37',41),(445,0,'38',41),(446,55,'39',41),(447,0,'40',41),(448,5,'41',41),(449,20,'42',41),(450,5,'43',41),(451,0,'44',41),(452,0,'34',42),(453,0,'35',42),(454,0,'36',42),(455,0,'37',42),(456,17,'38',42),(457,8,'39',42),(458,0,'40',42),(459,28,'41',42),(460,0,'42',42),(461,20,'43',42),(462,0,'44',42),(463,0,'34',43),(464,0,'35',43),(465,0,'36',43),(466,2,'37',43),(467,0,'38',43),(468,3,'39',43),(469,1,'40',43),(470,0,'41',43),(471,0,'42',43),(472,0,'43',43),(473,5,'44',43),(474,0,'34',44),(475,0,'35',44),(476,0,'36',44),(477,0,'37',44),(478,5,'38',44),(479,18,'39',44),(480,0,'40',44),(481,15,'41',44),(482,5,'42',44),(483,10,'43',44),(484,35,'44',44),(485,0,'34',45),(486,0,'35',45),(487,0,'36',45),(488,3,'37',45),(489,0,'38',45),(490,0,'39',45),(491,10,'40',45),(492,0,'41',45),(493,5,'42',45),(494,0,'43',45),(495,0,'44',45),(496,0,'34',46),(497,0,'35',46),(498,0,'36',46),(499,0,'37',46),(500,0,'38',46),(501,0,'39',46),(502,0,'40',46),(503,0,'41',46),(504,0,'42',46),(505,0,'43',46),(506,0,'44',46),(507,0,'34',47),(508,0,'35',47),(509,5,'36',47),(510,5,'37',47),(511,10,'38',47),(512,10,'39',47),(513,0,'40',47),(514,0,'41',47),(515,0,'42',47),(516,0,'43',47),(517,0,'44',47),(518,0,'34',48),(519,0,'35',48),(520,5,'36',48),(521,0,'37',48),(522,0,'38',48),(523,6,'39',48),(524,5,'40',48),(525,0,'41',48),(526,0,'42',48),(527,0,'43',48),(528,0,'44',48),(529,0,'34',49),(530,0,'35',49),(531,3,'36',49),(532,8,'37',49),(533,5,'38',49),(534,0,'39',49),(535,0,'40',49),(536,20,'41',49),(537,0,'42',49),(538,33,'43',49),(539,0,'44',49),(540,20,'34',50),(541,0,'35',50),(542,8,'36',50),(543,20,'37',50),(544,8,'38',50),(545,0,'39',50),(546,20,'40',50),(547,20,'41',50),(548,0,'42',50),(549,0,'43',50),(550,0,'44',50),(551,10,'34',51),(552,0,'35',51),(553,22,'36',51),(554,0,'37',51),(555,28,'38',51),(556,0,'39',51),(557,7,'40',51),(558,0,'41',51),(559,6,'42',51),(560,0,'43',51),(561,0,'44',51),(562,0,'34',52),(563,5,'35',52),(564,0,'36',52),(565,4,'37',52),(566,0,'38',52),(567,5,'39',52),(568,0,'40',52),(569,0,'41',52),(570,0,'42',52),(571,9,'43',52),(572,11,'44',52),(573,20,'34',53),(574,7,'35',53),(575,0,'36',53),(576,2,'37',53),(577,0,'38',53),(578,0,'39',53),(579,0,'40',53),(580,0,'41',53),(581,6,'42',53),(582,0,'43',53),(583,6,'44',53),(584,0,'34',54),(585,0,'35',54),(586,0,'36',54),(587,0,'37',54),(588,0,'38',54),(589,0,'39',54),(590,0,'40',54),(591,0,'41',54),(592,0,'42',54),(593,0,'43',54),(594,0,'44',54),(595,10,'34',55),(596,11,'35',55),(597,0,'36',55),(598,0,'37',55),(599,7,'38',55),(600,0,'39',55),(601,0,'40',55),(602,0,'41',55),(603,7,'42',55),(604,0,'43',55),(605,0,'44',55),(606,0,'34',56),(607,4,'35',56),(608,8,'36',56),(609,5,'37',56),(610,13,'38',56),(611,10,'39',56),(612,0,'40',56),(613,0,'41',56),(614,0,'42',56),(615,0,'43',56),(616,0,'44',56),(617,0,'34',57),(618,0,'35',57),(619,0,'36',57),(620,6,'37',57),(621,0,'38',57),(622,26,'39',57),(623,0,'40',57),(624,9,'41',57),(625,15,'42',57),(626,0,'43',57),(627,11,'44',57),(628,0,'34',58),(629,0,'35',58),(630,0,'36',58),(631,10,'37',58),(632,25,'38',58),(633,0,'39',58),(634,0,'40',58),(635,0,'41',58),(636,20,'42',58),(637,0,'43',58),(638,0,'44',58),(639,0,'34',59),(640,0,'35',59),(641,0,'36',59),(642,0,'37',59),(643,20,'38',59),(644,15,'39',59),(645,20,'40',59),(646,0,'41',59),(647,0,'42',59),(648,0,'43',59),(649,0,'44',59),(650,0,'34',60),(651,0,'35',60),(652,0,'36',60),(653,0,'37',60),(654,2,'38',60),(655,5,'39',60),(656,25,'40',60),(657,7,'41',60),(658,0,'42',60),(659,0,'43',60),(660,0,'44',60),(661,0,'34',61),(662,0,'35',61),(663,0,'36',61),(664,0,'37',61),(665,1,'38',61),(666,8,'39',61),(667,7,'40',61),(668,0,'41',61),(669,0,'42',61),(670,0,'43',61),(671,0,'44',61),(672,0,'34',62),(673,0,'35',62),(674,0,'36',62),(675,0,'37',62),(676,5,'38',62),(677,6,'39',62),(678,5,'40',62),(679,5,'41',62),(680,8,'42',62),(681,0,'43',62),(682,25,'44',62),(683,0,'34',63),(684,0,'35',63),(685,0,'36',63),(686,0,'37',63),(687,0,'38',63),(688,0,'39',63),(689,0,'40',63),(690,0,'41',63),(691,12,'42',63),(692,0,'43',63),(693,0,'44',63),(694,70,'34',64),(695,21,'35',64),(696,10,'36',64),(697,40,'37',64),(698,0,'38',64),(699,20,'39',64),(700,0,'40',64),(701,0,'41',64),(702,0,'42',64),(703,0,'43',64),(704,0,'44',64),(705,3,'34',65),(706,5,'35',65),(707,2,'36',65),(708,5,'37',65),(709,5,'38',65),(710,1,'39',65),(711,0,'40',65),(712,0,'41',65),(713,0,'42',65),(714,0,'43',65),(715,0,'44',65),(716,0,'34',66),(717,0,'35',66),(718,0,'36',66),(719,0,'37',66),(720,29,'38',66),(721,5,'39',66),(722,25,'40',66),(723,10,'41',66),(724,15,'42',66),(725,20,'43',66),(726,0,'44',66);
/*!40000 ALTER TABLE `product_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_image`
--

DROP TABLE IF EXISTS `product_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_image` (
  `product_id` bigint NOT NULL,
  `image` longtext COLLATE utf8mb4_vietnamese_ci,
  KEY `FK1n91c4vdhw8pa4frngs4qbbvs` (`product_id`),
  CONSTRAINT `FK1n91c4vdhw8pa4frngs4qbbvs` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_image`
--

LOCK TABLES `product_image` WRITE;
/*!40000 ALTER TABLE `product_image` DISABLE KEYS */;
INSERT INTO `product_image` VALUES (1,'b670d9d4-e854-4a58-8780-52abde91e729.webp'),(1,'a88dbd2a-f27d-42b6-a890-721409eebec2.webp'),(1,'a174d75a-bea3-485d-afa1-229b78688c25.webp'),(1,'ef52c0f7-6770-479e-955a-b702e9594ab3.webp'),(1,'27184e45-9381-437b-857d-0fa43905ad12.webp'),(2,'4e50c419-1992-4856-8767-26482b6a5921.webp'),(2,'bdc26ea7-7d97-4e83-b0a6-3e14dcca308b.webp'),(2,'680c7956-b06b-4fb0-8b6f-166536483ba2.webp'),(2,'43dccfd2-32a9-4588-9018-6a0c27c7e53f.webp'),(2,'e34b734d-1ef9-4579-8beb-3b4528ea1f30.webp'),(2,'eeeb138c-d206-4a7b-942f-2a1793b8c9f3.webp'),(2,'f8d5e356-3f82-4622-976e-17d6b079bc58.webp'),(3,'2bfd9273-dba9-48b9-b3d6-3602f946447e.webp'),(3,'9a44d0d6-6763-4759-b9e1-fb05a692aabd.webp'),(3,'ed6327fd-216d-4f66-9459-e051e0d209d1.webp'),(3,'75c172ec-e3ee-4def-939e-42107866b034.webp'),(3,'2442008a-313c-4639-8aca-052de56baeaf.webp'),(3,'546b0d2b-6b68-4a3f-a637-7520f7cb77da.webp'),(3,'cb2252e4-cedb-4e9f-9713-56e7cb2a3736.webp'),(3,'0a694549-f151-4874-b985-6b53ca52f0f2.webp'),(3,'ac016584-0287-4c2c-a35c-16c1ea76708e.webp'),(3,'896bdae0-6b77-4979-a2a7-f48948a4cdfc.webp'),(3,'a49beb45-104c-43fb-bf7e-54cf786861fd.webp'),(3,'172d2702-c68c-4be9-a2bb-56b93738f35c.webp'),(3,'e40e3313-6473-440a-b23f-ec1866a0ee3e.jpg'),(3,'0ade741f-ef88-4001-b600-43105cf3ecc9.webp'),(4,'2dc1c3f7-147f-4780-b079-4387478214f8.webp'),(4,'27f11f14-350d-4190-b286-8b7a9a29856f.webp'),(4,'739bb03e-153c-4629-9fa5-07c126e11149.webp'),(4,'bcd521fd-1aea-4fb5-aa3a-9f578569b6a6.webp'),(4,'c4f8f3b1-e816-4afd-8abe-c49215b481c0.webp'),(5,'442dfbc9-e782-481a-84e8-c03aaeebe14d.webp'),(5,'87e1e6e1-638f-4df5-8636-bc041db511ea.webp'),(5,'694cbfa8-eaf6-4a6c-856a-f99028c50540.webp'),(5,'58b37d57-e904-437e-b22e-d36b9eef63d5.webp'),(5,'27bf7486-73c1-4810-9c2f-429fcee7d9e3.webp'),(5,'a0718a69-3c01-4ed3-8606-4b0d6252a345.webp'),(5,'9aca75d8-5307-428d-8bc0-6228068661a4.webp'),(6,'73ec15d7-e3a5-4f8c-97cd-a81a61a32124.webp'),(6,'d2fe3284-2f8a-46f0-abc4-facc7e9183e0.webp'),(6,'3a8b1a07-b9e7-4f59-a653-1304b9cfda05.webp'),(6,'fa9b7225-69b9-4536-b35e-5e2194708677.webp'),(6,'fe3dd790-4f90-4c98-afff-c2eeba2e9d6c.webp'),(6,'bea5d78c-b41d-4e70-84c1-e97951798550.webp'),(6,'cf9a4710-ded0-4fe7-8bc0-4690ed4d42a1.webp'),(7,'3ce214ca-af04-43e0-b1f0-f6e352337f71.jpg'),(7,'69d1aed8-f075-4093-9578-aaafe6e3af82.webp'),(7,'3b7e1387-0a86-4867-87a4-418d82e932e1.webp'),(7,'b9ea151a-a7fd-40cc-8ae4-3aa1e9ec5e2f.webp'),(7,'b6beca48-9bee-4e94-8fb7-1f67552078de.jpg'),(7,'bdd0ca30-e905-4f6f-8db8-e7a6f4497bc0.webp'),(8,'dd33e883-e3da-4821-98d4-931dccd4f571.webp'),(8,'71e3f103-0368-4e2e-a179-85ac52f9ee6e.webp'),(8,'88af267d-4760-4c6d-9c06-d3268fcd8785.webp'),(8,'eb83566a-4ef6-4efb-b53a-841b62183ec7.webp'),(8,'9349c97c-bb36-41f1-9445-7ddfb1354472.webp'),(8,'faf1e67a-b37d-46d0-b189-c2c20124168b.webp'),(9,'45365f21-4c80-4aca-8eae-88720106853e.webp'),(9,'868293eb-e650-42f2-8e3c-095c154ae617.webp'),(9,'3f8a7ba4-bfc0-420c-b4c1-01305575689b.webp'),(9,'7316d026-a7de-4840-84f8-d4cfd736f5eb.webp'),(9,'fae896cc-a8b7-4f75-8df4-cf6aca6e2078.webp'),(10,'ec6c8397-7dbf-4b70-aedd-dedfb2b0547f.webp'),(10,'ce2b0dd6-acc3-4d43-98ff-7a7571c657a7.webp'),(10,'4f5ef103-8b83-4dab-80f2-736d8efe301b.jpg'),(10,'1a37ef85-b65d-438a-aed7-1b76f00a7189.jpg'),(10,'7157c081-d20d-4d6f-90a4-eb387c7c413b.jpg'),(10,'12abd828-98fb-48ab-bb64-7b976be90b8e.webp'),(10,'b8c3184b-1552-42ea-8b05-c384498a18e2.webp'),(10,'db1af30a-1247-4c84-82c9-917a4bce3d4f.jpg'),(11,'61270b58-bcf1-429d-9320-5de90bc9db81.webp'),(11,'f6f760ed-d022-484b-8115-57d95334f41d.webp'),(11,'67665bd8-aa3b-4b3d-b384-2ef685d3072c.webp'),(11,'36cce1dc-98d4-46c7-be37-b26a9f27dcc2.webp'),(11,'54cd7bc3-9386-4604-8d0a-fc82111769bf.webp'),(11,'35a34b94-6cfc-4af8-b95c-da679695eff5.jpg'),(12,'6d9028f0-908b-447d-aa33-2a666d9298a6.webp'),(12,'5ab36d6c-f3a6-4e0c-a913-853e67d09bf6.webp'),(12,'2d7dc0b0-f444-4fee-8de3-4a3b3512493b.webp'),(12,'fc0302fb-9783-4e07-84e2-c21d9fdaa636.webp'),(12,'00d449e6-8395-4fd5-ae12-f391d7137603.webp'),(12,'98e1843d-4188-4e25-9ace-34c5fa794c3e.webp'),(12,'8656182f-b8f9-4830-9245-81c1ab4d9a37.webp'),(12,'0c6c9848-4282-4770-bcb6-c4ecbd19996d.webp'),(13,'5f66e36c-8d2b-420b-a607-f3c35d99b27b.webp'),(13,'e136d15c-48b6-464e-bde2-127786c3b7f4.webp'),(13,'054a010d-9ff8-4b21-a4a9-cf0ae162cffd.webp'),(13,'dfbd30c7-214c-4594-a981-7ae86154d336.webp'),(13,'41199ac6-98a6-4c27-91c9-4b51de09749a.webp'),(13,'67297193-d329-4a39-a4b8-84bd32aa0632.webp'),(13,'e1184029-46fc-4dd7-8c25-7d1b0d8690b1.webp'),(14,'1582fa0c-67d9-4cae-95e8-1608bab054dd.webp'),(14,'dea4b7b5-73b8-4d33-899c-7091de5166ec.webp'),(14,'9d3046ac-63a2-4165-bc9d-d07341284960.webp'),(14,'a9b87c66-0322-4d42-8c81-d49426af60f6.webp'),(14,'ce11d6ba-0f42-41e1-a10d-5967db68e3f3.webp'),(14,'bd8946f9-47df-469b-947f-5edd6e4bcda9.webp'),(15,'6a72b4c9-c17c-45e5-9c98-9524c00517d0.webp'),(15,'f6e52f77-b5c4-47fd-bac0-7fbb43307650.webp'),(15,'a75c14c6-ab9a-4789-b359-953c71fcd675.webp'),(15,'d255e940-39d7-44d5-87bd-55de26e1d058.webp'),(15,'0757f89a-8f1c-485d-ba10-90d79cd82ce8.webp'),(15,'e1b4310e-1eda-4589-9255-fe9e452f0206.webp'),(15,'54b36e1c-0474-434b-b4f9-09cf76014b94.webp'),(15,'9a8c3421-1cde-4392-b3bf-38cca1a136a7.webp'),(15,'e5a30cf3-7c13-4254-adb8-ef8ccfe3a991.webp'),(15,'3dba6db5-34e9-44c9-9b43-b67e3eaf1b90.webp'),(16,'363012ce-47cf-48a1-85d0-bbb8d567fa8c.jpg'),(16,'9e6e9d83-8118-46fb-ba9f-399429aabe37.webp'),(16,'56e10e70-90ab-4e40-b570-f68c7177848f.jpg'),(16,'2a95c751-d72c-4c84-8756-8cec27aa8d32.jpg'),(16,'15836971-43be-46d6-9fb9-fffff1008a23.jpg'),(16,'6f25460c-fcb5-4268-9195-65810a3513d4.jpg'),(16,'42b91f8e-8d9f-496c-a420-9978568cd83c.jpg'),(17,'26c7ab8e-9ae0-4193-81dc-9e0066bafb7a.webp'),(17,'05152b95-14c4-4d08-9727-246d8bf2749d.webp'),(17,'64acbdb7-a76f-428c-ae9a-885de53e7aad.webp'),(17,'e59a095f-da22-4519-96a8-255ecef873d5.webp'),(17,'b966fb4a-8668-4f42-8303-dcb428eb07f7.webp'),(17,'34f1b608-3b8b-4e70-beea-e00e1cc4c013.webp'),(17,'7385111e-6f2c-40f5-b53b-03a329f4d75d.webp'),(18,'27bab9a2-65e0-435b-87ba-c1c897278531.webp'),(18,'5b636117-9a17-466a-b8bf-99fe134d2398.webp'),(18,'5aba67e9-71ed-4f32-ac4e-31c7078d1eb3.webp'),(18,'fbcf4d5e-736b-4f01-8787-f8a6fa74159a.webp'),(18,'457c0d66-582a-4a2e-85b4-3b22cbdcebe8.webp'),(18,'c93293d9-0fb3-4c91-829a-e19d0ece38a6.webp'),(18,'b2217a77-97bf-4a40-a22d-5654ef410dd5.webp'),(18,'eae1304c-d9e3-4a9a-bb10-c37064bc5587.webp'),(19,'e2eddd87-c088-4f61-96bd-10a211db705f.webp'),(19,'f5d9c7cb-02fc-46e1-a867-93a166cc3453.jpg'),(19,'6f1d9d24-90a0-4675-9b3a-ec76601a3930.webp'),(19,'48149d5e-416a-46fb-b8b7-9aa94010ac97.jpg'),(19,'11d38ea8-8a47-4d75-ac18-5ddd9de3190c.jpg'),(19,'d65f3e45-46d5-4280-8f54-4969e4e3986f.jpg'),(20,'137a108c-91c8-4da0-97f5-e2f2ade0d38f.webp'),(20,'d655e27d-9ef4-4289-9151-6200e90b663d.webp'),(20,'ccf000f5-bfae-4df5-a411-e01f6e6e7278.webp'),(20,'06f6ffa1-9508-4845-bbb9-fb9375fa39de.webp'),(20,'112c2ce0-dca1-49fc-8e3b-99799d1ee321.webp'),(20,'a37be53b-a91e-4116-bc72-5f5022ee3417.webp'),(20,'0fa41d7c-5ad4-43d6-a78b-81060e27eeab.webp'),(21,'0f13481d-3cb9-4709-bba3-877003e28a91.jpg'),(21,'f9a1cbca-d764-45b3-9d8c-e4aead46d152.webp'),(21,'b034d28c-5bdb-4d7b-9fb1-41d9def04f9d.webp'),(21,'1b5a4427-9ebb-47c4-8bbc-191c17101c6d.webp'),(21,'7f700627-7ea7-469b-85c8-c7a99e13981a.webp'),(21,'630004c5-8281-4171-baa7-b66b2412e584.webp'),(22,'798de8af-2a19-401e-bb60-dcc07113e780.webp'),(22,'8ebdd10d-c787-4728-be4e-8d4dad2a8ed4.webp'),(22,'b6a7cf99-c7c5-4e5e-8e78-2ac95e9aeb10.webp'),(22,'e299c00c-424c-4cab-8709-31e99c423e85.webp'),(22,'bdef5b12-6444-41e0-bfd5-4cadf1a22b4b.webp'),(23,'7940b673-f013-4250-ac08-202023ae7db4.webp'),(23,'f2de1a11-3e8e-4fbc-8335-4a56ae999dab.webp'),(23,'8b9b377d-a15e-445f-bba9-3e7b45922ddd.webp'),(23,'17accf19-8552-49ae-ab68-89a0048eff82.webp'),(23,'bd8b3e73-4d5d-4ecc-b42a-27047c49a8d7.webp'),(23,'0934c824-991b-48f6-b961-4bccdf2f68f5.webp'),(23,'59aa9cab-b415-4c14-ac5e-bfa4a5e9ef26.webp'),(24,'5cf81348-03f4-42bd-8908-bdc47879403d.jpg'),(24,'2dfab23d-81bb-4a85-8606-37060fd9d1cc.jpg'),(24,'729c7453-2f25-4dbb-b5e2-ffd263c254ee.webp'),(24,'2d7ce100-3c42-41d2-94a7-ed7fc5f04cc4.webp'),(24,'82745610-a86d-49e8-836c-efc4527d8ceb.jpg'),(25,'96be7a7c-d493-45bc-acdd-a712e9297af1.webp'),(25,'41358056-0cf8-49ae-9625-7640e828c311.webp'),(25,'d810b9f7-9e11-41e5-8ad4-20ed50dfa198.webp'),(25,'7cbe6c9e-7e84-4585-8719-9a21433ef63d.webp'),(25,'eda5a9c0-7cae-4c1a-ae0e-8bc526ffb4a0.webp'),(25,'f9b97f6a-314c-44f4-af45-a5f972b45404.webp'),(25,'9c892766-d6bf-419d-9d76-50ea0adbb85e.webp'),(25,'e5be81ad-75a4-43a8-92a1-e0d95ddb5062.webp'),(26,'a1b57a1d-18a8-4800-930d-5d587dd9b425.webp'),(26,'f213a995-c76a-43ad-bd55-06cce9ffc86b.webp'),(26,'552a7a9a-7b9a-4d39-b02a-90519773b7e7.webp'),(26,'09585e93-5b9d-480d-bc3e-331c74550333.webp'),(26,'7e20d1e2-83cd-469f-89b1-d33999437773.webp'),(26,'2de19915-8f1e-41cd-a263-fbd00c36ff55.webp'),(27,'27ac557c-8177-4be8-a353-0ff09b45b7b9.webp'),(27,'fa07a19e-e3bc-468f-b347-ffb45fa35f8c.jpg'),(27,'f27525b7-972a-4b14-89aa-8a3a8bc59aad.webp'),(27,'ae27e55b-fd68-410a-8eab-933fd9c58237.webp'),(27,'88ff4e06-03a6-41be-b4e1-62051c7da7ea.webp'),(27,'559a7d00-f532-4878-be31-61132704e93c.webp'),(27,'584c8f24-35cd-4d82-b6b1-33abfabc11d6.webp'),(27,'480a647a-3bd3-4249-b3e7-21925064b58f.webp'),(28,'4cb98112-5470-43da-a159-91b0b3e402a2.webp'),(28,'2e5fb10a-ad5f-4080-a357-bcfd620d7abe.webp'),(28,'fd8a4f1a-cc4f-4e0d-8974-5bfc1bda6ff2.webp'),(28,'f967f3a4-ee3b-4af5-bb20-3c9c4ab967cf.webp'),(28,'98d2ba99-fc16-4990-813d-a50ebdf30adf.webp'),(28,'89b2234b-9904-42d8-80a7-aaecce05306a.webp'),(28,'00644e9c-1c54-4a33-b519-7d85acab8ce1.webp'),(28,'92d9974a-22b1-44ea-8d6f-ea8d834cc317.webp'),(28,'fac7c3f0-b783-4bc9-ad0b-fc4a86d949ab.webp'),(28,'2f067a78-de82-48d4-8393-58cc31685af5.webp'),(28,'da9e745d-0ac5-4717-b499-d6de99a51aff.webp'),(29,'c4cd7e24-b413-4298-a30b-849337bf58ef.webp'),(29,'62df37cc-1809-4a18-87e0-39039f8bb649.webp'),(29,'a2787072-8429-4c65-8fa1-86c0d9d2828c.webp'),(29,'6649a482-9eb0-4158-bf21-63c3fe694e41.webp'),(29,'7f88f1f1-079a-43a8-96d1-8a8d48629242.webp'),(30,'40c4891c-038f-4012-849f-0929aa2326bd.webp'),(30,'9c7a4e26-f71b-410a-93aa-8d75ed1379a4.webp'),(30,'b00e91d6-5d12-4612-9459-b9cc05670b79.webp'),(30,'26a015da-efc8-423d-9501-771dd95f3d9e.webp'),(30,'b71b8c0b-e6e9-4000-82b7-19be4b8e7fac.webp'),(30,'9939a203-24c9-4664-b84a-0767e1aecc11.webp'),(31,'5beec0d6-65b6-4eb8-84e0-acd4b2e1cae6.jpg'),(31,'3b7a76b4-3ac3-4b6f-8ee3-1f0805857aa3.webp'),(31,'34c6df6d-c4d5-450c-9cc4-ed45c99cec22.webp'),(31,'bdf2f107-5eb7-48c2-8ef3-65f29127f643.jpg'),(31,'65529b8f-ce19-4a44-9da4-d0c37e2af809.webp'),(31,'2cf8af71-3682-48e6-8bd1-33a0f91ebd96.jpg'),(31,'bca90cdb-ec57-47bc-8034-2d52ec1dc48b.webp'),(32,'61e40285-1100-47db-912b-263327104cb6.webp'),(32,'c1bfea7b-0f00-4462-a994-4660274c0bf3.jpg'),(32,'1a342f06-f682-4264-a097-41bf33f7424b.jpg'),(32,'d83f6189-6726-4b8d-9c2f-ebd00d19042c.jpg'),(32,'408c8aa5-bff5-4367-9060-ad016d7ebc06.jpg'),(32,'10b0e8b1-5642-459c-b61e-7c11e9e7c88f.jpg'),(32,'f47563c0-9746-47ea-b8e9-be49029090d7.jpg'),(33,'10383f16-3bac-41f2-9c82-ae09cf3a1e56.webp'),(33,'434cf9fc-ca0f-430b-8a90-13d4e486d7d9.webp'),(33,'7b97472c-0486-4e1b-a22f-21b0787c63d5.webp'),(33,'b1117445-4695-4e07-90ce-f4df57103bf6.jpg'),(33,'330a7b18-ad65-430a-94c1-feb0d5b80426.webp'),(33,'6109c915-dd48-410b-89c8-fd80e46659ac.webp'),(34,'3eb3e8d1-a13d-4e05-a54e-5f54e4f8b3f8.webp'),(34,'9abb2a4a-d683-49e3-b14f-6a00359ebdbc.webp'),(34,'c3052330-7988-4619-ac43-83c9b807c716.webp'),(34,'ce63e3a2-71cf-4c26-beae-2f02badccfd1.webp'),(34,'015d06e0-1789-440a-9f4c-d3dde9a00d1e.webp'),(34,'1e163133-ec53-4ff0-84ec-32c08a6852f3.webp'),(34,'61db2a7a-32dc-4cfb-8939-9c3bf8698563.webp'),(35,'4e0f4a52-0ffb-4bb2-a2e6-605ae62664d4.webp'),(35,'0e73ca0e-91be-47a3-8b0f-36f6be4efc6c.webp'),(35,'7754be40-29d5-4f9d-a8c7-02c312ec72c6.webp'),(35,'5a4caf92-c3bc-4d9a-9478-2320767bc52d.webp');
/*!40000 ALTER TABLE `product_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `description` longtext COLLATE utf8mb4_vietnamese_ci,
  `enabled` bit(1) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `price` bigint DEFAULT NULL,
  `slug` longtext COLLATE utf8mb4_vietnamese_ci,
  `thumbnail` longtext COLLATE utf8mb4_vietnamese_ci,
  `brand_id` bigint DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKa3a4mpsfdf4d2y6r8ra3sc8mv` (`brand_id`),
  KEY `FKog2rp4qthbtt2lfyhfo32lsw9` (`category_id`),
  CONSTRAINT `FKa3a4mpsfdf4d2y6r8ra3sc8mv` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`),
  CONSTRAINT `FKog2rp4qthbtt2lfyhfo32lsw9` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'2024-05-01 19:19:43.192000','2024-05-04 03:42:11.558000','<p><strong>Đặc điểm nổi bật:</strong></p>\r\n<ul>\r\n<li><strong>Chất liệu</strong>: Da b&ograve; thật 100%, mang đến vẻ ngo&agrave;i s&aacute;ng b&oacute;ng v&agrave; đẳng cấp.</li>\r\n<li><strong>Đế</strong>: Đế được l&agrave;m từ cao su tổng hợp, tạo độ ma s&aacute;t tốt tr&ecirc;n nhiều bề mặt.</li>\r\n<li><strong>C&ocirc;ng nghệ Cushion Soft&trade;</strong>: T&iacute;ch hợp lớp đệm với hỗ trợ Ortholite trong l&ograve;ng gi&agrave;y, giảm thiểu &aacute;p lực l&ecirc;n ch&acirc;n v&agrave; tăng cường sự thoải m&aacute;i suốt cả ng&agrave;y.</li>\r\n<li><strong>Kiểu d&aacute;ng</strong>: Thiết kế Oxford truyền thống với c&aacute;c chi tiết đục lỗ tinh tế, tạo điểm nhấn ấn tượng v&agrave; tinh tế.</li>\r\n<li><strong>Phong c&aacute;ch</strong>: Phối hợp ho&agrave;n hảo với quần t&acirc;y, quần jeans lịch l&atilde;m hoặc kết hợp c&ugrave;ng quần kaki v&agrave; &aacute;o sơ mi cho những dịp b&aacute;n ch&iacute;nh thức.</li>\r\n</ul>\r\n<p><strong>L&yacute; do n&ecirc;n chọn:</strong> Gi&agrave;y Oxford Clarks Tilden Walk l&agrave; lựa chọn l&yacute; tưởng cho những qu&yacute; &ocirc;ng y&ecirc;u th&iacute;ch sự kết hợp giữa truyền thống v&agrave; hiện đại. Kh&ocirc;ng chỉ thể hiện gu thẩm mỹ tinh tế, đ&ocirc;i gi&agrave;y n&agrave;y c&ograve;n cung cấp sự thoải m&aacute;i đ&aacute;ng kinh ngạc, ph&ugrave; hợp để mang trong suốt một ng&agrave;y d&agrave;i l&agrave;m việc hoặc tham dự c&aacute;c sự kiện. Với Clarks Tilden Walk, mỗi bước ch&acirc;n của bạn kh&ocirc;ng chỉ l&agrave; sự tự tin m&agrave; c&ograve;n l&agrave; biểu tượng của phong c&aacute;ch v&agrave; đẳng cấp.</p>',_binary '','Giày Tây Boot Nam BVM001788',1669000,'giay-tay-boot-nam-bitis-bvm001788','e9eb6006-cf7b-4b83-9df8-e204783d91b8.webp',9,1),(2,'2024-05-01 19:56:09.647000','2024-05-01 19:56:09.726000','<p><strong>Giới thiệu:</strong> Crocs Kids\' Classic Clog l&agrave; sự lựa chọn ho&agrave;n hảo cho b&eacute; trai năng động. Với thiết kế dễ chịu, nhẹ nh&agrave;ng v&agrave; đầy m&agrave;u sắc, đ&ocirc;i d&eacute;p n&agrave;y kh&ocirc;ng chỉ th&iacute;ch hợp cho mọi hoạt động chơi ngo&agrave;i trời m&agrave; c&ograve;n ph&ugrave; hợp cho c&aacute;c buổi d&atilde; ngoại hay thời gian b&ecirc;n bể bơi.</p>\r\n<p><strong>Đặc điểm nổi bật:</strong></p>\r\n<ul>\r\n<li><strong>Chất liệu</strong>: Sử dụng c&ocirc;ng nghệ Croslite&trade; độc quyền của Crocs, mang lại cảm gi&aacute;c nhẹ nh&agrave;ng, tho&aacute;ng kh&iacute; cho đ&ocirc;i ch&acirc;n b&eacute;.</li>\r\n<li><strong>Thiết kế</strong>: D&eacute;p kẹp với d&acirc;y đeo sau g&oacute;t ch&acirc;n, dễ d&agrave;ng điều chỉnh để ph&ugrave; hợp với mọi hoạt động.</li>\r\n<li><strong>An to&agrave;n</strong>: Đế d&eacute;p được thiết kế với c&aacute;c r&atilde;nh chống trượt, đảm bảo an to&agrave;n tối đa cho b&eacute; khi chạy nhảy.</li>\r\n<li><strong>M&agrave;u sắc</strong>: Đa dạng m&agrave;u sắc v&agrave; họa tiết, k&iacute;ch th&iacute;ch thị gi&aacute;c v&agrave; ph&ugrave; hợp với sở th&iacute;ch của b&eacute;.</li>\r\n<li><strong>Dễ d&agrave;ng vệ sinh</strong>: D&eacute;p Crocs c&oacute; thể dễ d&agrave;ng rửa sạch với nước, tiện lợi cho việc bảo quản v&agrave; l&agrave;m sạch sau mỗi lần sử dụng.</li>\r\n</ul>\r\n<p><strong>L&yacute; do n&ecirc;n chọn:</strong> D&eacute;p Crocs Kids\' Classic Clog l&agrave; lựa chọn l&yacute; tưởng cho b&eacute; trai với đặc t&iacute;nh nổi bật về sự thoải m&aacute;i v&agrave; tiện dụng. Đ&ocirc;i d&eacute;p n&agrave;y kh&ocirc;ng chỉ bền bỉ để đồng h&agrave;nh c&ugrave;ng b&eacute; trong mọi cuộc phi&ecirc;u lưu m&agrave; c&ograve;n dễ d&agrave;ng ph&ugrave; hợp với mọi trang phục nhờ thiết kế đơn giản nhưng thời trang. D&acirc;y đeo an to&agrave;n gi&uacute;p đảm bảo d&eacute;p lu&ocirc;n vững chắc tr&ecirc;n ch&acirc;n b&eacute;, ngay cả khi hoạt động mạnh.</p>',_binary '','Dép Eva Phun Trẻ Em E-Cloud BEB001801',128000,'dep-eva-phun-tre-em-e-cloud-beb001801','ce9d366b-9b6d-44c3-90c2-425074db42eb.webp',4,4),(3,'2024-05-01 20:01:30.643000','2024-05-01 20:01:30.778000','<p><strong>Giới thiệu:</strong> Clarks Couture Bloom l&agrave; đ&ocirc;i gi&agrave;y b&uacute;p b&ecirc; d&agrave;nh cho phụ nữ, kết hợp ho&agrave;n hảo giữa phong c&aacute;ch cổ điển v&agrave; hiện đại. Với thiết kế thanh lịch v&agrave; nữ t&iacute;nh, đ&ocirc;i gi&agrave;y n&agrave;y kh&ocirc;ng chỉ ph&ugrave; hợp cho c&ocirc;ng sở m&agrave; c&ograve;n l&yacute; tưởng để dạo phố hay tham gia c&aacute;c buổi tiệc nhẹ.</p>\r\n<p><strong>Đặc điểm nổi bật:</strong></p>\r\n<ul>\r\n<li><strong>Chất liệu</strong>: L&agrave;m từ da tổng hợp cao cấp, mềm mại, đem lại vẻ đẹp sang trọng v&agrave; độ bền cao.</li>\r\n<li><strong>Đế gi&agrave;y</strong>: Đế được l&agrave;m từ cao su non, c&oacute; khả năng chống trượt tốt, an to&agrave;n cho người sử dụng tr&ecirc;n nhiều loại bề mặt.</li>\r\n<li><strong>C&ocirc;ng nghệ Cushion Soft&trade;</strong>: Đệm l&oacute;t được t&iacute;ch hợp c&ocirc;ng nghệ Cushion Soft&trade;, mang lại cảm gi&aacute;c &ecirc;m &aacute;i tối ưu v&agrave; hỗ trợ ch&acirc;n suốt ng&agrave;y d&agrave;i.</li>\r\n<li><strong>Kiểu d&aacute;ng</strong>: Thiết kế mũi tr&ograve;n cổ điển với nơ nhỏ xinh ở mặt trước, tạo điểm nhấn dễ thương v&agrave; duy&ecirc;n d&aacute;ng.</li>\r\n<li><strong>M&agrave;u sắc</strong>: Sẵn c&oacute; trong nhiều m&agrave;u sắc, từ trung t&iacute;nh đến nổi bật, dễ d&agrave;ng phối đồ với c&aacute;c trang phục kh&aacute;c nhau.</li>\r\n</ul>\r\n<p><strong>L&yacute; do n&ecirc;n chọn:</strong> Gi&agrave;y B&uacute;p B&ecirc; Clarks Couture Bloom l&agrave; lựa chọn l&yacute; tưởng cho c&aacute;c qu&yacute; c&ocirc; y&ecirc;u th&iacute;ch sự thoải m&aacute;i nhưng kh&ocirc;ng muốn hy sinh phong c&aacute;ch thời trang. Với thiết kế tinh tế v&agrave; chất lượng vượt trội, đ&ocirc;i gi&agrave;y n&agrave;y kh&ocirc;ng chỉ đ&aacute;p ứng nhu cầu đi lại h&agrave;ng ng&agrave;y m&agrave; c&ograve;n ph&ugrave; hợp với c&aacute;c dịp đặc biệt. Khả năng kết hợp linh hoạt với nhiều loại trang phục từ quần jeans đến v&aacute;y v&oacute;c, Clarks Couture Bloom l&agrave; sự bổ sung ho&agrave;n hảo cho tủ đồ của mọi c&ocirc; g&aacute;i.</p>',_binary '','Giày Búp Bê Nữ BBW001788',393000,'giay-bup-be-nu-bbw001788','6716f3cc-bd23-458f-bdf6-cf92b20acff6.webp',10,2),(4,'2024-05-01 20:04:08.733000','2024-05-01 22:02:50.491000','<p><strong>Giới thiệu:</strong> Clarks Couture Bloom l&agrave; đ&ocirc;i gi&agrave;y b&uacute;p b&ecirc; d&agrave;nh cho phụ nữ, kết hợp ho&agrave;n hảo giữa phong c&aacute;ch cổ điển v&agrave; hiện đại. Với thiết kế thanh lịch v&agrave; nữ t&iacute;nh, đ&ocirc;i gi&agrave;y n&agrave;y kh&ocirc;ng chỉ ph&ugrave; hợp cho c&ocirc;ng sở m&agrave; c&ograve;n l&yacute; tưởng để dạo phố hay tham gia c&aacute;c buổi tiệc nhẹ.</p>\r\n<p><strong>Đặc điểm nổi bật:</strong></p>\r\n<ul>\r\n<li><strong>Chất liệu</strong>: L&agrave;m từ da tổng hợp cao cấp, mềm mại, đem lại vẻ đẹp sang trọng v&agrave; độ bền cao.</li>\r\n<li><strong>Đế gi&agrave;y</strong>: Đế được l&agrave;m từ cao su non, c&oacute; khả năng chống trượt tốt, an to&agrave;n cho người sử dụng tr&ecirc;n nhiều loại bề mặt.</li>\r\n<li><strong>C&ocirc;ng nghệ Cushion Soft&trade;</strong>: Đệm l&oacute;t được t&iacute;ch hợp c&ocirc;ng nghệ Cushion Soft&trade;, mang lại cảm gi&aacute;c &ecirc;m &aacute;i tối ưu v&agrave; hỗ trợ ch&acirc;n suốt ng&agrave;y d&agrave;i.</li>\r\n<li><strong>Kiểu d&aacute;ng</strong>: Thiết kế mũi tr&ograve;n cổ điển với nơ nhỏ xinh ở mặt trước, tạo điểm nhấn dễ thương v&agrave; duy&ecirc;n d&aacute;ng.</li>\r\n<li><strong>M&agrave;u sắc</strong>: Sẵn c&oacute; trong nhiều m&agrave;u sắc, từ trung t&iacute;nh đến nổi bật, dễ d&agrave;ng phối đồ với c&aacute;c trang phục kh&aacute;c nhau.</li>\r\n</ul>\r\n<p><strong>L&yacute; do n&ecirc;n chọn:</strong> Gi&agrave;y B&iacute;t&nbsp; l&agrave; lựa chọn l&yacute; tưởng cho c&aacute;c qu&yacute; c&ocirc; y&ecirc;u th&iacute;ch sự thoải m&aacute;i nhưng kh&ocirc;ng muốn hy sinh phong c&aacute;ch thời trang. Với thiết kế tinh tế v&agrave; chất lượng vượt trội, đ&ocirc;i gi&agrave;y n&agrave;y kh&ocirc;ng chỉ đ&aacute;p ứng nhu cầu đi lại h&agrave;ng ng&agrave;y m&agrave; c&ograve;n ph&ugrave; hợp với c&aacute;c dịp đặc biệt. Khả năng kết hợp linh hoạt với nhiều loại trang phục từ quần jeans đến v&aacute;y v&oacute;c, Clarks Couture Bloom l&agrave; sự bổ sung ho&agrave;n hảo cho tủ đồ của mọi c&ocirc; g&aacute;i.</p>',_binary '','Giày Bít Thời Trang Nữ BFW003588',432000,'giay-bit-thoi-trang-nu-bfw003588','5558dca4-a4d9-4fa1-a88d-9140e120ae7c.webp',11,2),(5,'2024-05-01 20:07:43.850000','2024-05-01 20:07:43.877000','<p><strong>Giới thiệu:</strong> Adidas FortaRun X CF l&agrave; đ&ocirc;i gi&agrave;y thể thao được thiết kế đặc biệt cho b&eacute; g&aacute;i, ph&ugrave; hợp với mọi hoạt động từ chơi đ&ugrave;a cho đến thể thao. Đ&ocirc;i gi&agrave;y n&agrave;y kết hợp giữa t&iacute;nh năng hiện đại v&agrave; sự thoải m&aacute;i để đồng h&agrave;nh c&ugrave;ng b&eacute; y&ecirc;u trong từng bước chạy năng động.</p>\r\n<p><strong>Đặc điểm nổi bật:</strong></p>\r\n<ul>\r\n<li><strong>Chất liệu</strong>: Được l&agrave;m từ vải lưới v&agrave; nhựa tổng hợp, đảm bảo độ bền v&agrave; khả năng thở cao, gi&uacute;p đ&ocirc;i ch&acirc;n của b&eacute; lu&ocirc;n kh&ocirc; tho&aacute;ng.</li>\r\n<li><strong>D&acirc;y đai d&aacute;n</strong>: T&iacute;ch hợp d&acirc;y đai d&aacute;n điều chỉnh dễ d&agrave;ng, cho ph&eacute;p b&eacute; tự m&igrave;nh điều chỉnh ph&ugrave; hợp với đ&ocirc;i ch&acirc;n một c&aacute;ch nhanh ch&oacute;ng v&agrave; dễ d&agrave;ng.</li>\r\n<li><strong>Đế gi&agrave;y</strong>: Đế cao su kh&ocirc;ng để lại vết, an to&agrave;n cho s&agrave;n nh&agrave; v&agrave; mặt s&acirc;n, đồng thời cung cấp độ b&aacute;m v&agrave; đ&agrave;n hồi tốt khi b&eacute; nhảy v&agrave; chạy.</li>\r\n<li><strong>C&ocirc;ng nghệ đệm Cloudfoam</strong>: Mang lại cảm gi&aacute;c &ecirc;m &aacute;i v&agrave; hỗ trợ đặc biệt dưới l&ograve;ng b&agrave;n ch&acirc;n, gi&uacute;p giảm thiểu &aacute;p lực l&ecirc;n ch&acirc;n khi vận động.</li>\r\n<li><strong>M&agrave;u sắc v&agrave; thiết kế</strong>: Sử dụng t&ocirc;ng m&agrave;u tươi s&aacute;ng v&agrave; họa tiết vui nhộn, ph&ugrave; hợp với thị hiếu của b&eacute; g&aacute;i, tăng th&ecirc;m phần th&uacute; vị khi mặc đ&ocirc;i gi&agrave;y n&agrave;y.</li>\r\n</ul>\r\n<p><strong>L&yacute; do n&ecirc;n chọn:</strong> Adidas FortaRun X CF kh&ocirc;ng chỉ l&agrave; đ&ocirc;i gi&agrave;y chạy bền bỉ m&agrave; c&ograve;n l&agrave; người bạn đồng h&agrave;nh l&yacute; tưởng cho c&aacute;c c&ocirc; b&eacute; trong mọi hoạt động. Với thiết kế thời trang, năng động v&agrave; đặc biệt ph&ugrave; hợp với lối sống đầy năng lượng của trẻ nhỏ, đ&ocirc;i gi&agrave;y n&agrave;y sẽ gi&uacute;p b&eacute; y&ecirc;u cảm thấy tự tin hơn khi tham gia c&aacute;c hoạt động thể thao hoặc c&aacute;c tr&ograve; chơi ngo&agrave;i trời. Độ an to&agrave;n cao, sự thoải m&aacute;i tối ưu v&agrave; khả năng t&ugrave;y chỉnh dễ d&agrave;ng l&agrave;m cho Adidas FortaRun X CF trở th&agrave;nh lựa chọn h&agrave;ng đầu cho phụ huynh khi t&igrave;m mua gi&agrave;y thể thao cho b&eacute; g&aacute;i.</p>',_binary '','Giày Thể Thao Bé Gái BSG002600',447000,'giay-the-thao-be-gai-bsg002600','da301596-2ae2-4bc6-8324-4024b3dce598.webp',2,5),(6,'2024-05-01 20:12:03.831000','2024-05-01 20:49:23.204000','<p><strong>Giới thiệu:</strong> Clarks Couture Bloom l&agrave; đ&ocirc;i gi&agrave;y b&uacute;p b&ecirc; d&agrave;nh cho phụ nữ, kết hợp ho&agrave;n hảo giữa phong c&aacute;ch cổ điển v&agrave; hiện đại. Với thiết kế thanh lịch v&agrave; nữ t&iacute;nh, đ&ocirc;i gi&agrave;y n&agrave;y kh&ocirc;ng chỉ ph&ugrave; hợp cho c&ocirc;ng sở m&agrave; c&ograve;n l&yacute; tưởng để dạo phố hay tham gia c&aacute;c buổi tiệc nhẹ.</p>\r\n<p><strong>Đặc điểm nổi bật:</strong></p>\r\n<ul>\r\n<li><strong>Chất liệu</strong>: L&agrave;m từ da tổng hợp cao cấp, mềm mại, đem lại vẻ đẹp sang trọng v&agrave; độ bền cao.</li>\r\n<li><strong>Đế gi&agrave;y</strong>: Đế được l&agrave;m từ cao su non, c&oacute; khả năng chống trượt tốt, an to&agrave;n cho người sử dụng tr&ecirc;n nhiều loại bề mặt.</li>\r\n<li><strong>C&ocirc;ng nghệ Cushion Soft&trade;</strong>: Đệm l&oacute;t được t&iacute;ch hợp c&ocirc;ng nghệ Cushion Soft&trade;, mang lại cảm gi&aacute;c &ecirc;m &aacute;i tối ưu v&agrave; hỗ trợ ch&acirc;n suốt ng&agrave;y d&agrave;i.</li>\r\n<li><strong>Kiểu d&aacute;ng</strong>: Thiết kế mũi tr&ograve;n cổ điển với nơ nhỏ xinh ở mặt trước, tạo điểm nhấn dễ thương v&agrave; duy&ecirc;n d&aacute;ng.</li>\r\n<li><strong>M&agrave;u sắc</strong>: Sẵn c&oacute; trong nhiều m&agrave;u sắc, từ trung t&iacute;nh đến nổi bật, dễ d&agrave;ng phối đồ với c&aacute;c trang phục kh&aacute;c nhau.</li>\r\n</ul>\r\n<p><strong>L&yacute; do n&ecirc;n chọn:</strong> Gi&agrave;y B&uacute;p B&ecirc; Clarks Couture Bloom l&agrave; lựa chọn l&yacute; tưởng cho c&aacute;c qu&yacute; c&ocirc; y&ecirc;u th&iacute;ch sự thoải m&aacute;i nhưng kh&ocirc;ng muốn hy sinh phong c&aacute;ch thời trang. Với thiết kế tinh tế v&agrave; chất lượng vượt trội, đ&ocirc;i gi&agrave;y n&agrave;y kh&ocirc;ng chỉ đ&aacute;p ứng nhu cầu đi lại h&agrave;ng ng&agrave;y m&agrave; c&ograve;n ph&ugrave; hợp với c&aacute;c dịp đặc biệt. Khả năng kết hợp linh hoạt với nhiều loại trang phục từ quần jeans đến v&aacute;y v&oacute;c, Clarks Couture Bloom l&agrave; sự bổ sung ho&agrave;n hảo cho tủ đồ của mọi c&ocirc; g&aacute;i.</p>',_binary '','Giày Sneaker Nữ DSW066300XAD',565000,'giay-sneaker-nu-dsw066300xad','a40b3dac-80f3-4615-90cb-c4f7e06270d4.webp',8,2),(7,'2024-05-01 20:15:33.320000','2024-05-01 20:15:33.430000','<p><strong>Giới thiệu:</strong> Crocs Kids&rsquo; Classic Clog l&agrave; lựa chọn l&yacute; tưởng cho b&eacute; trai y&ecirc;u th&iacute;ch sự thoải m&aacute;i v&agrave; tự do khi chơi đ&ugrave;a. Với thiết kế đặc trưng nhẹ nh&agrave;ng, dễ d&agrave;ng sử dụng, đ&ocirc;i d&eacute;p n&agrave;y th&iacute;ch hợp cho mọi hoạt động từ đi biển đến chơi tại c&ocirc;ng vi&ecirc;n.</p>\r\n<p><strong>Đặc điểm nổi bật:</strong></p>\r\n<ul>\r\n<li><strong>Chất liệu Croslite&trade;:</strong> Độc quyền của Crocs, chất liệu n&agrave;y đem lại sự nhẹ nh&agrave;ng, &ecirc;m &aacute;i, kh&ocirc;ng thấm nước, v&agrave; bền bỉ.</li>\r\n<li><strong>Thiết kế th&ocirc;ng tho&aacute;ng:</strong> Lỗ th&ocirc;ng hơi tr&ecirc;n th&acirc;n d&eacute;p gi&uacute;p ch&acirc;n b&eacute; tho&aacute;ng kh&iacute; v&agrave; kh&ocirc; r&aacute;o, đồng thời cho ph&eacute;p c&aacute;c b&eacute; tự do trang tr&iacute; d&eacute;p bằng c&aacute;c Jibbitz&trade; charms.</li>\r\n<li><strong>D&acirc;y đeo g&oacute;t ch&acirc;n:</strong> D&acirc;y đeo chắc chắn gi&uacute;p d&eacute;p &ocirc;m s&aacute;t ch&acirc;n b&eacute; trong khi chạy nhảy v&agrave; vui đ&ugrave;a, đảm bảo an to&agrave;n.</li>\r\n<li><strong>M&agrave;u sắc đa dạng:</strong> Sẵn c&oacute; trong nhiều m&agrave;u sắc v&agrave; họa tiết, thu h&uacute;t &aacute;nh nh&igrave;n v&agrave; ph&ugrave; hợp với mọi lựa chọn của b&eacute;.</li>\r\n<li><strong>Dễ d&agrave;ng vệ sinh v&agrave; bảo quản:</strong> Chỉ cần rửa sạch với nước, d&eacute;p sẽ nhanh ch&oacute;ng sạch v&agrave; sẵn s&agrave;ng cho lần mặc tiếp theo.</li>\r\n</ul>\r\n<p><strong>L&yacute; do n&ecirc;n chọn:</strong> D&eacute;p Crocs Kids&rsquo; Classic Clog l&agrave; lựa chọn h&agrave;ng đầu cho phụ huynh khi t&igrave;m kiếm một đ&ocirc;i d&eacute;p vừa tiện lợi, vừa an to&agrave;n cho b&eacute; trai. Sự linh hoạt v&agrave; khả năng ph&ugrave; hợp với mọi ho&agrave;n cảnh từ trong nh&agrave; đến ngo&agrave;i trời c&ugrave;ng với đặc t&iacute;nh dễ d&agrave;ng l&agrave;m sạch l&agrave;m cho đ&ocirc;i d&eacute;p n&agrave;y trở th&agrave;nh sự lựa chọn ho&agrave;n hảo cho b&eacute; trong mọi hoạt động.</p>',_binary '','Dép Bé Trai BPB000400',167000,'dep-be-trai-bpb000400','4a33b637-efd5-4931-ac42-43ed73a0a55f.webp',5,4),(8,'2024-05-01 20:23:35.981000','2024-05-01 20:23:36.027000','<p><strong>Giới thiệu:</strong> Adidas FortaRun X CF l&agrave; đ&ocirc;i gi&agrave;y thể thao được thiết kế đặc biệt cho b&eacute; g&aacute;i, ph&ugrave; hợp với mọi hoạt động từ chơi đ&ugrave;a cho đến thể thao. Đ&ocirc;i gi&agrave;y n&agrave;y kết hợp giữa t&iacute;nh năng hiện đại v&agrave; sự thoải m&aacute;i để đồng h&agrave;nh c&ugrave;ng b&eacute; y&ecirc;u trong từng bước chạy năng động.</p>\r\n<p><strong>Đặc điểm nổi bật:</strong></p>\r\n<ul>\r\n<li><strong>Chất liệu</strong>: Được l&agrave;m từ vải lưới v&agrave; nhựa tổng hợp, đảm bảo độ bền v&agrave; khả năng thở cao, gi&uacute;p đ&ocirc;i ch&acirc;n của b&eacute; lu&ocirc;n kh&ocirc; tho&aacute;ng.</li>\r\n<li><strong>D&acirc;y đai d&aacute;n</strong>: T&iacute;ch hợp d&acirc;y đai d&aacute;n điều chỉnh dễ d&agrave;ng, cho ph&eacute;p b&eacute; tự m&igrave;nh điều chỉnh ph&ugrave; hợp với đ&ocirc;i ch&acirc;n một c&aacute;ch nhanh ch&oacute;ng v&agrave; dễ d&agrave;ng.</li>\r\n<li><strong>Đế gi&agrave;y</strong>: Đế cao su kh&ocirc;ng để lại vết, an to&agrave;n cho s&agrave;n nh&agrave; v&agrave; mặt s&acirc;n, đồng thời cung cấp độ b&aacute;m v&agrave; đ&agrave;n hồi tốt khi b&eacute; nhảy v&agrave; chạy.</li>\r\n<li><strong>C&ocirc;ng nghệ đệm Cloudfoam</strong>: Mang lại cảm gi&aacute;c &ecirc;m &aacute;i v&agrave; hỗ trợ đặc biệt dưới l&ograve;ng b&agrave;n ch&acirc;n, gi&uacute;p giảm thiểu &aacute;p lực l&ecirc;n ch&acirc;n khi vận động.</li>\r\n<li><strong>M&agrave;u sắc v&agrave; thiết kế</strong>: Sử dụng t&ocirc;ng m&agrave;u tươi s&aacute;ng v&agrave; họa tiết vui nhộn, ph&ugrave; hợp với thị hiếu của b&eacute; g&aacute;i, tăng th&ecirc;m phần th&uacute; vị khi mặc đ&ocirc;i gi&agrave;y n&agrave;y.</li>\r\n</ul>\r\n<p><strong>L&yacute; do n&ecirc;n chọn:</strong> Adidas FortaRun X CF kh&ocirc;ng chỉ l&agrave; đ&ocirc;i gi&agrave;y chạy bền bỉ m&agrave; c&ograve;n l&agrave; người bạn đồng h&agrave;nh l&yacute; tưởng cho c&aacute;c c&ocirc; b&eacute; trong mọi hoạt động. Với thiết kế thời trang, năng động v&agrave; đặc biệt ph&ugrave; hợp với lối sống đầy năng lượng của trẻ nhỏ, đ&ocirc;i gi&agrave;y n&agrave;y sẽ gi&uacute;p b&eacute; y&ecirc;u cảm thấy tự tin hơn khi tham gia c&aacute;c hoạt động thể thao hoặc c&aacute;c tr&ograve; chơi ngo&agrave;i trời. Độ an to&agrave;n cao, sự thoải m&aacute;i tối ưu v&agrave; khả năng t&ugrave;y chỉnh dễ d&agrave;ng l&agrave;m cho Adidas FortaRun X CF trở th&agrave;nh lựa chọn h&agrave;ng đầu cho phụ huynh khi t&igrave;m mua gi&agrave;y thể thao cho b&eacute; g&aacute;i.</p>',_binary '','Giày Thể Thao Bé Gái DSG003811',415000,'giay-the-thao-be-gai-dsg003811','c06cbef2-0333-4787-a5bc-2697a75027c5.webp',7,5),(9,'2024-05-01 20:24:52.453000','2024-05-01 20:30:26.218000','',_binary '','Giày Eva Phun Bé Gái BSG004000',172000,'giay-thong-dung-eva-phun-be-gai-bsg004000','f3f0308b-3f38-4ac1-b11e-f68c2c1fabe3.webp',1,5),(10,'2024-05-01 20:27:03.546000','2024-05-01 22:00:12.615000','',_binary '','Giày Thể Thao Thông Dụng Nữ BSM000700',420000,'giay-the-thao-thong-dung-nam-bsm000700','a21a24b3-8e98-4293-a305-1d38c5676276.webp',11,3),(11,'2024-05-01 20:32:51.913000','2024-05-01 20:32:52.005000','<p><strong>Giới thiệu:</strong> Crocs Kids&rsquo; Classic Clog l&agrave; lựa chọn l&yacute; tưởng cho b&eacute; trai y&ecirc;u th&iacute;ch sự thoải m&aacute;i v&agrave; tự do khi chơi đ&ugrave;a. Với thiết kế đặc trưng nhẹ nh&agrave;ng, dễ d&agrave;ng sử dụng, đ&ocirc;i d&eacute;p n&agrave;y th&iacute;ch hợp cho mọi hoạt động từ đi biển đến chơi tại c&ocirc;ng vi&ecirc;n.</p>\r\n<p><strong>Đặc điểm nổi bật:</strong></p>\r\n<ul>\r\n<li><strong>Chất liệu Croslite&trade;:</strong> Độc quyền của Crocs, chất liệu n&agrave;y đem lại sự nhẹ nh&agrave;ng, &ecirc;m &aacute;i, kh&ocirc;ng thấm nước, v&agrave; bền bỉ.</li>\r\n<li><strong>Thiết kế th&ocirc;ng tho&aacute;ng:</strong> Lỗ th&ocirc;ng hơi tr&ecirc;n th&acirc;n d&eacute;p gi&uacute;p ch&acirc;n b&eacute; tho&aacute;ng kh&iacute; v&agrave; kh&ocirc; r&aacute;o, đồng thời cho ph&eacute;p c&aacute;c b&eacute; tự do trang tr&iacute; d&eacute;p bằng c&aacute;c Jibbitz&trade; charms.</li>\r\n<li><strong>D&acirc;y đeo g&oacute;t ch&acirc;n:</strong> D&acirc;y đeo chắc chắn gi&uacute;p d&eacute;p &ocirc;m s&aacute;t ch&acirc;n b&eacute; trong khi chạy nhảy v&agrave; vui đ&ugrave;a, đảm bảo an to&agrave;n.</li>\r\n<li><strong>M&agrave;u sắc đa dạng:</strong> Sẵn c&oacute; trong nhiều m&agrave;u sắc v&agrave; họa tiết, thu h&uacute;t &aacute;nh nh&igrave;n v&agrave; ph&ugrave; hợp với mọi lựa chọn của b&eacute;.</li>\r\n<li><strong>Dễ d&agrave;ng vệ sinh v&agrave; bảo quản:</strong> Chỉ cần rửa sạch với nước, d&eacute;p sẽ nhanh ch&oacute;ng sạch v&agrave; sẵn s&agrave;ng cho lần mặc tiếp theo.</li>\r\n</ul>\r\n<p><strong>L&yacute; do n&ecirc;n chọn:</strong> D&eacute;p Crocs Kids&rsquo; Classic Clog l&agrave; lựa chọn h&agrave;ng đầu cho phụ huynh khi t&igrave;m kiếm một đ&ocirc;i d&eacute;p vừa tiện lợi, vừa an to&agrave;n cho b&eacute; trai. Sự linh hoạt v&agrave; khả năng ph&ugrave; hợp với mọi ho&agrave;n cảnh từ trong nh&agrave; đến ngo&agrave;i trời c&ugrave;ng với đặc t&iacute;nh dễ d&agrave;ng l&agrave;m sạch l&agrave;m cho đ&ocirc;i d&eacute;p n&agrave;y trở th&agrave;nh sự lựa chọn ho&agrave;n hảo cho b&eacute; trong mọi hoạt động.</p>',_binary '','Dép Eva Bé Trai DEB010101',221000,'dep-eva-be-trai-deb010101','88eb31c3-9469-4482-b7e4-df8f470c173a.webp',6,4),(12,'2024-05-01 20:34:21.148000','2024-05-01 20:34:21.206000','',_binary '','Sandal Eva Phun Bé Trai BEB001700',300000,'sandal-eva-phun-be-trai-beb001700','f6649fbc-27f1-4610-b436-00b97f94f733.webp',5,4),(13,'2024-05-01 20:36:39.182000','2024-05-01 20:36:39.198000','',_binary '','Giày Búp Bê Nữ BBW001888',393000,'giay-bup-be-nu-bbw001888','014822d3-b688-4f83-86e2-b2f20b81a0ba.webp',3,2),(14,'2024-05-01 20:40:17.966000','2024-05-01 20:40:18.026000','<p>Một trong những đ&ocirc;i gi&agrave;y da nam kinh điển, phối hợp ho&agrave;n hảo với trang phục c&ocirc;ng sở hoặc c&aacute;c dịp trang trọng kh&aacute;c.</p>\r\n<p>Với thiết kế sang trọng v&agrave; lịch l&atilde;m, đ&ocirc;i Oxford n&agrave;y đem lại sự thoải m&aacute;i kh&ocirc;ng thể so s&aacute;nh.</p>',_binary '','Giày Tây Da Nam Cao Cấp DVM288770',860000,'giay-tay-da-nam-cao-cap-dvm288770','85d5d2e5-472d-476f-b977-e2eddb72939b.webp',9,1),(15,'2024-05-01 20:43:52.241000','2024-05-01 20:43:52.300000','<p>Nếu bạn muốn t&igrave;m một đ&ocirc;i gi&agrave;y đ&aacute; banh chất lượng, thiết kế thể thao v&agrave; &ecirc;m &aacute;i khi sử dụng th&igrave; h&atilde;y chọn Gi&agrave;y Đ&aacute; Banh Nam HSM003600. C&ugrave;ng t&igrave;m hiểu chi tiết về sản phẩm trong b&agrave;i viết dưới đ&acirc;y.</p>',_binary '','Giày Đá Bóng Nam HSM003600',687000,'giay-dja-bong-nam-hsm003600','a544c92d-e8bc-4dfb-b21f-c4775b89aa7a.webp',2,1),(16,'2024-05-01 20:51:20.663000','2024-05-01 20:51:20.784000','',_binary '','Giày Thể Thao Bé Trai BSB003800',432000,'giay-the-thao-be-trai-bsb003800','e71d2b0e-e042-444b-af46-77c79e77338f.webp',1,4),(17,'2024-05-01 20:53:42.474000','2024-05-01 20:53:42.556000','',_binary '','Sandal Thể Thao Bé Trai BYB000400',349000,'sandal-the-thao-be-trai-byb000400','8a31c948-028c-4264-a73e-a48fc4786646.webp',6,4),(18,'2024-05-01 21:01:44.489000','2024-05-01 22:00:44.315000','',_binary '','Giày Thể Thao Nam Street HSM006302',814000,'giay-the-thao-nam-hunter-street-hsm006302','0e3d62c4-9ea3-413e-9c47-40f611ddb6df.webp',4,3),(19,'2024-05-01 21:06:07.747000','2024-05-01 21:06:07.784000','',_binary '','Giày Thể Thao Nữ Hunter X DSWH03401',1019000,'giay-the-thao-nu-hunter-x-dswh03401','4113b353-eeb3-47b4-bdcd-de001189140b.webp',2,2),(20,'2024-05-01 21:07:47.056000','2024-05-01 21:07:47.096000','',_binary '','Giày Mocasin Nam DMM374770',668000,'giay-mocasin-nam-dmm374770','53f9c502-9dee-49f9-b151-042c0a97598a.webp',10,1),(21,'2024-05-01 21:11:23.377000','2024-05-02 00:33:55.987000','',_binary '','Giày Thể Thao Nam X Dune lửa HSM004204',1256000,'giay-the-thao-nam-r-x-dune-lua-hsm004204','c4fc00d3-f58f-46a6-8694-4162f1edba54.webp',3,1),(22,'2024-05-01 21:12:51.020000','2024-05-01 21:12:51.040000','',_binary '','Giày Thể Thao Faith Edition HSM008100',1502000,'giay-the-thao-faith-edition-hsm008100','320752cf-35c6-4ef7-bdad-0b9dae506278.webp',8,1),(23,'2024-05-01 21:17:04.944000','2024-05-01 22:00:22.295000','',_binary '','Giày Thể Thao Cao Cấp Nam Layered Upper DSMH02800',972000,'giay-the-thao-cao-cap-nam-layered-upper-dsmh02800','a0bd0231-c23d-4d92-83fd-4cc42569138a.webp',10,3),(24,'2024-05-01 21:41:22.723000','2024-05-01 21:41:22.778000','<p><strong>Giới thiệu:</strong> Clarks Couture Bloom l&agrave; đ&ocirc;i gi&agrave;y b&uacute;p b&ecirc; d&agrave;nh cho phụ nữ, kết hợp ho&agrave;n hảo giữa phong c&aacute;ch cổ điển v&agrave; hiện đại. Với thiết kế thanh lịch v&agrave; nữ t&iacute;nh, đ&ocirc;i gi&agrave;y n&agrave;y kh&ocirc;ng chỉ ph&ugrave; hợp cho c&ocirc;ng sở m&agrave; c&ograve;n l&yacute; tưởng để dạo phố hay tham gia c&aacute;c buổi tiệc nhẹ.</p>\r\n<p><strong>Đặc điểm nổi bật:</strong></p>\r\n<ul>\r\n<li><strong>Chất liệu</strong>: L&agrave;m từ da tổng hợp cao cấp, mềm mại, đem lại vẻ đẹp sang trọng v&agrave; độ bền cao.</li>\r\n<li><strong>Đế gi&agrave;y</strong>: Đế được l&agrave;m từ cao su non, c&oacute; khả năng chống trượt tốt, an to&agrave;n cho người sử dụng tr&ecirc;n nhiều loại bề mặt.</li>\r\n<li><strong>C&ocirc;ng nghệ Cushion Soft&trade;</strong>: Đệm l&oacute;t được t&iacute;ch hợp c&ocirc;ng nghệ Cushion Soft&trade;, mang lại cảm gi&aacute;c &ecirc;m &aacute;i tối ưu v&agrave; hỗ trợ ch&acirc;n suốt ng&agrave;y d&agrave;i.</li>\r\n<li><strong>Kiểu d&aacute;ng</strong>: Thiết kế mũi tr&ograve;n cổ điển với nơ nhỏ xinh ở mặt trước, tạo điểm nhấn dễ thương v&agrave; duy&ecirc;n d&aacute;ng.</li>\r\n<li><strong>M&agrave;u sắc</strong>: Sẵn c&oacute; trong nhiều m&agrave;u sắc, từ trung t&iacute;nh đến nổi bật, dễ d&agrave;ng phối đồ với c&aacute;c trang phục kh&aacute;c nhau.</li>\r\n</ul>\r\n<p><strong>L&yacute; do n&ecirc;n chọn:</strong> Gi&agrave;y B&uacute;p B&ecirc; Clarks Couture Bloom l&agrave; lựa chọn l&yacute; tưởng cho c&aacute;c qu&yacute; c&ocirc; y&ecirc;u th&iacute;ch sự thoải m&aacute;i nhưng kh&ocirc;ng muốn hy sinh phong c&aacute;ch thời trang. Với thiết kế tinh tế v&agrave; chất lượng vượt trội, đ&ocirc;i gi&agrave;y n&agrave;y kh&ocirc;ng chỉ đ&aacute;p ứng nhu cầu đi lại h&agrave;ng ng&agrave;y m&agrave; c&ograve;n ph&ugrave; hợp với c&aacute;c dịp đặc biệt. Khả năng kết hợp linh hoạt với nhiều loại trang phục từ quần jeans đến v&aacute;y v&oacute;c, Clarks Couture Bloom l&agrave; sự bổ sung ho&agrave;n hảo cho tủ đồ của mọi c&ocirc; g&aacute;i.</p>',_binary '','Giày Thể Thao Nữ Hunter Core HSW002400',737000,'giay-the-thao-nu-hunter-core-hsw002400','ee5fe421-bab3-43f2-96b4-0ab42aac1f7d.jpg',7,2),(25,'2024-05-01 21:58:45.174000','2024-05-01 21:58:45.257000','<p><strong>Giới thiệu:</strong> Clarks Couture Bloom l&agrave; đ&ocirc;i gi&agrave;y b&uacute;p b&ecirc; d&agrave;nh cho phụ nữ, kết hợp ho&agrave;n hảo giữa phong c&aacute;ch cổ điển v&agrave; hiện đại. Với thiết kế thanh lịch v&agrave; nữ t&iacute;nh, đ&ocirc;i gi&agrave;y n&agrave;y kh&ocirc;ng chỉ ph&ugrave; hợp cho c&ocirc;ng sở m&agrave; c&ograve;n l&yacute; tưởng để dạo phố hay tham gia c&aacute;c buổi tiệc nhẹ.</p>\r\n<p><strong>Đặc điểm nổi bật:</strong></p>\r\n<ul>\r\n<li><strong>Chất liệu</strong>: L&agrave;m từ da tổng hợp cao cấp, mềm mại, đem lại vẻ đẹp sang trọng v&agrave; độ bền cao.</li>\r\n<li><strong>Đế gi&agrave;y</strong>: Đế được l&agrave;m từ cao su non, c&oacute; khả năng chống trượt tốt, an to&agrave;n cho người sử dụng tr&ecirc;n nhiều loại bề mặt.</li>\r\n<li><strong>C&ocirc;ng nghệ Cushion Soft&trade;</strong>: Đệm l&oacute;t được t&iacute;ch hợp c&ocirc;ng nghệ Cushion Soft&trade;, mang lại cảm gi&aacute;c &ecirc;m &aacute;i tối ưu v&agrave; hỗ trợ ch&acirc;n suốt ng&agrave;y d&agrave;i.</li>\r\n<li><strong>Kiểu d&aacute;ng</strong>: Thiết kế mũi tr&ograve;n cổ điển với nơ nhỏ xinh ở mặt trước, tạo điểm nhấn dễ thương v&agrave; duy&ecirc;n d&aacute;ng.</li>\r\n<li><strong>M&agrave;u sắc</strong>: Sẵn c&oacute; trong nhiều m&agrave;u sắc, từ trung t&iacute;nh đến nổi bật, dễ d&agrave;ng phối đồ với c&aacute;c trang phục kh&aacute;c nhau.</li>\r\n</ul>\r\n<p><strong>L&yacute; do n&ecirc;n chọn:</strong> Gi&agrave;y B&uacute;p B&ecirc; Clarks Couture Bloom l&agrave; lựa chọn l&yacute; tưởng cho c&aacute;c qu&yacute; c&ocirc; y&ecirc;u th&iacute;ch sự thoải m&aacute;i nhưng kh&ocirc;ng muốn hy sinh phong c&aacute;ch thời trang. Với thiết kế tinh tế v&agrave; chất lượng vượt trội, đ&ocirc;i gi&agrave;y n&agrave;y kh&ocirc;ng chỉ đ&aacute;p ứng nhu cầu đi lại h&agrave;ng ng&agrave;y m&agrave; c&ograve;n ph&ugrave; hợp với c&aacute;c dịp đặc biệt. Khả năng kết hợp linh hoạt với nhiều loại trang phục từ quần jeans đến v&aacute;y v&oacute;c, Clarks Couture Bloom l&agrave; sự bổ sung ho&agrave;n hảo cho tủ đồ của mọi c&ocirc; g&aacute;i.</p>',_binary '','Giày Búp Bê Nữ Embrace - DBW004500',447000,'giay-bup-be-nu-embrace-dbw004500','8d0b799b-5d8b-48ec-83d3-50dd1e7baaa0.webp',7,2),(26,'2024-05-01 22:07:35.647000','2024-05-01 22:07:35.913000','',_binary '','Sandal Eva Phun Trẻ Em DEB007999',299000,'sandal-eva-phun-tre-em-deb007999','16c4fa59-3a9a-47bb-9007-a98d53943bf1.webp',11,1),(27,'2024-05-01 22:08:52.573000','2024-05-01 22:08:52.591000','',_binary '','Giày Thể Thao Street Z Collection DSMH06200',499000,'giay-the-thao-street-z-collection-dsmh06200','ff6995c7-dcb7-4d60-8b4a-c2a5cb72b2d3.webp',4,3),(28,'2024-05-01 22:11:19.440000','2024-05-01 22:11:19.465000','',_binary '','Sandal Thể Thao Eva Phun Nam HEM000500',336000,'sandal-the-thao-eva-phun-nam-hem000500','71c2c175-8c0c-484c-8d6f-6a0ba4526fc3.webp',3,1),(29,'2024-05-01 22:15:44.414000','2024-05-01 22:15:44.518000','',_binary '','Giày Thể Thao Nam DSM075200',368000,'giay-the-thao-nam-dsm075200','8667eb15-9e53-4bec-b5ac-807d0444b83c.webp',1,3),(30,'2024-05-01 22:18:41.301000','2024-05-01 22:18:41.321000','',_binary '','Giày Thể Thao Nam Hunter Core HSM000500',737000,'giay-the-thao-nam-hunter-core-hsm000500','95c2f6f6-2659-4e5d-872b-85fe870a24b2.webp',9,1),(31,'2024-05-01 22:20:20.033000','2024-05-01 23:19:54.349000','',_binary '','Giày Thể Thao Nam Wavy Collection HSM001400',981000,'giay-the-thao-nam-wavy-collection-hsm001400','aa61da22-85be-48b6-9e7b-d2248c67a008.webp',6,1),(32,'2024-05-01 22:26:00.456000','2024-05-01 22:26:00.478000','',_binary '','Giày Bé Trai Marvel BSB002398',609000,'giay-be-trai-marvel-bsb002398','f5472316-d31b-4e65-ab38-5952cc45cacd.webp',8,4),(33,'2024-05-01 22:27:15.514000','2024-05-01 22:27:15.520000','',_binary '','Giày Thể Thao Bé Gái Hunter DSGH00300',575000,'giay-the-thao-be-gai-hunter-dsgh00300','e2d85c16-990e-4531-ba9a-062cc7b9bfff.webp',2,5),(34,'2024-05-01 22:28:58.103000','2024-05-01 22:28:58.115000','',_binary '','Giày Thể Thao Bé Gái DSG004500',413000,'giay-the-thao-be-gai-dsg004500','2bf2efc7-59bd-4e07-9def-885614df0a18.webp',1,5),(35,'2024-05-01 22:30:51.798000','2024-05-01 22:30:51.835000','',_binary '','Giày Thể Thao Nam Mid Americano DSMH03600',756000,'giay-the-thao-nam-mid-americano-dsmh03600','af1074c1-8808-4024-88da-2c158e3ee620.webp',11,3);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `receipt_details`
--

DROP TABLE IF EXISTS `receipt_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `receipt_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `price` bigint DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `product_details_id` bigint DEFAULT NULL,
  `receipt_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKolael09bv0lm95ajv0gfwf34t` (`product_details_id`),
  KEY `FKgg9qo0w1vjcu9ridx36dyrhn2` (`receipt_id`),
  CONSTRAINT `FKgg9qo0w1vjcu9ridx36dyrhn2` FOREIGN KEY (`receipt_id`) REFERENCES `receipts` (`id`),
  CONSTRAINT `FKolael09bv0lm95ajv0gfwf34t` FOREIGN KEY (`product_details_id`) REFERENCES `product_details` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=283 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `receipt_details`
--

LOCK TABLES `receipt_details` WRITE;
/*!40000 ALTER TABLE `receipt_details` DISABLE KEYS */;
INSERT INTO `receipt_details` VALUES (1,300000,10,595,1),(2,300000,12,596,1),(3,300000,13,610,1),(4,300000,10,611,1),(5,300000,11,627,1),(6,300000,9,624,1),(7,300000,7,599,1),(8,300000,15,625,1),(9,300000,8,608,1),(10,300000,5,609,1),(11,300000,6,622,1),(12,300000,7,603,1),(13,300000,4,607,1),(14,300000,6,620,1),(15,300000,5,364,2),(16,300000,6,366,2),(17,300000,7,369,2),(18,300000,8,372,2),(19,300000,10,373,2),(20,300000,10,376,2),(21,300000,9,382,2),(22,300000,5,384,2),(23,300000,4,391,2),(24,300000,8,392,2),(25,300000,10,389,2),(26,300000,30,387,2),(27,300000,12,379,2),(28,300000,1,375,2),(29,230000,10,551,3),(30,230000,9,553,3),(31,230000,8,555,3),(32,230000,7,557,3),(33,230000,6,559,3),(34,230000,5,563,3),(35,230000,4,565,3),(36,230000,5,567,3),(37,230000,9,571,3),(38,230000,11,572,3),(39,220000,5,243,4),(40,220000,7,246,4),(41,220000,10,248,4),(42,220000,13,251,4),(43,220000,5,247,4),(44,220000,8,253,4),(45,220000,10,254,4),(46,220000,9,259,4),(47,220000,8,260,4),(48,220000,7,263,4),(49,220000,12,256,4),(50,350000,10,191,5),(51,350000,16,192,5),(52,350000,5,190,5),(53,350000,6,196,5),(54,350000,6,199,5),(55,350000,7,203,5),(56,350000,5,206,5),(57,350000,5,208,5),(58,350000,5,218,5),(59,350000,5,212,5),(60,400000,5,576,6),(61,400000,6,583,6),(62,400000,7,574,6),(63,400000,6,581,6),(64,800000,5,423,7),(65,800000,6,424,7),(66,800000,10,422,7),(67,800000,8,421,7),(68,800000,6,425,7),(69,800000,7,432,7),(70,800000,7,433,7),(71,800000,10,434,7),(72,600000,5,509,8),(73,600000,5,510,8),(74,600000,10,511,8),(75,600000,10,512,8),(76,600000,5,520,8),(77,600000,6,523,8),(78,600000,5,524,8),(79,800000,5,676,9),(80,800000,6,677,9),(81,800000,7,678,9),(82,800000,10,679,9),(83,800000,10,680,9),(84,800000,5,682,9),(85,700000,10,401,10),(86,700000,10,402,10),(87,700000,10,403,10),(88,700000,9,404,10),(89,700000,9,416,10),(90,700000,9,417,10),(91,100000,5,122,11),(92,100000,6,123,11),(93,100000,9,135,11),(94,100000,6,133,11),(95,100000,7,136,11),(96,100000,10,124,11),(97,100000,5,126,11),(98,100000,10,134,11),(99,700000,5,655,12),(100,700000,5,654,12),(101,700000,5,656,12),(102,700000,7,657,12),(103,700000,5,665,12),(104,700000,8,666,12),(105,700000,7,667,12),(106,900000,5,488,13),(107,900000,8,490,13),(108,900000,5,493,13),(109,900000,10,491,13),(110,350000,5,342,14),(111,350000,10,343,14),(112,350000,10,344,14),(113,350000,5,355,14),(114,350000,5,353,14),(115,350000,9,354,14),(116,400000,5,531,15),(117,400000,8,532,15),(118,400000,5,533,15),(119,400000,8,544,15),(120,400000,10,542,15),(121,650000,5,721,16),(122,650000,5,722,16),(123,650000,10,723,16),(124,650000,10,720,16),(125,650000,15,724,16),(126,350000,5,705,17),(127,350000,5,706,17),(128,350000,5,707,17),(129,350000,5,708,17),(130,350000,5,709,17),(131,350000,5,710,17),(132,500000,5,694,18),(133,500000,10,695,18),(134,500000,10,696,18),(135,500000,5,697,18),(136,100000,5,12,19),(137,100000,8,13,19),(138,100000,10,14,19),(139,100000,10,25,19),(140,100000,10,24,19),(141,100000,5,23,19),(142,100000,5,26,19),(143,1500000,5,6,20),(144,1500000,5,7,20),(145,1500000,8,8,20),(146,1500000,4,9,20),(147,1500000,3,10,20),(148,300000,5,36,21),(149,300000,5,37,21),(150,300000,5,38,21),(151,300000,2,50,21),(152,300000,6,48,21),(153,300000,5,51,21),(154,400000,5,78,22),(155,400000,3,79,22),(156,400000,2,80,22),(157,400000,5,81,22),(158,350000,5,59,23),(159,350000,3,60,23),(160,350000,5,58,23),(161,350000,7,61,23),(162,350000,5,69,23),(163,350000,3,71,23),(164,150000,5,221,24),(165,150000,3,222,24),(166,150000,3,223,24),(167,150000,5,224,24),(168,150000,5,225,24),(169,150000,6,226,24),(170,150000,5,232,24),(171,150000,3,236,24),(172,150000,5,237,24),(173,300000,5,144,25),(174,300000,3,145,25),(175,300000,2,146,25),(176,300000,2,147,25),(177,300000,5,155,25),(178,300000,2,158,25),(179,300000,2,159,25),(180,100000,5,166,26),(181,100000,2,167,26),(182,100000,2,168,26),(183,100000,2,181,26),(184,100000,5,182,26),(185,100000,2,177,26),(186,700000,5,292,27),(187,700000,2,293,27),(188,700000,3,296,27),(189,700000,3,306,27),(190,700000,3,308,27),(191,1000000,5,466,28),(192,1000000,3,468,28),(193,1000000,3,469,28),(194,1000000,5,473,28),(195,1200000,5,478,29),(196,1200000,2,479,29),(197,1200000,10,483,29),(198,1200000,15,481,29),(199,1200000,5,482,29),(200,500000,5,313,30),(201,500000,5,314,30),(202,500000,10,336,30),(203,500000,5,338,30),(204,500000,15,335,30),(205,500000,5,318,30),(206,250000,5,268,31),(207,250000,15,270,31),(208,250000,10,280,31),(209,250000,15,281,31),(210,250000,5,271,31),(211,250000,10,267,31),(212,500000,5,448,32),(213,500000,10,459,32),(214,500000,5,450,32),(215,500000,10,446,32),(216,500000,8,457,32),(217,500000,15,441,32),(218,500000,20,449,32),(219,200000,5,631,33),(220,200000,25,632,33),(221,200000,20,643,34),(222,200000,15,644,34),(223,200000,20,645,34),(224,200000,5,631,34),(225,200000,20,636,34),(226,120000,20,364,35),(227,350000,20,555,35),(228,350000,20,197,35),(229,150000,20,243,36),(230,350000,20,254,36),(231,350000,20,536,36),(232,200000,20,243,37),(233,350000,20,192,37),(234,350000,20,188,37),(235,850000,20,197,38),(236,350000,20,193,38),(237,300000,20,220,39),(238,350000,20,543,39),(239,350000,20,540,39),(240,750000,20,446,40),(241,350000,20,357,40),(242,500000,20,722,41),(243,450000,20,181,41),(244,350054,20,146,41),(245,213545,20,199,42),(246,350000,20,193,42),(247,350000,20,694,42),(248,321321,20,691,43),(249,350000,20,243,43),(250,350000,20,699,43),(251,350000,20,26,43),(252,350000,20,148,43),(253,350000,20,444,43),(254,350000,20,456,43),(255,350000,20,682,43),(256,250000,20,479,44),(257,350000,20,697,44),(258,350000,20,126,44),(259,356000,20,484,45),(260,500000,20,547,45),(261,120000,20,134,45),(262,500000,20,327,45),(263,450000,20,622,46),(264,350000,20,553,46),(265,350000,20,720,46),(266,650000,20,406,46),(267,350000,20,461,46),(268,450000,20,210,47),(269,350000,20,573,47),(270,350000,20,725,47),(271,350000,15,697,47),(272,350000,20,695,47),(273,350000,45,694,47),(274,250000,20,122,48),(275,350000,35,538,48),(276,350000,20,546,48),(277,1200000,20,656,49),(278,1200000,25,446,49),(279,1200000,15,484,49),(280,1200000,20,459,50),(281,500000,15,10,50),(282,1200000,20,223,50);
/*!40000 ALTER TABLE `receipt_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `receipts`
--

DROP TABLE IF EXISTS `receipts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `receipts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `employee_id` bigint DEFAULT NULL,
  `supplier_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjt1sorl99fbaiimf1ygo10fdn` (`employee_id`),
  KEY `FK4ksphcrrl2epyxvdqwo0gat9d` (`supplier_id`),
  CONSTRAINT `FK4ksphcrrl2epyxvdqwo0gat9d` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`),
  CONSTRAINT `FKjt1sorl99fbaiimf1ygo10fdn` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `receipts`
--

LOCK TABLES `receipts` WRITE;
/*!40000 ALTER TABLE `receipts` DISABLE KEYS */;
INSERT INTO `receipts` VALUES (1,'2021-06-01 22:57:54.796000','2024-04-01 22:57:54.881000',1111111111111,3),(2,'2021-05-01 23:02:17.194000','2024-05-01 23:02:17.243000',1111111111111,1),(3,'2021-12-01 23:04:58.438000','2024-05-01 23:04:58.462000',1111111111111,2),(4,'2021-11-01 23:08:43.789000','2024-04-01 23:08:43.821000',1714561478492,4),(5,'2024-04-01 23:12:49.513000','2024-05-01 23:12:49.542000',1714561478492,1),(6,'2024-04-01 23:14:23.355000','2024-05-01 23:14:23.369000',1714561478492,4),(7,'2022-01-01 23:16:29.995000','2024-06-01 23:16:30.018000',1714561478492,1),(8,'2021-03-01 23:18:54.718000','2024-05-01 23:18:54.745000',1714561577522,4),(9,'2024-01-01 23:22:04.157000','2024-05-01 23:22:04.167000',1111111111111,2),(10,'2024-03-01 23:29:18.046000','2024-06-01 23:29:18.076000',1111111111111,2),(11,'2023-05-01 23:34:44.465000','2024-03-01 23:34:44.495000',1111111111111,4),(12,'2022-03-01 23:47:02.922000','2024-05-01 23:47:03.045000',1111111111111,3),(13,'2023-02-01 23:48:58.705000','2024-05-01 23:48:58.726000',1714561577522,4),(14,'2022-02-01 23:50:59.211000','2023-09-01 23:50:59.232000',1714561577522,4),(15,'2022-03-01 23:57:05.544000','2024-05-01 23:57:05.572000',1714561577522,1),(16,'2023-05-02 00:01:08.169000','2024-05-02 00:01:08.190000',1714561577522,2),(17,'2023-03-02 00:05:42.855000','2024-05-02 00:05:42.885000',1714561577522,3),(18,'2022-05-02 00:07:30.367000','2024-05-02 00:07:30.381000',1714561577522,4),(19,'2023-01-02 00:10:39.427000','2024-05-02 00:10:39.457000',1111111111111,2),(20,'2021-10-02 00:15:13.703000','2024-05-02 00:15:13.733000',1111111111111,1),(21,'2021-09-02 00:16:54.953000','2024-05-02 00:16:54.973000',1111111111111,3),(22,'2023-05-02 00:23:30.792000','2024-01-02 00:23:30.814000',1111111111111,4),(23,'2023-12-02 00:25:07.475000','2024-01-02 00:25:07.500000',1111111111111,4),(24,'2023-03-02 00:27:09.468000','2024-05-02 00:27:09.502000',1111111111111,1),(25,'2021-07-02 00:28:48.505000','2024-04-02 00:28:48.523000',1111111111111,3),(26,'2024-01-02 00:30:43.410000','2024-05-02 00:30:43.440000',1111111111111,3),(27,'2022-03-02 00:32:27.907000','2024-05-02 00:32:27.920000',1111111111111,2),(28,'2023-01-02 00:34:46.875000','2024-05-02 00:34:46.901000',1111111111111,2),(29,'2024-05-02 00:36:12.629000','2024-02-02 00:36:12.639000',1714561478492,1),(30,'2022-04-02 00:38:15.046000','2024-05-02 00:38:15.083000',1714561478492,3),(31,'2022-01-02 00:39:34.285000','2024-05-02 00:39:34.298000',1714561478492,4),(32,'2023-11-02 00:41:24.891000','2024-05-02 00:41:24.909000',1714561478492,2),(33,'2024-02-02 00:42:14.731000','2024-05-02 00:42:14.738000',1714561478492,3),(34,'2022-12-02 00:43:30.001000','2024-05-02 00:43:30.019000',1714561478492,4),(35,'2022-11-02 00:49:01.389000','2024-05-02 00:49:01.458000',1111111111111,1),(36,'2022-01-02 00:49:28.424000','2024-05-02 00:49:28.434000',1111111111111,2),(37,'2024-02-02 00:49:52.142000','2023-05-02 00:49:52.160000',1111111111111,2),(38,'2022-09-02 00:50:10.074000','2023-08-02 00:50:10.084000',1111111111111,3),(39,'2022-08-02 00:50:46.798000','2024-05-02 00:50:46.810000',1111111111111,2),(40,'2023-12-02 00:51:19.024000','2024-05-02 00:51:19.034000',1111111111111,4),(41,'2022-07-02 00:52:08.074000','2023-05-02 00:52:08.088000',1111111111111,2),(42,'2021-08-02 00:52:30.734000','2024-03-02 00:52:30.743000',1111111111111,4),(43,'2023-07-02 00:53:41.482000','2024-05-02 00:53:41.513000',1111111111111,4),(44,'2023-08-02 00:54:14.235000','2024-05-02 00:54:14.252000',1111111111111,1),(45,'2023-06-02 00:56:51.287000','2024-05-02 00:56:51.302000',1111111111111,4),(46,'2023-09-02 00:58:01.592000','2023-05-02 00:58:01.610000',1111111111111,3),(47,'2023-10-02 00:59:15.539000','2024-05-02 00:59:15.560000',1111111111111,2),(48,'2022-06-02 00:59:52.304000','2024-01-02 00:59:52.324000',1111111111111,4),(49,'2023-04-02 01:01:34.805000','2024-05-02 01:01:34.843000',1111111111111,4),(50,'2022-10-02 01:02:12.442000','2024-05-02 01:02:12.460000',1111111111111,4);
/*!40000 ALTER TABLE `receipts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `return_product_details`
--

DROP TABLE IF EXISTS `return_product_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `return_product_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` int DEFAULT NULL,
  `reason` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `return_type` bit(1) DEFAULT NULL,
  `product_details_id` bigint DEFAULT NULL,
  `return_product_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKonynx8m581mt6aho5hrk8i4au` (`product_details_id`),
  KEY `FKthl12ouxtaw08drs9adcg6i0l` (`return_product_id`),
  CONSTRAINT `FKonynx8m581mt6aho5hrk8i4au` FOREIGN KEY (`product_details_id`) REFERENCES `product_details` (`id`),
  CONSTRAINT `FKthl12ouxtaw08drs9adcg6i0l` FOREIGN KEY (`return_product_id`) REFERENCES `return_products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `return_product_details`
--

LOCK TABLES `return_product_details` WRITE;
/*!40000 ALTER TABLE `return_product_details` DISABLE KEYS */;
INSERT INTO `return_product_details` VALUES (1,1,'Lỗi nhà sản xuất',_binary '',655,1),(2,2,'Không ưng ý',_binary '',336,2),(3,3,'Chất lượng sản phẩm không như quảng cáo',_binary '',59,3),(4,3,'Kích cỡ không phù hợp',_binary '',576,4),(5,1,'Không ưng ý',_binary '',707,5),(6,1,'Không ưng ý',_binary '',490,6),(7,4,'Khách từ chối nhận hàng',_binary '',622,7),(8,1,'Khách từ chối nhận',_binary '',655,8),(9,2,NULL,_binary '\0',292,9),(10,1,'Không ưng ý',_binary '',665,10);
/*!40000 ALTER TABLE `return_product_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `return_products`
--

DROP TABLE IF EXISTS `return_products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `return_products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `reason` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `status` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `employee_id` bigint DEFAULT NULL,
  `order_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcxyrgfykvj7ofkhw7smxoroyu` (`employee_id`),
  KEY `FK4ncg3gnnd9f9wcadd80whdc89` (`order_id`),
  CONSTRAINT `FK4ncg3gnnd9f9wcadd80whdc89` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKcxyrgfykvj7ofkhw7smxoroyu` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `return_products`
--

LOCK TABLES `return_products` WRITE;
/*!40000 ALTER TABLE `return_products` DISABLE KEYS */;
INSERT INTO `return_products` VALUES (1,'2024-05-02 03:28:05.989000','2024-05-02 03:28:06.026000',NULL,'RETURN_APPROVED',1111111111111,2),(2,'2024-05-02 03:29:16.594000','2024-05-02 03:29:16.598000',NULL,'RETURN_APPROVED',1111111111111,16),(3,'2024-05-02 03:30:21.088000','2024-05-02 03:31:17.765000','Không chấp nhận lý do này','RETURN_REJECTED',1111111111111,32),(4,'2024-05-02 09:32:44.784000','2024-05-02 20:39:04.277000','Không thích','RETURN_REJECTED',1111111111111,28),(5,'2024-05-02 20:40:50.529000','2024-05-02 20:41:37.385000',NULL,'RETURN_APPROVED',1111111111111,3),(6,'2024-05-02 20:43:45.363000','2024-05-02 20:43:45.372000',NULL,'RETURN_APPROVED',1111111111111,30),(7,'2024-05-02 23:34:00.996000','2024-05-02 23:34:01.057000',NULL,'RETURN_APPROVED',1111111111111,1),(8,'2024-05-02 23:34:44.340000','2024-05-02 23:34:44.351000',NULL,'RETURN_APPROVED',1111111111111,2),(9,'2024-05-03 02:33:32.343000','2024-05-03 02:33:32.402000',NULL,'RETURN_APPROVED',1111111111111,8),(10,'2024-05-03 04:19:45.994000','2024-05-03 04:19:45.994000',NULL,'RETURN_PENDING',NULL,35);
/*!40000 ALTER TABLE `return_products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_EMPLOYEE'),(3,'ROLE_USER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sale_product`
--

DROP TABLE IF EXISTS `sale_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sale_product` (
  `sale_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  KEY `FKo7f4v8r1qnpggq7h0y0erj20h` (`product_id`),
  KEY `FK9r7nw142wp5qccenjf2k2uyme` (`sale_id`),
  CONSTRAINT `FK9r7nw142wp5qccenjf2k2uyme` FOREIGN KEY (`sale_id`) REFERENCES `sales` (`id`),
  CONSTRAINT `FKo7f4v8r1qnpggq7h0y0erj20h` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sale_product`
--

LOCK TABLES `sale_product` WRITE;
/*!40000 ALTER TABLE `sale_product` DISABLE KEYS */;
INSERT INTO `sale_product` VALUES (1,1),(1,3),(1,6),(2,2),(2,5),(2,8),(2,15),(2,26),(3,22),(3,30),(3,34);
/*!40000 ALTER TABLE `sale_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `discount` int DEFAULT NULL,
  `end_date` datetime(6) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `start_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
INSERT INTO `sales` VALUES (1,'2024-05-01 20:17:30.100000','2024-05-01 20:17:30.100000',15,'2024-05-03 23:59:59.000000','Khuyễn mãi 30/4 - 1/5 giảm giá 15%','2024-04-28 00:00:00.000000'),(2,'2024-05-01 22:34:08.296000','2024-05-01 22:34:08.296000',20,'2024-11-25 23:59:59.000000','Tưng bừng khuyến mãi Black Friday 2024','2024-11-08 00:00:00.000000'),(3,'2024-05-01 22:35:23.342000','2024-05-01 22:35:23.342000',10,'2024-05-10 23:59:59.000000','Mừng sinh nhật Shoes Station 4 năm thành lập','2024-05-03 00:00:00.000000');
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `suppliers`
--

DROP TABLE IF EXISTS `suppliers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suppliers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `address` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suppliers`
--

LOCK TABLES `suppliers` WRITE;
/*!40000 ALTER TABLE `suppliers` DISABLE KEYS */;
INSERT INTO `suppliers` VALUES (1,'2024-05-01 18:07:21.970000','2024-05-01 18:07:21.970000','Gia Lâm, Hà Nội','Nike Inc','0359456895'),(2,'2024-05-01 18:08:22.404000','2024-05-01 18:08:22.404000','TP Nam Định, Tỉnh Nam Định','Công ty TNHH giày Nam Định','0369876665'),(3,'2024-05-01 18:09:02.634000','2024-05-01 18:09:02.634000','HongKong, Trung Quốc','TaoBao','0989465656'),(4,'2024-05-01 22:32:33.577000','2024-05-01 22:32:33.577000','Gia Lâm, Hà Nội','Adidas VietNam Inc','0956495942');
/*!40000 ALTER TABLE `suppliers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-10  1:19:05
