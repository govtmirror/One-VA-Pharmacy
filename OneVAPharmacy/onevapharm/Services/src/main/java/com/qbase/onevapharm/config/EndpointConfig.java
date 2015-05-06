package com.qbase.onevapharm.config;

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

import java.net.URL;

import java.util.HashMap;
import java.util.Map;


/**
 * Class description
 *
 *
 * @version        v1.0, 2013-12-27
 * @author         Jim Horner
 */
public class EndpointConfig {

    /** Field description */
    private static final int DEFAULT_CONNECT_TIMEOUT = 5000;

    /** Field description */
    private static final int DEFAULT_REQUEST_TIMEOUT = 10000;

    /** Field description */
    private boolean debugMode;

    /** Field description */
    private String id;

    /** Field description */
    private String name;

    /** Field description */
    private Map<ActionType, TimeoutConfig> timeouts;

    /** Field description */
    private URL url;

    /**
     * Constructs ...
     *
     */
    public EndpointConfig() {

        super();

        this.timeouts = new HashMap<>();
    }

    /**
     * Constructs ...
     *
     *
     * @param url
     */
    public EndpointConfig(URL url) {

        this();
        this.url = url;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Method description
     *
     *
     * @param action
     *
     * @return
     */
    public TimeoutConfig getTimeoutConfig(ActionType action) {

        TimeoutConfig result = this.timeouts.get(action);

        if (result == null) {

            result = new TimeoutConfig(DEFAULT_CONNECT_TIMEOUT, DEFAULT_REQUEST_TIMEOUT);
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public Map<ActionType, TimeoutConfig> getTimeouts() {
        return timeouts;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean isDebugMode() {
        return debugMode;
    }

    /**
     * Method description
     *
     *
     * @param debugMode
     */
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    /**
     * Method description
     *
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Method description
     *
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method description
     *
     *
     * @param timeouts
     */
    public void setTimeouts(Map<ActionType, TimeoutConfig> timeouts) {
        this.timeouts = timeouts;
    }

    /**
     * Method description
     *
     *
     * @param url
     */
    public void setUrl(URL url) {
        this.url = url;
    }
}
