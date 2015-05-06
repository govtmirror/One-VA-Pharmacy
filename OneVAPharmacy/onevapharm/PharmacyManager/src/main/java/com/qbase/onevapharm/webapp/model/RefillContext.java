package com.qbase.onevapharm.webapp.model;

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

import ca.uhn.hl7v2.model.Message;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.qbase.onevapharm.model.FillRequest;
import com.qbase.onevapharm.model.FillResponse;

import com.qbase.onevapharm.webapp.util.OrderControlType;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-02-27
 * @author         Jim Horner
 */
public class RefillContext {

    /** Field description */
    private FillRequest fillRequest;

    /** Field description */
    private FillResponse fillResponse;

    /** Field description */
    private String localSiteNumber;

    /** Field description */
    @JsonIgnore
    private final Message message;

    /** Field description */
    private OrderControlType orderControlCode;

    /** Field description */
    private String remoteSiteNumber;

    /**
     * Constructs ...
     *
     *
     * @param message
     */
    public RefillContext(Message message) {

        super();
        this.message = message;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public FillRequest getFillRequest() {
        return fillRequest;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public FillResponse getFillResponse() {
        return fillResponse;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getLocalSiteNumber() {
        return localSiteNumber;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public Message getMessage() {
        return message;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public OrderControlType getOrderControlCode() {
        return orderControlCode;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getRemoteSiteNumber() {
        return remoteSiteNumber;
    }

    /**
     * Method description
     *
     *
     * @param fillRequest
     */
    public void setFillRequest(FillRequest fillRequest) {
        this.fillRequest = fillRequest;
    }

    /**
     * Method description
     *
     *
     * @param fillResponse
     */
    public void setFillResponse(FillResponse fillResponse) {
        this.fillResponse = fillResponse;
    }

    /**
     * Method description
     *
     *
     * @param localSiteNumber
     */
    public void setLocalSiteNumber(String localSiteNumber) {
        this.localSiteNumber = localSiteNumber;
    }

    /**
     * Method description
     *
     *
     * @param orderControlCode
     */
    public void setOrderControlCode(OrderControlType orderControlCode) {
        this.orderControlCode = orderControlCode;
    }

    /**
     * Method description
     *
     *
     * @param remoteSiteNumber
     */
    public void setRemoteSiteNumber(String remoteSiteNumber) {
        this.remoteSiteNumber = remoteSiteNumber;
    }
}
