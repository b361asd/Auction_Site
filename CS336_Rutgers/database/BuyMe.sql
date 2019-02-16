DROP DATABASE IF EXISTS BuyMe;
CREATE DATABASE IF NOT EXISTS BuyMe;
USE BuyMe;



DROP USER IF EXISTS 'cs336'@'%';
CREATE USER 'cs336'@'%' IDENTIFIED BY 'cs336_password';
GRANT ALL PRIVILEGES ON BuyMe.* TO 'cs336'@'%';
FLUSH PRIVILEGES;



DROP TABLE IF EXISTS User;
CREATE TABLE User
(
   username  VARCHAR(64)  NOT NULL,
   password  VARCHAR(64)  NOT NULL,
   email     VARCHAR(128) NOT NULL,
   firstname VARCHAR(64)  NOT NULL,
   lastname  VARCHAR(64)  NOT NULL,
   address   VARCHAR(128) NOT NULL,
   phone     VARCHAR(32)  NOT NULL,
   active    BOOLEAN      NOT NULL,
   usertype  INT          NOT NULL,
   --
   PRIMARY KEY (username)
);



DROP TABLE IF EXISTS Category;
CREATE TABLE Category
(
   categoryName VARCHAR(64) NOT NULL,
   --
   PRIMARY KEY (categoryName)
);


DROP TABLE IF EXISTS Field;
CREATE TABLE Field
(
   fieldID   INT AUTO_INCREMENT,
   fieldName VARCHAR(64) NOT NULL,
   fieldType INT, -- 1 string 2 int 3 boolean
   --
   PRIMARY KEY (fieldID)
);


DROP TABLE IF EXISTS CategoryField;
CREATE TABLE CategoryField
(
   categoryName VARCHAR(64) NOT NULL,
   fieldID      INT,
   --
   FOREIGN KEY (categoryName) REFERENCES Category (categoryName) ON DELETE CASCADE,
   FOREIGN KEY (fieldID) REFERENCES Field (fieldID) ON DELETE CASCADE,
   PRIMARY KEY (categoryName, fieldID)
);


DROP TABLE IF EXISTS Offer;
CREATE TABLE Offer
(
   offerId      INT AUTO_INCREMENT,
   categoryName VARCHAR(64)    NOT NULL,
   --
   seller       VARCHAR(64)    NOT NULL,
   min_price    DECIMAL(20, 2) NOT NULL,
   startDate    DATETIME,
   endDate      DATETIME,
   status       INT, -- 0:Active, 1:Withdrawal, 2:Completed, 3:NoBid
   --
   FOREIGN KEY (seller) REFERENCES User (username) ON DELETE CASCADE,
   PRIMARY KEY (offerId)
);



DROP TABLE IF EXISTS OfferField;
CREATE TABLE OfferField
(
   offerId   INT AUTO_INCREMENT,
   fieldID   INT,
   --
   fieldText VARCHAR(64) NOT NULL,
   --
   FOREIGN KEY (offerId) REFERENCES Offer (offerId) ON DELETE CASCADE,
   FOREIGN KEY (fieldID) REFERENCES Field (fieldID) ON DELETE CASCADE,
   PRIMARY KEY (offerId, fieldID)
);



DROP TABLE IF EXISTS Bid;
CREATE TABLE Bid
(
   bidId             INT AUTO_INCREMENT,
   offerId           INT,
   buyer             VARCHAR(64),
   price             DECIMAL(20, 2),
   isAutoRebid       BOOLEAN,
   autoRebidLimit    DECIMAL(20, 2),
   autoRebidPriceInc DECIMAL(20, 2),
   date              DATETIME,
   --
   FOREIGN KEY (buyer) REFERENCES User (username) ON DELETE CASCADE,
   FOREIGN KEY (offerId) REFERENCES Offer (offerId) ON DELETE CASCADE,
   PRIMARY KEY (bidId)
);



DROP TABLE IF EXISTS Trade;
CREATE TABLE Trade
(
   tradeId INT AUTO_INCREMENT,
   offerId INT,
   bidId   INT,
   date    DATETIME,
   --
   FOREIGN KEY (offerId) REFERENCES Offer (offerId) ON DELETE CASCADE,
   FOREIGN KEY (bidId) REFERENCES Bid (bidId) ON DELETE CASCADE,
   PRIMARY KEY (tradeId)
);



DROP TABLE IF EXISTS Alert;
CREATE TABLE Alert
(
   alertId INT AUTO_INCREMENT,
   userId  VARCHAR(50)  NOT NULL,
   message VARCHAR(250) NOT NULL,
   seen    BOOLEAN DEFAULT FALSE,
   --
   FOREIGN KEY (userId) REFERENCES User (username) ON DELETE CASCADE,
   PRIMARY KEY (alertId)
);



DROP TABLE IF EXISTS Question;
CREATE TABLE Question
(
   questionId INT AUTO_INCREMENT,
   userId     VARCHAR(50),
   question   VARCHAR(250) NOT NULL,
   answer     VARCHAR(250) DEFAULT NULL,
   --
   FOREIGN KEY (userId) REFERENCES User (username) ON DELETE CASCADE,
   PRIMARY KEY (questionId)
);
