select categoryName, CategoryField.fieldID, fieldName
from CategoryField
		  inner join field on categoryfield.fieldID = field.fieldID
order by categoryName, CategoryField.fieldID;

select now(), DATE_ADD(NOW(), INTERVAL + 7 DAY);

select *
from Offer;

select *
from OfferField;

select *
from Bid;
-- delete from Offer where offerId in ('');

-- select categoryName, seller, min_price, description, startDate, endDate, status from Offer where offerId = '4881397927f045b2b9e9f21bf186c7c6';


-- select OfferField.fieldID, fieldName, fieldType, fieldText from OfferField inner join Field on OfferField.fieldID = Field.fieldID where OfferField.offerId = '4881397927f045b2b9e9f21bf186c7c6' order by OfferField.fieldID;


