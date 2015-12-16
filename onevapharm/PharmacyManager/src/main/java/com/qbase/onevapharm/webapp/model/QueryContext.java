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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ca.uhn.hl7v2.model.Message;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.qbase.onevapharm.model.QueryResponse;
import com.qbase.onevapharm.model.SitePatientID;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-01-03
 * @author         Jim Horner
 */
public class QueryContext {

    /** Field description */
    @JsonIgnore
    private final Message message;

    /** Field description */
    private final List<QueryResponse> queryResponses;

    /** Field description */
    private final List<SitePatientID> sitePatientIDs;

    /**
     * Constructs ...
     *
     *
     *
     *
     * @param message
     */
    public QueryContext(Message message) {

        super();

        this.message = message;
        this.sitePatientIDs = new ArrayList<>();
        this.queryResponses = new ArrayList<>();
    }

    /**
     * Method description
     *
     *
     *
     * @param responses
     */
    public void addAllQueryResponses(Collection<QueryResponse> responses) {

        this.queryResponses.addAll(responses);
    }

    /**
     * Method description
     *
     *
     * @param identifiers
     */
    public void addAllSitePatientIDs(Collection<SitePatientID> identifiers) {

        this.sitePatientIDs.addAll(identifiers);
    }

    /**
     * Method description
     *
     *
     *
     * @param response
     */
    public void addQueryResponse(QueryResponse response) {

        this.queryResponses.add(response);
    }

    /**
     * Method description
     *
     *
     * @param identifier
     */
    public void addSitePatientID(SitePatientID identifier) {

        this.sitePatientIDs.add(identifier);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public Message getMessage() {
        return this.message;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public Collection<QueryResponse> getQueryResponses() {

        return Collections.unmodifiableCollection(this.queryResponses);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public SitePatientID[] getSitePatientIDs() {

        return this.sitePatientIDs.toArray(new SitePatientID[this.sitePatientIDs.size()]);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean isNak() {
        return false;
    }
}
