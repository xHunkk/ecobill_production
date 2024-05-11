package com.ecobill.ecobill.controllers;

import com.ecobill.ecobill.domain.dto.CustomerDto;
import com.ecobill.ecobill.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(path = "/signup")
    public CustomerDto registerCustomer(@RequestBody CustomerDto customerDto) {
        return authService.signUp(customerDto);
    }

    @PostMapping(path = "/login/phone_number")
    public CustomerDto loginByPhoneNumber(@RequestBody CustomerDto customerDto) {
        return authService.loginCustomerByPhoneNumber(customerDto);
    }

    @PostMapping(path = "/login/email")
    public CustomerDto loginByEmail(@RequestBody CustomerDto customerDto) {
        return authService.loginCustomerByEmail(customerDto);
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