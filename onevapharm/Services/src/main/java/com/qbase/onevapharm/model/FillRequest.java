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

import java.util.Date;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-03-16
 * @author         Jim Horner
 */
public class FillRequest {

    /** Field description */
    private int daysSupply;

    /** Field description */
    private Date fillDate;

    /** Field description */
    private FillLocationType location;

    /** Field description */
    private String pharmacist;

    /** Field description */
    private String phoneNumber;

    /** Field description */
    private int quantity;

    /** Field description */
    private String remarks;

    /** Field description */
    private String requestingSite;

    /** Field description */
    private String rxNumber;

    /**
     * Constructs ...
     *
     */
    public FillRequest() {
        super();
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public int getDaysSupply() {
        return daysSupply;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public Date getFillDate() {
        return fillDate;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public FillLocationType getLocation() {
        return location;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getPharmacist() {
        return pharmacist;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getRequestingSite() {
        return requestingSite;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getRxNumber() {
        return rxNumber;
    }

    /**
     * Method description
     *
     *
     * @param daysSupply
     */
    public void setDaysSupply(int daysSupply) {
        this.daysSupply = daysSupply;
    }

    /**
     * Method description
     *
     *
     * @param fillDate
     */
    public void setFillDate(Date fillDate) {
        this.fillDate = fillDate;
    }

    /**
     * Method description
     *
     *
     * @param location
     */
    public void setLocation(FillLocationType location) {
        this.location = location;
    }

    /**
     * Method description
     *
     *
     * @param pharmacist
     */
    public void setPharmacist(String pharmacist) {
        this.pharmacist = pharmacist;
    }

    /**
     * Method description
     *
     *
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Method description
     *
     *
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Method description
     *
     *
     * @param remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * Method description
     *
     *
     * @param requestingSite
     */
    public void setRequestingSite(String requestingSite) {
        this.requestingSite = requestingSite;
    }

    /**
     * Method description
     *
     *
     * @param rxNumber
     */
    public void setRxNumber(String rxNumber) {
        this.rxNumber = rxNumber;
    }
}
