package com.qbase.onevapharm.service;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import com.qbase.onevapharm.client.CaterpillarClient;

import com.qbase.onevapharm.config.ActionType;
import com.qbase.onevapharm.config.PathType;
import com.qbase.onevapharm.config.SiteConfig;

import com.qbase.onevapharm.model.FillRequest;
import com.qbase.onevapharm.model.FillResponse;
import com.qbase.onevapharm.model.MedicationOrder;
import com.qbase.onevapharm.model.StatusType;

import com.qbase.onevapharm.transformer.JsonNode2FillResponseTransformer;
import com.qbase.onevapharm.transformer.JsonNode2ValueTransformer;
import com.qbase.onevapharm.transformer.PsrxTransformer;
import com.qbase.onevapharm.transformer.ZeroNodeTransformer;

import com.qbase.onevapharm.util.VistaDateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slf4j.profiler.Profiler;


/**
 * Class description
 *
 *
 * @version        v1.0, 2013-12-27
 * @author         Jim Horner
 */
@ApplicationScoped
public class PharmacyQueryService extends AbstractPharmacyQueryService {

    /** Field description */
    private static final Collection<StatusType> ACTIVE_STATUSES =
        Lists.newArrayList(StatusType.Active, StatusType.Suspended, StatusType.Hold,
                           StatusType.ProviderHold);

    /** Field description */
    @Inject
    private CaterpillarClient jsonClient;


    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** Field description */
    @Inject
    private PsrxTransformer psrxTransformer;

    /** Field description */
    @Inject
    private JsonNode2ValueTransformer valueNodeTransformer;

    /** Field description */
    @Inject
    private ZeroNodeTransformer zeroNodeTransformer;

    /**
     * Constructs ...
     *
     *
     *
     */
    public PharmacyQueryService() {

        super();
    }

    /**
     * Method description
     *
     *
     * @param val
     *
     * @return
     */
    private String asIntOrBlank(Integer val) {

        String result = "";

        if (val != null) {

            result = val.toString();
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param siteConfig
     * @param dfn
     *
     * @return
     *
     * @throws IOException
     */
    private Collection<String> callMedHistory(SiteConfig siteConfig, String dfn)
            throws IOException {

        URL siteUrl = siteConfig.getEndpoint().getUrl();
        String path = getManagerConfig().getPath(PathType.MedHistory);

        URL url = this.jsonClient.buildURL(siteUrl, path, Collections.singletonList(dfn));

        JsonNode json = this.jsonClient.getJson(siteConfig, ActionType.Query, url);

        // grab the "P" node
        return this.zeroNodeTransformer.apply(json.path("P"));
    }

    /**
     * Method description
     *
     *
     * @param siteConfig
     * @param iens
     *
     * @return
     *
     * @throws IOException
     */
    private List<MedicationOrder> callMedRetrieval(SiteConfig siteConfig,
            Collection<String> iens)
            throws IOException {

        Multimap<String, String> nodes = ArrayListMultimap.create();

        for (String ien : iens) {

            nodes.put("node", String.format("PSRX/%s", ien));
        }

        URL siteUrl = siteConfig.getEndpoint().getUrl();
        String path = getManagerConfig().getPath(PathType.NodeList);

        URL url = this.jsonClient.buildURL(siteUrl, path, nodes);

        Map<String, JsonNode> jsonNodes = this.jsonClient.getAtomJson(siteConfig,
                                              ActionType.Query, url);

        List<MedicationOrder> result = new ArrayList<>();

        for (String ien : iens) {

            JsonNode medNode = jsonNodes.get(String.format("PSRX/%s", ien));

            if ((medNode.isMissingNode() == false)
                    && (medNode.get("0").isMissingNode() == false)) {

                MedicationOrder order = this.psrxTransformer.apply(medNode);

                result.add(order);
            }
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param siteConfig
     * @param refs
     *
     * @return
     *
     * @throws IOException
     */
    private Map<String, String> callRefRetrieval(SiteConfig siteConfig, Set<String> refs)
            throws IOException {

        Multimap<String, String> nodes = ArrayListMultimap.create();

        for (String ref : refs) {

            nodes.put("node", ref);
        }

        URL siteUrl = siteConfig.getEndpoint().getUrl();
        String path = getManagerConfig().getPath(PathType.NodeList);

        URL url = this.jsonClient.buildURL(siteUrl, path, nodes);

        Map<String, JsonNode> jsonNodeMap = this.jsonClient.getAtomJson(siteConfig,
                                                ActionType.Query, url);

        return Maps.transformValues(jsonNodeMap, this.valueNodeTransformer);
    }

    /**
     * Method description
     *
     *
     * @param id
     *
     * @return
     */
    private String createDrugNamePath(String id) {

        return String.format("PSDRUG/%s/0?piece=1", id);
    }

    /**
     * Method description
     *
     *
     * @param id
     *
     * @return
     */
    private String createStopDatePath(String id) {

        return String.format("OR/100/%s/0?piece=9", id);
    }


    /**
     * Method description
     *
     *
     * @param siteConfig
     * @param dfn
     *
     * @return
     *
     * @throws IOException
     */
    @Override
    public Collection<MedicationOrder> retrieveActiveMedications(SiteConfig siteConfig,
            String dfn)
            throws IOException {

        List<MedicationOrder> result = new ArrayList<>();

        Profiler profiler = new Profiler(getClass().getSimpleName());

        profiler.setLogger(logger);

        try {

            // grab node/PS/55/DFN
            profiler.start("OrderIENs->node/PS/55/{DFN}");

            Collection<String> iens = callMedHistory(siteConfig, dfn);

            if (iens.isEmpty() == false) {

                // grab node/PSRX/52 - "P" nodes from above
                profiler.start("Details->node/PSRX/52/{IEN}{IEN}..{IEN}");

                Collection<MedicationOrder> repmeds = new ArrayList<>();

                repmeds.addAll(callMedRetrieval(siteConfig, iens));

                Set<String> refs = new HashSet<>();

                for (MedicationOrder repmed : repmeds) {

                    // only want active medications
                    if (ACTIVE_STATUSES.contains(repmed.getStatus())) {

                        repmed.setSiteNumber(siteConfig.getSiteNumber());

                        // need drug name
                        String drugien = repmed.getDrugId();

                        if (drugien != null) {

                            refs.add(createDrugNamePath(drugien));
                        }

                        // need stop date
                        String orifn = repmed.getOrderNumber();

                        if (orifn != null) {

                            refs.add(createStopDatePath(orifn));
                        }

                        result.add(repmed);
                    }
                }

                Map<String, String> refmap = new HashMap<>();

                if (result.isEmpty() == false) {

                    profiler.start("Details->node/OR/100/{IEN} :: node/PSDRUG/{IEN}");
                    refmap.putAll(callRefRetrieval(siteConfig, refs));

                    // merge data

                    for (MedicationOrder repmed : result) {

                        String drugkey = createDrugNamePath(repmed.getDrugId());
                        String drugvalue = refmap.get(drugkey);

                        if (drugvalue == null) {

                            logger.warn("Key %s is not found to fill drugName.",
                                        drugvalue);

                            // detail is computed
                            repmed.setDetail(
                                String.format(
                                    "%s Qty: %s for %s days", "**ERROR**",
                                    asIntOrBlank(repmed.getQuantity()),
                                    asIntOrBlank(repmed.getDaysSupply())));

                        } else {

                            repmed.setDrugName(drugvalue);

                            // detail is computed
                            repmed.setDetail(
                                String.format(
                                    "%s Qty: %s for %s days", repmed.getDrugName(),
                                    asIntOrBlank(repmed.getQuantity()),
                                    asIntOrBlank(repmed.getDaysSupply())));
                        }

                        String sdtkey = createStopDatePath(repmed.getOrderNumber());
                        String sdtvalue = refmap.get(sdtkey);

                        if (sdtvalue == null) {

                            logger.warn("Key %s is not found to fill stopDate.",
                                        drugvalue);

                        } else {

                            repmed.setStopDate(
                                VistaDateUtil.parseVistaDate(refmap.get(sdtkey)));
                        }
                    }
                }
            }

        } finally {

            // at least log
            profiler.stop().log();
        }

        return result;
    }
}
