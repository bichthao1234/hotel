 USE hotel_management; 

 GO 
 DECLARE @room_kind_id varchar(150); 
 DECLARE @room_kind_name nvarchar(150); 
 SET @room_kind_id = 'RK00001'; 
 SET @room_kind_name = N'Loại phòng A'; 
 INSERT INTO LOAI_PHONG (ID_LP, TEN_LP) 
 SELECT @room_kind_id, @room_kind_name 
 WHERE NOT EXISTS (SELECT ID_LP FROM LOAI_PHONG WHERE ID_LP = @room_kind_id); 

 GO 
 DECLARE @room_kind_id varchar(150); 
 DECLARE @room_kind_name nvarchar(150); 
 SET @room_kind_id = 'RK00002'; 
 SET @room_kind_name = N'Loại phòng B'; 
 INSERT INTO LOAI_PHONG (ID_LP, TEN_LP) 
 SELECT @room_kind_id, @room_kind_name 
 WHERE NOT EXISTS (SELECT ID_LP FROM LOAI_PHONG WHERE ID_LP = @room_kind_id); 

 GO 
 DECLARE @room_kind_id varchar(150); 
 DECLARE @room_kind_name nvarchar(150); 
 SET @room_kind_id = 'RK00003'; 
 SET @room_kind_name = N'Loại phòng C'; 
 INSERT INTO LOAI_PHONG (ID_LP, TEN_LP) 
 SELECT @room_kind_id, @room_kind_name 
 WHERE NOT EXISTS (SELECT ID_LP FROM LOAI_PHONG WHERE ID_LP = @room_kind_id); 

 GO 
 DECLARE @room_kind_id varchar(150); 
 DECLARE @room_kind_name nvarchar(150); 
 SET @room_kind_id = 'RK00004'; 
 SET @room_kind_name = N'Loại phòng D'; 
 INSERT INTO LOAI_PHONG (ID_LP, TEN_LP) 
 SELECT @room_kind_id, @room_kind_name 
 WHERE NOT EXISTS (SELECT ID_LP FROM LOAI_PHONG WHERE ID_LP = @room_kind_id); 

