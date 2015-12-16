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

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;

import ch.qos.logback.classic.spi.ILoggingEvent;

import ch.qos.logback.core.AppenderBase;

import com.qbase.onevapharm.webapp.util.EventBusHolder;

import org.slf4j.MDC;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-03-11
 * @author         Jim Horner
 */
public class WebSocketAppender extends AppenderBase<ILoggingEvent> {

    /** Field description */
    private PatternLayoutEncoder encoder;

    /**
     * Method description
     *
     *
     *
     * @param loggingEvent
     */
    @Override
    protected void append(ILoggingEvent loggingEvent) {

        System.out.println("Posting logging message...");
        EventBusHolder.getInstance().post(loggingEvent);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public PatternLayoutEncoder getEncoder() {
        return encoder;
    }

    /**
     * Method description
     *
     *
     * @param encoder
     */
    public void setEncoder(PatternLayoutEncoder encoder) {
        this.encoder = encoder;
    }
}
