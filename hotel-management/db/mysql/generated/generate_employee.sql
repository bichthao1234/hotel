 USE hotel_management; 

 SET @room_id = 'EMPL00001'; 
 SET @first_name = N'Nguyễn'; 
 SET @last_name = N'Duy Tân'; 
 SET @gender = '0'; 
 SET @birthday = STR_TO_DATE('19/04/1999', '%d/%m/%Y'); 
 SET @address = N'Bến Tre'; 
 SET @phone = N'0389211236'; 
 SET @email = N'tannd1904@gmail.com'; 
 SET @username = N'tannd1904'; 
 SET @password = N'$2a$10$QSYFYy.II3uDsndl8MYQyewp7iC20imtd.Krqo1GCmBGHXRHFaeXu'; 
 SELECT ID_BP INTO @department_id FROM BO_PHAN WHERE ID_BP = N'MANAGEMENT'; 
 INSERT INTO NHAN_VIEN (ID_NV, HO, TEN, PHAI, NGAY_SINH, DIA_CHI, SDT, EMAIL, USERNAME, PASSWORD, ID_BP) 
 SELECT @room_id, @first_name, @last_name, @gender, @birthday, @address, @phone, @email, @username, @password, @department_id 
 WHERE NOT EXISTS (SELECT USERNAME FROM NHAN_VIEN WHERE USERNAME = @username); 

 SET @room_id = 'EMPL00002'; 
 SET @first_name = N'Huỳnh'; 
 SET @last_name = N'Quốc Khánh'; 
 SET @gender = '0'; 
 SET @birthday = STR_TO_DATE('07/07/2000', '%d/%m/%Y'); 
 SET @address = N'Cà Mau'; 
 SET @phone = N'0987654321'; 
 SET @email = N'abc@gmail.com'; 
 SET @username = N'khanh123'; 
 SET @password = N'$2a$10$PabpMObsEI4I2UC4ACoLUu5eHfde82CcgM3FXr6UCgvb7PklW2H3u'; 
 SELECT ID_BP INTO @department_id FROM BO_PHAN WHERE ID_BP = N'MANAGEMENT'; 
 INSERT INTO NHAN_VIEN (ID_NV, HO, TEN, PHAI, NGAY_SINH, DIA_CHI, SDT, EMAIL, USERNAME, PASSWORD, ID_BP) 
 SELECT @room_id, @first_name, @last_name, @gender, @birthday, @address, @phone, @email, @username, @password, @department_id 
 WHERE NOT EXISTS (SELECT USERNAME FROM NHAN_VIEN WHERE USERNAME = @username); 

 SET @room_id = 'EMPL00003'; 
 SET @first_name = N'Phan'; 
 SET @last_name = N'Thị Bích Thảo'; 
 SET @gender = '1'; 
 SET @birthday = STR_TO_DATE('30/4/2001', '%d/%m/%Y'); 
 SET @address = N'Bình Định'; 
 SET @phone = N'0357845631'; 
 SET @email = N'thaoptb.bd@gmail.com'; 
 SET @username = N'thaoptb'; 
 SET @password = N'$2a$10$Phg0atBIZmLt1sQQLiRzduXnb71p1VNUELo5vtVHqGmqqunqt444.'; 
 SELECT ID_BP INTO @department_id FROM BO_PHAN WHERE ID_BP = N'MANAGEMENT'; 
 INSERT INTO NHAN_VIEN (ID_NV, HO, TEN, PHAI, NGAY_SINH, DIA_CHI, SDT, EMAIL, USERNAME, PASSWORD, ID_BP) 
 SELECT @room_id, @first_name, @last_name, @gender, @birthday, @address, @phone, @email, @username, @password, @department_id 
 WHERE NOT EXISTS (SELECT USERNAME FROM NHAN_VIEN WHERE USERNAME = @username); 

