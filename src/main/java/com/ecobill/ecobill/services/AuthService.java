package com.ecobill.ecobill.services;

import com.ecobill.ecobill.domain.dto.CustomerDto;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    CustomerDto signUp(CustomerDto customerDto);

    CustomerDto loginCustomerByPhoneNumber(CustomerDto customerDto);

    CustomerDto loginCustomerByEmail(CustomerDto customerDto);

    CustomerDto updateCustomer(Long id, CustomerDto customerDto);

    public Long authenticateToken(HttpServletRequest request);

}
