select categoryName, CategoryField.fieldID, fieldName
from CategoryField
		  inner join field on categoryfield.fieldID = field.fieldID
order by categoryName, CategoryField.fieldID;

select now(), DATE_ADD(NOW(), INTERVAL + 7 DAY);

select * from User;

select *
from Offer;

select *
from OfferField;

select *
from Bid;
-- delete from Offer where offerId in ('');

-- select categoryName, seller, min_price, description, startDate, endDate, status from Offer where offerId = '4881397927f045b2b9e9f21bf186c7c6';


-- select OfferField.fieldID, fieldName, fieldType, fieldText from OfferField inner join Field on OfferField.fieldID = Field.fieldID where OfferField.offerId = '4881397927f045b2b9e9f21bf186c7c6' order by OfferField.fieldID;


use `cs336buyme`;

select Offer.offerId from Offer inner join OfferField f1 on Offer.offerId = f1.offerId and f1.fieldID = 1 and f1.fieldText = 'blue' inner join OfferField f2 on Offer.offerId = f2.offerId and f2.fieldID = 2 and f2.fieldText = 'toyota' inner join OfferField f3 on Offer.offerId = f3.offerId and f3.fieldID = 3 and f3.fieldText = 'gold';


select Offer.offerId from Offer inner join OfferField f1 on Offer.offerId = f1.offerId and f1.fieldID = 1 and not exists (

select * from OfferField f2 where f2.offerId = Offer.offerId and 

(
not (f2.fieldID = 1 and f2.fieldText = 'blue') and 
not (f2.fieldID = 2 and f2.fieldText = 'toyota') and
not (f2.fieldID = 3 and f2.fieldText = 'gold')
)
);
