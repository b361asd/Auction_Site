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





-- Handles Auto Rebid and outbid alert when a row is inserted into Bid table
DROP TRIGGER IF EXISTS AlertTriggerEmail;
DELIMITER $$
	CREATE TRIGGER AlertTriggerEmail AFTER INSERT ON Alert
	FOR EACH ROW
	BEGIN
		INSERT Email (emailID, sender, receiver, sub, sendDate, content)
			SELECT REPLACE(UUID(),'-',''), 'DoNotReply@BuyMe.com', email, 
            (CASE WHEN NEW.bidID IS NULL THEN 'New Offer Alert' ELSE 'Outbid Alert' END), 
            NOW(), NEW.content FROM User WHERE username = NEW.receiver;
	END $$
DELIMITER ;

	

















-- Not Used: Dynamic SQL not allowed in Triggers or Procedures
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



-- Not Used: In trigger on Bid, not allow to change Bid.
-- Auto rebid
DROP PROCEDURE IF EXISTS doAutoRebidAndOutbidAlert;
DELIMITER $$
CREATE PROCEDURE doAutoRebidAndOutbidAlert(IN newofferID VARCHAR(32), IN newbidID VARCHAR(32), IN newprice DECIMAL(20, 2))
BEGIN
	DECLARE priceAdjust DECIMAL(20, 2);
	--
	CREATE TEMPORARY TABLE _Temp		-- Latest Bid with auto rebid before insert
	SELECT * FROM Bid b1 WHERE b1.price = (SELECT MAX(b2.price) FROM Bid b2 WHERE b2.offerID = newofferID AND NOT b2.bidID = newbidID) AND b1.autoRebidLimit > 0 AND b1.offerID = newofferID AND NOT b1.bidID = newbidID LIMIT 0,1;
	--
	SELECT newprice + o.increment INTO priceAdjust FROM Offer o WHERE o.offerID = newofferID;
	--
	IF ((SELECT COUNT(*) FROM _Temp) = 0) THEN -- Either New is 1st bid or last is not auto rebid
		BEGIN
		--
		END;
	ELSEIF ((SELECT autoRebidLimit FROM _Temp) >= priceAdjust) THEN	-- Auto rebid and autoRebidLimit >= New.price + increment. Do a rebid
		BEGIN
			INSERT Bid (bidID, offerID, buyer, price, autoRebidLimit, bidDate) SELECT bidID, offerID, buyer, priceAdjust, autoRebidLimit, NOW() FROM _Temp WHERE NOW() <= (SELECT endDate from Offer o1 where o1.offerID = newofferID);
		END;
	ELSE	-- Price >= Auto Rebid. Send Alert.
		BEGIN
			INSERT Alert (alertID, receiver, offerID, bidID, alertDate, dismissedDate) SELECT REPLACE(UUID(),'-',''), buyer, NULL, bidID, NOW(), NULL FROM _Temp;
		END;
	END IF;
	--
	DROP TEMPORARY TABLE _Temp;
END $$
DELIMITER ;



-- Not Used: In trigger on Bid, not allow to change Bid.
-- Handles Auto Rebid and outbid alert when a row is inserted into Bid table
DROP TRIGGER IF EXISTS AutoRebidAndOutbidAlert;
DELIMITER $$
	CREATE TRIGGER AutoRebidAndOutbidAlert AFTER INSERT ON _Bid_
	FOR EACH ROW
	BEGIN
		CALL doAutoRebidAndOutbidAlert(NEW.offerID, NEW.bidID, NEW.price);
	END $$
DELIMITER ;



-- Not Used: Dynamic SQL not allowed in Triggers or Procedures
-- Handles Auto Rebid and outbid alert when a row is inserted into Bid table
DROP TRIGGER IF EXISTS NewOfferAlert;
DELIMITER $$
	CREATE TRIGGER NewOfferAlert AFTER INSERT ON _Offer_
	FOR EACH ROW
	BEGIN
		CALL GenerateNewOfferAlert (NEW.offerID, NEW.categoryName);
	END $$
DELIMITER ;
