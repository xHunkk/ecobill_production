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

    List<InvoiceEntity> findAllByEpr(EPREntity epr);

    List<InvoiceEntity> findAllByTotalAmountBetween(Long lower, Long upper);

    List<InvoiceEntity> findAllByCustomerPhoneNumberAndCreationDateBetween(Long userNumber, Timestamp lower,
            Timestamp upper);

    @Query(value = "SELECT * FROM invoice WHERE customer_phone_number = ?1 ORDER BY creation_date DESC LIMIT ?2 OFFSET ?3", nativeQuery = true)
    List<InvoiceEntity> findInvoicesByCustomerPhoneNumberWithOffset(Long userNumber, int limit, int offset);
}
