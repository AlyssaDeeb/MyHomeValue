CREATE DATABASE IF NOT EXISTS `myhomevalue`;
USE myhomevalue;


DROP TABLE IF EXISTS `comparables_cache`;
DROP TABLE IF EXISTS `homes_cache`;
DROP TABLE IF EXISTS `saved_homes`;
DROP TABLE IF EXISTS `search_history`;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(254) DEFAULT NULL,
  `password` varchar(25) DEFAULT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `phoneNumber` varchar(15) DEFAULT NULL,
  `gender` varchar(1) DEFAULT NULL,
  `street1` varchar(254) DEFAULT NULL,
  `street2` varchar(254) DEFAULT NULL,
  `city` varchar(254) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  `zip` varchar(5) DEFAULT NULL,
  `ip_address` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10101 DEFAULT CHARSET=latin1;

 CREATE TABLE `saved_homes` (
  `saved_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` int(11) NOT NULL,
  `home_id` varchar(25) NOT NULL,
  `address` varchar(150) NOT NULL,
  `bed` int NOT NULL,
  `bath` double NOT NULL, 
  `sq_ft` int NOT NULL,
  `image_url` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`home_id`, `user_id`),
  CONSTRAINT `saved_homes_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `search_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `home_id` varchar(25) NOT NULL,
  `search_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY `id` (`id`),
  CONSTRAINT `search_history_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `homes_cache` (
  `cache_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `insert_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `home_id` varchar(25) NOT NULL,
  `addressLine` varchar(100) NOT NULL,
  `city` varchar(50) NOT NULL,
  `state` varchar(20) NOT NULL,
  `zip` varchar(10) NOT NULL,
  `value` int NOT NULL,
  `type` varchar(50) DEFAULT NULL,
  `yearBuilt` int DEFAULT NULL,
  `bedrooms` int DEFAULT NULL,
  `bathrooms` double DEFAULT NULL, 
  `squareFeet` int DEFAULT NULL,
  `acres` double DEFAULT NULL, 
  `stories` int DEFAULT NULL, 
  `parkingSpaces` int DEFAULT NULL, 
  `latitude` double DEFAULT NULL, 
  `longitude` double DEFAULT NULL,
  `url` varchar(150),
  `soldYear` int DEFAULT NULL,
  `soldValue` int DEFAULT NULL,
  `growthRate` varchar(7) DEFAULT NULL,
  `eleSchoolRating` int DEFAULT NULL,
  `eleSchoolName` varchar(100) DEFAULT NULL,
  `midSchoolRating` int DEFAULT NULL,
  `midSchoolName` varchar(100) DEFAULT NULL,
  `highSchoolRating` int DEFAULT NULL,
  `highSchoolName` varchar(100) DEFAULT NULL,
  `walkscore` int DEFAULT NULL,
  `walkscore_desc` varchar(100) DEFAULT NULL,
  `transitscore` int DEFAULT NULL,
  `transitscore_desc` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`home_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10101 DEFAULT CHARSET=latin1;

CREATE TABLE `comparables_cache` (
  `home_id` varchar(25) NOT NULL,
  `cache_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `insert_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `shortAddress` varchar(100) NOT NULL,
  `price` int NOT NULL,
  `squareFeet` int DEFAULT NULL,
  `bedrooms` int DEFAULT NULL,
  `bathrooms` int DEFAULT NULL,
  `lat` double DEFAULT NULL,
  `long` double DEFAULT NULL,
  PRIMARY KEY (`home_id`, `shortAddress`),
  CONSTRAINT `comparables_cache_ibfk_1` FOREIGN KEY (`home_id`) REFERENCES `homes_cache` (`home_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10101 DEFAULT CHARSET=latin1;


DROP TRIGGER IF EXISTS full_cache;

delimiter $$

CREATE TRIGGER full_cache BEFORE INSERT ON `homes_cache`
FOR EACH ROW BEGIN
	IF((SELECT COUNT(*) FROM `homes_cache`) = 50) THEN
		DELETE FROM `homes_cache` WHERE `home_id`= (SELECT `home_id` FROM `homes_cache` ORDER By `insert_date` ASC LIMIT 1);
	END IF;
END;

$$

DROP TRIGGER IF EXISTS full_comp_cache;

delimiter $$

CREATE TRIGGER full_comp_cache BEFORE INSERT ON `comparables_cache`
FOR EACH ROW BEGIN
	IF((SELECT COUNT(*) FROM `homes_cache`) = 500) THEN
		DELETE FROM `comparables_cache` WHERE `shortAddress`= (SELECT `shortAddress` FROM `comparables_cache` ORDER By `cache_date` ASC LIMIT 1);
	END IF;
END;

$$
