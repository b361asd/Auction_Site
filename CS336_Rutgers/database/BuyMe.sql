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
    CONSTRAINT INIT_CK CHECK(initPrice > 0),
    CONSTRAINT INC_CK CHECK(increment > 0),
    CONSTRAINT MIN_CK CHECK(minPrice = 0 OR minPrice>=initPrice),
	--
	startDate     DATETIME       NOT NULL,
	endDate       DATETIME       NOT NULL,
    CONSTRAINT ENDDATE_CK CHECK(endDate > startDate),
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
	autoRebidLimit DECIMAL(20, 2) NULL, -- NULL or 0 if not auto rebid
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
	criterionID  VARCHAR(32)   NOT NULL,
	--
	buyer        VARCHAR(64)   NOT NULL,
	--
	criterionName VARCHAR(64)   NOT NULL,
    --
	triggerTxt   VARCHAR(2048) NOT NULL,
    --
	generateDate DATETIME    NOT NULL,
	--
	PRIMARY KEY (criterionID)
);


-- Alerts for outbidded auto-rebid and new offers met offer alert criteria.
DROP TABLE IF EXISTS Alert;
CREATE TABLE Alert
(
	alertID       VARCHAR(32) NOT NULL,
	receiver      VARCHAR(64)  NOT NULL,
	--
	offerID       VARCHAR(32)  NULL, -- Will be not null for offer alert
	bidID         VARCHAR(32)  NULL, -- Will be not null for auto-rebid outbid alert.
	--
	alertDate     DATETIME     NOT NULL,
	dismissedDate DATETIME     NULL, -- Default NULL. NOT NULL means dismissed.
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
	questionID   VARCHAR(32) NOT NULL,
	userID       VARCHAR(64) NOT NULL,
	--
	question     VARCHAR(1024) NOT NULL,
	answer     	VARCHAR(1024) NULL,
	--
	repID       VARCHAR(64)   NULL,
	--
	questionDate DATETIME      NOT NULL,
	answerDate DATETIME      NULL,
	--
	FOREIGN KEY (userID) REFERENCES User (username) ON DELETE CASCADE,
	FOREIGN KEY (repID) REFERENCES User (username) ON DELETE CASCADE,
	--
	PRIMARY KEY (questionID)
);


-- Simulated Emails
DROP TABLE IF EXISTS Email;
CREATE TABLE Email
(
	emailID  VARCHAR(32)   NOT NULL,
	sender   VARCHAR(250)  NOT NULL, -- from
	receiver VARCHAR(250)  NOT NULL, -- to
	sub      VARCHAR(250)  NOT NULL, -- subject
	sendDate DATETIME      NOT NULL, -- date_time
	content  VARCHAR(1024) NOT NULL, -- content
	--
	PRIMARY KEY (emailID)
);



-- Procedure to generate alert for new offers if criteria matched
DROP PROCEDURE IF EXISTS GenerateNewOfferAlert;
DELIMITER $$
CREATE PROCEDURE GenerateNewOfferAlert(IN offerID VARCHAR(32), IN categoryName VARCHAR(32))
BEGIN
	DECLARE sqlTxt VARCHAR(2048);
	DECLARE criteriaCursor CURSOR FOR SELECT criterionID, buyer, categoryName, triggerTxt FROM OfferAlertCriterion;
	--
	OPEN criteriaCursor;
	--
	cursorLoop: WHILE (@criteriaCursor) DO
		SET sqlTxt = REPLACE (triggerText, '$offerID$', offerID);
		IF (@criteriaCursor.categoryName = categoryName) THEN
			BEGIN
				PREPARE stmt FROM @sqlTxt;
				EXECUTE stmt USING @sqlTxt;
				DEALLOCATE PREPARE stmt;
			END;
		END IF;
	END WHILE cursorLoop;
	--
	CLOSE criteriaCursor;
END $$
DELIMITER ;



-- Procedure to match offer and bid
DROP PROCEDURE IF EXISTS DoTrade;
DELIMITER $$
CREATE PROCEDURE DoTrade()
BEGIN
	UPDATE Offer o SET o.status = 4 WHERE o.offerID <> 'A' AND o.status = 1 AND NOW() > endDate AND 
    NOT EXISTS (SELECT * FROM Bid b WHERE b.offerID = o.offerID);
	--
	UPDATE Offer o SET o.status = 5 WHERE o.offerID <> 'A' AND o.status = 1 AND NOW() > endDate AND 
    (o.minPrice >= o.initPrice AND NOT EXISTS (SELECT * FROM Bid b WHERE b.offerID = o.offerID AND b.price >= o.minPrice));
	--
	UPDATE Offer o SET o.status = 13 WHERE o.offerID <> 'A' AND o.status = 1 AND NOW() > endDate AND 
	(
		(o.minPrice >= o.initPrice AND EXISTS (SELECT * FROM Bid b WHERE b.offerID = o.offerID AND b.price >= o.minPrice))
        OR 
		(NOT o.minPrice >= o.initPrice AND EXISTS (SELECT * FROM Bid b WHERE b.offerID = o.offerID AND b.price >= o.initPrice))
    );
	--
	INSERT INTO Trade (tradeID, offerID, bidID, tradeDate) 
    SELECT REPLACE(UUID(),'-',''), o.offerID, b.bidID, NOW() FROM Offer o, Bid b WHERE o.status = 13 AND o.offerID = b.offerID AND 
    b.price = (SELECT MAX(b2.price) FROM Bid b2 WHERE b2.offerID = o.offerID ) LIMIT 0, 1;
	--
	UPDATE Offer SET status = 3 WHERE offerID <> 'A' AND status = 13;
END $$
DELIMITER ;



-- Handles Auto Rebid and outbid alert when a row is inserted into Bid table
DROP TRIGGER IF EXISTS AutoRebidAndOutbidAlert;
DELIMITER $$
	CREATE TRIGGER AutoRebidAndOutbidAlert AFTER INSERT ON Bid
	FOR EACH ROW
	BEGIN
		DECLARE priceAdjust DECIMAL(20, 2);
		DECLARE timeNow DATETIME;
		--
		CREATE TEMPORARY TABLE Temp		-- Latest Bid with auto rebid before insert
		SELECT * FROM Bid b1 WHERE b1.price = (SELECT MAX(b2.price) FROM Bid b2 WHERE b2.offerID = NEW.offerID AND NOT b1.bidID = NEW.bidID) AND b1.autoRebidLimit > 0 AND b1.offerID = NEW.offerID AND NOT b1.bidID = NEW.bidID LIMIT 0,1;
		--
		SET priceAdjust = (NEW.price + (SELECT o.increment FROM Offer o WHERE o.offerID = NEW.offerID));
		SET timeNow = NOW();
		--
		IF ((SELECT COUNT(*) FROM Temp) = 0) THEN -- Either New is 1st bid or last is not auto rebid
		BEGIN
		--
		END;
		ELSEIF ((SELECT autoRebidLimit FROM Temp) >= @priceAdjust) THEN	-- Auto rebid and autoRebidLimit >= New.price + increment. Do a rebid
		BEGIN
			INSERT Bid (bidID, offerID, buyer, price, autoRebidLimit, bidDate) SELECT bidID, offerID, buyer, @priceAdjust, autoRebidLimit, @timeNow FROM Temp WHERE @timeNow <= (SELECT endDate from Offer o1 where o1.offerID = NEW.offerID);
		END;
		ELSE	-- Price >= Auto Rebid. Send Alert.
		BEGIN
			INSERT Alert (alertID, receiver, message, offerID, bidID, alertDate, dismissedDate) SELECT REPLACE(UUID(),'-',''), buyer, 'Item Price Exceeded Auto Rebid Limit', offerID, bidID, @timeNow, NULL FROM Temp;
		END;
		END IF;
		--
		DROP TEMPORARY TABLE Temp;
	END $$
DELIMITER ;



-- Handles Auto Rebid and outbid alert when a row is inserted into Bid table
DROP TRIGGER IF EXISTS NewOfferAlert;
DELIMITER $$
	CREATE TRIGGER NewOfferAlert AFTER INSERT ON Offer
	FOR EACH ROW
	BEGIN
		CALL GenerateNewOfferAlert (NEW.offerID, NEW.categoryName);
	END $$
DELIMITER ;




-- An event that processes trades / matches offers and bids
DROP EVENT IF EXISTS ProcessTrade;
DELIMITER $$
	CREATE EVENT ProcessTrade
	ON SCHEDULE EVERY 1 MINUTE STARTS NOW()
	COMMENT 'Process trades'
	DO
		BEGIN
			CALL DoTrade();
		END $$
DELIMITER ;
