package com.ecobill.ecobill.domain.dto;

import java.sql.Date;

import com.ecobill.ecobill.domain.entities.CustomerEntity;
import com.ecobill.ecobill.domain.entities.EPREntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceDto {

    private Integer invoiceId;

    private String name;

    private EPREntity epr;

    private EPREntity eprTaxNumber;

    private CustomerEntity customer;

    private Date invoiceDate;

    private Long totalAmount;

}
