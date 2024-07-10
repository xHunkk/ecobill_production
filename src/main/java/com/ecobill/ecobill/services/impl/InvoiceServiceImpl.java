package com.ecobill.ecobill.services.impl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
                    .totalAmount((Double) invoiceHashMap.get("total_amount"))
                    .vatAmount((Double) invoiceHashMap.get("vat_amount"))
                    .totalAmountWithVat((Double) invoiceHashMap.get("total_amount_with_vat"))
                    .paymentMethod((String) invoiceHashMap.get("payment_method"))
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


    public List<InvoiceDto> findInvoicesByCustomerIdAndEprName(Long customerId, String eprName) {
        // Fetch invoices from repository
        List<InvoiceEntity> invoices = invoiceRepository.findByCustomer_IdAndEpr_Name(customerId, eprName);

        // Map to DTOs
        List<InvoiceDto> invoiceDtos = invoices.stream()
                .map(invoiceMapper::mapTo) // Assuming you have an InvoiceMapper for conversion
                .collect(Collectors.toList());

        return invoiceDtos;
    }


    public List<InvoiceDto> findInvoicesByCustomerIdAndAmountRangeWithVat(Long customerId, double minAmount, double maxAmount) {
        List<InvoiceEntity> invoiceEntities = invoiceRepository.findByCustomer_IdAndTotalAmountWithVatBetween(customerId, minAmount, maxAmount);

        // Map entities to DTOs
        List<InvoiceDto> invoiceDtos = invoiceEntities.stream()
                .map(invoiceMapper::mapTo)
                .collect(Collectors.toList());

        return invoiceDtos;
    }


    public List<InvoiceDto> findInvoicesByCustomerIdAndCategory(Long customerId, String category) {
        List<InvoiceEntity> invoiceEntities = invoiceRepository.findByCustomer_IdAndEpr_Category(customerId, category);

        // Map entities to DTOs
        List<InvoiceDto> invoiceDtos = invoiceEntities.stream()
                .map(invoiceMapper::mapTo)
                .collect(Collectors.toList());

        return invoiceDtos;
    }


    @Override
    public List<InvoiceDto> getInvoiceByCreationDateBetween(Long id, Timestamp lower, Timestamp upper) {
        try {
            return invoiceRepository.findAllByCustomerIdAndCreationDateBetween(id, lower, upper)
                    .stream()
                    .map(invoiceMapper::mapTo)
                    .collect(Collectors.toList());
        } catch (NullPointerException e) {
            System.out.println(e);
            return new ArrayList<>();
        }
    }

    public List<InvoiceDto> findInvoicesByCustomerIdInRange(Long customerId, int start, int end) {
        int pageNumber = start - 1; // Calculate page number (0-based index)

        // If start and end are the same, adjust pageSize to 1 to fetch only one record
        int pageSize = (end - start) + 1;

        // Create Pageable object for pagination and sorting by creation date descending
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("creationDate").descending());

        // Fetch a page of invoices for the customer
        Page<InvoiceEntity> invoicePage = invoiceRepository.findByCustomer_Id(customerId, pageable);

        // Map page content to DTOs
        List<InvoiceDto> invoiceDtos = invoicePage.getContent().stream()
                .map(invoiceMapper::mapTo)
                .collect(Collectors.toList());

        return invoiceDtos;
    }


    @Override
    public HashMap<String, Object> getCustomerStatistics(Long id, int numberOfMonths) {
        LocalDateTime now = LocalDateTime.now();
        Timestamp lower = Timestamp.valueOf(now.minusMonths(numberOfMonths).withDayOfMonth(1).with(LocalTime.MIN));
        Timestamp upper = Timestamp.valueOf(now.with(LocalTime.MAX));

        List<InvoiceDto> invoices = getInvoiceByCreationDateBetween(id, lower, upper);

        if (invoices.isEmpty()) {
            HashMap<String, Object> emptyHashMap = new HashMap<>();
            return emptyHashMap;
        }

        List<InvoiceDto> filteredInvoices = invoices.stream()
                .filter(invoice -> {
                    LocalDateTime creationDate = invoice.getCreationDate().toLocalDate().atStartOfDay();
                    return creationDate.isAfter(lower.toLocalDateTime()) && creationDate.isBefore(upper.toLocalDateTime());
                })
                .collect(Collectors.toList());

        double averageSpent = filteredInvoices.stream()
                .mapToDouble(InvoiceDto::getTotalAmountWithVat)
                .average()
                .orElse(0.0);

        EPREntity mostVisitedCompany = filteredInvoices.stream()
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
                .mapToDouble(InvoiceDto::getTotalAmountWithVat)
                .sum();

        double lastMonthSpending = invoices.stream()
                .filter(invoice -> {
                    LocalDateTime firstDayOfLastMonth = now.minusMonths(2).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
                    LocalDateTime firstDayOfThisMonth = now.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
                    LocalDate creationDate = invoice.getCreationDate().toLocalDate(); // Convert to LocalDate
                    return creationDate.isAfter(firstDayOfLastMonth.toLocalDate()) && creationDate.isBefore(firstDayOfThisMonth.toLocalDate());
                })
                .mapToDouble(InvoiceDto::getTotalAmountWithVat)
                .sum();

        HashMap<String, Object> statisticsHashMap = new HashMap<>();

        statisticsHashMap.put("id", id);
        statisticsHashMap.put("averageSpent", String.format("%.2f", averageSpent));
        statisticsHashMap.put("mostVisitedCompany", mostVisitedCompanyName);
        statisticsHashMap.put("mostVisitedCompanyLogo", mostVisitedCompany.getLogo());
        statisticsHashMap.put("mostVisitedCompanyCategory", mostVisitedCompany.getCategory());
        statisticsHashMap.put("invoice", filteredInvoices.get(0));
        statisticsHashMap.put("thisMonthSpendings", String.format("%.2f", thisMonthSpending));
        statisticsHashMap.put("lastMonthSpendings", String.format("%.2f", lastMonthSpending));
        statisticsHashMap.put("monthlySpendingsDifference",
                String.format("%.2f", ((lastMonthSpending - thisMonthSpending) / thisMonthSpending) * 100));

        return statisticsHashMap;
    }


    public List<InvoiceDto> getInvoiceByCreationDateBetweenAndTotalAmountBetweenAndEprName(
            Long id, Timestamp beforeDate, Timestamp afterDate, Long lowerLimit, long upperLimit,
            String name) {
        List<InvoiceEntity> invoiceEntities = invoiceRepository
                .findAllByCustomerIdAndCreationDateBetweenAndTotalAmountBetweenAndEprName(id,
                        beforeDate, afterDate, lowerLimit, upperLimit, name);
        List<InvoiceDto> invoiceDtos = new ArrayList<>();
        for (int i = 0; i < invoiceEntities.size(); i++) {
            invoiceDtos.add(invoiceMapper.mapTo(invoiceEntities.get(i)));
        }

        return invoiceDtos;
    }

}
