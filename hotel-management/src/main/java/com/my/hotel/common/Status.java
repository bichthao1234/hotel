package com.my.hotel.common;

import java.util.Objects;

public enum Status {
    CANCELLED(0, "Đã hủy"),
	RESERVED(1, "Đã đặt"),
	RENTED(2, "Đã thuê");

    private final Integer value;
    private final String name;

    Status(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static String convert(Integer status) {
        for (Status statusEnum : values()) {
            if (Objects.equals(statusEnum.getValue(), status)) {
                return statusEnum.getName();
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + status);
    }

    public static StatusWrapper getStatusName(Integer status) {
        for (Status statusEnum : values()) {
            if (Objects.equals(statusEnum.getValue(), status)) {
                return new StatusWrapper(statusEnum);
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + status);
    }

    public static StatusWrapper getStatusValue(String status) {
        for (Status statusEnum : values()) {
            if (Objects.equals(statusEnum.getName(), status)) {
                return new StatusWrapper(statusEnum);
            }
        }
        throw new IllegalArgumentException("Invalid status name: " + status);
    }

    public static class StatusWrapper {
        private final Integer value;
        private final String name;

        public StatusWrapper(Status status) {
            this.value = status.getValue();
            this.name = status.getName();
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }
}
