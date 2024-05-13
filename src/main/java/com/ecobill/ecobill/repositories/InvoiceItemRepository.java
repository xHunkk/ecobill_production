package com.ecobill.ecobill.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecobill.ecobill.domain.entities.InvoiceItemEntity;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItemEntity, Long> {
    List<InvoiceItemEntity> findByInvoiceQrCodeAndInvoiceCustomerId(Long qrCode, Long id);

}
