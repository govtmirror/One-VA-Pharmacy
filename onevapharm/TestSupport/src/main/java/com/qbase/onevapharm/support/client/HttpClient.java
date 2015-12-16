/*
 * @(#)HttpClient.java
 * Date 2012-09-29
 * Version 1.0
 * Author Jim Horner
 * Copyright (c)2012
 */


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

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import com.google.common.base.Joiner;

import com.google.common.collect.Maps;

import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteStreams;

import com.qbase.onevapharm.support.transformer.MapQueryStringTransformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author jim
 */
@ApplicationScoped
public class HttpClient {

    /** Field description */
    private static final String AUTH_HEADER = "Authorization";

    /** Field description */
    private static final String BASIC_HEADER = "Basic";

    /** Field description */
    private final BaseEncoding base64;

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** Field description */
    @Inject
    private MapQueryStringTransformer queryStringTransformer;

    /**
     * Constructs ...
     *
     */
    public HttpClient() {

        super();
        this.base64 = BaseEncoding.base64Url();
    }

    /**
     * Method description
     *
     *
     *
     *
     * @param endpoint
     * @param queryParams
     *
     * @return
     *
     * @throws IOException
     */
    public byte[] get(HttpEndpoint endpoint, Map<String, String> queryParams)
            throws IOException {

        byte[] result = null;
        HttpURLConnection conn = null;

        try {

            URL url = makeURL(endpoint.getUrl(), queryParams);

            logger.debug("Retrieving {}.", url.toString());

            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty(AUTH_HEADER, makeAuthHeader(endpoint));

            conn.setConnectTimeout(endpoint.getConnectTimeout());
            conn.setReadTimeout(endpoint.getRequestTimeout());
            conn.connect();

            result = ByteStreams.toByteArray(conn.getInputStream());

        } finally {

            if (conn != null) {
                conn.disconnect();
            }
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param endpoint
     *
     * @return
     */
    private String makeAuthHeader(HttpEndpoint endpoint) {

        String authstr = String.format("%s:%s", endpoint.getUserName(),
                                       endpoint.getPassword());

        String auth64 = this.base64.encode(authstr.getBytes());

        return String.format("%s %s", BASIC_HEADER, auth64);
    }

    /**
     * Method description
     *
     *
     * @param resource
     * @param queryParams
     *
     * @return
     *
     * @throws MalformedURLException
     */
    private URL makeURL(URL resource, Map<String, String> queryParams)
            throws MalformedURLException {

        StringBuilder sb = new StringBuilder(resource.toString());

        if ((queryParams != null) && (queryParams.isEmpty() == false)) {

            sb.append("?");

            sb.append(Joiner.on("&").join(Maps.transformEntries(queryParams,
                    this.queryStringTransformer).values()));
        }

        return new URL(sb.toString());
    }

    /**
     * Method description
     *
     *
     *
     * @param endpoint
     * @param queryParams
     * @param contentType
     * @param body
     *
     * @return
     *
     * @throws IOException
     */
    public byte[] postBody(HttpEndpoint endpoint, Map<String, String> queryParams,
                           String contentType, String body)
            throws IOException {

        byte[] result = null;
        HttpURLConnection conn = null;

        try {

            URL url = makeURL(endpoint.getUrl(), queryParams);

            logger.debug("Posting body {} -> {}.", url.toString(), body);

            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty(AUTH_HEADER, makeAuthHeader(endpoint));

            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(endpoint.getConnectTimeout());
            conn.setReadTimeout(endpoint.getRequestTimeout());
            conn.connect();

            conn.getOutputStream().write(body.getBytes());

            result = ByteStreams.toByteArray(conn.getInputStream());

        } finally {

            if (conn != null) {
                conn.disconnect();
            }
        }

        return result;
    }
}
