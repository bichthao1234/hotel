 USE hotel_management; 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @name nvarchar(150); 
 DECLARE @icon nvarchar(150); 
 SET @id = 'CNV00001'; 
 SET @name = N'Tủ quần áo'; 
 SET @icon = ''; 
 INSERT INTO TIEN_NGHI (ID_TN, TEN_TN, ICON) 
 SELECT @id, @name, @icon 
 WHERE NOT EXISTS (SELECT ID_TN FROM TIEN_NGHI WHERE ID_TN = @id); 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @name nvarchar(150); 
 DECLARE @icon nvarchar(150); 
 SET @id = 'CNV00002'; 
 SET @name = N'Phòng có bồn tắm'; 
 SET @icon = ''; 
 INSERT INTO TIEN_NGHI (ID_TN, TEN_TN, ICON) 
 SELECT @id, @name, @icon 
 WHERE NOT EXISTS (SELECT ID_TN FROM TIEN_NGHI WHERE ID_TN = @id); 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @name nvarchar(150); 
 DECLARE @icon nvarchar(150); 
 SET @id = 'CNV00003'; 
 SET @name = N'Wifi'; 
 SET @icon = ''; 
 INSERT INTO TIEN_NGHI (ID_TN, TEN_TN, ICON) 
 SELECT @id, @name, @icon 
 WHERE NOT EXISTS (SELECT ID_TN FROM TIEN_NGHI WHERE ID_TN = @id); 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @name nvarchar(150); 
 DECLARE @icon nvarchar(150); 
 SET @id = 'CNV00004'; 
 SET @name = N'Bàn làm việc'; 
 SET @icon = ''; 
 INSERT INTO TIEN_NGHI (ID_TN, TEN_TN, ICON) 
 SELECT @id, @name, @icon 
 WHERE NOT EXISTS (SELECT ID_TN FROM TIEN_NGHI WHERE ID_TN = @id); 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @name nvarchar(150); 
 DECLARE @icon nvarchar(150); 
 SET @id = 'CNV00005'; 
 SET @name = N'Truyền hình cáp/Vệ tinh'; 
 SET @icon = ''; 
 INSERT INTO TIEN_NGHI (ID_TN, TEN_TN, ICON) 
 SELECT @id, @name, @icon 
 WHERE NOT EXISTS (SELECT ID_TN FROM TIEN_NGHI WHERE ID_TN = @id); 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @name nvarchar(150); 
 DECLARE @icon nvarchar(150); 
 SET @id = 'CNV00006'; 
 SET @name = N'Khăn tắm'; 
 SET @icon = ''; 
 INSERT INTO TIEN_NGHI (ID_TN, TEN_TN, ICON) 
 SELECT @id, @name, @icon 
 WHERE NOT EXISTS (SELECT ID_TN FROM TIEN_NGHI WHERE ID_TN = @id); 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @name nvarchar(150); 
 DECLARE @icon nvarchar(150); 
 SET @id = 'CNV00007'; 
 SET @name = N'Vòi sen'; 
 SET @icon = ''; 
 INSERT INTO TIEN_NGHI (ID_TN, TEN_TN, ICON) 
 SELECT @id, @name, @icon 
 WHERE NOT EXISTS (SELECT ID_TN FROM TIEN_NGHI WHERE ID_TN = @id); 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @name nvarchar(150); 
 DECLARE @icon nvarchar(150); 
 SET @id = 'CNV00008'; 
 SET @name = N'Quầy bar mini'; 
 SET @icon = ''; 
 INSERT INTO TIEN_NGHI (ID_TN, TEN_TN, ICON) 
 SELECT @id, @name, @icon 
 WHERE NOT EXISTS (SELECT ID_TN FROM TIEN_NGHI WHERE ID_TN = @id); 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @name nvarchar(150); 
 DECLARE @icon nvarchar(150); 
 SET @id = 'CNV00009'; 
 SET @name = N'Ga trải giường, gối'; 
 SET @icon = ''; 
 INSERT INTO TIEN_NGHI (ID_TN, TEN_TN, ICON) 
 SELECT @id, @name, @icon 
 WHERE NOT EXISTS (SELECT ID_TN FROM TIEN_NGHI WHERE ID_TN = @id); 

