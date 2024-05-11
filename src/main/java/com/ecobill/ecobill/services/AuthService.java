package com.ecobill.ecobill.services;

import com.ecobill.ecobill.domain.dto.CustomerDto;

public interface AuthService {
    CustomerDto signUp(CustomerDto customerDto);

    CustomerDto loginCustomerByPhoneNumber(CustomerDto customerDto);

    CustomerDto loginCustomerByEmail(CustomerDto customerDto);

    CustomerDto updateCustomer(Long phoneNumber, CustomerDto customerDto);

}
