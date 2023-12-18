 USE hotel_management; 

 GO 
 DECLARE @department_id varchar(150); 
 DECLARE @department_name nvarchar(150); 
 SET @department_id = ''; 
 SET @department_name = N'MANAGEMENT'; 
 INSERT INTO BO_PHAN (ID_BP, TEN_BP) 
 SELECT @department_id, @department_name 
 WHERE NOT EXISTS (SELECT ID_BP FROM BO_PHAN WHERE ID_BP = @department_id); 

 GO 
 DECLARE @department_id varchar(150); 
 DECLARE @department_name nvarchar(150); 
 SET @department_id = ''; 
 SET @department_name = N'FRONT_OFFICE'; 
 INSERT INTO BO_PHAN (ID_BP, TEN_BP) 
 SELECT @department_id, @department_name 
 WHERE NOT EXISTS (SELECT ID_BP FROM BO_PHAN WHERE ID_BP = @department_id); 

 GO 
 DECLARE @department_id varchar(150); 
 DECLARE @department_name nvarchar(150); 
 SET @department_id = ''; 
 SET @department_name = N'KITCHEN'; 
 INSERT INTO BO_PHAN (ID_BP, TEN_BP) 
 SELECT @department_id, @department_name 
 WHERE NOT EXISTS (SELECT ID_BP FROM BO_PHAN WHERE ID_BP = @department_id); 

 GO 
 DECLARE @department_id varchar(150); 
 DECLARE @department_name nvarchar(150); 
 SET @department_id = ''; 
 SET @department_name = N'HOUSE_KEEPING'; 
 INSERT INTO BO_PHAN (ID_BP, TEN_BP) 
 SELECT @department_id, @department_name 
 WHERE NOT EXISTS (SELECT ID_BP FROM BO_PHAN WHERE ID_BP = @department_id); 

