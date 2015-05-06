package com.qbase.onevapharm.webapp.web;

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

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import javax.servlet.annotation.WebListener;

import com.qbase.onevapharm.webapp.logging.LoggingBroadcaster;

import com.qbase.onevapharm.webapp.util.EventBusHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-03-13
 * @author         Jim Horner
 */
@WebListener
public class EventBusStartupListener implements ServletContextListener {

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Method description
     *
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        // do nothing
    }

    /**
     * Method description
     *
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        EventBusHolder.getInstance().register(new LoggingBroadcaster());
        
        logger.info("EventBus started.");
    }
}
