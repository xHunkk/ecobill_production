package com.ecobill.ecobill.repositories;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecobill.ecobill.domain.entities.EPREntity;
import com.ecobill.ecobill.domain.entities.InvoiceEntity;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {

        Optional<InvoiceEntity> findByQrCode(Long qrCode);

        List<InvoiceEntity> findAllByEprAndCustomerId(EPREntity epr, Long id);

        List<InvoiceEntity> findAllByTotalAmountBetweenAndCustomerId(Long lowerLimit, Long upperLimit, Long id);

        List<InvoiceEntity> findAllByCustomerIdAndCreationDateBetween(Long id, Timestamp beforeDate,
                        Timestamp afterDate);

        List<InvoiceEntity> findAllByCustomerIdAndCreationDateBetweenAndTotalAmountBetweenAndEprName(
                        Long id, Timestamp beforeDate, Timestamp afterDate, Long lowerLimit, long upperLimit,
                        String name);

        @Query(value = "SELECT * FROM invoice WHERE customer_id = ?1 ORDER BY creation_date DESC LIMIT ?2 OFFSET ?3", nativeQuery = true)
        List<InvoiceEntity> findInvoicesByCustomerIdWithOffset(Long id, int limit, int offset);
}
