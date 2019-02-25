USE cs336buyme;

-- delete from User;

insert User (username, password, email, firstname, lastname, address, phone, active, usertype)
VALUES ('admin', 'admin_pwd', 'admin@buyme.com', 'Fnadmin', 'Lnadmin', '123 Main St., Nowhere Town, NJ 12345',
		  '2345678909', true, 1);

insert User (username, password, email, firstname, lastname, address, phone, active, usertype)
VALUES ('rep', 'rep_pwd', 'rep@buyme.com', 'Fnrep', 'Lnrep', '123 Main St., Somewhere Town, NJ 12345', '2355678909',
		  true, 2);

insert User (username, password, email, firstname, lastname, address, phone, active, usertype)
VALUES ('user', 'user_pwd', 'user@buyme.com', 'Fnuser', 'Lnuser', '123 Main St., Nowhere Town, NJ 56789', '2365678909',
		  true, 3);


insert Category (categoryName) VALUES ('car');
insert Category (categoryName) VALUES ('truck');
insert Category (categoryName) VALUES ('motorbike');

insert Field (fieldName, fieldType) VALUES ('color', 1);
insert Field (fieldName, fieldType) VALUES ('manufacturer', 1);
insert Field (fieldName, fieldType) VALUES ('horsepower', 2);
insert Field (fieldName, fieldType) VALUES ('autoTransmission', 3);
insert Field (fieldName, fieldType) VALUES ('torque', 2);
insert Field (fieldName, fieldType) VALUES ('engineSize', 2);



insert CategoryField (categoryName, fieldID, sortOrder) VALUES ('car', 1, 10);
insert CategoryField (categoryName, fieldID, sortOrder) VALUES ('car', 2, 20);
insert CategoryField (categoryName, fieldID, sortOrder) VALUES ('car', 3, 30);
insert CategoryField (categoryName, fieldID, sortOrder) VALUES ('car', 4, 40);

insert CategoryField (categoryName, fieldID, sortOrder) VALUES ('truck', 1, 10);
insert CategoryField (categoryName, fieldID, sortOrder) VALUES ('truck', 2, 20);
insert CategoryField (categoryName, fieldID, sortOrder) VALUES ('truck', 3, 30);
insert CategoryField (categoryName, fieldID, sortOrder) VALUES ('truck', 5, 40);

insert CategoryField (categoryName, fieldID, sortOrder) VALUES ('motorbike', 1, 10);
insert CategoryField (categoryName, fieldID, sortOrder) VALUES ('motorbike', 2, 20);
insert CategoryField (categoryName, fieldID, sortOrder) VALUES ('motorbike', 3, 30);
insert CategoryField (categoryName, fieldID, sortOrder) VALUES ('motorbike', 6, 40);

