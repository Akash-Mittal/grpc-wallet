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
  `currency_id` int(11) NOT NULL AUTO_INCREMENT,
  `currency_val` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`currency_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bp_currency`
--

LOCK TABLES `bp_currency` WRITE;
/*!40000 ALTER TABLE `bp_currency` DISABLE KEYS */;
INSERT INTO `bp_currency` VALUES (1,'USD');
/*!40000 ALTER TABLE `bp_currency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bp_user`
--

DROP TABLE IF EXISTS `bp_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `bp_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bp_user`
--

LOCK TABLES `bp_user` WRITE;
/*!40000 ALTER TABLE `bp_user` DISABLE KEYS */;
INSERT INTO `bp_user` VALUES (1,NULL);
/*!40000 ALTER TABLE `bp_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bp_user_currency`
--

DROP TABLE IF EXISTS `bp_user_currency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `bp_user_currency` (
  `user_currency_id` int(11) NOT NULL AUTO_INCREMENT,
  `currency_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_currency_id`),
  KEY `FK_eu9rybh7u0sqk99ooym123556` (`currency_id`),
  KEY `FK_hwbw1arry2jcl5l502jofjp2b` (`user_id`),
  CONSTRAINT `FK_eu9rybh7u0sqk99ooym123556` FOREIGN KEY (`currency_id`) REFERENCES `bp_currency` (`currency_id`),
  CONSTRAINT `FK_hwbw1arry2jcl5l502jofjp2b` FOREIGN KEY (`user_id`) REFERENCES `bp_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bp_user_currency`
--

LOCK TABLES `bp_user_currency` WRITE;
/*!40000 ALTER TABLE `bp_user_currency` DISABLE KEYS */;
/*!40000 ALTER TABLE `bp_user_currency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bp_user_wallet`
--

DROP TABLE IF EXISTS `bp_user_wallet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `bp_user_wallet` (
  `user_wallet_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_balance` float DEFAULT NULL,
  `user_currency_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_wallet_id`),
  KEY `FK_pyvdoi4q1boy438lwfu52kspp` (`user_currency_id`),
  CONSTRAINT `FK_pyvdoi4q1boy438lwfu52kspp` FOREIGN KEY (`user_currency_id`) REFERENCES `bp_user_currency` (`user_currency_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bp_user_wallet`
--

LOCK TABLES `bp_user_wallet` WRITE;
/*!40000 ALTER TABLE `bp_user_wallet` DISABLE KEYS */;
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

-- Dump completed on 2018-09-16 23:17:25
