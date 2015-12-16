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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import javax.enterprise.concurrent.ManagedThreadFactory;

import javax.inject.Inject;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import com.qbase.onevapharm.config.ManagerConfig;
import com.qbase.onevapharm.config.SiteConfig;

import com.qbase.onevapharm.model.QueryResponse;
import com.qbase.onevapharm.model.SitePatientID;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-03-22
 * @author         Jim Horner
 */
public abstract class AbstractPharmacyQueryService implements IPharmacyQueryService {

    /** Field description */
    private ListeningExecutorService executorPool;

    /** Field description */
    @Inject
    private ManagerConfig managerConfig;

    /** Field description */
    @Resource
    private ManagedThreadFactory threadFactory;

    /**
     * Constructs ...
     *
     */
    public AbstractPharmacyQueryService() {

        super();
    }

    /**
     * Method description
     *
     *
     * @return
     */
    protected ManagerConfig getManagerConfig() {
        return managerConfig;
    }

    /**
     * Method description
     *
     */
    @PostConstruct
    public void postConstruct() {

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 10, 5,
                                            TimeUnit.SECONDS,
                                            new ArrayBlockingQueue<Runnable>(10),
                                            this.threadFactory);

        this.executorPool = MoreExecutors.listeningDecorator(threadPool);
    }

    /**
     * Method description
     *
     *
     * @param sites
     * @return
     *
     * @throws IOException
     */
    @Override
    public Collection<QueryResponse> retrieveActiveMedications(
            Collection<SitePatientID> sites)
            throws IOException {

        List<ListenableFuture<QueryResponse>> futures = new ArrayList<>();

        for (SitePatientID site : sites) {

            SiteConfig siteConfig = this.managerConfig.findSite(site.getSiteNumber());

            if (siteConfig != null) {

                futures.add(this.executorPool.submit(new QueryCallable(this, siteConfig,
                        site.getPatientID())));
            }
        }

        List<QueryResponse> result = new ArrayList<>();

        try {

            result.addAll(Futures.allAsList(futures).get());

        } catch (InterruptedException | ExecutionException e) {

            throw new IOException(e);
        }

        return result;
    }
}
