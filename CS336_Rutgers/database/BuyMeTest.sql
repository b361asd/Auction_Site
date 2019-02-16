select categoryName, categoryfield.fieldID, fieldName
from categoryfield
		  inner join field on categoryfield.fieldID = field.fieldID
order by categoryName, categoryfield.fieldID;