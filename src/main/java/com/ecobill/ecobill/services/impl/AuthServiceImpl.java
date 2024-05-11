package com.ecobill.ecobill.services.impl;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ecobill.ecobill.domain.dto.CustomerDto;
import com.ecobill.ecobill.domain.entities.CustomerEntity;
import com.ecobill.ecobill.mappers.Mapper;
import com.ecobill.ecobill.repositories.CustomerRepository;
import com.ecobill.ecobill.services.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private CustomerRepository customerRepository;

    private PasswordEncoder passwordEncoder;
    private Mapper<CustomerEntity, CustomerDto> customerMapper;

    public AuthServiceImpl(CustomerRepository customerRepository, PasswordEncoder passwordEncoder,
            Mapper<CustomerEntity, CustomerDto> customerMapper) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.customerMapper = customerMapper;

    }

    @Override
    public CustomerDto signUp(CustomerDto customerDto) {
        if (customerDto.getPhoneNumber() == null || customerDto.getPhoneNumber().describeConstable().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }

        if (customerRepository.existsByPhoneNumber(customerDto.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number is already taken");
        }

        CustomerEntity customerEntity = CustomerEntity.builder()
                .id(customerDto.getId())
                .firstName(customerDto.getFirstName())
                .lastName(customerDto.getLastName())
                .username(customerDto.getUsername())
                .email(customerDto.getEmail())
                .phoneNumber(customerDto.getPhoneNumber())
                .password(passwordEncoder.encode(customerDto.getPassword()))
                .build();

        return customerMapper.mapTo(customerRepository.save(customerEntity));
    }

    @Override
    public CustomerDto loginCustomerByEmail(CustomerDto customerDto) {
        Optional<CustomerEntity> customerEntityOptional = customerRepository
                .findByEmail(customerDto.getEmail());

        if (customerEntityOptional.isPresent()) {
            if (passwordEncoder.matches(customerDto.getPassword(), customerEntityOptional.get().getPassword())) {
                return customerMapper.mapTo(customerEntityOptional.get());
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }
    }

    @Override
    public CustomerDto loginCustomerByPhoneNumber(CustomerDto customerDto) {
        Optional<CustomerEntity> customerEntityOptional = customerRepository
                .findByPhoneNumber(customerDto.getPhoneNumber());
        if (customerEntityOptional.isPresent()) {
            if (passwordEncoder.matches(customerDto.getPassword(), customerEntityOptional.get().getPassword())) {
                return customerMapper.mapTo(customerEntityOptional.get());
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }
    }

    @Override
    public CustomerDto updateCustomer(Long phoneNumber, CustomerDto customerDto) {
        CustomerEntity customerEntity = customerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        if (customerDto.getFirstName() != null) {
            customerEntity.setFirstName(customerDto.getFirstName());
        }
        if (customerDto.getLastName() != null) {
            customerEntity.setLastName(customerDto.getLastName());
        }
        if (customerDto.getUsername() != null) {
            customerEntity.setUsername(customerDto.getUsername());
        }
        if (customerDto.getEmail() != null) {
            customerEntity.setEmail(customerDto.getEmail());
        }
        if (customerDto.getPassword() != null) {
            customerEntity.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        }

        return customerMapper.mapTo(customerRepository.save(customerEntity));

    }

}
