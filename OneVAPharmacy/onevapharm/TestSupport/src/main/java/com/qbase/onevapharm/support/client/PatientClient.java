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

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.qbase.onevapharm.client.CaterpillarClient;

import com.qbase.onevapharm.config.ActionType;
import com.qbase.onevapharm.config.EndpointConfig;
import com.qbase.onevapharm.config.SiteConfig;

import com.qbase.onevapharm.support.model.Patient;

import com.qbase.onevapharm.support.transformer.Patient2FileManTransformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-05-20
 * @author         Jim Horner
 */
@ApplicationScoped
public class PatientClient {

    /** Field description */
    @Inject
    private CaterpillarClient client;

    /** Field description */
    private final JsonNodeFactory factory;

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** Field description */
    private final ObjectMapper mapper;

    /** Field description */
    @Inject
    private Patient2FileManTransformer transformer;

    /**
     * Constructs ...
     *
     */
    public PatientClient() {

        super();

        this.factory = JsonNodeFactory.instance;
        this.mapper = new ObjectMapper();
    }

    /**
     * Method description
     *
     *
     *
     *
     * @param siteConfig
     * @param patient
     *
     * @return
     *
     * @throws IOException
     */
    public JsonNode createPatient(SiteConfig siteConfig, Patient patient)
            throws IOException {

        EndpointConfig endpoint = siteConfig.getEndpoint();
        URL url = this.client.buildURL(endpoint.getUrl(),
                                       "/function/ZJTHWrapper/CreatePatient");

        JsonNode result = this.client.postJson(siteConfig, ActionType.Default, url,
                              this.transformer.apply(patient));

        if (logger.isDebugEnabled()) {

            logger.debug("Response -> {}", this.mapper.writeValueAsString(result));
        }

        return result;
    }

    /**
     * Method description
     *
     *
     *
     * @param siteConfig
     * @param dfn
     * @param siteDfns
     *
     * @return
     *
     * @throws IOException
     */
    public JsonNode syncPatient(SiteConfig siteConfig, String dfn,
                                Map<String, String> siteDfns)
            throws IOException {

        ObjectNode bodyNode = this.factory.objectNode();

        bodyNode.put("dfn", dfn);

        ArrayNode tflist = bodyNode.putArray("tflist");

        for (Map.Entry<String, String> siteDfn : siteDfns.entrySet()) {

            ObjectNode tfNode = this.factory.objectNode();

            tfNode.put("site", siteDfn.getKey());
            tfNode.put("dfn", siteDfn.getValue());

            tflist.add(tfNode);
        }

        EndpointConfig endpoint = siteConfig.getEndpoint();
        URL url = this.client.buildURL(endpoint.getUrl(),
                                       "/function/ZJTHWrapper/SyncPatient");

        JsonNode result = this.client.postJson(siteConfig, ActionType.Default, url,
                              bodyNode);

        if (logger.isDebugEnabled()) {

            logger.debug("Response -> {}", this.mapper.writeValueAsString(result));
        }

        return result;
    }
}
