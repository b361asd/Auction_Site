-- Procedure to match offer and bid
DROP PROCEDURE IF EXISTS DoTrade;
DELIMITER $$
CREATE PROCEDURE DoTrade()
BEGIN
	DECLARE process_date DATETIME;
    set process_date = DATE_SUB(NOW(), INTERVAL 4 HOUR);
	--
	UPDATE Offer o SET o.status = 4 WHERE o.offerID <> 'A' AND o.status = 1 AND process_date > endDate AND 
    NOT EXISTS (SELECT * FROM Bid b WHERE b.offerID = o.offerID);
	--
	UPDATE Offer o SET o.status = 5 WHERE o.offerID <> 'A' AND o.status = 1 AND process_date > endDate AND 
    (o.minPrice >= o.initPrice AND NOT EXISTS (SELECT * FROM Bid b WHERE b.offerID = o.offerID AND b.price >= o.minPrice));
	--
	UPDATE Offer o SET o.status = 13 WHERE o.offerID <> 'A' AND o.status = 1 AND process_date > endDate AND 
	(
		(o.minPrice >= o.initPrice AND EXISTS (SELECT * FROM Bid b WHERE b.offerID = o.offerID AND b.price >= o.minPrice))
        OR 
		(NOT o.minPrice >= o.initPrice AND EXISTS (SELECT * FROM Bid b WHERE b.offerID = o.offerID AND b.price >= o.initPrice))
    );
	--
	INSERT INTO Trade (tradeID, offerID, bidID, tradeDate) 
    SELECT REPLACE(UUID(),'-',''), o.offerID, b.bidID, process_date FROM Offer o, Bid b WHERE o.status = 13 AND o.offerID = b.offerID AND 
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
