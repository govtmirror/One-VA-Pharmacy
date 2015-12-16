
package com.qbase.onevapharm.service;

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

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.base.Joiner;

import com.qbase.onevapharm.client.CaterpillarClient;

import com.qbase.onevapharm.config.ActionType;
import com.qbase.onevapharm.config.ManagerConfig;
import com.qbase.onevapharm.config.PathType;
import com.qbase.onevapharm.config.SiteConfig;

import com.qbase.onevapharm.model.FillRequest;
import com.qbase.onevapharm.model.FillResponse;
import com.qbase.onevapharm.model.GlobalLocation;

import com.qbase.onevapharm.transformer.JsonNode2FillResponseTransformer;

import com.qbase.onevapharm.util.VistaDateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-08-27
 * @author         Jim Horner
 */
@ApplicationScoped
public class PharmacyFillService {

    /** Field description */
    @Inject
    private JsonNode2FillResponseTransformer fillTransformer;

    /** Field description */
    @Inject
    private CaterpillarClient jsonClient;

    /** Field description */
    private final JsonNodeFactory jsonNodeFactory;

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** Field description */
    @Inject
    private ManagerConfig managerConfig;

    /**
     * Constructs ...
     *
     */
    public PharmacyFillService() {

        super();

        this.jsonNodeFactory = JsonNodeFactory.instance;
    }

    /**
     * Method description
     *
     *
     *
     * @param localSiteConfig
     * @param remoteSiteConfig
     * @param request
     *
     * @return
     *
     * @throws IOException
     */
    public FillResponse executePartialFill(SiteConfig localSiteConfig,
            SiteConfig remoteSiteConfig, FillRequest request)
            throws IOException {

        ObjectNode jsonNode = this.jsonNodeFactory.objectNode();

        jsonNode.put("rxNumber", request.getRxNumber());
        jsonNode.put("fillDate", VistaDateUtil.createVistaDate(request.getFillDate()));
        jsonNode.put("location", request.getLocation().getAbbreviation());

        jsonNode.put("quantity", Integer.toString(request.getQuantity()));
        jsonNode.put("daysSupply", Integer.toString(request.getDaysSupply()));
        jsonNode.put("remarks", request.getRemarks());

        jsonNode.put("pharmacist", request.getPharmacist());
        jsonNode.put("phoneNumber", request.getPhoneNumber());
        jsonNode.put("requestingSite", request.getRequestingSite());

        URL siteUrl = remoteSiteConfig.getEndpoint().getUrl();
        String path = this.managerConfig.getPath(PathType.PartialFill);

        URL url = this.jsonClient.buildURL(siteUrl, path);

        JsonNode json = this.jsonClient.postJson(remoteSiteConfig, ActionType.Fill, url,
                            jsonNode);

        FillResponse result = transformJson(json);

        if (handleLabelData(localSiteConfig, remoteSiteConfig,
                            result.getLabelDataLocation())) {

            result.setLabelDataSuccess(true);
        }

        return result;
    }

    /**
     * Method description
     *
     *
     *
     * @param localSiteConfig
     * @param remoteSiteConfig
     * @param request
     *
     * @return
     *
     * @throws IOException
     */
    public FillResponse executeRefill(SiteConfig localSiteConfig,
                                      SiteConfig remoteSiteConfig, FillRequest request)
            throws IOException {

        ObjectNode jsonNode = this.jsonNodeFactory.objectNode();

        jsonNode.put("rxNumber", request.getRxNumber());
        jsonNode.put("fillDate", VistaDateUtil.createVistaDate(request.getFillDate()));
        jsonNode.put("location", request.getLocation().getAbbreviation());
        jsonNode.put("pharmacist", request.getPharmacist());
        jsonNode.put("phoneNumber", request.getPhoneNumber());
        jsonNode.put("requestingSite", request.getRequestingSite());

        URL siteUrl = remoteSiteConfig.getEndpoint().getUrl();
        String path = this.managerConfig.getPath(PathType.Refill);

        URL url = this.jsonClient.buildURL(siteUrl, path);

        JsonNode json = this.jsonClient.postJson(remoteSiteConfig, ActionType.Fill, url,
                            jsonNode);

        FillResponse result = transformJson(json);

        if (handleLabelData(localSiteConfig, remoteSiteConfig,
                            result.getLabelDataLocation())) {

            result.setLabelDataSuccess(true);
        }

        return result;
    }

    /**
     * Method description
     *
     *
     *
     *
     *
     * @param localSiteConfig
     * @param remoteSiteConfig
     * @param location
     *
     * @return
     */
    private boolean handleLabelData(SiteConfig localSiteConfig,
                                    SiteConfig remoteSiteConfig,
                                    GlobalLocation location) {

        boolean result = false;

        if (location != null) {

            try {

                JsonNode labelData = pullLabelData(remoteSiteConfig, location);

                if (pushLabelData(localSiteConfig, location, labelData) == true) {

                    result = true;
                }

            } catch (IOException e) {

                logger.error("Unable to handle label data.", e);
            }
        }

        return result;
    }

    /**
     * Method description
     *
     *
     *
     *
     * @param siteConfig
     * @param location
     *
     * @return
     *
     * @throws IOException
     */
    private JsonNode pullLabelData(SiteConfig siteConfig, GlobalLocation location)
            throws IOException {

        URL siteUrl = siteConfig.getEndpoint().getUrl();
        String path = String.format("/node/%s/%s", location.getGlobal(),
                                    Joiner.on("/").join(location.getSubscripts()));

        URL url = this.jsonClient.buildURL(siteUrl, path);

        return this.jsonClient.getJson(siteConfig, ActionType.Query, url);
    }

    /**
     * Method description
     *
     *
     *
     *
     * @param siteConfig
     * @param location
     * @param labelData
     *
     * @return
     */
    private boolean pushLabelData(SiteConfig siteConfig, GlobalLocation location,
                                  JsonNode labelData) throws IOException {

        URL siteUrl = siteConfig.getEndpoint().getUrl();
        String path = String.format("/node/%s/%s", location.getGlobal(),
                                    Joiner.on("/").join(location.getSubscripts()));

        URL url = this.jsonClient.buildURL(siteUrl, path);

        this.jsonClient.putJson(siteConfig, ActionType.Query, url, labelData);

        return true;
    }

    /**
     * Method description
     *
     *
     * @param json
     *
     * @return
     *
     * @throws IOException
     */
    private FillResponse transformJson(JsonNode json) throws IOException {

        if (json == null) {

            throw new IOException("No response from remote system.");
        }

        return this.fillTransformer.apply(json);
    }
}
