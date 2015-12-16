package com.qbase.onevapharm.webapp.rest;

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

import java.io.IOException;

import java.net.URL;

import javax.inject.Inject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.qbase.onevapharm.config.ManagerConfig;
import com.qbase.onevapharm.config.SiteConfig;

import com.qbase.onevapharm.webapp.hapi.MdwsQueryApplication;
import com.qbase.onevapharm.webapp.hapi.RestQueryApplication;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-03-22
 * @author         Jim Horner
 */
@Path("/rest/support")
public class SupportResource {

    /** Field description */
    @Inject
    private ManagerConfig config;

    /** Field description */
    private final JsonNodeFactory factory;

    /** Field description */
    private final ObjectMapper mapper;

    /** Field description */
    @Inject
    private MdwsQueryApplication mdwsQueryService;

    /** Field description */
    @Inject
    private RestQueryApplication queryService;

    /**
     * Constructs ...
     *
     */
    public SupportResource() {

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
     * @return
     *
     * @throws IOException
     */
    @GET
    @Path("/status")
    @Produces("application/json")
    public String getStatus() throws IOException {

        ObjectNode result = this.factory.objectNode();

        ObjectNode queryNode = this.factory.objectNode();
        
        ObjectNode mdwsNode = this.factory.objectNode();

        mdwsNode.put("enabled", this.mdwsQueryService.isEnabled());
        queryNode.put("mdws", mdwsNode);

        ObjectNode gbNode = this.factory.objectNode();

        gbNode.put("enabled", this.queryService.isEnabled());
        queryNode.put("globals", gbNode);
        
        result.put("query", queryNode);

        ObjectNode urlNode = this.factory.objectNode();

        for (SiteConfig siteConfig : this.config.getSites()) {

            urlNode.put(siteConfig.getSiteNumber(),
                        siteConfig.getEndpoint().getUrl().toString());
        }
        
        result.put("url", urlNode);

        return this.mapper.writeValueAsString(result);
    }

    /**
     * Method description
     *
     *
     * @return
     *
     * @throws IOException
     */
    @GET
    @Path("/toggle")
    @Produces("application/json")
    public String toggle() throws IOException {

        this.queryService.setEnabled(this.queryService.isEnabled() == false);
        this.mdwsQueryService.setEnabled(this.mdwsQueryService.isEnabled() == false);

        return getStatus();
    }

    /**
     * Method description
     *
     *
     * @param siteNumber
     * @param urlstr
     *
     * @return
     *
     * @throws IOException
     */
    @GET
    @Path("/site/{siteNumber}")
    @Produces("application/json")
    public String updateSiteURL(@PathParam("siteNumber") String siteNumber,
                                @QueryParam("url") String urlstr)
            throws IOException {

        URL url = new URL(urlstr);

        SiteConfig siteConfig = this.config.findSite(siteNumber);

        if (siteConfig == null) {

            String msg = String.format("Unable to find config for site '%s'.",
                                       siteNumber);

            throw new IOException(msg);
        }

        siteConfig.getEndpoint().setUrl(url);

        return getStatus();
    }
}
