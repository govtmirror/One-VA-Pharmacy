
package com.qbase.onevapharm.support.model;

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

import org.joda.time.DateTime;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-05-19
 * @author         Jim Horner
 */
public class Patient {

    /** Field description */
    private DateTime birthDate;

    /** Field description */
    private String dfn;

    /** Field description */
    private String gender;

    /** Field description */
    private String icn;

    /** Field description */
    private boolean multipleBirth;

    /** Field description */
    private String name;

    /** Field description */
    private String patientType;

    /** Field description */
    private boolean serviceConnected;

    /** Field description */
    private String ssn;

    /** Field description */
    private boolean veteran;

    /**
     * Constructs ...
     *
     */
    public Patient() {

        super();
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public DateTime getBirthDate() {
        return birthDate;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getDfn() {
        return dfn;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getGender() {
        return gender;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getIcn() {
        return icn;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getPatientType() {
        return patientType;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getSsn() {
        return ssn;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean isMultipleBirth() {
        return multipleBirth;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean isServiceConnected() {
        return serviceConnected;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean isVeteran() {
        return veteran;
    }

    /**
     * Method description
     *
     *
     * @param birthDate
     */
    public void setBirthDate(DateTime birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Method description
     *
     *
     * @param dfn
     */
    public void setDfn(String dfn) {
        this.dfn = dfn;
    }

    /**
     * Method description
     *
     *
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Method description
     *
     *
     * @param icn
     */
    public void setIcn(String icn) {
        this.icn = icn;
    }

    /**
     * Method description
     *
     *
     * @param multipleBirth
     */
    public void setMultipleBirth(boolean multipleBirth) {
        this.multipleBirth = multipleBirth;
    }

    /**
     * Method description
     *
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method description
     *
     *
     * @param patientType
     */
    public void setPatientType(String patientType) {
        this.patientType = patientType;
    }

    /**
     * Method description
     *
     *
     * @param serviceConnected
     */
    public void setServiceConnected(boolean serviceConnected) {
        this.serviceConnected = serviceConnected;
    }

    /**
     * Method description
     *
     *
     * @param ssn
     */
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    /**
     * Method description
     *
     *
     * @param veteran
     */
    public void setVeteran(boolean veteran) {
        this.veteran = veteran;
    }
}
