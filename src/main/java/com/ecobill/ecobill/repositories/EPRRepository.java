package com.ecobill.ecobill.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecobill.ecobill.domain.entities.EPREntity;

public interface EPRRepository extends JpaRepository<EPREntity, Long> {

    Optional<EPREntity> findByCommercialRegister(Long commercialRegister);

}
