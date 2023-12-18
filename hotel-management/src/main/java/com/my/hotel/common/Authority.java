package com.my.hotel.common;

public enum Authority {
	SYSTEM_MANAGEMENT("SYSTEM_MANAGEMENT"),
	VIEW_REPORT("VIEW_REPORT"),
	BOOK_ROOM("BOOK_ROOM"),
	SERVICE_REQUEST("SERVICE_REQUEST "),
	;

	private final String value;

	Authority(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
