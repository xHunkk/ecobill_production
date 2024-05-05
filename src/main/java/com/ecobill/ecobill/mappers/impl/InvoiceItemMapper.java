package com.ecobill.ecobill.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.ecobill.ecobill.domain.dto.InvoiceItemDto;
import com.ecobill.ecobill.domain.entities.InvoiceItemEntity;
import com.ecobill.ecobill.mappers.Mapper;

@Component
public class InvoiceItemMapper implements Mapper<InvoiceItemEntity, InvoiceItemDto> {

    private ModelMapper modelMapper;

    public InvoiceItemMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public InvoiceItemDto mapTo(InvoiceItemEntity invoiceItemEntity) {
        return modelMapper.map(invoiceItemEntity, InvoiceItemDto.class);
    }

    @Override
    public InvoiceItemEntity mapFrom(InvoiceItemDto invoiceItemDto) {
        return modelMapper.map(invoiceItemDto, InvoiceItemEntity.class);
    }

}
