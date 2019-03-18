USE cs336buyme;

-- delete from User;

/*
Cars
	- AutoTransmission	(T or F)
	- Trim
Trucks
	- AutoTransmission
	- TowingCapacity
	- Trim
	- Torque
Motorcycles
	- EngineSize
Common
	- Make
	- Model
	- Year
	- Color
	- Mileage (0 if New)
*/

INSERT User (username, password, email, firstname, lastname, address, phone, active, userType)
VALUES ('admin', 'admin_pwd', 'admin@buyme.com', 'Fnadmin', 'Lnadmin', '123 Main St., Nowhere Town, NJ 12345',
		  '2345678909', true, 1);

INSERT User (username, password, email, firstname, lastname, address, phone, active, userType)
VALUES ('rep', 'rep_pwd', 'rep@buyme.com', 'Fnrep', 'Lnrep', '123 Main St., Somewhere Town, NJ 12345', '2355678909',
		  true, 2);

INSERT User (username, password, email, firstname, lastname, address, phone, active, userType)
VALUES ('user', 'user_pwd', 'user@buyme.com', 'Fnuser', 'Lnuser', '123 Main St., Nowhere Town, NJ 56789', '2365678909',
		  true, 3);


INSERT Category (categoryName) VALUES ('car');
INSERT Category (categoryName) VALUES ('truck');
INSERT Category (categoryName) VALUES ('motorbike');

-- fieldType: 1:string, 2:int, 3:boolean

-- Common
INSERT Field (fieldName, fieldType) VALUES ('color', 1);
INSERT Field (fieldName, fieldType) VALUES ('manufacturer', 1);
INSERT Field (fieldName, fieldType) VALUES ('mileage', 2);
INSERT Field (fieldName, fieldType) VALUES ('model', 1);
INSERT Field (fieldName, fieldType) VALUES ('year', 2);

-- Car
INSERT Field (fieldName, fieldType) VALUES ('autoTransmission', 3);
INSERT Field (fieldName, fieldType) VALUES ('trim', 1);

-- Truck
INSERT Field (fieldName, fieldType) VALUES ('towingCapacity', 2);
INSERT Field (fieldName, fieldType) VALUES ('torque', 2);

-- Motorcycle
INSERT Field (fieldName, fieldType) VALUES ('engineSize', 2);


-- Car
INSERT CategoryField (categoryName, fieldID, sortOrder) VALUES ('car', 1, 10);
INSERT CategoryField (categoryName, fieldID, sortOrder) VALUES ('car', 2, 20);
INSERT CategoryField (categoryName, fieldID, sortOrder) VALUES ('car', 3, 30);
INSERT CategoryField (categoryName, fieldID, sortOrder) VALUES ('car', 4, 40);
INSERT CategoryField (categoryName, fieldID, sortOrder) VALUES ('car', 5, 50);
INSERT CategoryField (categoryName, fieldID, sortOrder) VALUES ('car', 6, 60);
INSERT CategoryField (categoryName, fieldID, sortOrder) VALUES ('car', 7, 70);

-- Truck
INSERT CategoryField (categoryName, fieldID, sortOrder) VALUES ('truck', 1, 10);
INSERT CategoryField (categoryName, fieldID, sortOrder) VALUES ('truck', 2, 20);
INSERT CategoryField (categoryName, fieldID, sortOrder) VALUES ('truck', 3, 30);
INSERT CategoryField (categoryName, fieldID, sortOrder) VALUES ('truck', 4, 40);
INSERT CategoryField (categoryName, fieldID, sortOrder) VALUES ('truck', 5, 50);
INSERT CategoryField (categoryName, fieldID, sortOrder) VALUES ('truck', 7, 60);
INSERT CategoryField (categoryName, fieldID, sortOrder) VALUES ('truck', 8, 70);
INSERT CategoryField (categoryName, fieldID, sortOrder) VALUES ('truck', 9, 80);

-- Motorcycle
INSERT CategoryField (categoryName, fieldID, sortOrder) VALUES ('motorbike', 1, 10);
INSERT CategoryField (categoryName, fieldID, sortOrder) VALUES ('motorbike', 2, 20);
INSERT CategoryField (categoryName, fieldID, sortOrder) VALUES ('motorbike', 3, 30);
INSERT CategoryField (categoryName, fieldID, sortOrder) VALUES ('motorbike', 4, 40);
INSERT CategoryField (categoryName, fieldID, sortOrder) VALUES ('motorbike', 5, 50);
INSERT CategoryField (categoryName, fieldID, sortOrder) VALUES ('motorbike', 10, 60);

