package com.ecobill.ecobill.services;

import java.util.Map;

import com.ecobill.ecobill.domain.entities.CustomerEntity;
import com.ecobill.ecobill.domain.entities.EPREntity;
import com.ecobill.ecobill.domain.entities.InvoiceEntity;

public interface InvoiceService {

    InvoiceEntity createInvoice(Map<String, Object> invoiceMap, EPREntity eprEntity, CustomerEntity customerEntity);

}
