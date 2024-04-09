package com.ecobill.ecobill.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ecobill.ecobill.domain.entities.CustomerEntity;
import com.ecobill.ecobill.repositories.CustomerRepository;
import com.ecobill.ecobill.services.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerEntity createCustomer(Map<String, Object> customerMap) {
        HashMap<String, Object> customerHashMap = new HashMap<>(customerMap);

        CustomerEntity customerEntity = CustomerEntity.builder()
                .phoneNumber((Long) customerHashMap.get("phone_number"))
                .build();

        return customerRepository.save(customerEntity);

    }

}
