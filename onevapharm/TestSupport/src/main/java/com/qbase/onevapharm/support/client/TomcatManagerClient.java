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

import java.net.URL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.base.Splitter;

import com.qbase.onevapharm.config.ActionType;
import com.qbase.onevapharm.config.EndpointConfig;
import com.qbase.onevapharm.config.SiteConfig;
import com.qbase.onevapharm.config.TimeoutConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-06-10
 * @author         Jim Horner
 */
public class TomcatManagerClient {

    /** Field description */
    private final JsonNodeFactory factory;

    /** Field description */
    @Inject
    private HttpClient httpClient;

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** Field description */
    private final ObjectMapper mapper;

    /**
     * Constructs ...
     *
     */
    public TomcatManagerClient() {

        super();
        this.factory = JsonNodeFactory.instance;
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    /**
     * Method description
     *
     *
     * @param siteConfig
     * @param contextPath
     *
     * @return
     *
     * @throws IOException
     */
    public JsonNode deployApplication(SiteConfig siteConfig, String contextPath)
            throws IOException {

        EndpointConfig endpoint = siteConfig.getEndpoint();
        String url = String.format("%s/deploy", endpoint.getUrl().toString());

        return executeURL(siteConfig, url, contextPath);
    }

    /**
     * Method description
     *
     *
     * @param siteConfig
     * @param url
     * @param contextPath
     *
     * @return
     *
     * @throws IOException
     */
    public JsonNode executeURL(SiteConfig siteConfig, String url, String contextPath)
            throws IOException {

        EndpointConfig endpoint = siteConfig.getEndpoint();

        ObjectNode result = this.factory.objectNode();

        HttpEndpoint httpEndpoint = new HttpEndpoint(new URL(url),
                                        siteConfig.getUserName(),
                                        siteConfig.getPassword());

        TimeoutConfig timeoutConfig = endpoint.getTimeoutConfig(ActionType.Default);

        httpEndpoint.setConnectTimeout(timeoutConfig.getConnectTimeout());
        httpEndpoint.setRequestTimeout(timeoutConfig.getRequestTimeout());

        Map<String, String> params = new HashMap<>();

        if (contextPath != null) {

            params.put("path", contextPath);
        }

        byte[] bytes = this.httpClient.get(httpEndpoint, params);

        result.put("result", new String(bytes));

        if (logger.isDebugEnabled()) {

            logger.debug("Response -> {}", this.mapper.writeValueAsString(result));
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param siteConfig
     *
     * @return
     *
     * @throws IOException
     */
    public JsonNode listApplications(SiteConfig siteConfig) throws IOException {

        EndpointConfig endpoint = siteConfig.getEndpoint();
        String url = String.format("%s/list", endpoint.getUrl().toString());

        JsonNode appsNode = executeURL(siteConfig, url, null);
        ObjectNode result = this.factory.objectNode();

        List<String> apps =
            Splitter.on("\r\n").splitToList(appsNode.get("result").asText());

        for (String app : apps) {

            if (app.startsWith("/")) {

                List<String> stats = Splitter.on(":").splitToList(app);

                ObjectNode appNode = this.factory.objectNode();

                appNode.put("state", stats.get(1));
                appNode.put("sessions", stats.get(2));

                result.put(stats.get(0), appNode);

            } else {

                if (result.path("result").isMissingNode()) {

                    result.put("result", app);
                }
            }
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param siteConfig
     * @param contextPath
     *
     * @return
     *
     * @throws IOException
     */
    public JsonNode startApplication(SiteConfig siteConfig, String contextPath)
            throws IOException {

        EndpointConfig endpoint = siteConfig.getEndpoint();
        String url = String.format("%s/start", endpoint.getUrl().toString());

        return executeURL(siteConfig, url, contextPath);
    }

    /**
     * Method description
     *
     *
     * @param siteConfig
     * @param contextPath
     *
     * @return
     *
     * @throws IOException
     */
    public JsonNode stopApplication(SiteConfig siteConfig, String contextPath)
            throws IOException {

        EndpointConfig endpoint = siteConfig.getEndpoint();
        String url = String.format("%s/stop", endpoint.getUrl().toString());

        return executeURL(siteConfig, url, contextPath);
    }
}
