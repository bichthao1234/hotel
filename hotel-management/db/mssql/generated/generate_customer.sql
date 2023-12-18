 USE hotel_management; 

 GO 
 DECLARE @customer_id nvarchar(150); 
 DECLARE @first_name nvarchar(150); 
 DECLARE @last_name nvarchar(150); 
 DECLARE @phone nvarchar(150); 
 DECLARE @email nvarchar(150); 
 DECLARE @address nvarchar(150); 
 DECLARE @tax_number nvarchar(150); 
 SET @customer_id = '321760104'; 
 SET @first_name = N'Nguyễn'; 
 SET @last_name = N'Duy Tân'; 
 SET @phone = N'0389211236'; 
 SET @email = N'tannd1904@gmail.com'; 
 SET @address = N'Bến Tre'; 
 SET @tax_number = N'123456789'; 
 INSERT INTO KHACH_HANG (CMND, HO, TEN, SDT, EMAIL, DIA_CHI, MA_SO_THUE) 
 SELECT @customer_id, @first_name, @last_name, @phone, @email, @address, @tax_number 
 WHERE NOT EXISTS (SELECT CMND FROM KHACH_HANG WHERE CMND = @customer_id); 

 GO 
 DECLARE @customer_id nvarchar(150); 
 DECLARE @first_name nvarchar(150); 
 DECLARE @last_name nvarchar(150); 
 DECLARE @phone nvarchar(150); 
 DECLARE @email nvarchar(150); 
 DECLARE @address nvarchar(150); 
 DECLARE @tax_number nvarchar(150); 
 SET @customer_id = '3217610654'; 
 SET @first_name = N'Nguyễn'; 
 SET @last_name = N'Thị Phương Thảo'; 
 SET @phone = N'0376116779'; 
 SET @email = N'thaontp.buh@gmail.com'; 
 SET @address = N'Bến Tre'; 
 SET @tax_number = N'123456789'; 
 INSERT INTO KHACH_HANG (CMND, HO, TEN, SDT, EMAIL, DIA_CHI, MA_SO_THUE) 
 SELECT @customer_id, @first_name, @last_name, @phone, @email, @address, @tax_number 
 WHERE NOT EXISTS (SELECT CMND FROM KHACH_HANG WHERE CMND = @customer_id); 

