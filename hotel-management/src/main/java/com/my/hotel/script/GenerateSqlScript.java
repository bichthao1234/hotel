package com.my.hotel.script;

import com.my.hotel.utils.Utilities;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.StringJoiner;
@Slf4j
@Component
public class GenerateSqlScript {

	public static final String INPUT_FILE = "SqlScriptMatrix.xlsx";
	public static final String OUTPUT_PREFIX = "./db/%s/generated/";
	public static final String FILE_NAME_PERMISSION = "generate_permission.sql";
	public static final String FILE_NAME_DEPARTMENT = "generate_department.sql";
	public static final String FILE_NAME_CUSTOMER = "generate_customer.sql";
	public static final String FILE_NAME_EMPLOYEE = "generate_employee.sql";
	public static final String FILE_NAME_ROOM_KIND = "generate_room_kind.sql";
	public static final String FILE_NAME_ROOM_TYPE = "generate_room_type.sql";
	public static final String FILE_NAME_ROOM_STATUS = "generate_room_status.sql";
	public static final String FILE_NAME_CONVENIENCE = "generate_convenience.sql";
	public static final String FILE_NAME_PROMOTION = "generate_promotion.sql";
	public static final String FILE_NAME_SERVICE = "generate_service.sql";
	public static final String FILE_NAME_SURCHARGE = "generate_surcharge.sql";
	public static final String FILE_NAME_ROOM = "generate_room.sql";

	@Autowired
	private PasswordEncoder passwordEncoder;

	public void generateScript(String sqlDialect) throws Exception {

		FileInputStream fis = new FileInputStream(INPUT_FILE);
		
		// Create Workbook instance holding reference to .xlsx file
		XSSFWorkbook workbook = new XSSFWorkbook(fis);

		// Department
		generateDepartment(workbook, sqlDialect);

		// Permission
		generatePermission(workbook, sqlDialect);

		// Customer
		generateCustomer(workbook, sqlDialect);

		// Employee
		generateEmployee(workbook, sqlDialect);

		// RoomKind
		generateRoomKind(workbook, sqlDialect);

		// RoomType
		generateRoomType(workbook, sqlDialect);

		// RoomStatus
		generateRoomStatus(workbook, sqlDialect);

		// Room
		generateRoom(workbook, sqlDialect);

		// Convenience
		generateConvenience(workbook, sqlDialect);

		// Promotion
		generatePromotion(workbook, sqlDialect);

		// Service
		generateService(workbook, sqlDialect);

		// Surcharge
		generateSurcharge(workbook, sqlDialect);

		workbook.close();
        fis.close();
		
	}

	private String getFilePath(String sqlDialect, String fileName) {
		return String.format(OUTPUT_PREFIX, sqlDialect) + fileName;
	}

	private String getId(String sqlDialect, String id) {
		String sqlFunction = sqlDialect.equals("mssql") ? "NEWID()" : "UUID()";
		return Utilities.isEmptyString(id) ? sqlFunction : "'" + id + "'";
	}
	
	private void generatePermission(XSSFWorkbook workbook, String sqlDialect) throws Exception {
		String filePath = getFilePath(sqlDialect, FILE_NAME_PERMISSION);
		Path path = Paths.get(filePath);
		Files.deleteIfExists(path);
		DataFormatter formatter = new DataFormatter();
		
		// Get ToolsCatalog sheet
		XSSFSheet xssfSheet = workbook.getSheet("Permission");
		
		StringJoiner sjQuery = new StringJoiner("\n", "", "\n");
		sjQuery.add(" USE hotel_management; ");

		Files.createFile(path);
		Files.write(path, (sjQuery + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		
		for (Row row : xssfSheet) {
			
			// Skip the first rows 
        	if(row.getRowNum() == 0) {
        	       continue; 
        	}
        	
        	sjQuery = new StringJoiner("\n", "", "\n");
			if (sqlDialect.equals("mssql")) {
				sjQuery.add(" GO ");
				sjQuery.add(" DECLARE @permission_id varchar(150); ");
//				sjQuery.add(" DECLARE @permission_code nvarchar(150); ");
				sjQuery.add(" DECLARE @permission_name nvarchar(150); ");
			}
    		
    		// ID
			Cell cellId = row.getCell(0);
			String id = formatter.formatCellValue(cellId);
			id = id.replace("'", "''").trim();
			
    		// Code
//			Cell cellCode = row.getCell(1);
//			String code = formatter.formatCellValue(cellCode);
//			code = code.replace("'", "''").trim();

    		// Name
			Cell cellName = row.getCell(1);
			String name = formatter.formatCellValue(cellName);
			name = name.replace("'", "''").trim();

//			sjQuery.add(" SET @permission_id = " + getId(sqlDialect, id) + "; ");
//			sjQuery.add(" SET @permission_code = N'" + code + "'; ");
			sjQuery.add(" SET @permission_id = '" + id + "'; ");
			sjQuery.add(" SET @permission_name = N'" + name + "'; ");
			
			sjQuery.add(" INSERT INTO NHOM_QUYEN (ID_NQ, TEN_NQ) ");
			sjQuery.add(" SELECT @permission_id, @permission_name ");
			sjQuery.add(" WHERE NOT EXISTS (SELECT ID_NQ FROM NHOM_QUYEN WHERE ID_NQ = @permission_id); ");
			
			String content = sjQuery.toString();
			log.info(content);
			
			Files.write(path, (content + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		}			
	}

	private void generateDepartment(XSSFWorkbook workbook, String sqlDialect) throws Exception {
		String filePath = getFilePath(sqlDialect, FILE_NAME_DEPARTMENT);
		Path path = Paths.get(filePath);
		Files.deleteIfExists(path);
		DataFormatter formatter = new DataFormatter();

		// Get ToolsCatalog sheet
		XSSFSheet xssfSheet = workbook.getSheet("Department");

		StringJoiner sjQuery = new StringJoiner("\n", "", "\n");
		sjQuery.add(" USE hotel_management; ");

		Files.createFile(path);
		Files.write(path, (sjQuery + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

		for (Row row : xssfSheet) {

			// Skip the first rows
			if(row.getRowNum() == 0) {
				continue;
			}

			sjQuery = new StringJoiner("\n", "", "\n");
			if (sqlDialect.equals("mssql")) {
				sjQuery.add(" GO ");
				sjQuery.add(" DECLARE @department_id varchar(150); ");
//				sjQuery.add(" DECLARE @department_code nvarchar(150); ");
				sjQuery.add(" DECLARE @department_name nvarchar(150); ");
			}

			// ID
			Cell cellId = row.getCell(0);
			String id = formatter.formatCellValue(cellId);
			id = id.replace("'", "''").trim();

			// Code
//			Cell cellCode = row.getCell(1);
//			String code = formatter.formatCellValue(cellCode);
//			code = code.replace("'", "''").trim();

			// Name
			Cell cellName = row.getCell(1);
			String name = formatter.formatCellValue(cellName);
			name = name.replace("'", "''").trim();

//			sjQuery.add(" SET @department_id = " + getId(sqlDialect, id) + "; ");
//			sjQuery.add(" SET @department_code = N'" + code + "'; ");
			sjQuery.add(" SET @department_id = '" + id + "'; ");
			sjQuery.add(" SET @department_name = N'" + name + "'; ");

			sjQuery.add(" INSERT INTO BO_PHAN (ID_BP, TEN_BP) ");
			sjQuery.add(" SELECT @department_id, @department_name ");
			sjQuery.add(" WHERE NOT EXISTS (SELECT ID_BP FROM BO_PHAN WHERE ID_BP = @department_id); ");

			String content = sjQuery.toString();
			log.info(content);

			Files.write(path, (content + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		}
	}

	private void generateCustomer(XSSFWorkbook workbook, String sqlDialect) throws Exception {
		String filePath = getFilePath(sqlDialect, FILE_NAME_CUSTOMER);
		Path path = Paths.get(filePath);
		Files.deleteIfExists(path);
		DataFormatter formatter = new DataFormatter();

		// Get ToolsCatalog sheet
		XSSFSheet xssfSheet = workbook.getSheet("Customer");

		StringJoiner sjQuery = new StringJoiner("\n", "", "\n");
		sjQuery.add(" USE hotel_management; ");

		Files.createFile(path);
		Files.write(path, (sjQuery + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

		for (Row row : xssfSheet) {

			// Skip the first rows
			if(row.getRowNum() == 0) {
				continue;
			}

			sjQuery = new StringJoiner("\n", "", "\n");
			if (sqlDialect.equals("mssql")) {
				sjQuery.add(" GO ");
				sjQuery.add(" DECLARE @customer_id nvarchar(150); ");
				sjQuery.add(" DECLARE @first_name nvarchar(150); ");
				sjQuery.add(" DECLARE @last_name nvarchar(150); ");
				sjQuery.add(" DECLARE @phone nvarchar(150); ");
				sjQuery.add(" DECLARE @email nvarchar(150); ");
				sjQuery.add(" DECLARE @address nvarchar(150); ");
				sjQuery.add(" DECLARE @tax_number nvarchar(150); ");
			}

			// ID
			Cell cellId = row.getCell(0);
			String id = formatter.formatCellValue(cellId);
			id = id.replace("'", "''").trim();

			// First name
			Cell cellFirstName = row.getCell(1);
			String firstName = formatter.formatCellValue(cellFirstName);
			firstName = firstName.replace("'", "''").trim();

			// Last name
			Cell cellName = row.getCell(2);
			String lastName = formatter.formatCellValue(cellName);
			lastName = lastName.replace("'", "''").trim();

			// Phone
			Cell cellPhone = row.getCell(3);
			String phone = formatter.formatCellValue(cellPhone);
			phone = phone.replace("'", "''").trim();

			// Email
			Cell cellEmail = row.getCell(4);
			String email = formatter.formatCellValue(cellEmail);
			email = email.replace("'", "''").trim();

			// Address
			Cell cellAddress = row.getCell(5);
			String address = formatter.formatCellValue(cellAddress);
			address = address.replace("'", "''").trim();

			// Tax number
			Cell cellTax = row.getCell(6);
			String taxNumber = formatter.formatCellValue(cellTax);
			taxNumber = taxNumber.replace("'", "''").trim();

			sjQuery.add(" SET @customer_id = '" + id + "'; ");
			sjQuery.add(" SET @first_name = N'" + firstName + "'; ");
			sjQuery.add(" SET @last_name = N'" + lastName + "'; ");
			sjQuery.add(" SET @phone = N'" + phone + "'; ");
			sjQuery.add(" SET @email = N'" + email + "'; ");
			sjQuery.add(" SET @address = N'" + address + "'; ");
			sjQuery.add(" SET @tax_number = N'" + taxNumber + "'; ");

			sjQuery.add(" INSERT INTO KHACH_HANG (CMND, HO, TEN, SDT, EMAIL, DIA_CHI, MA_SO_THUE) ");
			sjQuery.add(" SELECT @customer_id, @first_name, @last_name, @phone, @email, @address, @tax_number ");
			sjQuery.add(" WHERE NOT EXISTS (SELECT CMND FROM KHACH_HANG WHERE CMND = @customer_id); ");

			String content = sjQuery.toString();
			log.info(content);

			Files.write(path, (content + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		}
	}

	private void generateEmployee(XSSFWorkbook workbook, String sqlDialect) throws Exception {
		String filePath = getFilePath(sqlDialect, FILE_NAME_EMPLOYEE);
		Path path = Paths.get(filePath);
		Files.deleteIfExists(path);
		DataFormatter formatter = new DataFormatter();

		// Get ToolsCatalog sheet
		XSSFSheet xssfSheet = workbook.getSheet("Employee");

		StringJoiner sjQuery = new StringJoiner("\n", "", "\n");
		sjQuery.add(" USE hotel_management; ");

		Files.createFile(path);
		Files.write(path, (sjQuery + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

		for (Row row : xssfSheet) {

			// Skip the first rows
			if(row.getRowNum() == 0) {
				continue;
			}
			sjQuery = new StringJoiner("\n", "", "\n");
			if (sqlDialect.equals("mssql")) {
				sjQuery.add(" GO ");
				sjQuery.add(" DECLARE @room_id nvarchar(150); ");
				sjQuery.add(" DECLARE @first_name nvarchar(150); ");
				sjQuery.add(" DECLARE @last_name nvarchar(150); ");
				sjQuery.add(" DECLARE @gender bit; ");
				sjQuery.add(" DECLARE @birthday date; ");
				sjQuery.add(" DECLARE @address nvarchar(150); ");
				sjQuery.add(" DECLARE @phone nvarchar(150); ");
				sjQuery.add(" DECLARE @email nvarchar(150); ");
				sjQuery.add(" DECLARE @username nvarchar(150); ");
				sjQuery.add(" DECLARE @password nvarchar(150); ");
				sjQuery.add(" DECLARE @department_id nvarchar(150); ");
			}

			// ID
			Cell cellId = row.getCell(0);
			String id = formatter.formatCellValue(cellId);
			id = id.replace("'", "''").trim();

			// First name
			Cell cellCode = row.getCell(1);
			String firstName = formatter.formatCellValue(cellCode);
			firstName = firstName.replace("'", "''").trim();

			// Last name
			Cell cellName = row.getCell(2);
			String lastName = formatter.formatCellValue(cellName);
			lastName = lastName.replace("'", "''").trim();

			// Gender
			Cell cellGender = row.getCell(3);
			String gender = formatter.formatCellValue(cellGender);
			gender = gender.replace("'", "''").trim();


			// Birthday
			Cell cellBirthday = row.getCell(4);
			String birthday = formatter.formatCellValue(cellBirthday);
			birthday = birthday.replace("'", "''").trim();

			// Address
			Cell cellAddress = row.getCell(5);
			String address = formatter.formatCellValue(cellAddress);
			address = address.replace("'", "''").trim();

			// Phone
			Cell cellPhone = row.getCell(6);
			String phone = formatter.formatCellValue(cellPhone);
			phone = phone.replace("'", "''").trim();

			// Email
			Cell cellEmail = row.getCell(7);
			String email = formatter.formatCellValue(cellEmail);
			email = email.replace("'", "''").trim();

			// Username
			Cell cellUsername = row.getCell(8);
			String username = formatter.formatCellValue(cellUsername);
			username = username.replace("'", "''").trim();

			// Password
			Cell cellPassword = row.getCell(9);
			String password = formatter.formatCellValue(cellPassword);
			password = password.replace("'", "''").trim();
			password = passwordEncoder.encode(password);

			// Department
			Cell cellDepartment = row.getCell(10);
			String department = formatter.formatCellValue(cellDepartment);
			department = department.replace("'", "''").trim();

			sjQuery.add(" SET @room_id = " + getId(sqlDialect, id) + "; ");
			sjQuery.add(" SET @first_name = N'" + firstName + "'; ");
			sjQuery.add(" SET @last_name = N'" + lastName + "'; ");
			sjQuery.add(" SET @gender = '" +
					(gender.equalsIgnoreCase("Nam") || gender.equalsIgnoreCase("Male") ? "0" : "1") + "'; ");
			if (sqlDialect.equals("mssql")) {
				sjQuery.add(" SET @birthday = CONVERT(date, '" + birthday + "', 105); ");
			}
			if (sqlDialect.equals("mysql")) {
				sjQuery.add(" SET @birthday = STR_TO_DATE('" + birthday + "', '%d/%m/%Y'); ");
			}
			sjQuery.add(" SET @address = N'" + address + "'; ");
			sjQuery.add(" SET @phone = N'" + phone + "'; ");
			sjQuery.add(" SET @email = N'" + email + "'; ");
			sjQuery.add(" SET @username = N'" + username + "'; ");
			sjQuery.add(" SET @password = N'" + password + "'; ");
			if (sqlDialect.equals("mssql")) {
				sjQuery.add(" SELECT @department_id = ID_BP FROM BO_PHAN WHERE ID_BP = N'" + department + "'; "); // department_id
			}
			if (sqlDialect.equals("mysql")) {
				sjQuery.add(" SELECT ID_BP INTO @department_id FROM BO_PHAN WHERE ID_BP = N'" + department + "'; "); // department_id
			}

			sjQuery.add(" INSERT INTO NHAN_VIEN (ID_NV, HO, TEN, PHAI, NGAY_SINH, DIA_CHI, SDT, EMAIL, USERNAME, PASSWORD, ID_BP) ");
			sjQuery.add(" SELECT @room_id, @first_name, @last_name, @gender, @birthday, @address, @phone, @email, @username, @password, @department_id ");
			sjQuery.add(" WHERE NOT EXISTS (SELECT USERNAME FROM NHAN_VIEN WHERE USERNAME = @username); ");

			String content = sjQuery.toString();
			log.info(content);

			Files.write(path, (content + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		}
	}

	private void generateRoomKind(XSSFWorkbook workbook, String sqlDialect) throws Exception {
		String filePath = getFilePath(sqlDialect, FILE_NAME_ROOM_KIND);
		Path path = Paths.get(filePath);
		Files.deleteIfExists(path);
		DataFormatter formatter = new DataFormatter();

		// Get ToolsCatalog sheet
		XSSFSheet xssfSheet = workbook.getSheet("RoomKind");

		StringJoiner sjQuery = new StringJoiner("\n", "", "\n");
		sjQuery.add(" USE hotel_management; ");

		Files.createFile(path);
		Files.write(path, (sjQuery + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

		for (Row row : xssfSheet) {

			// Skip the first rows
			if(row.getRowNum() == 0) {
				continue;
			}

			sjQuery = new StringJoiner("\n", "", "\n");
			if (sqlDialect.equals("mssql")) {
				sjQuery.add(" GO ");
				sjQuery.add(" DECLARE @room_kind_id varchar(150); ");
				sjQuery.add(" DECLARE @room_kind_name nvarchar(150); ");
			}

			// ID
			Cell cellId = row.getCell(0);
			String id = formatter.formatCellValue(cellId);
			id = id.replace("'", "''").trim();

			// Name
			Cell cellName = row.getCell(1);
			String name = formatter.formatCellValue(cellName);
			name = name.replace("'", "''").trim();

			sjQuery.add(" SET @room_kind_id = '" + id + "'; ");
			sjQuery.add(" SET @room_kind_name = N'" + name + "'; ");

			sjQuery.add(" INSERT INTO LOAI_PHONG (ID_LP, TEN_LP) ");
			sjQuery.add(" SELECT @room_kind_id, @room_kind_name ");
			sjQuery.add(" WHERE NOT EXISTS (SELECT ID_LP FROM LOAI_PHONG WHERE ID_LP = @room_kind_id); ");

			String content = sjQuery.toString();
			log.info(content);

			Files.write(path, (content + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		}
	}

	private void generateRoomType(XSSFWorkbook workbook, String sqlDialect) throws Exception {
		String filePath = getFilePath(sqlDialect, FILE_NAME_ROOM_TYPE);
		Path path = Paths.get(filePath);
		Files.deleteIfExists(path);
		DataFormatter formatter = new DataFormatter();

		// Get ToolsCatalog sheet
		XSSFSheet xssfSheet = workbook.getSheet("RoomType");

		StringJoiner sjQuery = new StringJoiner("\n", "", "\n");
		sjQuery.add(" USE hotel_management; ");

		Files.createFile(path);
		Files.write(path, (sjQuery + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

		for (Row row : xssfSheet) {

			// Skip the first rows
			if(row.getRowNum() == 0) {
				continue;
			}

			sjQuery = new StringJoiner("\n", "", "\n");
			if (sqlDialect.equals("mssql")) {
				sjQuery.add(" GO ");
				sjQuery.add(" DECLARE @room_type_id varchar(150); ");
				sjQuery.add(" DECLARE @room_type_name nvarchar(150); ");
			}

			// ID
			Cell cellId = row.getCell(0);
			String id = formatter.formatCellValue(cellId);
			id = id.replace("'", "''").trim();

			// Name
			Cell cellName = row.getCell(1);
			String name = formatter.formatCellValue(cellName);
			name = name.replace("'", "''").trim();

			sjQuery.add(" SET @room_type_id = '" + id + "'; ");
			sjQuery.add(" SET @room_type_name = N'" + name + "'; ");

			sjQuery.add(" INSERT INTO KIEU_PHONG (ID_KP, TEN_KP) ");
			sjQuery.add(" SELECT @room_type_id, @room_type_name ");
			sjQuery.add(" WHERE NOT EXISTS (SELECT ID_KP FROM KIEU_PHONG WHERE ID_KP = @room_type_id); ");

			String content = sjQuery.toString();
			log.info(content);

			Files.write(path, (content + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		}
	}

	private void generateRoomStatus(XSSFWorkbook workbook, String sqlDialect) throws Exception {
		String filePath = getFilePath(sqlDialect, FILE_NAME_ROOM_STATUS);
		Path path = Paths.get(filePath);
		Files.deleteIfExists(path);
		DataFormatter formatter = new DataFormatter();

		// Get ToolsCatalog sheet
		XSSFSheet xssfSheet = workbook.getSheet("RoomStatus");

		StringJoiner sjQuery = new StringJoiner("\n", "", "\n");
		sjQuery.add(" USE hotel_management; ");

		Files.createFile(path);
		Files.write(path, (sjQuery + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

		for (Row row : xssfSheet) {

			// Skip the first rows
			if(row.getRowNum() == 0) {
				continue;
			}

			sjQuery = new StringJoiner("\n", "", "\n");
			if (sqlDialect.equals("mssql")) {
				sjQuery.add(" GO ");
				sjQuery.add(" DECLARE @room_status_id varchar(150); ");
				sjQuery.add(" DECLARE @room_status_name nvarchar(150); ");
			}

			// ID
			Cell cellId = row.getCell(0);
			String id = formatter.formatCellValue(cellId);
			id = id.replace("'", "''").trim();

			// Name
			Cell cellName = row.getCell(1);
			String name = formatter.formatCellValue(cellName);
			name = name.replace("'", "''").trim();

			sjQuery.add(" SET @room_status_id = '" + id + "'; ");
			sjQuery.add(" SET @room_status_name = N'" + name + "'; ");

			sjQuery.add(" INSERT INTO TRANG_THAI (ID_TT, TEN_TRANG_THAI) ");
			sjQuery.add(" SELECT @room_status_id, @room_status_name ");
			sjQuery.add(" WHERE NOT EXISTS (SELECT ID_TT FROM TRANG_THAI WHERE ID_TT = @room_status_id); ");

			String content = sjQuery.toString();
			log.info(content);

			Files.write(path, (content + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		}
	}

	private void generateRoom(XSSFWorkbook workbook, String sqlDialect) throws Exception {
		String filePath = getFilePath(sqlDialect, FILE_NAME_ROOM);
		Path path = Paths.get(filePath);
		Files.deleteIfExists(path);
		DataFormatter formatter = new DataFormatter();

		// Get ToolsCatalog sheet
		XSSFSheet xssfSheet = workbook.getSheet("Room");

		StringJoiner sjQuery = new StringJoiner("\n", "", "\n");
		sjQuery.add(" USE hotel_management; ");

		Files.createFile(path);
		Files.write(path, (sjQuery + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

		for (Row row : xssfSheet) {

			// Skip the first rows
			if(row.getRowNum() == 0) {
				continue;
			}

			sjQuery = new StringJoiner("\n", "", "\n");
			if (sqlDialect.equals("mssql")) {
				sjQuery.add(" GO ");
				sjQuery.add(" DECLARE @room_id varchar(150); ");
				sjQuery.add(" DECLARE @floor int; ");
				sjQuery.add(" DECLARE @status_id nvarchar(150); ");
				sjQuery.add(" DECLARE @room_class_id bigint; ");
			}

			// ID
			Cell cellId = row.getCell(0);
			String id = formatter.formatCellValue(cellId);
			id = id.replace("'", "''").trim();

			// Floor
			Cell cellFloor = row.getCell(1);
			String floor = formatter.formatCellValue(cellFloor);
			floor = floor.replace("'", "''").trim();

			// Status
			Cell cellStatus = row.getCell(2);
			String status = formatter.formatCellValue(cellStatus);
			status = status.replace("'", "''").trim();

			// Room Class
			Cell cellClass = row.getCell(3);
			String roomClass = formatter.formatCellValue(cellClass);
			roomClass = roomClass.replace("'", "''").trim();

			sjQuery.add(" SET @room_id = '" + id + "'; ");
			sjQuery.add(" SET @floor = '" + floor + "'; ");
			if (sqlDialect.equals("mssql")) {
				sjQuery.add(" SELECT @status_id = ID_TT FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'" + status + "'; "); // status_id
			}
			if (sqlDialect.equals("mysql")) {
				sjQuery.add(" SELECT ID_TT INTO @status_id FROM TRANG_THAI WHERE TEN_TRANG_THAI = N'" + status + "'; "); // status_id
			}
			sjQuery.add(" SET @room_class_id = '" + roomClass + "'; ");


			sjQuery.add(" INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) ");
			sjQuery.add(" SELECT @room_id, @floor, @room_class_id, @status_id ");
			sjQuery.add(" WHERE NOT EXISTS (SELECT SO_PHONG FROM PHONG WHERE SO_PHONG = @room_id); ");

			String content = sjQuery.toString();
			log.info(content);

			Files.write(path, (content + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		}
	}

	private void generateConvenience(XSSFWorkbook workbook, String sqlDialect) throws Exception {
		String filePath = getFilePath(sqlDialect, FILE_NAME_CONVENIENCE);
		Path path = Paths.get(filePath);
		Files.deleteIfExists(path);
		DataFormatter formatter = new DataFormatter();

		// Get ToolsCatalog sheet
		XSSFSheet xssfSheet = workbook.getSheet("Convenience");

		StringJoiner sjQuery = new StringJoiner("\n", "", "\n");
		sjQuery.add(" USE hotel_management; ");

		Files.createFile(path);
		Files.write(path, (sjQuery + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

		for (Row row : xssfSheet) {

			// Skip the first rows
			if(row.getRowNum() == 0) {
				continue;
			}

			sjQuery = new StringJoiner("\n", "", "\n");
			if (sqlDialect.equals("mssql")) {
				sjQuery.add(" GO ");
				sjQuery.add(" DECLARE @id varchar(150); ");
				sjQuery.add(" DECLARE @name nvarchar(150); ");
				sjQuery.add(" DECLARE @icon nvarchar(150); ");
			}

			// ID
			Cell cellId = row.getCell(0);
			String id = formatter.formatCellValue(cellId);
			id = id.replace("'", "''").trim();

			// Name
			Cell cellName = row.getCell(1);
			String name = formatter.formatCellValue(cellName);
			name = name.replace("'", "''").trim();

			// Icon
			Cell cellIcon = row.getCell(2);
			String icon = formatter.formatCellValue(cellIcon);
			icon = icon.replace("'", "''").trim();

			sjQuery.add(" SET @id = '" + id + "'; ");
			sjQuery.add(" SET @name = N'" + name + "'; ");
			sjQuery.add(" SET @icon = '" + icon + "'; ");


			sjQuery.add(" INSERT INTO TIEN_NGHI (ID_TN, TEN_TN, ICON) ");
			sjQuery.add(" SELECT @id, @name, @icon ");
			sjQuery.add(" WHERE NOT EXISTS (SELECT ID_TN FROM TIEN_NGHI WHERE ID_TN = @id); ");

			String content = sjQuery.toString();
			log.info(content);

			Files.write(path, (content + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		}
	}

	private void generatePromotion(XSSFWorkbook workbook, String sqlDialect) throws Exception {
		String filePath = getFilePath(sqlDialect, FILE_NAME_PROMOTION);
		Path path = Paths.get(filePath);
		Files.deleteIfExists(path);
		DataFormatter formatter = new DataFormatter();

		// Get ToolsCatalog sheet
		XSSFSheet xssfSheet = workbook.getSheet("Promotion");

		StringJoiner sjQuery = new StringJoiner("\n", "", "\n");
		sjQuery.add(" USE hotel_management; ");

		Files.createFile(path);
		Files.write(path, (sjQuery + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

		for (Row row : xssfSheet) {

			// Skip the first rows
			if(row.getRowNum() == 0) {
				continue;
			}

			sjQuery = new StringJoiner("\n", "", "\n");
			if (sqlDialect.equals("mssql")) {
				sjQuery.add(" GO ");
				sjQuery.add(" DECLARE @id varchar(150); ");
				sjQuery.add(" DECLARE @description nvarchar(max); ");
				sjQuery.add(" DECLARE @start_date date; ");
				sjQuery.add(" DECLARE @end_date date; ");
			}

			// ID
			Cell cellId = row.getCell(0);
			String id = formatter.formatCellValue(cellId);
			id = id.replace("'", "''").trim();

			// Description
			Cell cellDescription = row.getCell(1);
			String description = formatter.formatCellValue(cellDescription);
			description = description.replace("'", "''").trim();

			// Start Date
			Cell cellStartDate = row.getCell(2);
			String startDate = formatter.formatCellValue(cellStartDate);
			startDate = startDate.replace("'", "''").trim();

			// End Date
			Cell cellEndDate = row.getCell(3);
			String endDate = formatter.formatCellValue(cellEndDate);
			endDate = endDate.replace("'", "''").trim();

			sjQuery.add(" SET @id = '" + id + "'; ");
			sjQuery.add(" SET @description = N'" + description + "'; ");
			if (sqlDialect.equals("mssql")) {
				sjQuery.add(" SET @start_date = CONVERT(date, '" + startDate + "', 105); ");
				sjQuery.add(" SET @end_date = CONVERT(date, '" + endDate + "', 105); ");
			}


			sjQuery.add(" INSERT INTO KHUYEN_MAI (ID_KM, MO_TA_KM, NGAY_BAT_DAU, NGAY_KET_THUC) ");
			sjQuery.add(" SELECT @id, @description, @start_date, @end_date ");
			sjQuery.add(" WHERE NOT EXISTS (SELECT ID_KM FROM KHUYEN_MAI WHERE ID_KM = @id); ");

			String content = sjQuery.toString();
			log.info(content);

			Files.write(path, (content + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		}
	}

	private void generateService(XSSFWorkbook workbook, String sqlDialect) throws Exception {
		String filePath = getFilePath(sqlDialect, FILE_NAME_SERVICE);
		Path path = Paths.get(filePath);
		Files.deleteIfExists(path);
		DataFormatter formatter = new DataFormatter();

		// Get ToolsCatalog sheet
		XSSFSheet xssfSheet = workbook.getSheet("Service");

		StringJoiner sjQuery = new StringJoiner("\n", "", "\n");
		sjQuery.add(" USE hotel_management; ");

		Files.createFile(path);
		Files.write(path, (sjQuery + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

		for (Row row : xssfSheet) {

			// Skip the first rows
			if(row.getRowNum() == 0) {
				continue;
			}

			sjQuery = new StringJoiner("\n", "", "\n");
			if (sqlDialect.equals("mssql")) {
				sjQuery.add(" GO ");
				sjQuery.add(" DECLARE @id varchar(150); ");
				sjQuery.add(" DECLARE @name nvarchar(150); ");
				sjQuery.add(" DECLARE @description nvarchar(max); ");
				sjQuery.add(" DECLARE @price decimal(10, 2); ");
				sjQuery.add(" DECLARE @applied_date date; ");
			}

			// ID
			Cell cellId = row.getCell(0);
			String id = formatter.formatCellValue(cellId);
			id = id.replace("'", "''").trim();

			// Name
			Cell cellName = row.getCell(1);
			String name = formatter.formatCellValue(cellName);
			name = name.replace("'", "''").trim();

			// Description
			Cell cellDescription = row.getCell(2);
			String description = formatter.formatCellValue(cellDescription);
			description = description.replace("'", "''").trim();

			// Price
			Cell cellPrice = row.getCell(3);
			String price = formatter.formatCellValue(cellPrice);
			price = price.replace("'", "''").trim();

			// Date
			Cell cellDate = row.getCell(4);
			String date = formatter.formatCellValue(cellDate);
			date = date.replace("'", "''").trim();

			sjQuery.add(" SET @id = '" + id + "'; ");
			sjQuery.add(" SET @name = N'" + name + "'; ");
			sjQuery.add(" SET @description = N'" + description + "'; ");
			sjQuery.add(" SET @price = " + price + "; ");
			if (sqlDialect.equals("mssql")) {
				sjQuery.add(" SET @applied_date = CONVERT(date, '" + date + "', 105); ");
			}


			sjQuery.add(" INSERT INTO DICH_VU (ID_DV, TEN_DV, MO_TA) ");
			sjQuery.add(" SELECT @id, @name, @description ");
			sjQuery.add(" WHERE NOT EXISTS (SELECT ID_DV FROM DICH_VU WHERE ID_DV = @id); ");
			sjQuery.add(" INSERT INTO GIA_DICH_VU (ID_DV, NGAY_AP_DUNG, GIA, ID_NV) ");
			sjQuery.add(" SELECT @id, @applied_date, @price, 'EMPL00001' ");

			String content = sjQuery.toString();
			log.info(content);

			Files.write(path, (content + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		}
	}

	private void generateSurcharge(XSSFWorkbook workbook, String sqlDialect) throws Exception {
		String filePath = getFilePath(sqlDialect, FILE_NAME_SURCHARGE);
		Path path = Paths.get(filePath);
		Files.deleteIfExists(path);
		DataFormatter formatter = new DataFormatter();

		// Get ToolsCatalog sheet
		XSSFSheet xssfSheet = workbook.getSheet("Surcharge");

		StringJoiner sjQuery = new StringJoiner("\n", "", "\n");
		sjQuery.add(" USE hotel_management; ");

		Files.createFile(path);
		Files.write(path, (sjQuery + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

		for (Row row : xssfSheet) {

			// Skip the first rows
			if(row.getRowNum() == 0) {
				continue;
			}

			sjQuery = new StringJoiner("\n", "", "\n");
			if (sqlDialect.equals("mssql")) {
				sjQuery.add(" GO ");
				sjQuery.add(" DECLARE @id varchar(150); ");
				sjQuery.add(" DECLARE @name nvarchar(150); ");
				sjQuery.add(" DECLARE @description nvarchar(max); ");
				sjQuery.add(" DECLARE @price decimal(10, 2); ");
				sjQuery.add(" DECLARE @applied_date date; ");
			}

			// ID
			Cell cellId = row.getCell(0);
			String id = formatter.formatCellValue(cellId);
			id = id.replace("'", "''").trim();

			// Name
			Cell cellName = row.getCell(1);
			String name = formatter.formatCellValue(cellName);
			name = name.replace("'", "''").trim();

			// Description
			Cell cellDescription = row.getCell(2);
			String description = formatter.formatCellValue(cellDescription);
			description = description.replace("'", "''").trim();

			// Price
			Cell cellPrice = row.getCell(3);
			String price = formatter.formatCellValue(cellPrice);
			price = price.replace("'", "''").trim();

			// Date
			Cell cellDate = row.getCell(4);
			String date = formatter.formatCellValue(cellDate);
			date = date.replace("'", "''").trim();

			sjQuery.add(" SET @id = '" + id + "'; ");
			sjQuery.add(" SET @name = N'" + name + "'; ");
			sjQuery.add(" SET @description = N'" + description + "'; ");
			sjQuery.add(" SET @price = " + price + "; ");
			if (sqlDialect.equals("mssql")) {
				sjQuery.add(" SET @applied_date = CONVERT(date, '" + date + "', 105); ");
			}


			sjQuery.add(" INSERT INTO PHU_THU (ID_PHU_THU, TEN_PHU_THU, LY_DO) ");
			sjQuery.add(" SELECT @id, @name, @description ");
			sjQuery.add(" WHERE NOT EXISTS (SELECT ID_PHU_THU FROM PHU_THU WHERE ID_PHU_THU = @id); ");
			sjQuery.add(" INSERT INTO GIA_PHU_THU (ID_PHU_THU, NGAY_AP_DUNG, GIA, ID_NV) ");
			sjQuery.add(" SELECT @id, @applied_date, @price, 'EMPL00001' ");

			String content = sjQuery.toString();
			log.info(content);

			Files.write(path, (content + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		}
	}
}