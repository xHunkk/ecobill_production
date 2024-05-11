package com.ecobill.ecobill.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecobill.ecobill.domain.entities.CustomerEntity;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByPhoneNumber(Long phoneNumber);

    Optional<CustomerEntity> findByEmailAndPassword(String email, String password);

    Optional<CustomerEntity> findByEmail(String email);

    Optional<CustomerEntity> findByPhoneNumberAndPassword(Long phoneNumber, String password);

    boolean existsByPhoneNumber(Long phoneNumber);

}
