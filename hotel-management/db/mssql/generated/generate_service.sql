 USE hotel_management; 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @name nvarchar(150); 
 DECLARE @description nvarchar(max); 
 DECLARE @price decimal(10, 2); 
 DECLARE @applied_date date; 
 SET @id = 'SV00001'; 
 SET @name = N'Thuê xe máy'; 
 SET @description = N'Thuê xe máy theo ngày'; 
 SET @price = 150000; 
 SET @applied_date = CONVERT(date, '01/05/2023', 105); 
 INSERT INTO DICH_VU (ID_DV, TEN_DV, MO_TA) 
 SELECT @id, @name, @description 
 WHERE NOT EXISTS (SELECT ID_DV FROM DICH_VU WHERE ID_DV = @id); 
 INSERT INTO GIA_DICH_VU (ID_DV, NGAY_AP_DUNG, GIA, ID_NV) 
 SELECT @id, @applied_date, @price, 'EMPL00001' 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @name nvarchar(150); 
 DECLARE @description nvarchar(max); 
 DECLARE @price decimal(10, 2); 
 DECLARE @applied_date date; 
 SET @id = 'SV00002'; 
 SET @name = N'Giặt khô là hơi'; 
 SET @description = N'Giặt quần áo giao đến tại chỗ (theo lần)'; 
 SET @price = 50000; 
 SET @applied_date = CONVERT(date, '15/08/2023', 105); 
 INSERT INTO DICH_VU (ID_DV, TEN_DV, MO_TA) 
 SELECT @id, @name, @description 
 WHERE NOT EXISTS (SELECT ID_DV FROM DICH_VU WHERE ID_DV = @id); 
 INSERT INTO GIA_DICH_VU (ID_DV, NGAY_AP_DUNG, GIA, ID_NV) 
 SELECT @id, @applied_date, @price, 'EMPL00001' 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @name nvarchar(150); 
 DECLARE @description nvarchar(max); 
 DECLARE @price decimal(10, 2); 
 DECLARE @applied_date date; 
 SET @id = 'SV00003'; 
 SET @name = N'Thuê oto'; 
 SET @description = N'Thuê xe oto theo ngày'; 
 SET @price = 2000000; 
 SET @applied_date = CONVERT(date, '10/09/2023', 105); 
 INSERT INTO DICH_VU (ID_DV, TEN_DV, MO_TA) 
 SELECT @id, @name, @description 
 WHERE NOT EXISTS (SELECT ID_DV FROM DICH_VU WHERE ID_DV = @id); 
 INSERT INTO GIA_DICH_VU (ID_DV, NGAY_AP_DUNG, GIA, ID_NV) 
 SELECT @id, @applied_date, @price, 'EMPL00001' 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @name nvarchar(150); 
 DECLARE @description nvarchar(max); 
 DECLARE @price decimal(10, 2); 
 DECLARE @applied_date date; 
 SET @id = 'SV00004'; 
 SET @name = N'Cắt tóc'; 
 SET @description = N'Cắt tóc bao đẹp (theo lần)'; 
 SET @price = 100000; 
 SET @applied_date = CONVERT(date, '01/09/2023', 105); 
 INSERT INTO DICH_VU (ID_DV, TEN_DV, MO_TA) 
 SELECT @id, @name, @description 
 WHERE NOT EXISTS (SELECT ID_DV FROM DICH_VU WHERE ID_DV = @id); 
 INSERT INTO GIA_DICH_VU (ID_DV, NGAY_AP_DUNG, GIA, ID_NV) 
 SELECT @id, @applied_date, @price, 'EMPL00001' 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @name nvarchar(150); 
 DECLARE @description nvarchar(max); 
 DECLARE @price decimal(10, 2); 
 DECLARE @applied_date date; 
 SET @id = 'SV00005'; 
 SET @name = N'Xông hơi'; 
 SET @description = N'Xông hơi giải trí'; 
 SET @price = 400000; 
 SET @applied_date = CONVERT(date, '01/10/2023', 105); 
 INSERT INTO DICH_VU (ID_DV, TEN_DV, MO_TA) 
 SELECT @id, @name, @description 
 WHERE NOT EXISTS (SELECT ID_DV FROM DICH_VU WHERE ID_DV = @id); 
 INSERT INTO GIA_DICH_VU (ID_DV, NGAY_AP_DUNG, GIA, ID_NV) 
 SELECT @id, @applied_date, @price, 'EMPL00001' 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @name nvarchar(150); 
 DECLARE @description nvarchar(max); 
 DECLARE @price decimal(10, 2); 
 DECLARE @applied_date date; 
 SET @id = 'SV00006'; 
 SET @name = N'Massage'; 
 SET @description = N'Massage êm ái tận nốc'; 
 SET @price = 700000; 
 SET @applied_date = CONVERT(date, '01/09/2023', 105); 
 INSERT INTO DICH_VU (ID_DV, TEN_DV, MO_TA) 
 SELECT @id, @name, @description 
 WHERE NOT EXISTS (SELECT ID_DV FROM DICH_VU WHERE ID_DV = @id); 
 INSERT INTO GIA_DICH_VU (ID_DV, NGAY_AP_DUNG, GIA, ID_NV) 
 SELECT @id, @applied_date, @price, 'EMPL00001' 

