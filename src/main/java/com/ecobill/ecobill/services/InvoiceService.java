package com.ecobill.ecobill.services;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.ecobill.ecobill.domain.dto.InvoiceDto;
import com.ecobill.ecobill.domain.entities.CustomerEntity;
import com.ecobill.ecobill.domain.entities.EPREntity;
import com.ecobill.ecobill.domain.entities.InvoiceEntity;

public interface InvoiceService {

    InvoiceEntity createInvoice(Map<String, Object> invoiceMap, EPREntity eprEntity, CustomerEntity customerEntity);

    List<InvoiceDto> getInvoiceByEPRName(String name);

    List<InvoiceDto> getInvoiceByAmountLimits(Long min, Long max);

    List<InvoiceDto> getInvoiceByEPRCategory(String category);

    List<InvoiceDto> getByCreationDateBetweenAndUserNumber(Long userNumber, Timestamp lower, Timestamp upper);

    List<InvoiceDto> getInvoicesForUserInRange(Long userNumber, int start, int end);

    List<String> printUserStatistics(Long userNumber, int numberOfMonths);

}
