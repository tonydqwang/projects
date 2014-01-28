connect to cisc332g user 7tdw;

drop table Animal;
drop table Adoptive_Family;
drop table Vet;
drop table Employee;
drop table Appointment;
drop table Additional_Care;
drop table AF_Name;
drop table Responsible_For;


CREATE TABLE Employee (
E_ID 		INTEGER 	NOT NULL	PRIMARY KEY,
E_Fname	 	CHAR(20),
E_Lname 	CHAR(20),
E_Phone 		CHAR(11));

CREATE TABLE Adoptive_Family (
AF_ID 		INTEGER 	NOT NULL	PRIMARY KEY,
AF_Address 	VARCHAR(50),
AF_Phone 	CHAR(11));

CREATE TABLE Vet (
V_ID 		INTEGER 	NOT NULL	PRIMARY KEY,
V_Fname 	CHAR(20),
V_Lname 	CHAR(20),
V_Address 	VARCHAR(50),
V_Phone 		CHAR(11));

CREATE TABLE Animal (
A_ID 		INTEGER 	NOT NULL,
A_Name 		CHAR(20) 	NOT NULL,
Species 		CHAR(20),
Colour 		CHAR(20),
Gender 		CHAR(1),
Date_of_Birth 	DATE,
Date_of_Arrival	DATE,
Breed 		CHAR(30),
Extra_Comment 	VARCHAR(50),
Arrival_Notes 	VARCHAR(50),
Adopter_ID 	INTEGER,
Adoption_Date	DATE,
Prime_Care_ID 	INTEGER 	NOT NULL,
Vet_ID 		INTEGER	NOT NULL,
PRIMARY KEY	(A_ID),
FOREIGN KEY 	(Prime_Care_ID) REFERENCES Employee,
FOREIGN KEY 	(Adopter_ID) REFERENCES Adoptive_Family,
FOREIGN KEY 	(Vet_ID) REFERENCES Vet);

CREATE TABLE Appointment (
Animal_ID 	INTEGER 	NOT NULL,
Vet_ID 		INTEGER 	NOT NULL,
Date_of_Visit 	DATE 		NOT NULL,
A_Weight_lb	INTEGER,
Health_Concern 	VARCHAR(50),
PRIMARY KEY 	(Animal_ID, Vet_ID, Date_of_Visit),
FOREIGN KEY 	(Animal_ID) REFERENCES Animal ON DELETE CASCADE,
FOREIGN KEY 	(Vet_ID) REFERENCES Vet ON DELETE CASCADE);

CREATE TABLE Additional_Care (
A_ID 		INTEGER 	NOT NULL,
Employee_ID 	INTEGER 	NOT NULL,
PRIMARY KEY 	(A_ID, Employee_ID),
FOREIGN KEY 	(A_ID) REFERENCES Animal ON DELETE CASCADE,
FOREIGN KEY 	(Employee_ID) REFERENCES Employee ON DELETE CASCADE);

CREATE TABLE AF_Name (
AF_ID 		INTEGER 	NOT NULL,
AF_Name 	CHAR(40) 	NOT NULL,
PRIMARY KEY 	(AF_ID, AF_Name),
FOREIGN KEY 	(AF_ID) REFERENCES Adoptive_Family ON DELETE CASCADE);
	
CREATE TABLE Responsible_For (
A_ID 		INTEGER 	NOT NULL,
Vet_ID 		INTEGER 	NOT NULL,
Date_of_Visit 	DATE 		NOT NULL,	
PRIMARY KEY 	(A_ID, Vet_ID, Date_of_Visit),
FOREIGN KEY 	(A_ID, Vet_ID, Date_of_Visit) REFERENCES Appointment ON DELETE CASCADE);


insert into Employee values
(1001, 'Cathy', 'Lee', '16137779999'),
(1002, 'Will', 'Dawkins', '16135551111'),
(1003, 'Super', 'Man', '12345678910');

insert into Adoptive_Family values
(2001, '125 Fake St. Kingston, ON', '16134945959'),
(2002, '67 Ultimate Blvd. Kingston, ON', '16134909073');

insert into Vet values
(3001, 'Bob', 'Mason', '123 Fake St. Kingston, ON', '16131231234'),
(3002, 'George', 'Fakeman', '321 Real Ave. Kingston, ON', '16133213214'),
(3003, 'Jeff', 'Pullman', '23 Science Crest. Toronto, ON', '14165553333'),
(3004, 'Greg', 'House', '99 Plainsborough Blvd. Fredericton, NB', '15063334444');

insert into Animal values
(4001, 'Alison', 'Rabbit', 'White', 'M', '1965-01-08', '1965-01-09', 'American', 'Enjoy carrots', 'Rescued from fire', NULL, NULL, 1001, 3001),
(4002, 'Bob', 'Guinea Pig', 'Brown', 'M', '1969-03-19', '1999-01-01', 'Rex', 'Over weight', 'Abandoned', 2001, '2009-12-25', 1002, 3002),
(4003, 'Charlie', 'Hamster', 'White', 'M', '1975-02-02', '2001-01-01', 'Chinese', 'Fast runner', 'Owner diseased', NULL, NULL, 1001, 3003),
(4004, 'Rosie', 'Dog', 'Brown', 'F', '1995-11-17', '2001-09-09', 'Beagle', 'Lazy', 'Saved from abusive owner', 2002, '2010-12-25', 1002, 3004),
(4005, 'Jack', 'Gerbil', 'Gray', 'M', '2010-12-01', '2011-01-01', 'Mongolian', 'Enjoy carrots', 'Found on a farm', NULL, NULL, 1002, 3004);

insert into Appointment values
(4001, 3001, '1965-01-10', 1, 'Organs shut down imminent'),
(4001, 3001, '1965-01-11', 2, 'Critically ill'),
(4001, 3001, '1965-01-12', 1, 'Healthy'),
(4002, 3002, '1999-01-02', 1, 'Healthy'),
(4003, 3003, '2010-12-24', 1, 'Healthy'),
(4004, 3004, '2010-12-24', 10, 'Healthy'),
(4005, 3004, '2011-01-02', 1, 'Recovering from fever');

insert into Additional_Care values
(4001, 1003),
(4002, 1003),
(4003, 1003),
(4004, 1003);

insert into AF_Name values
(2001, 'Jake Sulles'),
(2001, 'Mary Lambert'),
(2002, 'Barrack Obama');

insert into Responsible_For values 
(4001, 3001, '1965-01-10'),
(4001, 3001, '1965-01-11'),
(4001, 3001, '1965-01-12'),
(4002, 3002, '1999-01-02'),
(4003, 3003, '2010-12-24'),
(4004, 3004, '2010-12-24'),
(4005, 3004, '2011-01-02');

select * from Additional_Care;
select * from Adoptive_Family;
select * from AF_Name;
select * from Animal;
select * from Appointment;
select * from Employee;
select * from Responsible_For;
select * from Vet;

terminate;