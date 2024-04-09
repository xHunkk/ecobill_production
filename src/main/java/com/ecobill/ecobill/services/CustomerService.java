package com.ecobill.ecobill.services;

import java.util.Map;

import com.ecobill.ecobill.domain.entities.CustomerEntity;

public interface CustomerService {
    CustomerEntity createCustomer(Map<String, Object> customerMap);

}
