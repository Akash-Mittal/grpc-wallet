CREATE DATABASE  IF NOT EXISTS `bp_wallet` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;
USE `bp_wallet`;
-- MySQL dump 10.13  Distrib 8.0.12, for Win64 (x86_64)
--
-- Host: 192.168.99.100    Database: bp_wallet
-- ------------------------------------------------------
-- Server version	8.0.12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bp_currency`
--

DROP TABLE IF EXISTS `bp_currency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `bp_currency` (
  `currency_id` varchar(45) NOT NULL,
  `currency_val` varchar(45) NOT NULL,
  PRIMARY KEY (`currency_id`),
  UNIQUE KEY `currency_val_UNIQUE` (`currency_val`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bp_currency`
--

LOCK TABLES `bp_currency` WRITE;
/*!40000 ALTER TABLE `bp_currency` DISABLE KEYS */;
INSERT INTO `bp_currency` VALUES ('34','EUR'),('35','GBP'),('33','USD');
/*!40000 ALTER TABLE `bp_currency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bp_user`
--

DROP TABLE IF EXISTS `bp_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `bp_user` (
  `user_id` varchar(45) NOT NULL,
  `user_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bp_user`
--

LOCK TABLES `bp_user` WRITE;
/*!40000 ALTER TABLE `bp_user` DISABLE KEYS */;
INSERT INTO `bp_user` VALUES ('44','MIKE'),('45','ADAM'),('46','John');
/*!40000 ALTER TABLE `bp_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bp_user_currency`
--

DROP TABLE IF EXISTS `bp_user_currency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `bp_user_currency` (
  `user_currency_id` varchar(45) NOT NULL,
  `user_id` varchar(45) DEFAULT NULL,
  `currency_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`user_currency_id`),
  KEY `fk-user-id_idx` (`user_id`),
  KEY `fk_currency_id_idx` (`currency_id`),
  CONSTRAINT `fk_currency_id` FOREIGN KEY (`currency_id`) REFERENCES `bp_currency` (`currency_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `bp_user` (`user_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bp_user_currency`
--

LOCK TABLES `bp_user_currency` WRITE;
/*!40000 ALTER TABLE `bp_user_currency` DISABLE KEYS */;
INSERT INTO `bp_user_currency` VALUES ('55','44','33'),('56','44','34'),('57','44','35');
/*!40000 ALTER TABLE `bp_user_currency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bp_user_wallet`
--

DROP TABLE IF EXISTS `bp_user_wallet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `bp_user_wallet` (
  `user_wallet_id` varchar(45) NOT NULL,
  `user_currency_id` varchar(45) DEFAULT NULL,
  `user_balance` float DEFAULT NULL,
  PRIMARY KEY (`user_wallet_id`),
  KEY `fk_user_currency_idx` (`user_currency_id`),
  CONSTRAINT `fk_user_currency` FOREIGN KEY (`user_currency_id`) REFERENCES `bp_user_currency` (`user_currency_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bp_user_wallet`
--

LOCK TABLES `bp_user_wallet` WRITE;
/*!40000 ALTER TABLE `bp_user_wallet` DISABLE KEYS */;
INSERT INTO `bp_user_wallet` VALUES ('66','55',99000),('67','56',4245),('68','57',7866);
/*!40000 ALTER TABLE `bp_user_wallet` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-09-13 13:34:39
