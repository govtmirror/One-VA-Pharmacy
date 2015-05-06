package com.qbase.onevapharm.mdws.service;

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
import java.util.List;

import com.qbase.onevapharm.config.SiteConfig;

import com.qbase.onevapharm.mdws.ws.EmrSvcEndpoint;

import com.qbase.onevapharm.model.SitePatientID;

import gov.va.medora.mdws.emrsvc.DataSourceArray;
import gov.va.medora.mdws.emrsvc.EmrSvcSoap;
import gov.va.medora.mdws.emrsvc.PatientTO;
import gov.va.medora.mdws.emrsvc.TaggedText;
import gov.va.medora.mdws.emrsvc.TaggedTextArray;
import javax.enterprise.context.ApplicationScoped;


/**
 * Class description
 *
 *
 * @version        v1.0, 2013-12-27
 * @author         Jim Horner
 */
@ApplicationScoped
public class MdwsPatientService {

    /** Field description */
    private EmrSvcEndpoint emrEndpoint;

    /**
     * Constructs ...
     *
     *
     */
    public MdwsPatientService() {

        super();
    }

    /**
     * Method description
     *
     *
     * @param config
     * @param dfn
     *
     * @return
     */
    public PatientTO retrievePatientInfo(SiteConfig config, String dfn) {

        PatientTO result = null;

        EmrSvcSoap port = this.emrEndpoint.createPortType();

        DataSourceArray connectResult = null;

        try {

            // connect
            connectResult = port.connect(config.getSiteNumber());

            // login
            port.login(config.getUserName(), config.getPassword(), "");

            // select
            result = port.select(dfn);

        } finally {

            if (connectResult != null) {

                // disconnect
                port.disconnect();
            }
        }

        return result;
    }

    /**
     * MDWS is using FACLIST^ORWCIRN
     * Does not appear this method will be useful as IENs are returned
     * instead of site numbers
     *
     * @param config
     * @param dfn
     *
     * @return
     */
    public List<SitePatientID> retrieveRemoteFacilities(SiteConfig config, String dfn) {

        List<SitePatientID> result = new ArrayList<>();

        EmrSvcSoap port = this.emrEndpoint.createPortType();

        DataSourceArray connectResult = null;

        try {

            // connect
            connectResult = port.connect(config.getSiteNumber());

            // login
            port.login(config.getUserName(), config.getPassword(), "");

            // retrieve IDs
            TaggedTextArray array = port.getCorrespondingIds("", dfn, "DFN");

            for (TaggedText item : array.getResults().getTaggedText()) {

                // MDWS v2.8.12
                // BEWARE tag is IEN not site number
                result.add(new SitePatientID(item.getTag(), item.getText()));
            }

        } finally {

            if (connectResult != null) {

                // disconnect
                port.disconnect();
            }
        }

        return result;
    }
}
