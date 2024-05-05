package com.ecobill.ecobill.services.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecobill.ecobill.domain.dto.InvoiceDto;
import com.ecobill.ecobill.domain.entities.CustomerEntity;
import com.ecobill.ecobill.domain.entities.EPREntity;
import com.ecobill.ecobill.domain.entities.InvoiceEntity;
import com.ecobill.ecobill.mappers.Mapper;
import com.ecobill.ecobill.repositories.EPRRepository;
import com.ecobill.ecobill.repositories.InvoiceRepository;
import com.ecobill.ecobill.services.InvoiceService;
import com.ecobill.ecobill.utils.ConversionUtils;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private InvoiceRepository invoiceRepository;
    private ConversionUtils conversionUtils;
    private Mapper<InvoiceEntity, InvoiceDto> invoiceMapper;
    private EPRRepository eprRepository;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, ConversionUtils conversionUtils,
            Mapper<InvoiceEntity, InvoiceDto> invoiceMapper, EPRRepository eprRepository) {
        this.invoiceRepository = invoiceRepository;
        this.conversionUtils = conversionUtils;
        this.invoiceMapper = invoiceMapper;
        this.eprRepository = eprRepository;
    }

    @Override
    public InvoiceEntity createInvoice(Map<String, Object> invoiceMap, EPREntity eprEntity,
            CustomerEntity customerEntity) {
        HashMap<String, Object> invoiceHashMap = new HashMap<>(invoiceMap);
        InvoiceEntity invoiceEntity = null;

        try {
            invoiceEntity = InvoiceEntity.builder()
                    .qrCode((Long) invoiceHashMap.get("qr_code"))
                    .epr(eprEntity).eprTaxNumber(eprEntity).customer(customerEntity)
                    .creationDate(conversionUtils.StringToDateConversion((String) invoiceHashMap.get("created_at")))
                    .totalAmount(conversionUtils.doubleToLongConversion((Double) invoiceHashMap.get("total_amount")))
                    .build();

            Optional<InvoiceEntity> invoiceEntityOptional = invoiceRepository
                    .findByQrCode(invoiceEntity.getQrCode());

            if (!invoiceEntityOptional.isPresent()) {
                return invoiceRepository.save(invoiceEntity);
            } else {
                return invoiceEntityOptional.get();
            }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }

    @Override
    public List<InvoiceDto> getInvoiceByEPRName(String name) {
        EPREntity eprEntity = eprRepository.findByName(name);
        try {
            List<InvoiceDto> invoiceDtos = invoiceRepository.findAllByEpr(eprEntity)
                    .stream()
                    .map(invoiceMapper::mapTo)
                    .collect(Collectors.toList());
            return invoiceDtos;
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public List<InvoiceDto> getInvoiceByAmountLimits(Long min, Long max) {
        try {
            return invoiceRepository.findAllByTotalAmountBetween(min, max).stream().map(invoiceMapper::mapTo)
                    .collect(Collectors.toList());
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public List<InvoiceDto> getInvoiceByEPRCategory(String category) {
        List<EPREntity> eprEntities = eprRepository.findAllByCategoryIgnoreCase(category);
        List<InvoiceDto> invoiceDtos = new ArrayList<>();

        for (EPREntity eprEntity : eprEntities) {
            invoiceDtos.addAll(getInvoiceByEPRName(eprEntity.getName()));
        }
        return invoiceDtos;
    }

    @Override
    public List<InvoiceDto> getByCreationDateBetweenAndUserNumber(Long userNumber, Timestamp lower, Timestamp upper) {
        try {
            return invoiceRepository.findAllByCustomerPhoneNumberAndCreationDateBetween(userNumber, lower, upper)
                    .stream()
                    .map(invoiceMapper::mapTo)
                    .collect(Collectors.toList());
        } catch (NullPointerException e) {
            System.out.println(e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<InvoiceDto> getInvoicesForUserInRange(Long userNumber, int start, int end) {
        int limit = end - start + 1;
        int offset = start - 1; // Assuming the first invoice has an index of 1
        try {
            return invoiceRepository.findInvoicesByCustomerPhoneNumberWithOffset(userNumber, limit, offset)
                    .stream()
                    .map(invoiceMapper::mapTo)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e);
            return new ArrayList<>();
        }
    }

    @Override
    public HashMap<String, Object> getCustomerStatistics(Long phoneNumber, int numberOfMonths) {
        Timestamp lower = Timestamp.valueOf(LocalDateTime.now().minusMonths(numberOfMonths));
        Timestamp upper = Timestamp.valueOf(LocalDateTime.now());

        List<InvoiceDto> invoices = getByCreationDateBetweenAndUserNumber(phoneNumber, lower, upper);

        double averageSpent = invoices.stream()
                .mapToDouble(InvoiceDto::getTotalAmount)
                .average()
                .orElse(0.0);

        EPREntity mostVisitedCompany = invoices.stream()
                .collect(Collectors.groupingBy(InvoiceDto::getEpr, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        String mostVisitedCompanyName = mostVisitedCompany != null ? mostVisitedCompany.getName()
                : "No company visited";

        double thisMonthSpending = invoices.stream()
                .filter(invoice -> invoice.getCreationDate()
                        .after(Timestamp.valueOf(LocalDateTime.now().minusMonths(1))))
                .mapToDouble(InvoiceDto::getTotalAmount)
                .sum();

        double lastMonthSpending = invoices.stream()
                .filter(invoice -> invoice.getCreationDate()
                        .before(Timestamp.valueOf(LocalDateTime.now().minusMonths(1))))
                .mapToDouble(InvoiceDto::getTotalAmount)
                .sum();

        HashMap<String, Object> statisticsHashMap = new HashMap<>();

        statisticsHashMap.put("phoneNumber", phoneNumber);
        statisticsHashMap.put("averageSpent", String.format("%.2f", averageSpent));
        statisticsHashMap.put("mostVisitedCompany", mostVisitedCompanyName);
        statisticsHashMap.put("thisMonthSpendings", String.format("%.2f", thisMonthSpending));
        statisticsHashMap.put("lastMonthSpendings", String.format("%.2f", lastMonthSpending));
        statisticsHashMap.put("monthlySpendingsDifference",
                String.format("%.2f", (thisMonthSpending - lastMonthSpending)));

        // List<String> userStatistics = new ArrayList<>();
        // userStatistics.add(String.valueOf(phoneNumber));
        // userStatistics.add(String.format("%.2f", averageSpent));
        // userStatistics.add(mostVisitedCompanyName);
        // userStatistics.add(String.format("%.2f", thisMonthSpending));
        // userStatistics.add(String.format("%.2f", lastMonthSpending));
        // userStatistics.add(String.format("%.2f", (thisMonthSpending -
        // lastMonthSpending)));

        return statisticsHashMap;
    }

}
