package com.qbase.onevapharm.support.client;

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

import java.net.URL;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-05-23
 * @author         Jim Horner
 */
public class HttpEndpoint {

    /** Field description */
    private static final int DEF_CONNTIMEOUT = 4000;

    /** Field description */
    private static final int DEF_READTIMEOUT = 8000;

    /** Field description */
    private int connectTimeout;

    /** Field description */
    private final String password;

    /** Field description */
    private int requestTimeout;

    /** Field description */
    private final URL url;

    /** Field description */
    private final String userName;

    /**
     * Constructs ...
     *
     *
     * @param url
     * @param username
     * @param password
     */
    public HttpEndpoint(URL url, String username, String password) {

        super();

        this.url = url;
        this.userName = username;
        this.password = password;

        this.connectTimeout = DEF_CONNTIMEOUT;
        this.requestTimeout = DEF_READTIMEOUT;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public int getConnectTimeout() {
        return connectTimeout;
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
    public int getRequestTimeout() {
        return requestTimeout;
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
    public String getUserName() {
        return userName;
    }

    /**
     * Method description
     *
     *
     * @param connectTimeout
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * Method description
     *
     *
     * @param requestTimeout
     */
    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }
}
