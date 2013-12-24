package com.citytechinc.monitoring.api.notification

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 *
 * @author Josh Durbin, CITYTECH, Inc. 2013
 *
 * Copyright 2013 CITYTECH, Inc.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NotificationAgentDefinition {

    /**
     *
     * Notification strategy for this service.
     *
     * @return
     */
    SubscriptionStrategy subscriptionStrategy() default SubscriptionStrategy.all

    /**
     *
     * @return
     */
    Class[] subscriptionStrategySpecifics() default []
}