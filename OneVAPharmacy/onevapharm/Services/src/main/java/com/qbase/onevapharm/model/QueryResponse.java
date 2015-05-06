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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.qbase.onevapharm.config.SiteConfig;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-05-16
 * @author         Jim Horner
 */
public class QueryResponse {

    /** Field description */
    private final String dfn;

    /** Field description */
    private ErrorType errorType;

    /** Field description */
    private final List<MedicationOrder> orders;

    /** Field description */
    private final SiteConfig siteConfig;

    /** Field description */
    private boolean success;

    /**
     * Enum description
     *
     */
    public enum ErrorType {
        none, unknown, connectionTimeout, responseTimeout
    }

    /**
     * Constructs ...
     *
     *
     * @param config
     * @param dfn
     */
    public QueryResponse(SiteConfig config, String dfn) {

        this(config, dfn, ErrorType.none);
    }

    /**
     * Constructs ...
     *
     *
     * @param config
     * @param dfn
     * @param errorType
     */
    public QueryResponse(SiteConfig config, String dfn, ErrorType errorType) {

        super();
        this.siteConfig = config;
        this.dfn = dfn;
        this.orders = new ArrayList<>();
        this.errorType = errorType;
    }

    /**
     * Method description
     *
     *
     * @param orders
     */
    public void addAllOrders(Collection<MedicationOrder> orders) {

        this.orders.addAll(orders);
    }

    /**
     * Method description
     *
     *
     * @param order
     */
    public void addOrder(MedicationOrder order) {

        this.orders.add(order);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public int getCount() {

        return this.orders.size();
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
    public ErrorType getErrorType() {
        return errorType;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public Collection<MedicationOrder> getOrders() {

        return Collections.unmodifiableCollection(this.orders);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public SiteConfig getSiteConfig() {
        return siteConfig;
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
     * @param errorType
     */
    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

    ;

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
