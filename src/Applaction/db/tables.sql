-- This CLP file was created using DB2LOOK Version "11.5" 
-- Timestamp: Fri 05 Nov 2021 09:55:37 AM CDT
-- Database Name: OLIST          
-- Database Manager Version: DB2/LINUXX8664 Version 11.5.6.0
-- Database Codepage: 1208
-- Database Collating Sequence is: IDENTITY
-- Alternate collating sequence(alt_collate): null
-- varchar2 compatibility(varchar2_compat): OFF


CONNECT TO OLIST user db2admin using 123456;

------------------------------------------------
-- DDL Statements for Schemas
------------------------------------------------

-- Running the DDL below will explicitly create a schema in the
-- new database that corresponds to an implicitly created schema
-- in the original database.

CREATE SCHEMA " db2admin";



------------------------------------------------
-- DDL Statements for Table  "CATEGORY"
------------------------------------------------
 

CREATE TABLE  "CATEGORY"  (
		  "NAME" VARCHAR(255 OCTETS) NOT NULL , 
		  "NAMEENG" VARCHAR(255 OCTETS) NOT NULL )   
		 IN "USERSPACE1"  
		 ORGANIZE BY ROW; 


-- DDL Statements for Primary Key on Table  "CATEGORY"

ALTER TABLE  "CATEGORY" 
	ADD PRIMARY KEY
		("NAME")
	ENFORCED;


------------------------------------------------
-- DDL Statements for Table  "GEOLOCATION"
------------------------------------------------
 

CREATE TABLE  "GEOLOCATION"  (
		  "ZIPCODE" INTEGER NOT NULL , 
		  "LAT" DOUBLE WITH DEFAULT NULL , 
		  "LNG" DOUBLE WITH DEFAULT NULL , 
		  "CITY" VARCHAR(255 OCTETS) WITH DEFAULT NULL , 
		  "STATE" VARCHAR(255 OCTETS) WITH DEFAULT NULL )   
		 IN "USERSPACE1"  
		 ORGANIZE BY ROW; 


-- DDL Statements for Primary Key on Table  "GEOLOCATION"

ALTER TABLE  "GEOLOCATION" 
	ADD PRIMARY KEY
		("ZIPCODE")
	ENFORCED;


------------------------------------------------
-- DDL Statements for Table  "USERS"
------------------------------------------------
 

CREATE TABLE  "USERS"  (
		  "USER_ID" VARCHAR(255 OCTETS) NOT NULL , 
		  "ZIPCODE" INTEGER NOT NULL )   
		 IN "USERSPACE1"  
		 ORGANIZE BY ROW; 


-- DDL Statements for Primary Key on Table  "USERS"

ALTER TABLE  "USERS" 
	ADD PRIMARY KEY
		("USER_ID")
	ENFORCED;



-- DDL Statements for Indexes on Table  "USERS"

SET SYSIBM.NLS_STRING_UNITS = 'SYSTEM';

CREATE INDEX  "HOME" ON  "USERS" 
		("ZIPCODE" ASC)
		
		COMPRESS NO 
		INCLUDE NULL KEYS ALLOW REVERSE SCANS;
------------------------------------------------
-- DDL Statements for Table  "REVIEWS"
------------------------------------------------
 

CREATE TABLE  "REVIEWS"  (
		  "REVIEW_ID" VARCHAR(255 OCTETS) NOT NULL , 
		  "ORDER_ID" VARCHAR(255 OCTETS) NOT NULL , 
		  "REVIEW_SCORE" INTEGER NOT NULL , 
		  "REVIEW_COMMENT_TITLE" VARCHAR(255 OCTETS) WITH DEFAULT NULL , 
		  "REVIEW_COMMENT_MESSAGE" VARCHAR(255 OCTETS) WITH DEFAULT NULL , 
		  "REVIEW_CREATION_DATE" TIMESTAMP WITH DEFAULT NULL , 
		  "REVIEW_ANSWER_TIMESTAMP" TIMESTAMP WITH DEFAULT NULL )   
		 IN "USERSPACE1"  
		 ORGANIZE BY ROW; 


-- DDL Statements for Primary Key on Table  "REVIEWS"

ALTER TABLE  "REVIEWS" 
	ADD PRIMARY KEY
		("REVIEW_ID")
	ENFORCED;



-- DDL Statements for Indexes on Table  "REVIEWS"

SET SYSIBM.NLS_STRING_UNITS = 'SYSTEM';

CREATE INDEX  "ORDER_ID" ON  "REVIEWS" 
		("ORDER_ID" ASC)
		
		COMPRESS NO 
		INCLUDE NULL KEYS ALLOW REVERSE SCANS;
------------------------------------------------
-- DDL Statements for Table  "PRODUCTS"
------------------------------------------------
 

CREATE TABLE  "PRODUCTS"  (
		  "PRODUCTID" VARCHAR(255 OCTETS) NOT NULL , 
		  "CATEGORY" VARCHAR(255 OCTETS) NOT NULL , 
		  "PHOTOSNUM" INTEGER NOT NULL , 
		  "WEIGHT" INTEGER NOT NULL , 
		  "LENGTH" INTEGER NOT NULL , 
		  "HEIGHT" INTEGER NOT NULL , 
		  "WIDTH" INTEGER NOT NULL )   
		 IN "USERSPACE1"  
		 ORGANIZE BY ROW; 


-- DDL Statements for Primary Key on Table  "PRODUCTS"

ALTER TABLE  "PRODUCTS" 
	ADD PRIMARY KEY
		("PRODUCTID")
	ENFORCED;



-- DDL Statements for Indexes on Table  "PRODUCTS"

SET SYSIBM.NLS_STRING_UNITS = 'SYSTEM';

CREATE INDEX  "CATEGORY" ON  "PRODUCTS" 
		("CATEGORY" ASC)
		
		COMPRESS NO 
		INCLUDE NULL KEYS ALLOW REVERSE SCANS;
------------------------------------------------
-- DDL Statements for Table  "ORDERS"
------------------------------------------------
 

CREATE TABLE  "ORDERS"  (
		  "ORDER_ID" VARCHAR(255 OCTETS) NOT NULL , 
		  "CUSTOMER_ID" VARCHAR(255 OCTETS) NOT NULL , 
		  "ORDER_STATUS" VARCHAR(255 OCTETS) NOT NULL , 
		  "ORDER_PURCHASE_TIMESTAMP" TIMESTAMP WITH DEFAULT NULL , 
		  "ORDER_APPROVED_AT" TIMESTAMP WITH DEFAULT NULL , 
		  "ORDER_DELIVERED_CARRIER_DATE" TIMESTAMP WITH DEFAULT NULL , 
		  "ORDER_DELIVERED_CUSTOMER_DATE" TIMESTAMP WITH DEFAULT NULL , 
		  "ORDER_ESTIMATED_DELIVERY_DATE" TIMESTAMP WITH DEFAULT NULL , 
		  "PAYMENT_SEQUENTIAL" INTEGER WITH DEFAULT NULL , 
		  "PAYMENT_TYPE" VARCHAR(255 OCTETS) WITH DEFAULT NULL , 
		  "PAYMENT_INSTALLMENTS" INTEGER WITH DEFAULT NULL , 
		  "PAYMENT_VALUE" DOUBLE WITH DEFAULT NULL , 
		  "PRODUCT_ID" VARCHAR(255 OCTETS) WITH DEFAULT NULL , 
		  "SELLER_ID" VARCHAR(255 OCTETS) WITH DEFAULT NULL , 
		  "ITEM_NUM" INTEGER WITH DEFAULT NULL , 
		  "SHIPPING_LIMIT_DATE" TIMESTAMP WITH DEFAULT NULL , 
		  "PRICE" DOUBLE WITH DEFAULT NULL , 
		  "FREIGHT_VALUE" DOUBLE WITH DEFAULT NULL )   
		 IN "USERSPACE1"  
		 ORGANIZE BY ROW; 


-- DDL Statements for Primary Key on Table  "ORDERS"

ALTER TABLE  "ORDERS" 
	ADD PRIMARY KEY
		("ORDER_ID")
	ENFORCED;



-- DDL Statements for Indexes on Table  "ORDERS"

SET SYSIBM.NLS_STRING_UNITS = 'SYSTEM';

CREATE INDEX  "CUSTOMER_ID" ON  "ORDERS" 
		("CUSTOMER_ID" ASC)
		
		COMPRESS NO 
		INCLUDE NULL KEYS ALLOW REVERSE SCANS;

-- DDL Statements for Indexes on Table  "ORDERS"

SET SYSIBM.NLS_STRING_UNITS = 'SYSTEM';

CREATE INDEX  "PRODUCT_ID" ON  "ORDERS" 
		("PRODUCT_ID" ASC)
		
		COMPRESS NO 
		INCLUDE NULL KEYS ALLOW REVERSE SCANS;

-- DDL Statements for Indexes on Table  "ORDERS"

SET SYSIBM.NLS_STRING_UNITS = 'SYSTEM';

CREATE INDEX  "SELLER_ID" ON  "ORDERS" 
		("SELLER_ID" ASC)
		
		COMPRESS NO 
		INCLUDE NULL KEYS ALLOW REVERSE SCANS;
-- DDL Statements for Foreign Keys on Table  "USERS"

ALTER TABLE  "USERS" 
	ADD CONSTRAINT "HOME" FOREIGN KEY
		("ZIPCODE")
	REFERENCES  "GEOLOCATION"
		("ZIPCODE")
	ON DELETE CASCADE
	ON UPDATE RESTRICT
	ENFORCED
	ENABLE QUERY OPTIMIZATION;

-- DDL Statements for Foreign Keys on Table  "REVIEWS"

ALTER TABLE  "REVIEWS" 
	ADD CONSTRAINT "ORDER_ID" FOREIGN KEY
		("ORDER_ID")
	REFERENCES  "ORDERS"
		("ORDER_ID")
	ON DELETE CASCADE
	ON UPDATE RESTRICT
	ENFORCED
	ENABLE QUERY OPTIMIZATION;

-- DDL Statements for Foreign Keys on Table  "PRODUCTS"

ALTER TABLE  "PRODUCTS" 
	ADD CONSTRAINT "CATEGORY" FOREIGN KEY
		("CATEGORY")
	REFERENCES  "CATEGORY"
		("NAME")
	ON DELETE CASCADE
	ON UPDATE RESTRICT
	ENFORCED
	ENABLE QUERY OPTIMIZATION;

-- DDL Statements for Foreign Keys on Table  "ORDERS"

ALTER TABLE  "ORDERS" 
	ADD CONSTRAINT "CUSTOMER_ID" FOREIGN KEY
		("CUSTOMER_ID")
	REFERENCES  "USERS"
		("USER_ID")
	ON DELETE CASCADE
	ON UPDATE RESTRICT
	ENFORCED
	ENABLE QUERY OPTIMIZATION;

ALTER TABLE  "ORDERS" 
	ADD CONSTRAINT "PRODUCT_ID" FOREIGN KEY
		("PRODUCT_ID")
	REFERENCES  "PRODUCTS"
		("PRODUCTID")
	ON DELETE CASCADE
	ON UPDATE RESTRICT
	ENFORCED
	ENABLE QUERY OPTIMIZATION;

ALTER TABLE  "ORDERS" 
	ADD CONSTRAINT "SELLER_ID" FOREIGN KEY
		("SELLER_ID")
	REFERENCES  "USERS"
		("USER_ID")
	ON DELETE CASCADE
	ON UPDATE RESTRICT
	ENFORCED
	ENABLE QUERY OPTIMIZATION;









COMMIT WORK;

CONNECT RESET;

TERMINATE;

