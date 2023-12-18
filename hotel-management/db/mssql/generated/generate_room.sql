 USE hotel_management; 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.1001'; 
 SET @floor = '1'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '2'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.1002'; 
 SET @floor = '1'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '3'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.1003'; 
 SET @floor = '1'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '4'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.2001'; 
 SET @floor = '2'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '5'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.2002'; 
 SET @floor = '2'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '6'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.2003'; 
 SET @floor = '2'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '7'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.2004'; 
 SET @floor = '2'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '8'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.2005'; 
 SET @floor = '2'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '9'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.2006'; 
 SET @floor = '2'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '10'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.3001'; 
 SET @floor = '3'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '11'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.3002'; 
 SET @floor = '3'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '12'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.3003'; 
 SET @floor = '3'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '2'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.3004'; 
 SET @floor = '3'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '3'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.3005'; 
 SET @floor = '3'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '4'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.3006'; 
 SET @floor = '3'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '5'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.3007'; 
 SET @floor = '3'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '6'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.3008'; 
 SET @floor = '3'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '7'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.3009'; 
 SET @floor = '3'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '8'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.3010'; 
 SET @floor = '3'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '9'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.4001'; 
 SET @floor = '4'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '10'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.4002'; 
 SET @floor = '4'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '11'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.4003'; 
 SET @floor = '4'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '12'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.4004'; 
 SET @floor = '4'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '2'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

 GO 
 DECLARE @room_id varchar(150); 
 DECLARE @floor int; 
 DECLARE @status_id nvarchar(150); 
 DECLARE @room_class_id bigint; 
 SET @room_id = 'R.4005'; 
 SET @floor = '4'; 
 SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'Phòng trống'; 
 SET @room_class_id = '3'; 
 INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) 
 SELECT @room_id, @floor, @room_class_id, @status_id 
 WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); 

