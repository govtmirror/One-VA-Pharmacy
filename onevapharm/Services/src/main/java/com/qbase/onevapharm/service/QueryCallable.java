package com.qbase.onevapharm.service;

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

import java.io.IOException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import java.util.Collection;

import java.util.concurrent.Callable;

import com.qbase.onevapharm.config.SiteConfig;

import com.qbase.onevapharm.model.MedicationOrder;
import com.qbase.onevapharm.model.QueryResponse;

import com.qbase.onevapharm.model.QueryResponse.ErrorType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-03-16
 * @author         Jim Horner
 */
public class QueryCallable implements Callable<QueryResponse> {

    /** Field description */
    private final String dfn;

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** Field description */
    private final IPharmacyQueryService pharmacyService;

    /** Field description */
    private final SiteConfig siteConfig;

    /**
     * Constructs ...
     *
     *
     * @param pharmacyService
     * @param siteConfig
     * @param dfn
     */
    public QueryCallable(IPharmacyQueryService pharmacyService, SiteConfig siteConfig,
                         String dfn) {

        super();
        this.pharmacyService = pharmacyService;
        this.siteConfig = siteConfig;
        this.dfn = dfn;
    }

    /**
     * Method description
     *
     *
     * @return
     *
     */
    @Override
    public QueryResponse call() {

        QueryResponse result = new QueryResponse(this.siteConfig, this.dfn);

        try {

            Collection<MedicationOrder> orders =
                this.pharmacyService.retrieveActiveMedications(this.siteConfig, this.dfn);

            result.setSuccess(true);
            result.addAllOrders(orders);

        } catch (ConnectException e) {

            result.setErrorType(ErrorType.connectionTimeout);

            logger.warn("Site {} threw an ConnectionException.",
                        this.siteConfig.getSiteNumber(), e);
        
        } catch (SocketTimeoutException e) {

            result.setErrorType(ErrorType.responseTimeout);
            
            logger.warn("Site {} threw an SocketTimeoutException.",
                        this.siteConfig.getSiteNumber(), e);
            
        } catch (IOException e) {

            result.setErrorType(ErrorType.unknown);

            logger.warn("Site {} threw an ***UNKNOWN*** error.",
                        this.siteConfig.getSiteNumber(), e);
        }

        return result;
    }
}
