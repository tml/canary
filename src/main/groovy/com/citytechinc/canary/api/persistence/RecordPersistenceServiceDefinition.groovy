package com.citytechinc.canary.api.persistence

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
public @interface RecordPersistenceServiceDefinition {

    /**
     *
     * The top ranking service is used to load records on start.
     *
     * @return
     */
    int ranking() default 100

    /**
     *
     * @return
     */
    boolean providesReadOperations() default true

    /**
     *
     * @return
     */
    boolean providesWriteOperations() default true

    /**
     *
     * @return
     */
    long maxExecutionTimeInMilliseconds() default 500L
}