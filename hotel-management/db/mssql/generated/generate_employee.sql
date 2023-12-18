 USE hotel_management; 

 GO 
 DECLARE @room_id nvarchar(150); 
 DECLARE @first_name nvarchar(150); 
 DECLARE @last_name nvarchar(150); 
 DECLARE @gender bit; 
 DECLARE @birthday date; 
 DECLARE @address nvarchar(150); 
 DECLARE @phone nvarchar(150); 
 DECLARE @email nvarchar(150); 
 DECLARE @username nvarchar(150); 
 DECLARE @password nvarchar(150); 
 DECLARE @department_id nvarchar(150); 
 SET @room_id = 'EMPL00001'; 
 SET @first_name = N'Nguyễn'; 
 SET @last_name = N'Duy Tân'; 
 SET @gender = '0'; 
 SET @birthday = CONVERT(date, '19/04/1999', 105); 
 SET @address = N'Bến Tre'; 
 SET @phone = N'0389211236'; 
 SET @email = N'tannd1904@gmail.com'; 
 SET @username = N'tannd1904'; 
 SET @password = N'$2a$10$.3S72caW.D/Ibizbyx9fEefIKhivz.tu6Luqn4DNaI/eabi5h/3y2'; 
 SELECT @department_id = ID_BP FROM BO_PHAN WHERE ID_BP = N'MANAGEMENT'; 
 INSERT INTO NHAN_VIEN (ID_NV, HO, TEN, PHAI, NGAY_SINH, DIA_CHI, SDT, EMAIL, USERNAME, PASSWORD, ID_BP) 
 SELECT @room_id, @first_name, @last_name, @gender, @birthday, @address, @phone, @email, @username, @password, @department_id 
 WHERE NOT EXISTS (SELECT USERNAME FROM NHAN_VIEN WHERE USERNAME = @username); 

 GO 
 DECLARE @room_id nvarchar(150); 
 DECLARE @first_name nvarchar(150); 
 DECLARE @last_name nvarchar(150); 
 DECLARE @gender bit; 
 DECLARE @birthday date; 
 DECLARE @address nvarchar(150); 
 DECLARE @phone nvarchar(150); 
 DECLARE @email nvarchar(150); 
 DECLARE @username nvarchar(150); 
 DECLARE @password nvarchar(150); 
 DECLARE @department_id nvarchar(150); 
 SET @room_id = 'EMPL00002'; 
 SET @first_name = N'Huỳnh'; 
 SET @last_name = N'Quốc Khánh'; 
 SET @gender = '0'; 
 SET @birthday = CONVERT(date, '07/07/2000', 105); 
 SET @address = N'Cà Mau'; 
 SET @phone = N'0987654321'; 
 SET @email = N'abc@gmail.com'; 
 SET @username = N'khanh123'; 
 SET @password = N'$2a$10$bELBb0tSh.A0Gv/sINrJ1.W2GRZcfkiV0QnaDAbVk9d/k.8iXZWPK'; 
 SELECT @department_id = ID_BP FROM BO_PHAN WHERE ID_BP = N'MANAGEMENT'; 
 INSERT INTO NHAN_VIEN (ID_NV, HO, TEN, PHAI, NGAY_SINH, DIA_CHI, SDT, EMAIL, USERNAME, PASSWORD, ID_BP) 
 SELECT @room_id, @first_name, @last_name, @gender, @birthday, @address, @phone, @email, @username, @password, @department_id 
 WHERE NOT EXISTS (SELECT USERNAME FROM NHAN_VIEN WHERE USERNAME = @username); 

 GO 
 DECLARE @room_id nvarchar(150); 
 DECLARE @first_name nvarchar(150); 
 DECLARE @last_name nvarchar(150); 
 DECLARE @gender bit; 
 DECLARE @birthday date; 
 DECLARE @address nvarchar(150); 
 DECLARE @phone nvarchar(150); 
 DECLARE @email nvarchar(150); 
 DECLARE @username nvarchar(150); 
 DECLARE @password nvarchar(150); 
 DECLARE @department_id nvarchar(150); 
 SET @room_id = 'EMPL00003'; 
 SET @first_name = N'Phan'; 
 SET @last_name = N'Thị Bích Thảo'; 
 SET @gender = '1'; 
 SET @birthday = CONVERT(date, '30/4/2001', 105); 
 SET @address = N'Bình Định'; 
 SET @phone = N'0357845631'; 
 SET @email = N'thaoptb.bd@gmail.com'; 
 SET @username = N'thaoptb'; 
 SET @password = N'$2a$10$p5AEIyBKr5FSBatvlrCunuueb/mNrALx6g/5B3rDTHCdVq/rNAqFa'; 
 SELECT @department_id = ID_BP FROM BO_PHAN WHERE ID_BP = N'MANAGEMENT'; 
 INSERT INTO NHAN_VIEN (ID_NV, HO, TEN, PHAI, NGAY_SINH, DIA_CHI, SDT, EMAIL, USERNAME, PASSWORD, ID_BP) 
 SELECT @room_id, @first_name, @last_name, @gender, @birthday, @address, @phone, @email, @username, @password, @department_id 
 WHERE NOT EXISTS (SELECT USERNAME FROM NHAN_VIEN WHERE USERNAME = @username); 

