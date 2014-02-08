package com.citytechinc.canary.constants;

import org.apache.commons.lang.time.FastDateFormat;

/**
 *
 * @author Josh Durbin, CITYTECH, Inc. 2013
 *
 * Copyright 2013 CITYTECH, Inc.
 *
 */
public final class Constants {

    /**
     * Deny outside instantiation.
     */
    private Constants() {

    }

    public static final String CITYTECH_SERVICE_VENDOR_NAME = "CITYTECH, Inc.";

    public static final String JMX_DATE_TIME_DEFINITION = "yyyy-MM-dd HH:mm:ss";
    public static final FastDateFormat JMX_DATE_TIME_FORMATTER = FastDateFormat.getInstance(JMX_DATE_TIME_DEFINITION);

    public static final String JCR_POLL_RESPONSE_NODE_STORAGE_DEFINITION = "yyyy-MM-dd-HH-mm-ss-SSS";
    public static final FastDateFormat JCR_POLL_RESPONSE_NODE_STORAGE_FORMATTER = FastDateFormat.getInstance(JCR_POLL_RESPONSE_NODE_STORAGE_DEFINITION);

    public static final String SERVICE_MONITOR_DASHBOARD_PAGE_COMPONENT_PATH = "/apps/osgi-service-monitor/components/page/servicemonitordashboard";
    public static final String SERVICE_MONITOR_DASHBOARD_TEMPLATE_PATH = "/apps/osgi-service-monitor/templates/servicemonitordashboard";
    public static final String DASHBOARD_COMPONENT_PATH = "osgi-service-monitor/components/content/dashboard";

}