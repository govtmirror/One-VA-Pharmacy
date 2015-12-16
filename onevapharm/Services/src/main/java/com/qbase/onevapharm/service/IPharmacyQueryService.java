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

import java.util.Collection;

import com.qbase.onevapharm.config.SiteConfig;

import com.qbase.onevapharm.model.MedicationOrder;
import com.qbase.onevapharm.model.QueryResponse;
import com.qbase.onevapharm.model.SitePatientID;


/**
 * Interface description
 *
 *
 * @version        v1.0, 2014-03-22
 * @author         Jim Horner
 */
public interface IPharmacyQueryService {

    /**
     * Method description
     *
     *
     * @param sites
     *
     * @return
     *
     * @throws IOException
     */
    public abstract Collection<QueryResponse> retrieveActiveMedications(
            Collection<SitePatientID> sites)
            throws IOException;

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
    public abstract Collection<MedicationOrder> retrieveActiveMedications(
            SiteConfig siteConfig, String dfn)
            throws IOException;
}
