package com.qbase.onevapharm.model;

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

/**
 * Class description
 *
 *
 * @version        v1.0, 2013-12-29
 * @author         Jim Horner    
 */
public class SitePatientID {

    /** Field description */
    private final String patientID;

    /** Field description */
    private final String siteNumber;

    /**
     * Constructs ...
     *
     *
     * @param siteNumber
     * @param patientID
     */
    public SitePatientID(String siteNumber, String patientID) {

        super();
        this.siteNumber = siteNumber;
        this.patientID = patientID;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getPatientID() {
        return patientID;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getSiteNumber() {
        return siteNumber;
    }
}
