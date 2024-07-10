package com.ecobill.ecobill.services;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecobill.ecobill.domain.dto.InvoiceDto;
import com.ecobill.ecobill.domain.entities.CustomerEntity;
import com.ecobill.ecobill.domain.entities.EPREntity;
import com.ecobill.ecobill.domain.entities.InvoiceEntity;

public interface InvoiceService {

    InvoiceEntity createInvoice(Map<String, Object> invoiceMap, EPREntity eprEntity, CustomerEntity customerEntity);

    List<InvoiceDto> findInvoicesByCustomerIdAndEprName(Long customerId, String eprName);

    List<InvoiceDto> findInvoicesByCustomerIdAndAmountRangeWithVat(Long customerId, double minAmount, double maxAmount);

    List<InvoiceDto> findInvoicesByCustomerIdAndCategory(Long customerId, String category);

    List<InvoiceDto> getInvoiceByCreationDateBetween(Long id, Timestamp lower, Timestamp upper);

    List<InvoiceDto> findInvoicesByCustomerIdInRange(Long customerId, int start, int end);

    HashMap<String, Object> getCustomerStatistics(Long id, int numberOfMonths);

    List<InvoiceDto> getInvoiceByCreationDateBetweenAndTotalAmountBetweenAndEprName(
            Long id, Timestamp beforeDate, Timestamp afterDate, Long lowerLimit, long upperLimit,
            String name);

}
