-- MySQL dump 10.13  Distrib 8.0.46, for Linux (aarch64)
--
-- Host: localhost    Database: fruitdb
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `fruit`
--

DROP TABLE IF EXISTS `fruit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fruit` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `region` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `price` bigint DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `image_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fruit`
--

LOCK TABLES `fruit` WRITE;
/*!40000 ALTER TABLE `fruit` DISABLE KEYS */;
INSERT INTO `fruit` VALUES (2,'マンゴー','南部',700,35,'甘くてジューシーな人気のフルーツです。','images/xoai.jpg'),(3,'ドラゴンフルーツ','南部',500,50,'ベトナムで人気の南国フルーツ。','images/thanhlong.jpg'),(4,'ランブータン','南部',900,25,'赤いとげのような皮が特徴の甘いフルーツです。','/images/mangcut.jpg'),(5,'モモ','北部',800,30,'やわらかくて甘みのある人気の果物です。','images/dao.jpg'),(6,'スモモ','北部',700,40,'甘酸っぱくてさわやかな味の果物です。','images/man.jpg'),(7,'オレンジ','北部',500,60,'ビタミンCが豊富でジューシーな果物です。','images/cam.jpg'),(8,'アボカド','中部',300,45,'クリーミーな食感で栄養が豊富なフルーツです。','images/bo.jpg'),(9,'バナナ','中部',350,80,'やわらかくて食べやすいフルーツです。','images/chuoi.jpg'),(10,'ジャックフルーツ','中部',2000,18,'大きくて甘い香りが特徴の南国フルーツです。','/images/mit.jpg'),(12,'quahongxiem','中部',150,12,'undefined','/images/hongxiem.jpg'),(13,'quabuoi','南部',450,10,'nhieu vitamin c','/images/quabuoi.jpg');
/*!40000 ALTER TABLE `fruit` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-17 15:13:31
