package com.qbsae.onvapharm.mdws.service;

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

import java.util.List;

import javax.ejb.embeddable.EJBContainer;

import javax.inject.Inject;

import com.qbase.onevapharm.config.ManagerConfig;

import com.qbase.onevapharm.mdws.service.MdwsPatientService;

import com.qbase.onevapharm.mdws.ws.EmrSvcEndpoint;

import com.qbase.onevapharm.model.SitePatientID;

import gov.va.medora.mdws.emrsvc.PatientTO;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


/**
 * Class description
 *
 *
 * @version        v1.0, 2013-12-28
 * @author         Jim Horner
 */
public class PatientServiceITCase {

    /** Field description */
    @Inject
    private ManagerConfig config;

    /** Field description */
    @Inject
    private MdwsPatientService service;

    /**
     * Method description
     *
     *
     * @throws Exception
     */
    @Test
    public void retrieveLocalCorrespondingIDsTest() throws Exception {

        EJBContainer.createEJBContainer().getContext().bind("inject", this);

        List<SitePatientID> ids =
            this.service.retrieveRemoteFacilities(this.config.findSite("2201"), "100232");

        assertNotNull(ids);
        assertFalse(ids.isEmpty());
    }

    /**
     * Method description
     *
     *
     * @throws Exception
     */
    @Test
    public void selectPatientTest() throws Exception {

        EJBContainer.createEJBContainer().getContext().bind("inject", this);

        PatientTO patient =
            this.service.retrievePatientInfo(this.config.findSite("2201"), "100232");

        assertNotNull(patient);
    }
}
