package com.qbase.onevapharm.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;


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

/**
 * Class description
 *
 *
 * @version        v1.0, 2014-03-16
 * @author         Jim Horner
 */
public class FillResponse {

    /** Field description */
    private String clerk;

    /** Field description */
    private final List<String> comments;

    /** Field description */
    private int daysSupply;

    /** Field description */
    private Date dispenseDate;

    /** Field description */
    private String divisionId;

    /** Field description */
    private String divisionName;

    /** Field description */
    private String drugName;

    /** Field description */
    private Date fillDate;

    /** Field description */
    private FillErrorType fillError;

    /** Field description */
    private GlobalLocation labelDataLocation;

    /** Field description */
    private boolean labelDataSuccess;

    /** Field description */
    private Date loginDate;

    /** Field description */
    private String nationalDrugCode;

    /** Field description */
    private String pharmacist;

    /** Field description */
    private String phoneNumber;

    /** Field description */
    private String prescriptionId;

    /** Field description */
    private int quantity;

    /** Field description */
    private String refillId;

    /** Field description */
    private String remarks;

    /** Field description */
    private String requestingSite;

    /** Field description */
    private String rxNumber;

    /** Field description */
    private boolean success;

    /**
     * Constructs ...
     *
     */
    public FillResponse() {

        super();

        this.fillError = FillErrorType.None;
        this.success = false;
        this.labelDataSuccess = false;
        this.comments = new ArrayList<>();
    }

    /**
     * Method description
     *
     *
     * @param comment
     */
    public void addComment(String comment) {

        this.comments.add(comment);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getClerk() {
        return clerk;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public Collection<String> getComments() {

        return Collections.unmodifiableCollection(this.comments);
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
    public Date getDispenseDate() {
        return dispenseDate;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getDivisionId() {
        return divisionId;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getDrugName() {
        return drugName;
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
    public FillErrorType getFillError() {
        return fillError;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public GlobalLocation getLabelDataLocation() {
        return labelDataLocation;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public Date getLoginDate() {
        return loginDate;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getNationalDrugCode() {
        return nationalDrugCode;
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
    public String getPrescriptionId() {
        return prescriptionId;
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
    public String getRefillId() {
        return refillId;
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
     * @return
     */
    public boolean isLabelDataSuccess() {
        return labelDataSuccess;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Method description
     *
     *
     * @param clerk
     */
    public void setClerk(String clerk) {
        this.clerk = clerk;
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
     * @param dispenseDate
     */
    public void setDispenseDate(Date dispenseDate) {
        this.dispenseDate = dispenseDate;
    }

    /**
     * Method description
     *
     *
     * @param divisionId
     */
    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * Method description
     *
     *
     * @param divisionName
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /**
     * Method description
     *
     *
     * @param drugName
     */
    public void setDrugName(String drugName) {
        this.drugName = drugName;
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
     * @param fillError
     */
    public void setFillError(FillErrorType fillError) {
        this.fillError = fillError;
    }

    /**
     * Method description
     *
     *
     * @param labelDataLocation
     */
    public void setLabelDataLocation(GlobalLocation labelDataLocation) {
        this.labelDataLocation = labelDataLocation;
    }

    /**
     * Method description
     *
     *
     * @param labelDataSuccess
     */
    public void setLabelDataSuccess(boolean labelDataSuccess) {
        this.labelDataSuccess = labelDataSuccess;
    }

    /**
     * Method description
     *
     *
     * @param loginDate
     */
    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    /**
     * Method description
     *
     *
     * @param nationalDrugCode
     */
    public void setNationalDrugCode(String nationalDrugCode) {
        this.nationalDrugCode = nationalDrugCode;
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
     * @param prescriptionId
     */
    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
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
     * @param refillId
     */
    public void setRefillId(String refillId) {
        this.refillId = refillId;
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

    /**
     * Method description
     *
     *
     * @param success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
