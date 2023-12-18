 USE hotel_management; 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @name nvarchar(150); 
 DECLARE @description nvarchar(max); 
 DECLARE @price decimal(10, 2); 
 DECLARE @applied_date date; 
 SET @id = 'SC00001'; 
 SET @name = N'Quá giờ'; 
 SET @description = N'Thời gian thuê quá thời hạn'; 
 SET @price = 150000; 
 SET @applied_date = CONVERT(date, '01/05/2023', 105); 
 INSERT INTO PHU_THU (ID_PHU_THU, TEN_PHU_THU, LY_DO) 
 SELECT @id, @name, @description 
 WHERE NOT EXISTS (SELECT ID_PHU_THU FROM PHU_THU WHERE ID_PHU_THU = @id); 
 INSERT INTO GIA_PHU_THU (ID_PHU_THU, NGAY_AP_DUNG, GIA, ID_NV) 
 SELECT @id, @applied_date, @price, 'EMPL00001' 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @name nvarchar(150); 
 DECLARE @description nvarchar(max); 
 DECLARE @price decimal(10, 2); 
 DECLARE @applied_date date; 
 SET @id = 'SC00002'; 
 SET @name = N'Dư người'; 
 SET @description = N'Ở nhiều hơn số người quy định của phòng'; 
 SET @price = 200000; 
 SET @applied_date = CONVERT(date, '15/08/2023', 105); 
 INSERT INTO PHU_THU (ID_PHU_THU, TEN_PHU_THU, LY_DO) 
 SELECT @id, @name, @description 
 WHERE NOT EXISTS (SELECT ID_PHU_THU FROM PHU_THU WHERE ID_PHU_THU = @id); 
 INSERT INTO GIA_PHU_THU (ID_PHU_THU, NGAY_AP_DUNG, GIA, ID_NV) 
 SELECT @id, @applied_date, @price, 'EMPL00001' 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @name nvarchar(150); 
 DECLARE @description nvarchar(max); 
 DECLARE @price decimal(10, 2); 
 DECLARE @applied_date date; 
 SET @id = 'SC00003'; 
 SET @name = N'Mất dép'; 
 SET @description = N'Mất dép có sẵn của khách sạn'; 
 SET @price = 50000; 
 SET @applied_date = CONVERT(date, '10/09/2023', 105); 
 INSERT INTO PHU_THU (ID_PHU_THU, TEN_PHU_THU, LY_DO) 
 SELECT @id, @name, @description 
 WHERE NOT EXISTS (SELECT ID_PHU_THU FROM PHU_THU WHERE ID_PHU_THU = @id); 
 INSERT INTO GIA_PHU_THU (ID_PHU_THU, NGAY_AP_DUNG, GIA, ID_NV) 
 SELECT @id, @applied_date, @price, 'EMPL00001' 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @name nvarchar(150); 
 DECLARE @description nvarchar(max); 
 DECLARE @price decimal(10, 2); 
 DECLARE @applied_date date; 
 SET @id = 'SC00004'; 
 SET @name = N'Mất dù'; 
 SET @description = N'Mất dù có sẵn của khách sạn'; 
 SET @price = 100000; 
 SET @applied_date = CONVERT(date, '01/09/2023', 105); 
 INSERT INTO PHU_THU (ID_PHU_THU, TEN_PHU_THU, LY_DO) 
 SELECT @id, @name, @description 
 WHERE NOT EXISTS (SELECT ID_PHU_THU FROM PHU_THU WHERE ID_PHU_THU = @id); 
 INSERT INTO GIA_PHU_THU (ID_PHU_THU, NGAY_AP_DUNG, GIA, ID_NV) 
 SELECT @id, @applied_date, @price, 'EMPL00001' 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @name nvarchar(150); 
 DECLARE @description nvarchar(max); 
 DECLARE @price decimal(10, 2); 
 DECLARE @applied_date date; 
 SET @id = 'SC00005'; 
 SET @name = N'Hư giường'; 
 SET @description = N'Làm hư giường của khách sạn'; 
 SET @price = 1000000; 
 SET @applied_date = CONVERT(date, '01/10/2023', 105); 
 INSERT INTO PHU_THU (ID_PHU_THU, TEN_PHU_THU, LY_DO) 
 SELECT @id, @name, @description 
 WHERE NOT EXISTS (SELECT ID_PHU_THU FROM PHU_THU WHERE ID_PHU_THU = @id); 
 INSERT INTO GIA_PHU_THU (ID_PHU_THU, NGAY_AP_DUNG, GIA, ID_NV) 
 SELECT @id, @applied_date, @price, 'EMPL00001'

