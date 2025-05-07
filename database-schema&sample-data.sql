CREATE DATABASE  IF NOT EXISTS `booking_system_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `booking_system_db`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: booking_system_db
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
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `booking_time` datetime(6) NOT NULL,
  `cancellation_time` datetime(6) DEFAULT NULL,
  `check_in_time` datetime(6) DEFAULT NULL,
  `checked_in` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `credit_refunded` bit(1) NOT NULL,
  `credits_used` int NOT NULL,
  `status` int DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `schedule_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKl2rdoocqp0clcwdcg7hiob7pm` (`schedule_id`),
  KEY `FKeyog2oic85xg7hsu2je2lx3s6` (`user_id`),
  CONSTRAINT `FKeyog2oic85xg7hsu2je2lx3s6` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKl2rdoocqp0clcwdcg7hiob7pm` FOREIGN KEY (`schedule_id`) REFERENCES `class_schedules` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `class_categories`
--

DROP TABLE IF EXISTS `class_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `class_categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `class_categories`
--

LOCK TABLES `class_categories` WRITE;
/*!40000 ALTER TABLE `class_categories` DISABLE KEYS */;
INSERT INTO `class_categories` VALUES (1,'2025-05-07 01:15:45.369588','Dance and rhythm training','Dance','2025-05-07 01:15:45.369588'),(2,'2025-05-07 01:15:45.396089','Programming and software development','Coding','2025-05-07 01:15:45.396089');
/*!40000 ALTER TABLE `class_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `class_schedules`
--

DROP TABLE IF EXISTS `class_schedules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `class_schedules` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `available_slots` int NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `end_time` datetime(6) NOT NULL,
  `location` varchar(255) NOT NULL,
  `start_time` datetime(6) NOT NULL,
  `status` int NOT NULL,
  `total_slots` int NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `class_id` bigint NOT NULL,
  `instructor_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKd4fidccuckfhabn3u91coqo6g` (`class_id`),
  KEY `FKaav7b30nwra2hycpgpue5m55` (`instructor_id`),
  CONSTRAINT `FKaav7b30nwra2hycpgpue5m55` FOREIGN KEY (`instructor_id`) REFERENCES `instructors` (`id`),
  CONSTRAINT `FKd4fidccuckfhabn3u91coqo6g` FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `class_schedules`
--

LOCK TABLES `class_schedules` WRITE;
/*!40000 ALTER TABLE `class_schedules` DISABLE KEYS */;
INSERT INTO `class_schedules` VALUES (1,25,'2025-05-07 01:15:45.467761','2025-05-11 11:30:45.465245','TH Dance Hall','2025-05-11 10:00:45.465245',1,25,'2025-05-07 01:15:45.467761',2,1),(2,30,'2025-05-07 01:15:45.473762','2025-05-12 05:00:45.473763','JP Tech Room','2025-05-12 03:00:45.473763',1,30,'2025-05-07 01:15:45.473762',1,2);
/*!40000 ALTER TABLE `class_schedules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `classes`
--

DROP TABLE IF EXISTS `classes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `classes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `required_credits` int NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `category_id` bigint NOT NULL,
  `country_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKh5by19d0jv86tfmqru9hdoack` (`category_id`),
  KEY `FKflwmbuqtdmdos7tpbytj36d50` (`country_id`),
  CONSTRAINT `FKflwmbuqtdmdos7tpbytj36d50` FOREIGN KEY (`country_id`) REFERENCES `countries` (`id`),
  CONSTRAINT `FKh5by19d0jv86tfmqru9hdoack` FOREIGN KEY (`category_id`) REFERENCES `class_categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `classes`
--

LOCK TABLES `classes` WRITE;
/*!40000 ALTER TABLE `classes` DISABLE KEYS */;
INSERT INTO `classes` VALUES (1,_binary '','2025-05-07 01:15:45.438108','Java programming fundamentals','Java Bootcamp',4,'2025-05-07 01:15:45.438108',2,3),(2,_binary '','2025-05-07 01:15:45.458115','Beginner salsa dancing class','Salsa Dance',2,'2025-05-07 01:15:45.458115',1,4);
/*!40000 ALTER TABLE `classes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `countries`
--

DROP TABLE IF EXISTS `countries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `countries` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `code` varchar(10) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `countries`
--

LOCK TABLES `countries` WRITE;
/*!40000 ALTER TABLE `countries` DISABLE KEYS */;
INSERT INTO `countries` VALUES (1,_binary '','SG','2025-05-07 01:15:45.412136','Singapore','2025-05-07 01:15:45.412136'),(2,_binary '','MM','2025-05-07 01:15:45.418168','Myanmar','2025-05-07 01:15:45.418168'),(3,_binary '','JP','2025-05-07 01:15:45.424488','Japan','2025-05-07 01:15:45.424488'),(4,_binary '','TH','2025-05-07 01:15:45.445110','Thailand','2025-05-07 01:15:45.445110');
/*!40000 ALTER TABLE `countries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instructors`
--

DROP TABLE IF EXISTS `instructors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `instructors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `bio` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `country_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKefl9vk0id3lolp7plludxeo0n` (`country_id`),
  CONSTRAINT `FKefl9vk0id3lolp7plludxeo0n` FOREIGN KEY (`country_id`) REFERENCES `countries` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instructors`
--

LOCK TABLES `instructors` WRITE;
/*!40000 ALTER TABLE `instructors` DISABLE KEYS */;
INSERT INTO `instructors` VALUES (1,_binary '','Certified trainer','2025-05-07 01:15:45.432424','johndoe@vinn.com','John Doe','2025-05-07 01:15:45.432424',3),(2,_binary '','Dance and fitness expert','2025-05-07 01:15:45.453111','jane@vinn.com','Jane Smith','2025-05-07 01:15:45.454111',4);
/*!40000 ALTER TABLE `instructors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `packages`
--

DROP TABLE IF EXISTS `packages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `packages` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `credits` int NOT NULL,
  `description` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `validity_days` int NOT NULL,
  `country_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKannqk4iqhf3tle7ydvus0nf5q` (`country_id`),
  CONSTRAINT `FKannqk4iqhf3tle7ydvus0nf5q` FOREIGN KEY (`country_id`) REFERENCES `countries` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `packages`
--

LOCK TABLES `packages` WRITE;
/*!40000 ALTER TABLE `packages` DISABLE KEYS */;
INSERT INTO `packages` VALUES (1,_binary '','2025-05-07 01:15:45.498190',5,'Basic package for SG','Basic Package SG',9.99,'2025-05-07 01:15:45.498190',30,1),(2,_binary '','2025-05-07 01:15:45.540157',10,'Premium package for SG','Premium Package SG',19.99,'2025-05-07 01:15:45.540157',60,1),(3,_binary '','2025-05-07 01:15:45.564145',4,'Basic package for MM','Basic Package MM',6.99,'2025-05-07 01:15:45.564145',30,2),(4,_binary '','2025-05-07 01:15:45.584147',8,'Premium package for MM','Premium Package MM',16.99,'2025-05-07 01:15:45.584147',60,2),(5,_binary '','2025-05-07 01:15:45.603323',5,'Basic package for JP','Basic Package JP',12.99,'2025-05-07 01:15:45.603323',30,3),(6,_binary '','2025-05-07 01:15:45.622362',5,'Basic package for TH','Basic Package TH',7.99,'2025-05-07 01:15:45.622362',30,4);
/*!40000 ALTER TABLE `packages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_cards`
--

DROP TABLE IF EXISTS `payment_cards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_cards` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `card_holder` varchar(255) NOT NULL,
  `card_number` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `cvv` varchar(255) NOT NULL,
  `is_default` bit(1) NOT NULL,
  `expiry_date` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_cards`
--

LOCK TABLES `payment_cards` WRITE;
/*!40000 ALTER TABLE `payment_cards` DISABLE KEYS */;
INSERT INTO `payment_cards` VALUES (1,'Alice','4111111111111101','2025-05-07 01:15:45.873446','111',_binary '','12/24','2025-05-07 01:15:45.873446'),(2,'Alice','4111111111111102','2025-05-07 01:15:45.880476','122',_binary '\0','12/25','2025-05-07 01:15:45.880476'),(3,'Bob','4111111111111101','2025-05-07 01:15:45.909734','111',_binary '','12/24','2025-05-07 01:15:45.909734'),(4,'Bob','4111111111111102','2025-05-07 01:15:45.914243','122',_binary '\0','12/25','2025-05-07 01:15:45.914243'),(5,'Charlie','4111111111111101','2025-05-07 01:15:45.961903','111',_binary '','12/24','2025-05-07 01:15:45.961903'),(6,'Charlie','4111111111111102','2025-05-07 01:15:45.968987','122',_binary '\0','12/25','2025-05-07 01:15:45.968987');
/*!40000 ALTER TABLE `payment_cards` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_transactions`
--

DROP TABLE IF EXISTS `payment_transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_transactions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(10,2) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `payment_method` varchar(255) NOT NULL,
  `status` int NOT NULL,
  `transaction_date` datetime(6) NOT NULL,
  `transaction_reference` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `package_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjkt1s2jjukonupxe155s2x0em` (`package_id`),
  CONSTRAINT `FKjkt1s2jjukonupxe155s2x0em` FOREIGN KEY (`package_id`) REFERENCES `packages` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_transactions`
--

LOCK TABLES `payment_transactions` WRITE;
/*!40000 ALTER TABLE `payment_transactions` DISABLE KEYS */;
INSERT INTO `payment_transactions` VALUES (1,9.99,'2025-05-07 01:15:45.896986','CARD',1,'2025-05-02 01:15:45.895985','TXN-effafebc-d8f4-4d48-901b-7a2d2e92097b','2025-05-07 01:15:45.896986',1),(2,19.99,'2025-05-07 01:15:45.905998','CARD',1,'2025-05-01 01:15:45.904992','TXN-7d22ebe8-fb76-4818-8bff-21ae8262b1a8','2025-05-07 01:15:45.905998',2),(3,9.99,'2025-05-07 01:15:45.925365','CARD',1,'2025-05-02 01:15:45.924753','TXN-3a60bc4e-8616-4705-bfb8-dde85fb2e6b7','2025-05-07 01:15:45.925365',1),(4,19.99,'2025-05-07 01:15:45.953902','CARD',1,'2025-05-01 01:15:45.951904','TXN-7b4d3980-66e5-4fe8-bdb1-9e696891c7aa','2025-05-07 01:15:45.953902',2),(5,9.99,'2025-05-07 01:15:45.993304','CARD',1,'2025-05-02 01:15:45.992293','TXN-6ac3055e-e086-4685-8ba2-5bdf64c16225','2025-05-07 01:15:45.993304',1),(6,19.99,'2025-05-07 01:15:46.006294','CARD',1,'2025-05-01 01:15:46.005296','TXN-fa7c7b3f-9ab5-4477-9ed4-b05ea4bdfd06','2025-05-07 01:15:46.006294',2);
/*!40000 ALTER TABLE `payment_transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_packages`
--

DROP TABLE IF EXISTS `user_packages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_packages` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `purchase_date` datetime(6) NOT NULL,
  `remaining_credits` int NOT NULL,
  `status` int NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `package_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKamf4ragc34vb3il9wxrwjxw7b` (`package_id`),
  KEY `FKlhj6dofmpikc4fp9nf3ciksdq` (`user_id`),
  CONSTRAINT `FKamf4ragc34vb3il9wxrwjxw7b` FOREIGN KEY (`package_id`) REFERENCES `packages` (`id`),
  CONSTRAINT `FKlhj6dofmpikc4fp9nf3ciksdq` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_packages`
--

LOCK TABLES `user_packages` WRITE;
/*!40000 ALTER TABLE `user_packages` DISABLE KEYS */;
INSERT INTO `user_packages` VALUES (1,'2025-05-07 01:15:45.890985','2025-05-02 01:15:45.889984',5,1,'2025-05-07 01:15:45.890985',1,1),(2,'2025-05-07 01:15:45.900984','2025-05-01 01:15:45.899984',10,1,'2025-05-07 01:15:45.900984',2,1),(3,'2025-05-07 01:15:45.921246','2025-05-02 01:15:45.920244',5,1,'2025-05-07 01:15:45.921246',1,2),(4,'2025-05-07 01:15:45.941895','2025-05-01 01:15:45.929736',10,1,'2025-05-07 01:15:45.941895',2,2),(5,'2025-05-07 01:15:45.985295','2025-05-02 01:15:45.984296',5,1,'2025-05-07 01:15:45.985295',1,3),(6,'2025-05-07 01:15:46.000295','2025-05-01 01:15:45.999300',10,1,'2025-05-07 01:15:46.000295',2,3);
/*!40000 ALTER TABLE `user_packages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `email_verified` bit(1) NOT NULL,
  `gender` int NOT NULL,
  `login_first_time` bit(1) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `status` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'2025-05-07 01:15:45.718473',NULL,'alice@vinn.com',_binary '',0,_binary '','Alice','$2a$10$YG2.F9iS/7/qNhz2xhqWA.iWESaM2JsUr6xb2gp5UN1G355ZseJYS',_binary '','2025-05-07 01:15:45.718473'),(2,'2025-05-07 01:15:45.795239',NULL,'bob@vinn.com',_binary '',1,_binary '','Bob','$2a$10$WjQoZDk4RLmLmCZ55Nhw8OqjTmQBhCt4dQo9HAEbSSZXMP5G5pByK',_binary '','2025-05-07 01:15:45.795239'),(3,'2025-05-07 01:15:45.869168',NULL,'charlie@vinn.com',_binary '',1,_binary '','Charlie','$2a$10$Ny7QvXqYMjv3uqAT78Tv3er.SG9WpSmEodZjWofB3.rt06mioDe7K',_binary '','2025-05-07 01:15:45.869168');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `waitlist`
--

DROP TABLE IF EXISTS `waitlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `waitlist` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `added_time` datetime(6) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `credits_reserved` int NOT NULL,
  `status` int NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `schedule_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `user_package_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKaxbbkyqtvuwjux2imq3pgaff1` (`schedule_id`),
  KEY `FKc99hy864betkwt5pdekgfxkk1` (`user_id`),
  KEY `FKsi60ttgmgxpkit4o8cwtjwgsq` (`user_package_id`),
  CONSTRAINT `FKaxbbkyqtvuwjux2imq3pgaff1` FOREIGN KEY (`schedule_id`) REFERENCES `class_schedules` (`id`),
  CONSTRAINT `FKc99hy864betkwt5pdekgfxkk1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKsi60ttgmgxpkit4o8cwtjwgsq` FOREIGN KEY (`user_package_id`) REFERENCES `user_packages` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `waitlist`
--

LOCK TABLES `waitlist` WRITE;
/*!40000 ALTER TABLE `waitlist` DISABLE KEYS */;
/*!40000 ALTER TABLE `waitlist` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-07  8:24:22
