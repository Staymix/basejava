package com.urise.webapp.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static final LocalDate NOW = LocalDate.of(3000, 1, 1);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    public static String format(LocalDate ld) {
        if(ld == null) return "";
        return ld.equals(NOW) ? "Сейчас" : ld.format(DATE_FORMATTER);
    }

    public static LocalDate parse(String date) {
        if (date == null || date.trim().length() == 0 || "Сейчас".equals(date)) {
            return NOW;
        }
        String[] dates = date.split("-");
        return LocalDate.of(Integer.parseInt(dates[0]), Month.of(Integer.parseInt(dates[1])), 1);
    }
}