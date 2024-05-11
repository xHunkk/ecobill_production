package com.ecobill.ecobill.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.ecobill.ecobill.domain.dto.CustomerDto;

import com.ecobill.ecobill.domain.entities.CustomerEntity;
import com.ecobill.ecobill.mappers.Mapper;

@Component
public class CustomerMapper implements Mapper<CustomerEntity, CustomerDto> {

    private ModelMapper modelMapper;

    public CustomerMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public CustomerDto mapTo(CustomerEntity CustomerEntity) {
        return modelMapper.map(CustomerEntity, CustomerDto.class);
    }

    @Override
    public CustomerEntity mapFrom(CustomerDto CustomerDto) {
        return modelMapper.map(CustomerDto, CustomerEntity.class);
    }

}
