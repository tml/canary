package com.citytechinc.monitoring.services.persistence

import com.citytechinc.monitoring.services.monitor.PollResponseType
import groovy.util.logging.Slf4j
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Properties as SCRProperties
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Service
import org.apache.sling.api.adapter.AdapterFactory
import org.apache.sling.api.resource.Resource
import org.apache.sling.api.resource.ValueMap
import org.osgi.framework.Constants as OsgiConstants

/**
 *
 * @author Josh Durbin, CITYTECH, Inc. 2013
 *
 * Copyright 2013 CITYTECH, Inc.
 *
 */
@Component(immediate = true)
@Service(AdapterFactory)
@SCRProperties([
    @Property(name = OsgiConstants.SERVICE_DESCRIPTION, value = "Monitor Record Adapter Factory"),
    @Property(name = "adapters", value = ['com.citytechinc.monitoring.services.persistence.ServiceMonitorRecord']),
    @Property(name = "adaptables", value = ['org.apache.sling.api.resource.Resource'])])
@Slf4j
class MonitorRecordAdapterFactory implements AdapterFactory {

    @Override
    def <AdapterType> AdapterType getAdapter(Object adaptable, Class<AdapterType> adapterTypeClass) {

        final AdapterType result

        final Resource resource = (Resource)adaptable
        final ValueMap properties = resource.adaptTo(ValueMap)

        def record = new ServiceMonitorRecord()

        record.monitoredService = properties.get('monitoredService')
        record.startTime = properties.get('startTime')
        record.endTime = properties.get('endTime')
        record.responseType = PollResponseType.valueOf(properties.get('responseType'))
        record.stackTrace = properties.get('stackTrace')

        (AdapterType) record
    }
}
