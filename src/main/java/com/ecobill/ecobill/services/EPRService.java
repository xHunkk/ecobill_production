package com.ecobill.ecobill.services;

import java.util.Map;

import com.ecobill.ecobill.domain.entities.EPREntity;
import com.ecobill.ecobill.domain.entities.SubscriptionEntity;

public interface EPRService {
    EPREntity createEpr(Map<String, Object> eprMap, SubscriptionEntity subscriptionEntity);

}
