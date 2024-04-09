package com.ecobill.ecobill.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ecobill.ecobill.domain.entities.CustomerEntity;
import com.ecobill.ecobill.domain.entities.EPREntity;
import com.ecobill.ecobill.domain.entities.InvoiceEntity;
import com.ecobill.ecobill.domain.entities.SubscriptionEntity;
import com.ecobill.ecobill.services.CustomerService;
import com.ecobill.ecobill.services.EPRService;
import com.ecobill.ecobill.services.InvoiceItemService;
import com.ecobill.ecobill.services.InvoiceService;
import com.ecobill.ecobill.services.SubscriptionService;

import java.util.List;
import java.util.Map;

@RestController
public class InvoiceController {

    private InvoiceService invoiceService;
    private EPRService eprService;
    private SubscriptionService subscriptionService;
    private CustomerService customerService;
    private InvoiceItemService invoiceItemService;

    public InvoiceController(InvoiceService invoiceService, EPRService eprService,
            SubscriptionService subscriptionService, CustomerService customerService,
            InvoiceItemService invoiceItemService) {
        this.invoiceService = invoiceService;
        this.eprService = eprService;
        this.subscriptionService = subscriptionService;
        this.customerService = customerService;
        this.invoiceItemService = invoiceItemService;
    }

    @PostMapping(path = "/invoices")
    public ResponseEntity<Void> createInvoice(@RequestBody Map<String, Object> requestBody) {
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

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}