package com.ecobill.ecobill.repositories;


import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ecobill.ecobill.domain.entities.EPREntity;
import com.ecobill.ecobill.domain.entities.InvoiceEntity;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {

        Optional<InvoiceEntity> findByQrCode(Long qrCode);

        List<InvoiceEntity> findByCustomer_IdAndEpr_Name(Long customerId, String eprName);

        List<InvoiceEntity> findByCustomer_IdAndEpr_Category(Long customerId, String category);

        List<InvoiceEntity> findByCustomer_IdAndTotalAmountWithVatBetween(Long customerId, double minAmount, double maxAmount);

        List<InvoiceEntity> findAllByCustomerIdAndCreationDateBetween(Long id, Timestamp beforeDate,
                        Timestamp afterDate);

        List<InvoiceEntity> findAllByCustomerIdAndCreationDateBetweenAndTotalAmountBetweenAndEprName(
                        Long id, Timestamp beforeDate, Timestamp afterDate, Long lowerLimit, long upperLimit,
                        String name);

        Page<InvoiceEntity> findByCustomer_Id(Long customerId, Pageable pageable);
}
