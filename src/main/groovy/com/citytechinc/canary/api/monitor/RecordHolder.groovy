package com.citytechinc.canary.api.monitor

import com.google.common.base.Optional
import com.google.common.collect.EvictingQueue
import com.google.common.collect.Lists
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import groovy.util.logging.Slf4j

import java.math.RoundingMode

/**
 *
 * @author Josh Durbin, CITYTECH, Inc. 2013
 *
 * Copyright 2013 CITYTECH, Inc.
 *
 */
@ToString(includeFields = true, includeNames = true)
@Slf4j
class RecordHolder {

    final String monitorIdentifier
    final Integer maxNumberOfRecords
    final AlarmCriteria alarmCriteria
    final Integer alarmThreshold
    final Queue<DetailedPollResponse> records

    /**
     *
     * @param monitorIdentifier
     * @param maxNumberOfRecords
     * @param alarmCriteria
     * @param alarmThreshold
     * @param records
     */
    public RecordHolder(String monitorIdentifier, Integer maxNumberOfRecords, AlarmCriteria alarmCriteria, Integer alarmThreshold, List<DetailedPollResponse> records) {
        this.monitorIdentifier = monitorIdentifier
        this.maxNumberOfRecords = maxNumberOfRecords
        this.alarmCriteria = alarmCriteria
        this.alarmThreshold = alarmThreshold

        this.records = new EvictingQueue<DetailedPollResponse>(maxNumberOfRecords)
        this.records.addAll(records)
    }

    Boolean addRecord(DetailedPollResponse record) {

        records.offer(record)
        isAlarmed()
    }

    void resetAlarm() {

        if (isAlarmed()) {
            getUnexcusedRecords().each { it.excused = true }
        }
    }

    List<DetailedPollResponse> getRecords() {
        (records as List).reverse()
    }

    List<DetailedPollResponse> getUnexcusedRecords() {
        getRecords().findAll { !it.excused }
    }

    Optional<Date> mostRecentPollDate() {

        records.empty ? Optional.absent() : Optional.of(getRecords().first().startTime)
    }

    Optional<PollResponseType> mostRecentPollResponse() {

        records.empty ? Optional.absent() : Optional.of(getRecords().first().responseType)
    }

    Integer numberOfPolls() {
        records.size()
    }

    Integer numberOfFailures() {
        getRecords().count { it.responseType != PollResponseType.SUCCESS}
    }

    BigDecimal failureRate(Boolean useUnexcused) {

        def scrutinizedRecords = useUnexcused ? getUnexcusedRecords() : getRecords()

        if (!scrutinizedRecords.empty) {

            BigDecimal failureRate = new BigDecimal(scrutinizedRecords.findAll { it.responseType != PollResponseType.SUCCESS }.size())
                    .divide(new BigDecimal(scrutinizedRecords.size()), 2, RoundingMode.HALF_DOWN)
            failureRate.movePointRight(2)
        } else {

            BigDecimal.ZERO
        }

    }

    Long averagePollExecutionTime(Boolean useUnexcused) {

        Long averageExecutionTime = 0L

        def scrutinizedRecords = useUnexcused ? getUnexcusedRecords() : getRecords()

        if (records.size() > 0) {

            Long numberOfRecords = scrutinizedRecords.size() as Long
            Long totalExecutionTime = 0L

            scrutinizedRecords.each { totalExecutionTime += it.executionTimeInMilliseconds()}

            averageExecutionTime = totalExecutionTime / numberOfRecords
        }

        averageExecutionTime
    }

    Boolean isAlarmed() {

        Boolean alarmed = false

        if (alarmCriteria == AlarmCriteria.AVERAGE_FAILURE_RATE) {

            alarmed = failureRate(true) > alarmThreshold

        } else if (alarmCriteria == AlarmCriteria.AVERAGE_EXECUTION_TIME) {

            alarmed = averagePollExecutionTime(true) > alarmThreshold

        } else if (alarmCriteria == AlarmCriteria.RECENT_POLLS) {

            if (getUnexcusedRecords().size() > alarmThreshold) {

                final List<DetailedPollResponse> scrutinizedRecords

                if (getRecords().size() > alarmThreshold) {
                    scrutinizedRecords = Lists.partition(getRecords(), alarmThreshold).first()
                } else {
                    scrutinizedRecords = getUnexcusedRecords()
                }

                alarmed = scrutinizedRecords.findAll { it.responseType != PollResponseType.SUCCESS }.size() == alarmThreshold
            }
        }

        alarmed
    }

}
