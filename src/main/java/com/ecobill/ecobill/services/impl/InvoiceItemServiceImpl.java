package com.ecobill.ecobill.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ecobill.ecobill.domain.dto.InvoiceItemDto;
import com.ecobill.ecobill.domain.entities.InvoiceEntity;
import com.ecobill.ecobill.domain.entities.InvoiceItemEntity;
import com.ecobill.ecobill.mappers.Mapper;
import com.ecobill.ecobill.repositories.InvoiceItemRepository;
import com.ecobill.ecobill.services.InvoiceItemService;
import com.ecobill.ecobill.utils.ConversionUtils;

@Service
public class InvoiceItemServiceImpl implements InvoiceItemService {

    private InvoiceItemRepository invoiceItemRepository;
    private ConversionUtils conversionUtils;
    private Mapper<InvoiceItemEntity, InvoiceItemDto> invoiceItemMapper;

    public InvoiceItemServiceImpl(InvoiceItemRepository invoiceItemRepository, ConversionUtils conversionUtils,
            Mapper<InvoiceItemEntity, InvoiceItemDto> invoiceItemMapper) {
        this.invoiceItemRepository = invoiceItemRepository;
        this.conversionUtils = conversionUtils;
        this.invoiceItemMapper = invoiceItemMapper;
    }

    @Override
    public void createInvoiceItem(List<Map<String, Object>> invoiceItemsList, InvoiceEntity invoiceEntity) {
        List<InvoiceItemEntity> invoiceItems = new ArrayList<InvoiceItemEntity>();

        if (invoiceItemsList != null) {
            for (Map<String, Object> item : invoiceItemsList) {
                InvoiceItemEntity invoiceItemEntity = InvoiceItemEntity.builder()
                        .name((String) item.get("name"))
                        .price((Double) item.get("price"))
                        .quantity((Integer) item.get("quantity"))
                        .invoice(invoiceEntity)
                        .build();

                invoiceItems.add(invoiceItemEntity);

            }
            invoiceItemRepository.saveAll(invoiceItems);
        }

    }

    @Override
    public List<InvoiceItemDto> getInvoiceItemByQrCode(Long qrCode, Long id) {
        List<InvoiceItemEntity> invoiceItemEntities = invoiceItemRepository
                .findByInvoiceQrCodeAndInvoiceCustomerId(qrCode, id);
        List<InvoiceItemDto> invoiceItemDtos = new ArrayList<>();
        for (int i = 0; i < invoiceItemEntities.size(); i++) {
            invoiceItemDtos.add(invoiceItemMapper.mapTo(invoiceItemEntities.get(i)));
        }
        return invoiceItemDtos;
    }

    @Override
    public List<InvoiceItemDto> getInvoiceItemByQrCodeDev(Long qrCode) {
        return List.of();
    }


}
