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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import com.google.common.collect.Collections2;

import com.qbase.onevapharm.config.ManagerConfig;
import com.qbase.onevapharm.config.SiteConfig;

import com.qbase.onevapharm.support.client.PatientClient;

import com.qbase.onevapharm.support.model.Patient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-05-22
 * @author         Jim Horner
 */
@ApplicationScoped
public class PatientSyncService {

    /** Field description */
    private static final String ERROR_FLAG = "_error";

    /** Field description */
    @Inject
    private PatientClient client;

    /** Field description */
    @Inject
    private ManagerConfig config;

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Method description
     *
     *
     * @param patient
     * @param sites
     *
     * @return
     */
    public Map<String, String> syncPatient(Patient patient, Set<String> sites) {

        Map<String, String> result = new HashMap<>();

        Collection<SiteConfig> siteConfigs = Collections2.filter(config.getSites(),
                                                 new SiteFilter(sites));

        // Create Patients
        for (SiteConfig siteConfig : siteConfigs) {

            try {

                String dfn = ERROR_FLAG;

                JsonNode respNode = this.client.createPatient(siteConfig, patient);
                JsonNode errNode = respNode.path(ERROR_FLAG);

                if (errNode.isMissingNode() == false) {

                    dfn = String.format("%s : %s", ERROR_FLAG, errNode.toString());

                } else {

                    JsonNode dfnNode = respNode.path("1");

                    if (dfnNode.isMissingNode() == false) {

                        dfn = dfnNode.asText();
                    }
                }

                result.put(siteConfig.getSiteNumber(), dfn);

            } catch (Exception e) {

                logger.error("Error creating", e);

                result.put(siteConfig.getSiteNumber(), ERROR_FLAG);
            }
        }

        Map<String, String> siteDfns = new HashMap<>();

        // Sync Patients
        for (SiteConfig siteConfig : siteConfigs) {

            String dfn = null;

            siteDfns.clear();

            for (Map.Entry<String, String> entry : result.entrySet()) {

                if (entry.getKey().equals(siteConfig.getSiteNumber())) {

                    dfn = entry.getValue();
                }

                siteDfns.put(entry.getKey(), entry.getValue());
            }

            if ((dfn != null) && (ERROR_FLAG.equals(dfn) == false)
                    && (dfn.startsWith(ERROR_FLAG) == false)) {

                try {

                    this.client.syncPatient(siteConfig, dfn, siteDfns);

                } catch (Exception e) {

                    logger.error("Error syncing", e);

                    result.put(siteConfig.getSiteNumber(),
                               String.format("ERROR - %s", e.getMessage()));
                }
            }
        }

        return result;
    }
}
