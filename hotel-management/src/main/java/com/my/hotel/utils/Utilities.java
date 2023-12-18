package com.my.hotel.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Utilities {

    private Utilities() {}

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean nonNull(Object obj) {
        return !isNull(obj);
    }

    public static boolean isEmptyString(String value) {
        return (Objects.isNull(value) || "".equals(value));
    }

    public static boolean nonEmptyString(String value) {
        return !isEmptyString(value);
    }

    public static boolean isEmptyList(@SuppressWarnings("rawtypes") Collection list) {
        return list == null || list.isEmpty();
    }

    public static boolean nonEmptyList(@SuppressWarnings("rawtypes") Collection list) {
        return !isEmptyList(list);
    }

    public static int parseInt(Object value) {
        try {
            return Integer.parseInt(parseString(value));
        } catch (Exception e) {
            return Integer.MIN_VALUE;
        }
    }

    public static Integer parseInt(Object value, Integer defaultValue) {
        try {
            return Integer.parseInt(parseString(value));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String parseString(Object value) {
        if (Objects.isNull(value)) {
            return null;
        }
        try {
            return String.valueOf(value);
        } catch (Exception e) {
            return "";
        }
    }

    public static Date parseDate(Object value) {
        if (Objects.isNull(value)) {
            return null;
        }

        try {
            return (Date) value;
        } catch (Exception e) {
            return null;
        }
    }

    public static Time parseTime(Object value) {
        if (Objects.isNull(value)) {
            return null;
        }

        try {
            return Time.valueOf(value.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Long parseLong(Object value) {
        return parseLong(value, Long.MIN_VALUE);
    }

    public static Long parseLong(Object value, Long defaultValue) {
        try {
            return Long.parseLong(Utilities.parseString(value));
        }catch (Exception e) {
            if(defaultValue == null) {
                return null;
            } else {
                return defaultValue;
            }
        }
    }

    public static boolean parseBoolean(Object value) {
        try {
            return Boolean.parseBoolean(parseString(value));
        } catch (Exception e) {
            return false;
        }
    }

    public static BigDecimal parseBigDecimal(Object value) {
        try {
            return BigDecimal.class.cast(value);
        } catch (Exception e) {
            return null;
        }
    }
    public static Float parseFloat(Object value) {
        if (Objects.isNull(value)) {
            return null;
        }

        try {
            return Float.parseFloat(value.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] serialize(Object obj) throws IOException {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(obj);
            }
            return b.toByteArray();
        }
    }

    /**
     * Get value by field name from Object.
     * @param obj Object is DTO/Entity
     * @param fieldName
     * @return value string
     */
    public static String getValueByFieldName(Object obj, String fieldName) {
        return Utilities.parseString(getValue(obj, fieldName));
    }

    public static Object getValue(Object obj, String fieldName) {
        if (Objects.isNull(obj) || Objects.isNull(fieldName) || Objects.equals(fieldName, "")) {
            return null;
        }
        if (isBasicDataType(obj)) {
            return obj;
        }

        try {
            Field field = obj.getClass().getDeclaredField(fieldName);

            String fieldType = field.getType().getTypeName();
            fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String method = "get" + fieldName;
            if (fieldType.equals("boolean")) {
                method = "is" + fieldName;
            }

            return obj.getClass().getMethod(method).invoke(obj);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | NoSuchFieldException e) {
            return null;
        }
    }

    public static boolean isBasicDataType(Object obj) {
        if (obj instanceof Integer) {
            return true;
        }
        if (obj instanceof Long) {
            return true;
        }
        if (obj instanceof Double) {
            return true;
        }
        if (obj instanceof BigDecimal) {
            return true;
        }
        if (obj instanceof String) {
            return true;
        }

        return false;
    }

    public static String listToCsv(List<?> list) {
        if (isEmptyList(list)) {
            return null;
        }
        return list.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    public static double calculatePercentage(double obtained, double total) {
        return obtained * 100 / total;
    }

    public static double calculateObtained(double total, double percent) {
        return total * percent / 100;
    }

    public static String formatDate(Date date, String format) {
        if (Objects.isNull(date)) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String convertJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    public static long getDayDiff(Date date1, Date date2) {
        long diffInMs = Math.abs(date2.getTime() - date1.getTime());
        return TimeUnit.DAYS.convert(diffInMs,TimeUnit.MILLISECONDS);
    }
}
