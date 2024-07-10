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
@RequestMapping("/invoicesDev")
public class InvoiceControllerDev {

    private InvoiceService invoiceService;
    private EPRService eprService;
    private SubscriptionService subscriptionService;
    private CustomerService customerService;
    private InvoiceItemService invoiceItemService;
    private Mapper<InvoiceEntity, InvoiceDto> invoiceMapper;

    public InvoiceControllerDev(InvoiceService invoiceService, EPRService eprService,
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
            @RequestParam(name = "max", required = false) Long upperLimit) {

        lowerLimit = lowerLimit == null ? 0 : lowerLimit;
        upperLimit = upperLimit == null ? Long.MAX_VALUE : upperLimit;
        return invoiceService.getInvoiceByAmountLimitsDev(lowerLimit, upperLimit);
    }

    @GetMapping("/items")
    public List<InvoiceItemDto> findInvoiceItemsByQrCode(
            @RequestParam(name = "qr_code") Long qrCode) {

        return invoiceItemService.getInvoiceItemByQrCodeDev(qrCode);
    }

    @GetMapping("/filters/categories")
    public List<InvoiceDto> categorizeInvoices(
            @RequestParam(name = "category") String category) {

        return invoiceService.getInvoiceByEPRCategoryDev(category);
    }

    @GetMapping("/filters/all")
    public List<InvoiceDto> findInvoiceByCreationDateAndTotalAmountAndEprName(
            @RequestParam(name = "before_date") Timestamp beforeDate,
            @RequestParam(name = "after_date") Timestamp afterDate,
            @RequestParam(name = "min", required = false) Long lowerLimit,
            @RequestParam(name = "max", required = false) Long upperLimit,
            @RequestParam(name = "company") String name) {

        return invoiceService.getInvoiceByCreationDateBetweenAndTotalAmountBetweenAndEprNameDev(
                beforeDate, afterDate, lowerLimit, upperLimit, name);
    }

    @GetMapping("/filters/companies")
    public List<InvoiceDto> findInvoiceByEPRName(
            @RequestParam(name = "company") String name) {

        return invoiceService.getInvoiceByEPRNameDev(name);
    }

    @GetMapping("/filters/date")
    public List<InvoiceDto> findInvoiceByDateBetween(
            @RequestParam(name = "before_date") Timestamp beforeDate,
            @RequestParam(name = "after_date") Timestamp afterDate) {

        return invoiceService.getInvoiceByCreationDateBetweenDev(beforeDate, afterDate);
    }

    @GetMapping("/filters/number_range")
    public List<InvoiceDto> findUserInvoiceInRange(Long userNumber, int start, int end) {
        return invoiceService.getInvoiceInRangeBetween(userNumber, start, end);
    }
// i think we do not need the stat for teh all users
//    @GetMapping("/statistics")
//    public HashMap<String, Object> getUserStatistics(
//            @RequestParam(name = "months") int numberOfMonths) {
//
//        return invoiceService.getCustomerStatistics(numberOfMonths);
//    }
}
