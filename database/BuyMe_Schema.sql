DROP DATABASE IF EXISTS buyme;
CREATE DATABASE IF NOT EXISTS buyme;
USE buyme;

-- Change "user1" and "user_password" to the username and password you set in MySQL
-- Create a new user when logged in as root
DROP USER user1@'%';
CREATE USER user1@'%' IDENTIFIED BY 'user_password';
GRANT ALL PRIVILEGES ON buyme.* TO user1@'%';
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
   userType  INT          NOT NULL, -- 1: Admin, 2: Rep, 3: End-User
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
   fieldType INT         NOT NULL, -- 1:string, 2:int, 3:boolean
   --
   PRIMARY KEY (fieldID)
);


-- Fields for categories. sortOrder help present these fields in GUI.
DROP TABLE IF EXISTS CategoryField;
CREATE TABLE CategoryField
(
   categoryName VARCHAR(64) NOT NULL,
   fieldID      INT         NOT NULL,
   sortOrder    INT         NOT NULL,
   --
   FOREIGN KEY (categoryName) REFERENCES Category (categoryName) ON UPDATE CASCADE ON DELETE CASCADE,
   FOREIGN KEY (fieldID) REFERENCES Field (fieldID) ON UPDATE CASCADE ON DELETE CASCADE,
   --
   PRIMARY KEY (categoryName, fieldID)
);


-- Offer of an item for sale. Fields of the offer in separate table so that new categories can be created without recompiling the code.
DROP TABLE IF EXISTS Offer;
CREATE TABLE Offer
(
   offerID       VARCHAR(32)    NOT NULL,
   --
   seller        VARCHAR(64)    NOT NULL,
   --
   categoryName  VARCHAR(64)    NOT NULL,

   /*
	1:New, 2:Like New, 3:Manufacturer Refurbished,
	4:Seller Refurbished, 5:Used, 6:For parts or Not Working.
	Ref: https://www.ebay.com/pages/help/sell/contextual/condition_1.html
	*/
   conditionCode INT            NOT NULL,

   description   VARCHAR(128)   NULL,
   --
   initPrice     DECIMAL(20, 2) NOT NULL,
   increment     DECIMAL(20, 2) NOT NULL,
   minPrice      DECIMAL(20, 2) NULL NOT NULL,
   CONSTRAINT INIT_CK CHECK (initPrice > 0),
   CONSTRAINT INC_CK CHECK (increment > 0),
   CONSTRAINT MIN_CK CHECK (minPrice = 0 OR minPrice >= initPrice),
   --
   startDate     DATETIME       NOT NULL,
   endDate       DATETIME       NOT NULL,
   CONSTRAINT ENDDATE_CK CHECK (endDate > startDate),
   --
   status        INT            NOT NULL, -- 1:Active, 2:Withdrawal, 3:Completed, 4:No bid, 5:Min not met

   --
   FOREIGN KEY (seller) REFERENCES User (username) ON UPDATE CASCADE ON DELETE CASCADE,
   --
   PRIMARY KEY (offerID)
);


-- Fields for an Offer
DROP TABLE IF EXISTS OfferField;
CREATE TABLE OfferField
(
   offerID   VARCHAR(32) NOT NULL,
   fieldID   INT         NOT NULL,
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
   bidID          VARCHAR(32)    NOT NULL,
   --
   offerID        VARCHAR(32)    NOT NULL,
   --
   buyer          VARCHAR(64)    NOT NULL,
   price          DECIMAL(20, 2) NOT NULL,
   autoRebidLimit DECIMAL(20, 2) NOT NULL,
   CONSTRAINT PRICE_CK CHECK (price > 0),
   CONSTRAINT AUTOREBID_CK CHECK (autoRebidLimit = 0 OR autoRebidLimit > price),
   --
   bidDate        DATETIME       NOT NULL,
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
   tradeID   VARCHAR(32) NOT NULL,
   offerID   VARCHAR(32) NOT NULL,
   bidID     VARCHAR(32) NOT NULL,
   tradeDate DATETIME    NOT NULL,
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
   criterionID   VARCHAR(32)   NOT NULL,
   --
   buyer         VARCHAR(64)   NOT NULL,
   --
   criterionName VARCHAR(64)   NOT NULL,
   --
   triggerTxt    VARCHAR(2048) NOT NULL,
   description   VARCHAR(2048) NOT NULL,
   --
   generateDate  DATETIME      NOT NULL,
   --
   PRIMARY KEY (criterionID)
);


-- Alerts for outbidded auto-rebid and new offers met offer alert criteria.
DROP TABLE IF EXISTS Alert;
CREATE TABLE Alert
(
   alertID   VARCHAR(32)   NOT NULL,
   receiver  VARCHAR(64)   NOT NULL,
   --
   offerID   VARCHAR(32)   NOT NULL,
   bidID     VARCHAR(32)   NULL,     -- Will be not null for auto-rebid outbid alert.
   --
   content   VARCHAR(1024) NOT NULL, -- content
   alertDate DATETIME      NOT NULL,
   --
   FOREIGN KEY (receiver) REFERENCES User (username) ON DELETE CASCADE,
   FOREIGN KEY (offerID) REFERENCES Offer (offerID) ON UPDATE CASCADE ON DELETE CASCADE,
   FOREIGN KEY (bidID) REFERENCES Bid (bidID) ON UPDATE CASCADE ON DELETE CASCADE,
   --
   PRIMARY KEY (alertID)
);


-- Simulated Emails
DROP TABLE IF EXISTS Email;
CREATE TABLE Email
(
   emailID  VARCHAR(32)   NOT NULL,
   sender   VARCHAR(250)  NOT NULL, -- from
   receiver VARCHAR(250)  NOT NULL, -- to
   sub      VARCHAR(250)  NOT NULL, -- subject
   content  VARCHAR(1024) NOT NULL, -- content
   sendDate DATETIME      NOT NULL, -- date_time
   --
   PRIMARY KEY (emailID)
);


-- Message from users to BuyMe company.
DROP TABLE IF EXISTS Question;
CREATE TABLE Question
(
   questionID   VARCHAR(32)   NOT NULL,
   userID       VARCHAR(64)   NOT NULL,
   --
   question     VARCHAR(1024) NOT NULL,
   answer       VARCHAR(1024) NULL,
   --
   repID        VARCHAR(64)   NULL,
   --
   questionDate DATETIME      NOT NULL,
   answerDate   DATETIME      NULL,
   --
   FOREIGN KEY (userID) REFERENCES User (username) ON DELETE CASCADE,
   FOREIGN KEY (repID) REFERENCES User (username) ON DELETE CASCADE,
   --
   PRIMARY KEY (questionID)
);


-- Procedure to match offer and bid
DROP PROCEDURE IF EXISTS DoTrade;
DELIMITER $$
CREATE PROCEDURE DoTrade()
BEGIN
   DECLARE process_date DATETIME;
   -- set process_date = DATE_SUB(NOW(), INTERVAL 4 HOUR);
   SET process_date = NOW();
   --
   UPDATE Offer o SET o.status = 4 WHERE o.offerID <> 'A' AND o.status = 1 AND process_date > endDate AND NOT EXISTS(SELECT * FROM Bid b WHERE b.offerID = o.offerID);
   --
   UPDATE Offer o SET o.status = 5 WHERE o.offerID <> 'A' AND o.status = 1 AND process_date > endDate AND (o.minPrice >= o.initPrice AND NOT EXISTS(SELECT * FROM Bid b WHERE b.offerID = o.offerID AND b.price >= o.minPrice));
   --
   UPDATE Offer o SET o.status = 13 WHERE o.offerID <> 'A' AND o.status = 1 AND process_date > endDate AND ((o.minPrice >= o.initPrice AND EXISTS(SELECT * FROM Bid b WHERE b.offerID = o.offerID AND b.price >= o.minPrice)) OR (NOT o.minPrice >= o.initPrice AND EXISTS(SELECT * FROM Bid b WHERE b.offerID = o.offerID AND b.price >= o.initPrice)));
   --
   INSERT INTO Trade (tradeID, offerID, bidID, tradeDate)
   SELECT REPLACE(UUID(), '-', ''), o.offerID, b.bidID, process_date
   FROM Offer o,
        Bid b
   WHERE o.status = 13
     AND o.offerID = b.offerID
     AND b.price = (SELECT MAX(b2.price) FROM Bid b2 WHERE b2.offerID = o.offerID)
   LIMIT 0, 1;
   --
   UPDATE Offer SET status = 3 WHERE offerID <> 'A' AND status = 13;
END $$
DELIMITER ;


-- An event that processes trades / matches offers and bids
DROP EVENT IF EXISTS ProcessTrade;
DELIMITER $$
CREATE EVENT ProcessTrade ON SCHEDULE EVERY 1 MINUTE STARTS NOW() COMMENT 'Process trades' DO BEGIN
   CALL DoTrade();
END $$
DELIMITER ;


-- Handles Auto Rebid and outbid alert when a row is inserted into Bid table
DROP TRIGGER IF EXISTS AlertTriggerEmail;
DELIMITER $$
CREATE TRIGGER AlertTriggerEmail
   AFTER INSERT
   ON Alert
   FOR EACH ROW
BEGIN
   INSERT Email (emailID, sender, receiver, sub, sendDate, content) SELECT REPLACE(UUID(), '-', ''), 'DoNotReply@BuyMe.com', email, (IF(NEW.bidID IS NULL, 'New Offer Alert', 'Outbid Alert')), NOW(), NEW.content FROM User WHERE username = NEW.receiver;
END $$
DELIMITER ;
