USE buyme;

SELECT NOW(), DATE_ADD(NOW(), INTERVAL + 7 DAY);

SELECT *
FROM User;

SELECT *
FROM Offer;

SELECT *
FROM OfferField;

SELECT *
FROM Alert;

SELECT *
FROM Question;


SELECT *
FROM Bid;

SELECT *
FROM OfferAlertCriterion;

DELETE
FROM OfferAlertCriterion
WHERE criterionID = 'e1ed6eb342194a60b619501d880bed24';

-- DELETE FROM Offer WHERE offerId in ('');

-- SELECT categoryName, seller, min_price, description, startDate, endDate, status FROM Offer WHERE offerId = '4881397927f045b2b9e9f21bf186c7c6';


-- SELECT OfferField.fieldID, fieldName, fieldType, fieldText FROM OfferField inner join Field on OfferField.fieldID = Field.fieldID WHERE OfferField.offerId = '4881397927f045b2b9e9f21bf186c7c6' order by OfferField.fieldID;


SELECT Offer.offerId
FROM Offer
        INNER JOIN OfferField f1 ON Offer.offerId = f1.offerId AND f1.fieldID = 1 AND f1.fieldText = 'blue'
        INNER JOIN OfferField f2 ON Offer.offerId = f2.offerId AND f2.fieldID = 2 AND f2.fieldText = 'toyota'
        INNER JOIN OfferField f3 ON Offer.offerId = f3.offerId AND f3.fieldID = 3 AND f3.fieldText = 'gold';



SELECT o.offerId, o.seller, o.categoryName, o.conditionCode, o.description, o.initPrice, o.increment, o.minPrice, o.startDate, o.endDate, o.status, o.price, of1.fieldID, of1.fieldText
FROM (SELECT o1.*, b.price
      FROM Offer o1
              LEFT OUTER JOIN (SELECT b1.price, b1.offerId FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b WHERE b.offerId = b1.offerId)) b ON o1.offerId = b.offerId) o
        INNER JOIN OfferField of1 ON o.offerId = of1.offerId AND (o.offerID = 'aaa') AND (o.seller = 'user') AND (o.categoryName = 'car') AND (o.conditionCode = 1) AND (o.description = 'Scratcges') AND (o.initPrice = 2) AND (o.increment = 4) AND (o.minPrice = 5) AND (o.startDate < NOW()) AND (o.endDate > NOW()) AND (o.status = 1) AND (o.price = 2) AND (NOT exists(SELECT * FROM OfferField of2 WHERE of2.offerId = o.offerId AND (FALSE OR (of2.fieldID = 1 AND (NOT (of2.fieldText = 'blue'))) OR (of2.fieldID = 2 AND (NOT (of2.fieldText = 'toyota'))) OR (of2.fieldID = 3 AND (NOT (of2.fieldText = '400'))) OR (of2.fieldID = 4 AND (NOT (of2.fieldText = 'yes'))))))
ORDER BY o.offerId, of1.fieldID;


-- Largest Bid Price

SELECT MAX(price)
FROM Bid b
WHERE b.offerId = 'fad64df32a6a4d3f91000a1d50e28696';

SELECT *
FROM Bid b1
WHERE b1.offerId = 'fad64df32a6a4d3f91000a1d50e28696';


SELECT *
FROM Bid b1
WHERE b1.offerId = 'fad64df32a6a4d3f91000a1d50e28696'
  AND b1.price = 1000;

SELECT *
FROM (SELECT * FROM Bid b1 WHERE b1.offerId = 'fad64df32a6a4d3f91000a1d50e28696' AND b1.price = (SELECT MAX(price) FROM Bid b WHERE b.offerId = 'fad64df32a6a4d3f91000a1d50e28696')) bb;



SELECT o1.*, b.price
FROM Offer o1
        LEFT OUTER JOIN (SELECT b1.price, b1.offerId FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b WHERE b.offerId = b1.offerId)) b ON o1.offerId = b.offerId;



SELECT *
FROM Offer o
        LEFT OUTER JOIN Bid b ON o.offerID = b.offerID AND o.offerID = 'fad64df32a6a4d3f91000a1d50e28696';


SHOW EVENTS;
