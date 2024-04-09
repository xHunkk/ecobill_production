package com.ecobill.ecobill.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecobill.ecobill.domain.entities.SubscriptionEntity;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

}
