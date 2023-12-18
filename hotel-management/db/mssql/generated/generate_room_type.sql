 USE hotel_management; 

 GO 
 DECLARE @room_type_id varchar(150); 
 DECLARE @room_type_name nvarchar(150); 
 SET @room_type_id = 'RT00001'; 
 SET @room_type_name = N'Kiểu phòng A'; 
 INSERT INTO KIEU_PHONG (ID_KP, TEN_KP) 
 SELECT @room_type_id, @room_type_name 
 WHERE NOT EXISTS (SELECT ID_KP FROM KIEU_PHONG WHERE ID_KP = @room_type_id); 

 GO 
 DECLARE @room_type_id varchar(150); 
 DECLARE @room_type_name nvarchar(150); 
 SET @room_type_id = 'RT00002'; 
 SET @room_type_name = N'Kiểu phòng B'; 
 INSERT INTO KIEU_PHONG (ID_KP, TEN_KP) 
 SELECT @room_type_id, @room_type_name 
 WHERE NOT EXISTS (SELECT ID_KP FROM KIEU_PHONG WHERE ID_KP = @room_type_id); 

 GO 
 DECLARE @room_type_id varchar(150); 
 DECLARE @room_type_name nvarchar(150); 
 SET @room_type_id = 'RT00003'; 
 SET @room_type_name = N'Kiểu phòng C'; 
 INSERT INTO KIEU_PHONG (ID_KP, TEN_KP) 
 SELECT @room_type_id, @room_type_name 
 WHERE NOT EXISTS (SELECT ID_KP FROM KIEU_PHONG WHERE ID_KP = @room_type_id); 

 GO 
 DECLARE @room_type_id varchar(150); 
 DECLARE @room_type_name nvarchar(150); 
 SET @room_type_id = 'RT00004'; 
 SET @room_type_name = N'Kiểu phòng D'; 
 INSERT INTO KIEU_PHONG (ID_KP, TEN_KP) 
 SELECT @room_type_id, @room_type_name 
 WHERE NOT EXISTS (SELECT ID_KP FROM KIEU_PHONG WHERE ID_KP = @room_type_id); 

 GO 
 DECLARE @room_type_id varchar(150); 
 DECLARE @room_type_name nvarchar(150); 
 SET @room_type_id = 'RT00005'; 
 SET @room_type_name = N'Kiểu phòng E'; 
 INSERT INTO KIEU_PHONG (ID_KP, TEN_KP) 
 SELECT @room_type_id, @room_type_name 
 WHERE NOT EXISTS (SELECT ID_KP FROM KIEU_PHONG WHERE ID_KP = @room_type_id); 

