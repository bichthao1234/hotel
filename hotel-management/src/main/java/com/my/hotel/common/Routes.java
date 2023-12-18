package com.my.hotel.common;

public final class Routes {

    private Routes() {
	}


	public static final String PREFIX_URI_V1 = "/api/v1";

	// Public
	public static final String URI_REST_PUBLIC = PREFIX_URI_V1 + "/public";

	// Auth
	public static final String URI_REST_AUTH = PREFIX_URI_V1 + "/auth";

	// Admin Config
	public static final String URI_REST_ADMIN_CONFIG = PREFIX_URI_V1 + "/admin-config";

	// Customer
	public static final String URI_REST_CUSTOMER = PREFIX_URI_V1 + "/customer";
	// Employee
	public static final String URI_REST_EMPLOYEE = PREFIX_URI_V1 + "/employee";
	// Department
	public static final String URI_REST_DEPARTMENT = PREFIX_URI_V1 + "/department";
	//Room Type
	public static final String URI_REST_ROOM_TYPE = PREFIX_URI_V1 + "/room-type";
	//Room Kind
	public static final String URI_REST_ROOM_KIND = PREFIX_URI_V1 + "/room-kind";
	//Room status
	public static final String URI_REST_ROOM_STATUS = PREFIX_URI_V1 + "/room-status";
	// Reservation
	public static final String URI_REST_RESERVATION = PREFIX_URI_V1 + "/reservation";
	// Rental Slip
	public static final String URI_REST_RENTAL_SLIP = PREFIX_URI_V1 + "/rental-slip";
	// Image
	public static final String URI_REST_IMAGE = PREFIX_URI_V1 + "/image";
	//Room Classification
	public static final String URI_REST_ROOM_CLASSIFICATION = PREFIX_URI_V1 + "/room-classification";

	// Payment Config
	public static final String URI_REST_PAYMENT = PREFIX_URI_V1 + "/payment";

	//Room
	public static final String URI_REST_ROOM = PREFIX_URI_V1 + "/room";

	// Service

	public static final String URI_REST_SERVICE = PREFIX_URI_V1 + "/service";
	// Invoice
	public static final String URI_REST_INVOICE = PREFIX_URI_V1 + "/invoice";

	public static final String URI_REST_SURCHARGE = PREFIX_URI_V1 + "/surcharge";

	// Convenience
	public static final String URI_REST_CONVENIENCE = PREFIX_URI_V1 + "/convenience";
	// Promotion
	public static final String URI_REST_PROMOTION = PREFIX_URI_V1 + "/promotion";

}