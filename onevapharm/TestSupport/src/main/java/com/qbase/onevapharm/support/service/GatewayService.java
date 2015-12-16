package com.qbase.onevapharm.support.service;

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

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import com.qbase.onevapharm.client.CaterpillarClient;
import com.qbase.onevapharm.config.ActionType;

import com.qbase.onevapharm.config.ManagerConfig;
import com.qbase.onevapharm.config.SiteConfig;

import com.qbase.onevapharm.support.client.TomcatManagerClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-06-12
 * @author         Jim Horner
 */
public class GatewayService {

    /** Field description */
    private static final String GDSITENUMBER = "GatewayDelay";

    /** Field description */
    private static final String GSSITENUMBER = "GatewayStop";

    /** Field description */
    private static final String PMSITENUMBER = "PharmacyManagerSupport";

    /** Field description */
    @Inject
    private ManagerConfig config;

    /** Field description */
    private final JsonNodeFactory factory;

    /** Field description */
    @Inject
    private CaterpillarClient jsonClient;

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** Field description */
    private final ObjectMapper mapper;

    /** Field description */
    @Inject
    private TomcatManagerClient tomcatClient;

    /**
     * Constructs ...
     *
     */
    public GatewayService() {

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
     * @param action
     * @param siteNumber
     * @param delay
     *
     * @return
     *
     * @throws IOException
     */
    public JsonNode execute(String action, String siteNumber, Integer delay)
            throws IOException {

        JsonNode result = null;

        if ("2200".equals(siteNumber)) {

            String gwSiteNumber = String.format("Gateway%s", siteNumber);

            SiteConfig gwSiteConfig = this.config.findSite(gwSiteNumber);
            String context = "/LLPRouter";

            // Pharmacy Manager
            if ("start".equalsIgnoreCase(action)) {

                JsonNode appsNode = this.tomcatClient.listApplications(gwSiteConfig);
                JsonNode stateNode = appsNode.path(context).path("state");

                if (appsNode.path(context).isMissingNode()) {

                    result = this.tomcatClient.deployApplication(gwSiteConfig, context);

                } else if ((stateNode.isMissingNode() == false)
                           && "stopped".equals(stateNode.asText())) {

                    result = this.tomcatClient.startApplication(gwSiteConfig, context);

                } else {

                    ObjectNode errNode = this.factory.objectNode();
                    String msg =
                        String.format("Servlet %s is already running; see status.",
                                      context);

                    errNode.put("error", msg);
                    errNode.put("status", appsNode);

                    result = errNode;
                }
            } else if ("stop".equalsIgnoreCase(action)) {

                JsonNode appsNode = this.tomcatClient.listApplications(gwSiteConfig);

                if (appsNode.path(context).isMissingNode() == false) {

                    result = this.tomcatClient.stopApplication(gwSiteConfig, context);

                } else {

                    ObjectNode errNode = this.factory.objectNode();
                    String msg = String.format("Servlet %s is not running; see status.",
                                               context);

                    errNode.put("error", msg);
                    errNode.put("status", appsNode);

                    result = errNode;
                }

            } else if ("status".equalsIgnoreCase(action)) {

                result = this.tomcatClient.listApplications(gwSiteConfig);
            }

        } else {

            SiteConfig pmSiteConfig = this.config.findSite(PMSITENUMBER);

            // VistA Systems

            if ("start".equalsIgnoreCase(action)) {

                SiteConfig siteConfig = this.config.findSite(siteNumber);
                URL url = siteConfig.getEndpoint().getUrl();

                Multimap<String, String> params = ArrayListMultimap.create();

                params.put("url", url.toString());

                String path = String.format("/site/%s", siteNumber);
                URL pmurl = this.jsonClient.buildURL(pmSiteConfig.getEndpoint().getUrl(),
                                path, params);

                result = this.jsonClient.getJson(pmSiteConfig, ActionType.Default, pmurl);

            } else if ("stop".equalsIgnoreCase(action)) {

                SiteConfig stopSiteConfig = this.config.findSite(GSSITENUMBER);
                URL url = stopSiteConfig.getEndpoint().getUrl();

                Multimap<String, String> params = ArrayListMultimap.create();

                params.put("url", url.toString());

                String path = String.format("/site/%s", siteNumber);
                URL pmurl = this.jsonClient.buildURL(pmSiteConfig.getEndpoint().getUrl(),
                                path, params);

                result = this.jsonClient.getJson(pmSiteConfig, ActionType.Default, pmurl);

            } else if ("delay".equalsIgnoreCase(action)) {

                SiteConfig gdSiteConfig = this.config.findSite(GDSITENUMBER);
                URL url = gdSiteConfig.getEndpoint().getUrl();

                Multimap<String, String> params = ArrayListMultimap.create();

                params.put("url", String.format("%s?delay=%d", url.toString(), delay));

                String path = String.format("/site/%s", siteNumber);
                URL pmurl = this.jsonClient.buildURL(pmSiteConfig.getEndpoint().getUrl(),
                                path, params);

                result = this.jsonClient.getJson(pmSiteConfig, ActionType.Default, pmurl);

            } else if ("status".equalsIgnoreCase(action)) {

                URL pmurl = this.jsonClient.buildURL(pmSiteConfig.getEndpoint().getUrl(),
                                "/status");

                result = this.jsonClient.getJson(pmSiteConfig, ActionType.Default, pmurl);
            }
        }

        return result;
    }
}
