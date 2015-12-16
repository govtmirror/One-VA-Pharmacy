package com.qbase.onevapharm.config;

/*
 * #%L
 * OneVA Pharmacy
 * %%
 * Copyright (C) 2013 - 2014 Qbase
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

/**
 * Class description
 *
 *
 * @version        v1.0, 2013-12-28
 * @author         Jim Horner
 */
public class SiteConfig {

    /** Field description */
    private EndpointConfig endpoint;

    /** Field description */
    private String name;

    /** Field description */
    private String password;

    /** Field description */
    private String siteNumber;

    /** Field description */
    private String userName;

    /**
     * Constructs ...
     *
     */
    public SiteConfig() {
        super();
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public EndpointConfig getEndpoint() {
        return endpoint;
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
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getSiteNumber() {
        return siteNumber;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Method description
     *
     *
     * @param endpoint
     */
    public void setEndpoint(EndpointConfig endpoint) {
        this.endpoint = endpoint;
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
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Method description
     *
     *
     * @param siteNumber
     */
    public void setSiteNumber(String siteNumber) {
        this.siteNumber = siteNumber;
    }

    /**
     * Method description
     *
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
