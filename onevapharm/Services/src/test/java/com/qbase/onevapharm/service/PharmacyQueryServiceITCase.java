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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.embeddable.EJBContainer;

import javax.inject.Inject;

import com.qbase.onevapharm.model.QueryResponse;
import com.qbase.onevapharm.model.SitePatientID;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


/**
 * Class description
 *
 *
 * @version        v1.0, 2013-12-31
 * @author         Jim Horner
 */
public class PharmacyQueryServiceITCase {

    /** Field description */
    @Inject
    private PharmacyQueryService pharmacyService;

    /**
     * Method description
     *
     *
     * @throws Exception
     */
    @Test
    public void retrieveActiveMedicationsTest() throws Exception {

        EJBContainer.createEJBContainer().getContext().bind("inject", this);

        List<SitePatientID> sites = new ArrayList<>();

        sites.add(new SitePatientID("2201", "100232"));
        sites.add(new SitePatientID("2202", "100232"));

        Collection<QueryResponse> meds =
            this.pharmacyService.retrieveActiveMedications(sites);

        assertNotNull(meds);
        assertFalse(meds.isEmpty());
    }
}
