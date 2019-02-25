DROP DATABASE IF EXISTS cs336buyme;
CREATE DATABASE IF NOT EXISTS cs336buyme;
USE cs336buyme;



DROP USER 'cs336'@'%';
CREATE USER 'cs336'@'%' IDENTIFIED BY 'cs336_password';
GRANT ALL PRIVILEGES ON cs336buyme.* TO 'cs336'@'%';
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
	usertype  INT          NOT NULL,	-- 1: Admin, 2: Rep, 3: End-User
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
	fieldType INT NOT NULL, 			-- 1 string 2 int 3 boolean
	--
	PRIMARY KEY (fieldID)
);


DROP TABLE IF EXISTS CategoryField;
CREATE TABLE CategoryField
(
	categoryName VARCHAR(64) NOT NULL,
	fieldID      INT NOT NULL,
    sortOrder  INT NOT NULL,
	--
	FOREIGN KEY (categoryName) REFERENCES Category (categoryName) ON DELETE CASCADE,
	FOREIGN KEY (fieldID) REFERENCES Field (fieldID) ON DELETE CASCADE,
	PRIMARY KEY (categoryName, fieldID)
);


DROP TABLE IF EXISTS Offer;
CREATE TABLE Offer
(
	offerId      VARCHAR(32)    NOT NULL,
	categoryName VARCHAR(64)    NOT NULL,
	--
	seller       VARCHAR(64)    NOT NULL,
    --
	initPrice	DECIMAL(20, 2)	NOT NULL,	
    increment	DECIMAL(20, 2)	NOT NULL,
	minPrice    DECIMAL(20, 2) NOT NULL,
    --
    conditionCode	INT	NOT NULL, -- 1:New, 2:Like New, 3:Manufacturer Refurbished, 4:Seller Refurbished, 5:Used, 6:For parts or Not Working. Found: https://www.ebay.com/pages/help/sell/contextual/condition_1.html
	description  VARCHAR(128)   NULL,
	startDate    DATETIME,
	endDate      DATETIME,
	status       INT, -- 1:Active, 2:Withdrawal, 3:Completed, 4:NoBid
	--
	FOREIGN KEY (seller) REFERENCES User (username) ON DELETE CASCADE,
	PRIMARY KEY (offerId)
);



DROP TABLE IF EXISTS OfferField;
CREATE TABLE OfferField
(
	offerId   VARCHAR(32) NOT NULL,
	fieldID   INT NOT NULL,
	--
	fieldText VARCHAR(64) NOT NULL,
	--
	FOREIGN KEY (offerId) REFERENCES Offer (offerId) ON DELETE CASCADE,
	FOREIGN KEY (fieldID) REFERENCES Field (fieldID) ON DELETE CASCADE,
	PRIMARY KEY (offerId, fieldID)
);






DROP TABLE IF EXISTS OfferAlertCriteria;
CREATE TABLE OfferAlertCriteria
(
	criteriaId      VARCHAR(32)    NOT NULL,
    --
	categoryName 	VARCHAR(256)     NULL,
	--
	seller       	VARCHAR(256)     NULL,
    --
    conditionCode	VARCHAR(256)	NULL,
	description  	VARCHAR(128)   NULL,
	startDate    	VARCHAR(256) NULL,
	endDate      	VARCHAR(256) NULL,
	--
	PRIMARY KEY (criteriaId)
);



DROP TABLE IF EXISTS OfferAlertCriteriaField;
CREATE TABLE OfferAlertCriteriaField
(
	criteriaId   VARCHAR(32) NOT NULL,
	fieldID   INT NOT NULL,
	--
	fieldText VARCHAR(256) NOT NULL,		-- Criteria
	--
	FOREIGN KEY (criteriaId) REFERENCES OfferAlertCriteria (criteriaId) ON DELETE CASCADE,
	FOREIGN KEY (fieldID) REFERENCES Field (fieldID) ON DELETE CASCADE,
	PRIMARY KEY (criteriaId, fieldID)
);











DROP TABLE IF EXISTS Bid;
CREATE TABLE Bid
(
	bidId              VARCHAR(32)    NOT NULL,
	offerId            VARCHAR(32)    NOT NULL,
	buyer              VARCHAR(64)    NOT NULL,
	price              DECIMAL(20, 2) NOT NULL,
	isAutoRebid        BOOLEAN        NOT NULL,
	autoRebidLimit     DECIMAL(20, 2) NOT NULL,
	bidDate            DATETIME       NOT NULL,
	--
	FOREIGN KEY (buyer) REFERENCES User (username) ON DELETE CASCADE,
	FOREIGN KEY (offerId) REFERENCES Offer (offerId) ON DELETE CASCADE,
	PRIMARY KEY (bidId)
);



DROP TABLE IF EXISTS Trade;
CREATE TABLE Trade
(
	tradeId VARCHAR(32) NOT NULL,
	offerId VARCHAR(32) NOT NULL,
	bidId   VARCHAR(32) NOT NULL,
	tradeDate    DATETIME NOT NULL,
	--
	FOREIGN KEY (offerId) REFERENCES Offer (offerId) ON DELETE CASCADE,
	FOREIGN KEY (bidId) REFERENCES Bid (bidId) ON DELETE CASCADE,
	PRIMARY KEY (tradeId)
);



DROP TABLE IF EXISTS Alert;
CREATE TABLE Alert
(
	alertId 	INT AUTO_INCREMENT,
	receiver  	VARCHAR(64)  NOT NULL,
	message 	VARCHAR(256) NOT NULL,
	--
    alertDate 	DATETIME NOT NULL,
    dismissedDate DATETIME NULL,	-- Default NULL. NOT NULL means dismissed.
    --
	FOREIGN KEY (receiver) REFERENCES User (username) ON DELETE CASCADE,
	PRIMARY KEY (alertId)
);



DROP TABLE IF EXISTS Question;
CREATE TABLE Question
(
	questionId INT AUTO_INCREMENT,
	userId     VARCHAR(64),
	question   VARCHAR(250) NOT NULL,
	answer     VARCHAR(250) DEFAULT NULL,
	--
	FOREIGN KEY (userId) REFERENCES User (username) ON DELETE CASCADE,
	PRIMARY KEY (questionId)
);



DROP TABLE IF EXISTS Email;
CREATE TABLE Email
(
	emailID	VARCHAR(32) NOT NULL,	
	sender	VARCHAR(250) NOT NULL,		-- from
    receiver VARCHAR(250) NOT NULL,		-- to
    sub		VARCHAR(250) NOT NULL,		-- subject
    sendDate	DATETIME NOT NULL,		-- date_time
    content	VARCHAR(250) NOT NULL,		-- content
    --
    PRIMARY KEY (emailID)
);