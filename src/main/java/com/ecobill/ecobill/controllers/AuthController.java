package com.ecobill.ecobill.controllers;

import com.ecobill.ecobill.domain.dto.CustomerDto;
import com.ecobill.ecobill.domain.dto.JwtResponseDto;
import com.ecobill.ecobill.services.AuthService;
import com.ecobill.ecobill.utils.JwtUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;
    private JwtUtils jwtUtils;

    public AuthController(AuthService authService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping(path = "/signup")
    public CustomerDto registerCustomer(@RequestBody CustomerDto customerDto) {
        return authService.signUp(customerDto);
    }

    @PostMapping(path = "/login/phone_number")
    public ResponseEntity<?> loginByPhoneNumber(@RequestBody CustomerDto customerDto) {
        CustomerDto authenticatedCustomer = authService.loginCustomerByPhoneNumber(customerDto);

        String token = jwtUtils.generateToken(String.valueOf(authenticatedCustomer.getId()));

        return ResponseEntity.ok(new JwtResponseDto(token, authenticatedCustomer.getId()));
    }

    @PostMapping(path = "/login/email")
    public ResponseEntity<?> loginByEmail(@RequestBody CustomerDto customerDto) {
        CustomerDto authenticatedCustomer = authService.loginCustomerByEmail(customerDto);

        String token = jwtUtils.generateToken(String.valueOf(authenticatedCustomer.getPhoneNumber()));

        return ResponseEntity.ok(new JwtResponseDto(token, authenticatedCustomer.getId()));
    }

    @PatchMapping("/{phoneNumber}")
    public CustomerDto editCustomerInfo(@PathVariable Long phoneNumber,
            @RequestBody CustomerDto customerDto) {
        if (customerDto.getPhoneNumber() != null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return null;
        }
        return authService.updateCustomer(phoneNumber, customerDto);

    }
}