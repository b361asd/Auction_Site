select categoryName, CategoryField.fieldID, fieldName
from CategoryField
		  inner join field on categoryfield.fieldID = field.fieldID
order by categoryName, CategoryField.fieldID;

select now(), DATE_ADD(NOW(), INTERVAL + 7 DAY);

select * from User;

select * from Offer;

select * from OfferField;

select * from Bid;

-- delete from Offer where offerId in ('');

-- select categoryName, seller, min_price, description, startDate, endDate, status from Offer where offerId = '4881397927f045b2b9e9f21bf186c7c6';


-- select OfferField.fieldID, fieldName, fieldType, fieldText from OfferField inner join Field on OfferField.fieldID = Field.fieldID where OfferField.offerId = '4881397927f045b2b9e9f21bf186c7c6' order by OfferField.fieldID;


use `cs336buyme`;

select Offer.offerId from Offer inner join OfferField f1 on Offer.offerId = f1.offerId and f1.fieldID = 1 and f1.fieldText = 'blue' inner join OfferField f2 on Offer.offerId = f2.offerId and f2.fieldID = 2 and f2.fieldText = 'toyota' inner join OfferField f3 on Offer.offerId = f3.offerId and f3.fieldID = 3 and f3.fieldText = 'gold';






-- select o.offerId, o.categoryName, o.seller, o.initPrice, o.increment, o.minPrice, o.conditionCode, o.description, o.startDate, o.endDate, o.status, f.fieldID, f.fieldText from Offer o inner join OfferField f on o.offerId = f.offerId and (o.categoryName='car') and (o.seller='user') and (o.initPrice=2) and (o.increment=4) and (o.minPrice=5) and (o.conditionCode=2) and (o.description='Scratcges') and (not exists (select * from OfferField f2 where f2.offerId = o.offerId and ( (f2.fieldID = 1 and (not (f2.fieldText = 'blue'))) or (f2.fieldID = 2 and (not (f2.fieldText = 'toyota'))) or (f2.fieldID = 3 and (not (f2.fieldText = '400'))) or (f2.fieldID = 4 and (not (f2.fieldText = 'yes'))) ))) order by o.offerId, f.fieldID



/*
select o.offerId, o.categoryName, o.seller, o.initPrice, o.increment, o.minPrice, o.conditionCode, o.description, o.startDate, o.endDate, o.status, f.fieldID, f.fieldText from Offer o inner join OfferField f on o.offerId = f.offerId 
and (o.categoryName='car')
and (o.seller='user')
and (o.initPrice=2)
and (o.increment=4)
and (o.minPrice=5)
and (o.conditionCode=2)
and (o.description='Scratcges')
and (o.startDate < NOW() )
and (o.endDate > NOW())
and (o.status=1)
and (not exists (select * from OfferField f2 where f2.offerId = o.offerId and (
(f2.fieldID = 1 and (not (f2.fieldText = 'blue'))) 
or (f2.fieldID = 2 and (not (f2.fieldText = 'toyota'))) 
-- or (f2.fieldID = 3 and (not (f2.fieldText = '400'))) 
-- or (f2.fieldID = 4 and (not (f2.fieldText = 'yes')))
))) order by o.offerId, f.fieldID;
*/