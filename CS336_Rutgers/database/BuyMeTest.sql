SELECT NOW(), DATE_ADD(NOW(), INTERVAL + 7 DAY);

SELECT * FROM User;

SELECT * FROM Offer;

SELECT * FROM OfferField;

SELECT * FROM Bid;

-- DELETE FROM Offer WHERE offerId in ('');

-- SELECT categoryName, seller, min_price, description, startDate, endDate, status FROM Offer WHERE offerId = '4881397927f045b2b9e9f21bf186c7c6';


-- SELECT OfferField.fieldID, fieldName, fieldType, fieldText FROM OfferField inner join Field on OfferField.fieldID = Field.fieldID WHERE OfferField.offerId = '4881397927f045b2b9e9f21bf186c7c6' order by OfferField.fieldID;


USE `cs336buyme`;

SELECT Offer.offerId FROM Offer inner join OfferField f1 on Offer.offerId = f1.offerId AND f1.fieldID = 1 AND f1.fieldText = 'blue' inner join OfferField f2 on Offer.offerId = f2.offerId AND f2.fieldID = 2 AND f2.fieldText = 'toyota' inner join OfferField f3 on Offer.offerId = f3.offerId AND f3.fieldID = 3 AND f3.fieldText = 'gold';



select o.offerId, o.seller, o.categoryName, o.conditionCode, o.description, o.initPrice, o.increment, o.minPrice, o.startDate, o.endDate, o.status, o.price, of1.fieldID, of1.fieldText from (SELECT o1.*, b.price FROM Offer o1 LEFT OUTER JOIN (SELECT b1.price, b1.offerId FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b where b.offerId = b1.offerId)) b ON o1.offerId = b.offerId) o inner join OfferField of1 on o.offerId = of1.offerId
and (o.offerID='aaa')
and (o.seller='user')
and (o.categoryName='car')
and (o.conditionCode=1)
and (o.description='Scratcges')
and (o.initPrice=2)
and (o.increment=4)
and (o.minPrice=5)
and (o.startDate < NOW() )
and (o.endDate > NOW())
and (o.status=1)
and (o.price=2)
and (not exists (select * from OfferField of2 where of2.offerId = o.offerId and (false
or (of2.fieldID = 1 and (not (of2.fieldText = 'blue')))
or (of2.fieldID = 2 and (not (of2.fieldText = 'toyota')))
or (of2.fieldID = 3 and (not (of2.fieldText = '400')))
or (of2.fieldID = 4 and (not (of2.fieldText = 'yes')))
))) order by o.offerId, of1.fieldID;



-- SELECT o.offerId, o.categoryName, o.seller, o.initPrice, o.increment, o.minPrice, o.conditionCode, o.description, o.startDate, o.endDate, o.status, f.fieldID, f.fieldText FROM Offer o inner join OfferField f on o.offerId = f.offerId AND (o.categoryName='car') AND (o.seller='user') AND (o.initPrice=2) AND (o.increment=4) AND (o.minPrice=5) AND (o.conditionCode=2) AND (o.description='Scratcges') AND (not exists (SELECT * FROM OfferField f2 WHERE f2.offerId = o.offerId AND ( (f2.fieldID = 1 AND (not (f2.fieldText = 'blue'))) or (f2.fieldID = 2 AND (not (f2.fieldText = 'toyota'))) or (f2.fieldID = 3 AND (not (f2.fieldText = '400'))) or (f2.fieldID = 4 AND (not (f2.fieldText = 'yes'))) ))) order by o.offerId, f.fieldID



/*
SELECT o.offerId, o.categoryName, o.seller, o.initPrice, o.increment, o.minPrice, o.conditionCode, o.description, o.startDate, o.endDate, o.status, f.fieldID, f.fieldText FROM Offer o inner join OfferField f on o.offerId = f.offerId
AND (o.categoryName='car')
AND (o.seller='user')
AND (o.initPrice=2)
AND (o.increment=4)
AND (o.minPrice=5)
AND (o.conditionCode=2)
AND (o.description='Scratcges')
AND (o.startDate < NOW() )
AND (o.endDate > NOW())
AND (o.status=1)
AND (not exists (SELECT * FROM OfferField f2 WHERE f2.offerId = o.offerId AND (
(f2.fieldID = 1 AND (not (f2.fieldText = 'blue')))
or (f2.fieldID = 2 AND (not (f2.fieldText = 'toyota')))
-- or (f2.fieldID = 3 AND (not (f2.fieldText = '400')))
-- or (f2.fieldID = 4 AND (not (f2.fieldText = 'yes')))
))) order by o.offerId, f.fieldID;
*/