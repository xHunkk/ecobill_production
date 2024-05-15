package com.ecobill.ecobill.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ecobill.ecobill.domain.dto.InvoiceDto;
import com.ecobill.ecobill.domain.dto.InvoiceItemDto;
import com.ecobill.ecobill.domain.entities.CustomerEntity;
import com.ecobill.ecobill.domain.entities.EPREntity;
import com.ecobill.ecobill.domain.entities.InvoiceEntity;
import com.ecobill.ecobill.domain.entities.SubscriptionEntity;
import com.ecobill.ecobill.mappers.Mapper;
import com.ecobill.ecobill.services.AuthService;
import com.ecobill.ecobill.services.CustomerService;
import com.ecobill.ecobill.services.EPRService;
import com.ecobill.ecobill.services.InvoiceItemService;
import com.ecobill.ecobill.services.InvoiceService;
import com.ecobill.ecobill.services.SubscriptionService;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private InvoiceService invoiceService;
    private EPRService eprService;
    private SubscriptionService subscriptionService;
    private CustomerService customerService;
    private InvoiceItemService invoiceItemService;
    private Mapper<InvoiceEntity, InvoiceDto> invoiceMapper;
    private AuthService authService;

    public InvoiceController(InvoiceService invoiceService, EPRService eprService,
            SubscriptionService subscriptionService, CustomerService customerService,
            InvoiceItemService invoiceItemService, Mapper<InvoiceEntity, InvoiceDto> invoiceMapper,
            AuthService authService) {
        this.invoiceService = invoiceService;
        this.eprService = eprService;
        this.subscriptionService = subscriptionService;
        this.customerService = customerService;
        this.invoiceItemService = invoiceItemService;
        this.invoiceMapper = invoiceMapper;
        this.authService = authService;

    }

    @PostMapping()
    public ResponseEntity<InvoiceDto> createNewInvoice(@RequestBody Map<String, Object> requestBody) {
        try {
            Map<String, Object> eprMap = (Map<String, Object>) requestBody.get("epr");
            Map<String, Object> customerMap = (Map<String, Object>) requestBody.get("customer");
            Map<String, Object> invoiceMap = (Map<String, Object>) requestBody.get("invoice");
            Map<String, Object> subscriptionMap = (Map<String, Object>) requestBody.get("subscription");
            List<Map<String, Object>> invoiceItemsList = (List<Map<String, Object>>) requestBody.get("invoice_items");

            SubscriptionEntity subscriptionEntity = subscriptionService.createSubscription(subscriptionMap);
            EPREntity eprEntity = eprService.createEpr(eprMap, subscriptionEntity);
            CustomerEntity customerEntity = customerService.createCustomer(customerMap);
            InvoiceEntity invoiceEntity = invoiceService.createInvoice(invoiceMap, eprEntity, customerEntity);
            invoiceItemService.createInvoiceItem(invoiceItemsList, invoiceEntity);

            ResponseEntity.status(HttpStatus.CREATED).build();
            return ResponseEntity.status(HttpStatus.CREATED).body(invoiceMapper.mapTo(invoiceEntity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/filters/price_range")
    public List<InvoiceDto> findInvoiceByPriceRange(
            @RequestParam(name = "min", required = false) Long lowerLimit,
            @RequestParam(name = "max", required = false) Long upperLimit,
            HttpServletRequest request) {

        Long id = authService.authenticateToken(request);

        if (id != null) {
            lowerLimit = lowerLimit == null ? 0 : lowerLimit;
            upperLimit = upperLimit == null ? Long.MAX_VALUE : upperLimit;
            return invoiceService.getInvoiceByAmountLimits(lowerLimit, upperLimit, id);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
        }

    }

    @GetMapping("/items")
    public List<InvoiceItemDto> findInvoiceItemsByQrCode(HttpServletRequest request,
            @RequestParam(name = "qr_code") Long qrCode) {

        Long id = authService.authenticateToken(request);

        if (id != null) {
            return invoiceItemService.getInvoiceItemByQrCode(qrCode, id);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
        }
    }

    @GetMapping("/filters/categories")
    public List<InvoiceDto> categorizeInvoices(HttpServletRequest request,
            @RequestParam(name = "category") String category) {

        Long id = authService.authenticateToken(request);

        if (id != null) {
            return invoiceService.getInvoiceByEPRCategory(category, id);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
        }
    }

    @GetMapping("/filters/all")
    public List<InvoiceDto> findInvoiceByCreationDateAndTotalAmountAndEprName(HttpServletRequest request,
            @RequestParam(name = "before_date") Timestamp beforeDate,
            @RequestParam(name = "after_date") Timestamp afterDate,
            @RequestParam(name = "min", required = false) Long lowerLimit,
            @RequestParam(name = "max", required = false) Long upperLimit,
            @RequestParam(name = "company") String name) {

        Long id = authService.authenticateToken(request);

        if (id != null) {
            return invoiceService.getInvoiceByCreationDateBetweenAndTotalAmountBetweenAndEprName(
                    id,
                    beforeDate, afterDate, lowerLimit, upperLimit, name);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
        }
    }

    @GetMapping("/filters/companies")
    public List<InvoiceDto> findInvoiceByEPRName(HttpServletRequest request,
            @RequestParam(name = "company") String name) {

        Long id = authService.authenticateToken(request);

        if (id != null) {
            return invoiceService.getInvoiceByEPRName(name, id);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
        }
    }

    @GetMapping("/filters/date")
    public List<InvoiceDto> findInvoiceByDateBetween(HttpServletRequest request,
            @RequestParam(name = "before_date") Timestamp beforeDate,
            @RequestParam(name = "after_date") Timestamp afterDate) {

        Long id = authService.authenticateToken(request);

        if (id != null) {
            return invoiceService.getInvoiceByCreationDateBetween(id, beforeDate, afterDate);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");

        }
    }

    @GetMapping("/filters/number_range")
    public List<InvoiceDto> findUserInvoiceInRange(Long userNumber, int start, int end) {
        return invoiceService.getInvoiceInRangeBetween(userNumber, start, end);
    }

    @GetMapping("/statistics")
    public HashMap<String, Object> getUserStatistics(
            @RequestParam(name = "months") int numberOfMonths, HttpServletRequest request) {

        Long id = authService.authenticateToken(request);

        if (id != null) {
            return invoiceService.getCustomerStatistics(id, numberOfMonths);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
        }

    }
}