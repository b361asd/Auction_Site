select categoryName, categoryfield.fieldID, fieldName
from categoryfield
		  inner join field on categoryfield.fieldID = field.fieldID
order by categoryName, categoryfield.fieldID;

select now(), DATE_ADD(NOW(), INTERVAL + 7 DAY);

select *
from offer;

select *
from offerfield;