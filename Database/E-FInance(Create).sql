CREATE DATABASE IF NOT EXISTS `E-FinanseDB`;
USE `E-FinanseDB`;


CREATE TABLE IF NOT EXISTS `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL UNIQUE,
  `description` varchar(200),
  PRIMARY KEY (`id`)
);


INSERT INTO `category`
VALUES 
(1,'General',NULL),
(2,'Food',NULL),
(3,'Clothes',NULL),
(4,'Entertainment',NULL),
(5,'Services',NULL),
(6,'Transport',NULL);

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) UNIQUE NOT NULL AUTO_INCREMENT,
  `first_name` varchar(30),
  `last_name` varchar(30),
  `login` varchar(30) UNIQUE NOT NULL,
  `password` varchar(500) NOT NULL,
  `email` varchar(40),
  PRIMARY KEY (`id`)
);


CREATE TABLE IF NOT EXISTS `limits` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `month` int(11),
  `year` year(4) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE IF NOT EXISTS `expenses` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `amount` double(10,2),
  `category_id` int(11) NOT NULL,
  `spendingtime` date,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
);

ALTER TABLE `expenses` CHANGE `spendingtime` `time` date;
