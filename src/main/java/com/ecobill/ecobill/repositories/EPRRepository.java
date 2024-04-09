package com.ecobill.ecobill.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecobill.ecobill.domain.entities.EPREntity;

public interface EPRRepository extends JpaRepository<EPREntity, Long> {

}
