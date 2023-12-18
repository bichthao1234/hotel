 USE hotel_management; 

 GO 
 DECLARE @room_status_id varchar(150); 
 DECLARE @room_status_name nvarchar(150); 
 SET @room_status_id = 'RS00001'; 
 SET @room_status_name = N'Phòng trống'; 
 INSERT INTO TRANG_THAI (ID_TT, TEN_TRANG_THAI) 
 SELECT @room_status_id, @room_status_name 
 WHERE NOT EXISTS (SELECT ID_TT FROM TRANG_THAI WHERE ID_TT = @room_status_id); 

 GO 
 DECLARE @room_status_id varchar(150); 
 DECLARE @room_status_name nvarchar(150); 
 SET @room_status_id = 'RS00002'; 
 SET @room_status_name = N'Đang cho thuê'; 
 INSERT INTO TRANG_THAI (ID_TT, TEN_TRANG_THAI) 
 SELECT @room_status_id, @room_status_name 
 WHERE NOT EXISTS (SELECT ID_TT FROM TRANG_THAI WHERE ID_TT = @room_status_id); 

 GO 
 DECLARE @room_status_id varchar(150); 
 DECLARE @room_status_name nvarchar(150); 
 SET @room_status_id = 'RS00003'; 
 SET @room_status_name = N'Đã đặt'; 
 INSERT INTO TRANG_THAI (ID_TT, TEN_TRANG_THAI) 
 SELECT @room_status_id, @room_status_name 
 WHERE NOT EXISTS (SELECT ID_TT FROM TRANG_THAI WHERE ID_TT = @room_status_id); 

 GO 
 DECLARE @room_status_id varchar(150); 
 DECLARE @room_status_name nvarchar(150); 
 SET @room_status_id = 'RS00004'; 
 SET @room_status_name = N'Bảo trì'; 
 INSERT INTO TRANG_THAI (ID_TT, TEN_TRANG_THAI) 
 SELECT @room_status_id, @room_status_name 
 WHERE NOT EXISTS (SELECT ID_TT FROM TRANG_THAI WHERE ID_TT = @room_status_id); 

