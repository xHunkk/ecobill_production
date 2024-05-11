package com.ecobill.ecobill.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ecobill.ecobill.domain.dto.InvoiceDto;
import com.ecobill.ecobill.domain.dto.InvoiceItemDto;
import com.ecobill.ecobill.domain.entities.CustomerEntity;
import com.ecobill.ecobill.domain.entities.EPREntity;
import com.ecobill.ecobill.domain.entities.InvoiceEntity;
import com.ecobill.ecobill.domain.entities.SubscriptionEntity;
import com.ecobill.ecobill.mappers.Mapper;
import com.ecobill.ecobill.services.CustomerService;
import com.ecobill.ecobill.services.EPRService;
import com.ecobill.ecobill.services.InvoiceItemService;
import com.ecobill.ecobill.services.InvoiceService;
import com.ecobill.ecobill.services.SubscriptionService;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private InvoiceService invoiceService;
    private EPRService eprService;
    private SubscriptionService subscriptionService;
    private CustomerService customerService;
    private InvoiceItemService invoiceItemService;
    private Mapper<InvoiceEntity, InvoiceDto> invoiceMapper;

    public InvoiceController(InvoiceService invoiceService, EPRService eprService,
            SubscriptionService subscriptionService, CustomerService customerService,
            InvoiceItemService invoiceItemService, Mapper<InvoiceEntity, InvoiceDto> invoiceMapper) {
        this.invoiceService = invoiceService;
        this.eprService = eprService;
        this.subscriptionService = subscriptionService;
        this.customerService = customerService;
        this.invoiceItemService = invoiceItemService;
        this.invoiceMapper = invoiceMapper;
    }

    @PostMapping()
    public InvoiceDto createNewInvoice(@RequestBody Map<String, Object> requestBody) {
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
            return invoiceMapper.mapTo(invoiceEntity);
        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            return null;
        }

    }

    @GetMapping("/filters/price_range")
    public List<InvoiceDto> findInvoiceByPriceRange(
            @RequestParam(name = "min", required = false) Long lowerLimit,
            @RequestParam(name = "max", required = false) Long upperLimit) {
        lowerLimit = lowerLimit == null ? 0 : lowerLimit;
        upperLimit = upperLimit == null ? Long.MAX_VALUE : upperLimit;
        return invoiceService.getInvoiceByAmountLimits(lowerLimit, upperLimit);
    }

    @GetMapping("/filters/items")
    public List<InvoiceItemDto> findInvoiceItemsByQrCode(@RequestParam(name = "qr_code") Long qrCode) {
        return invoiceItemService.getInvoiceItemByQrCode(qrCode);
    }

    @GetMapping("/filters/categories")
    public List<InvoiceDto> categorizeInvoices(@RequestParam(name = "category") String category) {
        return invoiceService.getInvoiceByEPRCategory(category);
    }

    @GetMapping("/filters/all")
    public List<InvoiceDto> findInvoiceByCreationDateAndTotalAmountAndEprName(
            @RequestParam(name = "phone_number") Long phoneNumber,
            @RequestParam(name = "before_date") Timestamp beforeDate,
            @RequestParam(name = "after_date") Timestamp afterDate,
            @RequestParam(name = "min", required = false) Long lowerLimit,
            @RequestParam(name = "max", required = false) Long upperLimit,
            @RequestParam(name = "company") String name) {
        return invoiceService.getInvoiceByPhoneNumberAndCreationDateBetweenAndTotalAmountBetweenAndEprName(phoneNumber,
                beforeDate, afterDate, lowerLimit, upperLimit, name);
    }

    @GetMapping("/filters/companies")
    public List<InvoiceDto> findInvoiceByEPRName(@RequestParam(name = "company") String name) {
        return invoiceService.getInvoiceByEPRName(name);
    }

    @GetMapping("/filters/date")
    public List<InvoiceDto> findInvoiceByDateBetween(@RequestParam(name = "phone_number") Long phoneNumber,
            @RequestParam(name = "before_date") Timestamp beforeDate,
            @RequestParam(name = "after_date") Timestamp afterDate) {
        return invoiceService.getByCreationDateBetweenAndUserNumber(phoneNumber, beforeDate, afterDate);
    }

    @GetMapping("/filters/number_range")
    public List<InvoiceDto> findUserInvoiceInRange(Long userNumber, int start, int end) {
        return invoiceService.getInvoicesForUserInRange(userNumber, start, end);
    }

    @GetMapping("/statistics")
    public HashMap<String, Object> getUserStatistics(@RequestParam(name = "phone_number") Long phoneNumber,
            @RequestParam(name = "months") int numberOfMonths) {
        return invoiceService.getCustomerStatistics(phoneNumber, numberOfMonths);
    }
}