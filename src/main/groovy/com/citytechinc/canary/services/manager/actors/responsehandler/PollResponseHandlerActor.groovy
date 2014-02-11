package com.citytechinc.canary.services.manager.actors.responsehandler

import com.citytechinc.canary.api.monitor.DetailedPollResponse
import com.citytechinc.canary.api.notification.SubscriptionStrategy
import com.citytechinc.canary.api.responsehandler.PollResponseWrapper
import com.citytechinc.canary.services.manager.actors.MissionControlActor
import com.citytechinc.canary.services.manager.actors.Statistics
import com.google.common.base.Stopwatch
import groovy.util.logging.Slf4j
import groovyx.gpars.actor.DynamicDispatchActor

import java.util.concurrent.TimeUnit

/**
 *
 * @author Josh Durbin, CITYTECH, Inc. 2013
 *
 * Copyright 2013 CITYTECH, Inc.
 *
 */
@Slf4j
final class PollResponseHandlerActor extends DynamicDispatchActor {

    static class PollResponseReceipt {

        String identifier
        DetailedPollResponse response
    }

    PollResponseWrapper wrapper
    Statistics statistics = new Statistics()

    void onMessage(MissionControlActor.GetStatistics message) {
        sender.send(statistics.clone())
    }

    void onMessage(PollResponseReceipt message) {

        ++statistics.deliveredMessages

        if (((wrapper.definition.strategy() == SubscriptionStrategy.OPT_INTO) && (wrapper.definition.specifics().contains(message.identifier)))
            || ((wrapper.definition.strategy() == SubscriptionStrategy.OPT_OUT_OF) && (!wrapper.definition.specifics().contains(message.identifier)))
            || (wrapper.definition.strategy() == SubscriptionStrategy.ALL)) {

            Stopwatch stopwatch = Stopwatch.createStarted()

            try {

                wrapper.handleResponse(message.identifier, message.response)
                ++statistics.processedMessages

            } catch (Exception e) {

                log.error("An EXCEPTION occurred calling the poll response handler: ${wrapper.identifier}", e)
                ++statistics.messageExceptions
            }

            statistics.addAndCalculateAverageProcessTime(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS))
        }
    }
}
