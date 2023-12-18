 USE hotel_management; 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @description nvarchar(max); 
 DECLARE @start_date date; 
 DECLARE @end_date date; 
 SET @id = 'PRM00001'; 
 SET @description = N'Khuyến mãi mừng 123'; 
 SET @start_date = CONVERT(date, '19/04/2023', 105); 
 SET @end_date = CONVERT(date, '19/05/2023', 105); 
 INSERT INTO KHUYEN_MAI (ID_KM, MO_TA_KM, NGAY_BAT_DAU, NGAY_KET_THUC) 
 SELECT @id, @description, @start_date, @end_date 
 WHERE NOT EXISTS (SELECT ID_KM FROM KHUYEN_MAI WHERE ID_KM = @id); 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @description nvarchar(max); 
 DECLARE @start_date date; 
 DECLARE @end_date date; 
 SET @id = 'PRM00002'; 
 SET @description = N'Đại lễ 456 giảm sốc'; 
 SET @start_date = CONVERT(date, '20/10/2023', 105); 
 SET @end_date = CONVERT(date, '20/11/2023', 105); 
 INSERT INTO KHUYEN_MAI (ID_KM, MO_TA_KM, NGAY_BAT_DAU, NGAY_KET_THUC) 
 SELECT @id, @description, @start_date, @end_date 
 WHERE NOT EXISTS (SELECT ID_KM FROM KHUYEN_MAI WHERE ID_KM = @id); 

 GO 
 DECLARE @id varchar(150); 
 DECLARE @description nvarchar(max); 
 DECLARE @start_date date; 
 DECLARE @end_date date; 
 SET @id = 'PRM00003'; 
 SET @description = N'Siêu hạ giá 789 hot deal'; 
 SET @start_date = CONVERT(date, '1/10/2023', 105); 
 SET @end_date = CONVERT(date, '15/10/2023', 105); 
 INSERT INTO KHUYEN_MAI (ID_KM, MO_TA_KM, NGAY_BAT_DAU, NGAY_KET_THUC) 
 SELECT @id, @description, @start_date, @end_date 
 WHERE NOT EXISTS (SELECT ID_KM FROM KHUYEN_MAI WHERE ID_KM = @id); 

