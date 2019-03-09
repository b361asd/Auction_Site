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




-- List of possible categories
DROP TABLE IF EXISTS Category;
CREATE TABLE Category
(
	categoryName VARCHAR(64) NOT NULL,
	--
	PRIMARY KEY (categoryName)
);




-- All possible fields for all categories. Multiple categories can have the same and/or different fields.
DROP TABLE IF EXISTS Field;
CREATE TABLE Field
(
	fieldID   INT AUTO_INCREMENT,
	fieldName VARCHAR(64) NOT NULL,
	fieldType INT NOT NULL, 			-- 1:string, 2:int, 3:boolean
	--
	PRIMARY KEY (fieldID)
);




-- Fields for categories. sortOrder help present these fields in GUI.
DROP TABLE IF EXISTS CategoryField;
CREATE TABLE CategoryField
(
	categoryName 	VARCHAR(64) NOT NULL,
	fieldID      	INT NOT NULL,
    sortOrder  		INT NOT NULL,
	--
	FOREIGN KEY (categoryName) REFERENCES Category (categoryName) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (fieldID) REFERENCES Field (fieldID) ON UPDATE CASCADE ON DELETE CASCADE,
    --
	PRIMARY KEY (categoryName, fieldID)
);




-- Offer of an item for sale. Fields of the offer in seperate table so that new categories can be created without recompiling the code. 
DROP TABLE IF EXISTS Offer;
CREATE TABLE Offer
(
	offerID      VARCHAR(32)    NOT NULL,
	--
	seller       VARCHAR(64)    NOT NULL,
    --
	categoryName VARCHAR(64)    NOT NULL,
    conditionCode	INT	NOT NULL, 	-- 1:New, 2:Like New, 3:Manufacturer Refurbished, 
									-- 4:Seller Refurbished, 5:Used, 6:For parts or Not Working.
                                    -- Ref.: https://www.ebay.com/pages/help/sell/contextual/condition_1.html
	description  VARCHAR(128)   NULL,
    --
	initPrice	DECIMAL(20, 2)	NOT NULL,	
    increment	DECIMAL(20, 2)	NOT NULL,
	minPrice    DECIMAL(20, 2) 	NULL,		-- NULL if user has not set a min price	
    --
	startDate    DATETIME		NOT NULL,
	endDate      DATETIME		NOT NULL,
    --
	status       INT			NOT NULL,	-- 1:Active, 2:Withdrawal, 3:Completed, 4:No bid, 5:Min not met
	--
	FOREIGN KEY (seller) REFERENCES User(username) ON UPDATE CASCADE ON DELETE CASCADE,
    --
	PRIMARY KEY (offerID)
);




-- Fields for an Offer
DROP TABLE IF EXISTS OfferField;
CREATE TABLE OfferField
(
	offerID   VARCHAR(32) NOT NULL,
	fieldID   INT NOT NULL,
	--
	fieldText VARCHAR(64) NOT NULL,
	--
	FOREIGN KEY (offerID) REFERENCES Offer (offerID) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (fieldID) REFERENCES Field (fieldID) ON UPDATE CASCADE ON DELETE CASCADE,
    --
	PRIMARY KEY (offerID, fieldID)
);




-- Bid
DROP TABLE IF EXISTS Bid;
CREATE TABLE Bid
(
	bidID              VARCHAR(32)    NOT NULL,
    --
	offerID            VARCHAR(32)    NOT NULL,
    --
	buyer              VARCHAR(64)    NOT NULL,
	price              DECIMAL(20, 2) NOT NULL,
	autoRebIDLimit     DECIMAL(20, 2) NULL,		-- NULL if not auto rebid
	bidDate            DATETIME       NOT NULL,
	--
	FOREIGN KEY (buyer) REFERENCES User (username) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (offerID) REFERENCES Offer (offerID) ON UPDATE CASCADE ON DELETE CASCADE,
    --
	PRIMARY KEY (bidID)
);




-- Transaction happens at the end of the Offer endDate
DROP TABLE IF EXISTS Trade;
CREATE TABLE Trade
(
	tradeID 	VARCHAR(32) NOT NULL,
	offerID 	VARCHAR(32) NOT NULL,
	bidID   	VARCHAR(32) NOT NULL,
	tradeDate   DATETIME NOT NULL,
	--
	FOREIGN KEY (offerID) REFERENCES Offer (offerID) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (bidID) REFERENCES Bid (bidID) ON UPDATE CASCADE ON DELETE CASCADE,
    --
	PRIMARY KEY (tradeID)
);




-- Store the SQL statement that will be run against new offers to generate an alert
DROP TABLE IF EXISTS OfferAlertCriterion;
CREATE TABLE OfferAlertCriterion
(
	criterionID      	VARCHAR(32)    	NOT NULL,
    --
	buyer       	VARCHAR(64)    	NOT NULL,
    --
    categoryName 	VARCHAR(64) 	NOT NULL,
	triggerTxt 		VARCHAR(2048)	NOT NULL,
	--
	PRIMARY KEY (criterionID)
);




-- Alerts for outbidded auto-rebid and new offers met offer alert criteria. 
DROP TABLE IF EXISTS Alert;
CREATE TABLE Alert
(
	alertID 		INT AUTO_INCREMENT,
	receiver  		VARCHAR(64)  NOT NULL,
	message 		VARCHAR(256) NOT NULL,
	--
    offerID     	VARCHAR(32) NULL,		-- Will be not null for offer alert
    bidID     		VARCHAR(32) NULL,		-- Will be not null for auto-rebid outbid alert.
    --
    alertDate 		DATETIME NOT NULL,
    dismissedDate 	DATETIME NULL,			-- Default NULL. NOT NULL means dismissed.
    --
	FOREIGN KEY (receiver) REFERENCES User (username) ON DELETE CASCADE,
	FOREIGN KEY (offerID) REFERENCES Offer (offerID) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (bidID) REFERENCES Bid (bidID) ON UPDATE CASCADE ON DELETE CASCADE,
    --
	PRIMARY KEY (alertID)
);




-- Message from users to BuyMe company.
DROP TABLE IF EXISTS Question;
CREATE TABLE Question
(
	questionID 			INT AUTO_INCREMENT,
	userID     			VARCHAR(64) 	NOT NULL,
    --
	question		   	VARCHAR(1024) 	NOT NULL,
	--
    questionDate		DATETIME		NOT NULL, 
    --
	FOREIGN KEY (userID) REFERENCES User (username) ON DELETE CASCADE,
    --
	PRIMARY KEY (questionID)
);




-- Answer for questions.
DROP TABLE IF EXISTS Answer;
CREATE TABLE Answer
(
	answerID 					INT AUTO_INCREMENT,
	userID     					VARCHAR(64) NOT NULL,
    --
	questionID 	INT NULL,								-- The question to answer.
    --
	answer     	VARCHAR(1024) 	NOT NULL,
	--
    answerDate	DATETIME		NOT NULL,
    --
	FOREIGN KEY (userID) REFERENCES User (username) ON DELETE CASCADE,
	FOREIGN KEY (questionID) REFERENCES Question (questionID) ON DELETE CASCADE,
    --
	PRIMARY KEY (answerID)
);




-- Simulated Emails
DROP TABLE IF EXISTS Email;
CREATE TABLE Email
(
	emailID	VARCHAR(32) NOT NULL,	
	sender	VARCHAR(250) NOT NULL,		-- from
    receiver VARCHAR(250) NOT NULL,		-- to
    sub		VARCHAR(250) NOT NULL,		-- subject
    sendDate	DATETIME NOT NULL,		-- date_time
    content	VARCHAR(1024) NOT NULL,		-- content
    --
    PRIMARY KEY (emailID)
);