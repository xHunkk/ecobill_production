package com.ecobill.ecobill.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecobill.ecobill.domain.entities.InvoiceEntity;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {
}
