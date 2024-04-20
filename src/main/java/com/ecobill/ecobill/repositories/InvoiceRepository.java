package com.ecobill.ecobill.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecobill.ecobill.domain.entities.InvoiceEntity;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {

    Optional<InvoiceEntity> findByQrCode(Long qrCode);
}
