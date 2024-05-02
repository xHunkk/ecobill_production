package com.ecobill.ecobill.services.impl;

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
    public List<InvoiceDto> getByEPR(String name) {
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
            invoiceDtos.addAll(getByEPR(eprEntity.getName()));
        }
        return invoiceDtos;
    }

}
