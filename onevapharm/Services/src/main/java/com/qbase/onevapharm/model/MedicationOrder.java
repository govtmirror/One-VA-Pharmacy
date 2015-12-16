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
 * @version        v1.0, 2014-03-11
 * @author         Jim Horner
 */
public class MedicationOrder {

    /** Field description */
    private Integer daysSupply;

    /** Field description */
    private String detail;

    /** Field description */
    private String drugId;

    /** Field description */
    private String drugName;

    /** Field description */
    private Date expirationDate;

    /** Field description */
    private String id;

    /** Field description */
    private Date issueDate;

    /** Field description */
    private Date lastFillDate;

    /** Field description */
    private String orderNumber;

    /** Field description */
    private String patientId;

    /** Field description */
    private Integer quantity;

    /** Field description */
    private Integer refills;

    /** Field description */
    private Integer refillsRemaining;

    /** Field description */
    private String rxNumber;

    /** Field description */
    private String sig;

    /** Field description */
    private String siteNumber;

    /** Field description */
    private StatusType status;

    /** Field description */
    private Date stopDate;

    /**
     * Constructs ...
     *
     */
    public MedicationOrder() {
        super();
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public Integer getDaysSupply() {
        return daysSupply;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getDetail() {
        return detail;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getDrugId() {
        return drugId;
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
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public Date getIssueDate() {
        return issueDate;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public Date getLastFillDate() {
        return lastFillDate;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public Integer getRefills() {
        return refills;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public Integer getRefillsRemaining() {
        return refillsRemaining;
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
    public String getSig() {
        return sig;
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

    /**
     * Method description
     *
     *
     * @return
     */
    public StatusType getStatus() {
        return status;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public Date getStopDate() {
        return stopDate;
    }

    /**
     * Method description
     *
     *
     * @param daysSupply
     */
    public void setDaysSupply(Integer daysSupply) {
        this.daysSupply = daysSupply;
    }

    /**
     * Method description
     *
     *
     * @param detail
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * Method description
     *
     *
     * @param drugId
     */
    public void setDrugId(String drugId) {
        this.drugId = drugId;
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
     * @param expirationDate
     */
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Method description
     *
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Method description
     *
     *
     * @param issueDate
     */
    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    /**
     * Method description
     *
     *
     * @param lastFillDate
     */
    public void setLastFillDate(Date lastFillDate) {
        this.lastFillDate = lastFillDate;
    }

    /**
     * Method description
     *
     *
     * @param orderNumber
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * Method description
     *
     *
     * @param patientId
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * Method description
     *
     *
     * @param quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Method description
     *
     *
     * @param refills
     */
    public void setRefills(Integer refills) {
        this.refills = refills;
    }

    /**
     * Method description
     *
     *
     * @param refillsRemaining
     */
    public void setRefillsRemaining(Integer refillsRemaining) {
        this.refillsRemaining = refillsRemaining;
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
     * @param sig
     */
    public void setSig(String sig) {
        this.sig = sig;
    }

    /**
     * Method description
     *
     *
     * @param siteNumber
     */
    public void setSiteNumber(String siteNumber) {
        this.siteNumber = siteNumber;
    }

    /**
     * Method description
     *
     *
     * @param status
     */
    public void setStatus(StatusType status) {
        this.status = status;
    }

    /**
     * Method description
     *
     *
     * @param stopDate
     */
    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }
}
