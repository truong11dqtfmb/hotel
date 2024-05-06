package com.dqt.hotel.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.*;

@Component
@RequiredArgsConstructor
public class DateUtils {

    public static Map<LocalDate, List<Date>> groupDateOfDay(List<Date> dates) {
        // Nhóm các đối tượng Date theo ngày
        Map<LocalDate, List<Date>> dateGroups = new HashMap<>();

        for (Date date : dates) {
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            List<Date> group = dateGroups.getOrDefault(localDate, new ArrayList<>());
            group.add(date);
            dateGroups.put(localDate, group);
        }
        return dateGroups;
    }

    public static Map<Integer, List<Date>> groupDateOfMonth(List<Date> dates) {
        // Nhóm các đối tượng Date theo tháng
        Map<Integer, List<Date>> monthGroups = new HashMap<>();

        for (Date date : dates) {
            Integer yearMonth = YearMonth.from(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).getMonthValue();
            List<Date> group = monthGroups.getOrDefault(yearMonth, new ArrayList<>());
            group.add(date);
            monthGroups.put(yearMonth, group);
        }
        return monthGroups;
    }

    public static Map<Integer, List<Date>> groupDateOfYear(List<Date> dates) {
        // Nhóm các đối tượng Date theo năm
        Map<Integer, List<Date>> yearGroups = new HashMap<>();

        for (Date date : dates) {
            Integer year = Year.from(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).getValue();
            List<Date> group = yearGroups.getOrDefault(year, new ArrayList<>());
            group.add(date);
            yearGroups.put(year, group);
        }
        return yearGroups;
    }

    public static LocalDate getStartDay() {
        // Lấy thời gian đầu ngày hiện tại
        LocalDate startOfDay = LocalDate.now().atStartOfDay().toLocalDate();
        return startOfDay;
    }

    public static LocalDate getStartWeek() {
        // Lấy thời gian đầu tuần hiện tại
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        return startOfWeek;
    }

    public static LocalDate getStartMonth() {
        // Lấy thời gian đầu tháng hiện tại
        LocalDate startOfMonth = YearMonth.now().atDay(1);
        return startOfMonth;
    }

    public static LocalDate getStartYear() {
        // Lấy thời gian đầu năm hiện tại
        LocalDate startOfYear = LocalDate.now().withDayOfYear(1);
        return startOfYear;
    }

    public static Date convertLocalDateToDate(LocalDate localDate) {
        // Chuyển đổi LocalDate sang Date
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return date;
    }

    public static LocalDate convertDateToLocalDate(Date date) {
        // Chuyển đổi Date sang LocalDate
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate;
    }


}
