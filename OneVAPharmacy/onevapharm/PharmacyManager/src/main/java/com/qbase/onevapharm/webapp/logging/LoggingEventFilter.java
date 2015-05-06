package com.qbase.onevapharm.webapp.logging;

/*
* #%L
 * * OneVA Pharmacy
 * *
 * %%
 * Copyright (C) 2013 - 2014 Qbase
 * *
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ch.qos.logback.classic.spi.ILoggingEvent;

import ch.qos.logback.core.filter.Filter;

import ch.qos.logback.core.spi.FilterReply;

import org.slf4j.MDC;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-03-13
 * @author         Jim Horner
 */
public class LoggingEventFilter extends Filter<ILoggingEvent> {

    /** Field description */
    public static final String MDC_NAME = "LoggingEvent";

    /**
     * Method description
     *
     *
     * @param e
     *
     * @return
     */
    @Override
    public FilterReply decide(ILoggingEvent e) {

        return ((MDC.get(MDC_NAME) != null) ? FilterReply.ACCEPT : FilterReply.DENY);
    }
}
