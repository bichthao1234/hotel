package com.my.hotel.common;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class Constants {

	public static final String JWT_HEADER = "Authorization";
	
	public static String JWT_KEY = "";

	public static final class CUSTOMER_IMPORT_HEADER {
		public static final String ID = "CMND";
		public static final String FIRST_NAME = "HO";
		public static final String LAST_NAME = "TEN";
		public static final String PHONE_NUMBER = "SDT";
		public static final String EMAIL = "EMAIL";
		public static final String ADDRESS = "DIACHI";
		public static final String TAX_NUMBER = "MASOTHUE";
		public static final Set<String> CUSTOMER_IMPORT_HEADER_SET = new HashSet<>(
				Arrays.asList(ID, FIRST_NAME, LAST_NAME, PHONE_NUMBER, EMAIL, ADDRESS, TAX_NUMBER));
	}

	// Images
	public static final String RESOURCE_IMAGES_PATH = "src/main/resources/images/";
	public static final String PUBLIC_IMAGES_PATH = RESOURCE_IMAGES_PATH + "public/";
	public static final String PRIVATE_IMAGES_PATH = RESOURCE_IMAGES_PATH + "private/";

	public static final class DATE_FORMAT {
		public static final String FORMAT_1 = "dd/MM/yyyy";
	}
}
