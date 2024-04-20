package com.ecobill.ecobill.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecobill.ecobill.domain.entities.CustomerEntity;
import com.ecobill.ecobill.domain.entities.EPREntity;
import com.ecobill.ecobill.domain.entities.InvoiceEntity;
import com.ecobill.ecobill.repositories.InvoiceRepository;
import com.ecobill.ecobill.services.InvoiceService;
import com.ecobill.ecobill.utils.ConversionUtils;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private InvoiceRepository invoiceRepository;
    private ConversionUtils conversionUtils;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, ConversionUtils conversionUtils) {
        this.invoiceRepository = invoiceRepository;
        this.conversionUtils = conversionUtils;
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

}
