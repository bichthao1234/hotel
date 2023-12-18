 USE hotel_management; 

 GO 
 DECLARE @permission_id varchar(150); 
 DECLARE @permission_name nvarchar(150); 
 SET @permission_id = ''; 
 SET @permission_name = N'SYSTEM_MANAGEMENT'; 
 INSERT INTO NHOM_QUYEN (ID_NQ, TEN_NQ) 
 SELECT @permission_id, @permission_name 
 WHERE NOT EXISTS (SELECT ID_NQ FROM NHOM_QUYEN WHERE ID_NQ = @permission_id); 

 GO 
 DECLARE @permission_id varchar(150); 
 DECLARE @permission_name nvarchar(150); 
 SET @permission_id = ''; 
 SET @permission_name = N'VIEW_REPORT'; 
 INSERT INTO NHOM_QUYEN (ID_NQ, TEN_NQ) 
 SELECT @permission_id, @permission_name 
 WHERE NOT EXISTS (SELECT ID_NQ FROM NHOM_QUYEN WHERE ID_NQ = @permission_id); 

 GO 
 DECLARE @permission_id varchar(150); 
 DECLARE @permission_name nvarchar(150); 
 SET @permission_id = ''; 
 SET @permission_name = N'BOOK_ROOM'; 
 INSERT INTO NHOM_QUYEN (ID_NQ, TEN_NQ) 
 SELECT @permission_id, @permission_name 
 WHERE NOT EXISTS (SELECT ID_NQ FROM NHOM_QUYEN WHERE ID_NQ = @permission_id); 

 GO 
 DECLARE @permission_id varchar(150); 
 DECLARE @permission_name nvarchar(150); 
 SET @permission_id = ''; 
 SET @permission_name = N'SERVICE_REQUEST'; 
 INSERT INTO NHOM_QUYEN (ID_NQ, TEN_NQ) 
 SELECT @permission_id, @permission_name 
 WHERE NOT EXISTS (SELECT ID_NQ FROM NHOM_QUYEN WHERE ID_NQ = @permission_id); 

 GO 
 DECLARE @permission_id varchar(150); 
 DECLARE @permission_name nvarchar(150); 
 SET @permission_id = ''; 
 SET @permission_name = N'VIEW_AVAILABILITY'; 
 INSERT INTO NHOM_QUYEN (ID_NQ, TEN_NQ) 
 SELECT @permission_id, @permission_name 
 WHERE NOT EXISTS (SELECT ID_NQ FROM NHOM_QUYEN WHERE ID_NQ = @permission_id); 

 GO 
 DECLARE @permission_id varchar(150); 
 DECLARE @permission_name nvarchar(150); 
 SET @permission_id = ''; 
 SET @permission_name = N'RESTAURANT_MANAGEMENT'; 
 INSERT INTO NHOM_QUYEN (ID_NQ, TEN_NQ) 
 SELECT @permission_id, @permission_name 
 WHERE NOT EXISTS (SELECT ID_NQ FROM NHOM_QUYEN WHERE ID_NQ = @permission_id); 

