package com.dqt.hotel.service;

import com.dqt.hotel.dto.response.ReportBillResponse;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.entity.Bill;
import com.dqt.hotel.entity.Booking;
import com.dqt.hotel.repository.*;
import com.dqt.hotel.utils.DateUtils;
import com.dqt.hotel.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final BookingRepository bookingRepository;
    private final BillRepository billRepository;

    public ResponseMessage reportBooking(Integer type) {
        LocalDate start;
        switch (type) {
            case 1:
                start = DateUtils.getStartDay();
                break;
            case 3:
                start = DateUtils.getStartMonth();
                break;
            case 4:
                start = DateUtils.getStartYear();
                break;
            default:
                return ResponseMessage.error("Type is 1: day || 2: month || 3 year");
        }
        Date date = DateUtils.convertLocalDateToDate(start);
        List<Booking> reportBooking = bookingRepository.findAllBetweenTime(date, null);
        return ResponseMessage.ok("reportBooking", reportBooking);
    }

    public ResponseMessage reportBill(Integer type) {
        switch (type) {
            case 1:
                return reportBillDay();
            case 2:
                return reportBillMonth();
            case 3:
                return reportBillYear();
            default:
                return ResponseMessage.error("Type is 1: day || 2: month || 3: year");
        }
    }

    public ResponseMessage reportBillDay() {
        LocalDate start = DateUtils.getStartDay();
        Date date = DateUtils.convertLocalDateToDate(start);
        List<Bill> bills = billRepository.findAllBetweenTime(date, null);
        List<Date> collect = bills.stream().map(Bill::getPaymentDate).collect(Collectors.toList());

        Map<LocalDate, List<Date>> mapDate = DateUtils.groupDateOfDay(collect);

        List<ReportBillResponse> report = new ArrayList<>();

        mapDate.forEach((localDate, dates) -> {
            ReportBillResponse response = new ReportBillResponse();
            response.setDate(localDate);
            response.setTotal(calculator(dates, bills));
            report.add(response);
        });

        return ResponseMessage.ok("reportBillDay", report);
    }

    public ResponseMessage reportBillMonth() {
        LocalDate start = DateUtils.getStartMonth();
        Date date = DateUtils.convertLocalDateToDate(start);
        List<Bill> bills = billRepository.findAllBetweenTime(date, null);
        List<Date> collect = bills.stream().map(Bill::getPaymentDate).collect(Collectors.toList());

        Map<Integer, List<Date>> mapDate = DateUtils.groupDateOfMonth(collect);
        List<ReportBillResponse> report = new ArrayList<>();

        mapDate.forEach((month, dates) -> {
            ReportBillResponse response = new ReportBillResponse();
            response.setMonth(month);
            response.setTotal(calculator(dates, bills));
            report.add(response);
        });
        return ResponseMessage.ok("reportBillMonth", report);
    }

    public ResponseMessage reportBillYear() {
        LocalDate start = DateUtils.getStartYear();
        Date date = DateUtils.convertLocalDateToDate(start);
        List<Bill> bills = billRepository.findAllBetweenTime(date, null);
        List<Date> collect = bills.stream().map(Bill::getPaymentDate).collect(Collectors.toList());

        Map<Integer, List<Date>> mapDate = DateUtils.groupDateOfYear(collect);
        List<ReportBillResponse> report = new ArrayList<>();

        mapDate.forEach((year, dates) -> {
            ReportBillResponse response = new ReportBillResponse();
            response.setYear(year);
            response.setTotal(calculator(dates, bills));
            report.add(response);
        });
        return ResponseMessage.ok("reportBillYear", report);
    }

    public String calculator(List<Date> dates, List<Bill> bills) {
        int total = 0;
        for (Date d : dates) {
            total += bills.stream().filter(b -> b.getPaymentDate().equals(d)).map(Bill::getAmount).reduce(0, (a, b) -> a + b);
        }
        return Utils.convertToCurrency(total);
    }

}
