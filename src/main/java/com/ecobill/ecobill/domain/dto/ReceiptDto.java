package com.ecobill.ecobill.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiptDto {

    private EPRDto eprDto;

    private CustomerDto customerDto;

    private InvoiceItemDto invoiceItemDto;

    private InvoiceDto invoiceDto;

}
