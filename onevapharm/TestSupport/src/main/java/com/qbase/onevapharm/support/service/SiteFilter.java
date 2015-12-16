package com.qbase.onevapharm.support.service;

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

import java.util.Set;

import com.google.common.base.Predicate;

import com.qbase.onevapharm.config.SiteConfig;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-05-22
 * @author         Jim Horner    
 */
public class SiteFilter implements Predicate<SiteConfig> {

    /** Field description */
    private final Set<String> sites;

    /**
     * Constructs ...
     *
     *
     * @param sites
     */
    public SiteFilter(Set<String> sites) {

        super();
        this.sites = sites;
    }

    /**
     * Method description
     *
     *
     * @param siteConfig
     *
     * @return
     */
    public boolean apply(SiteConfig siteConfig) {

        return this.sites.contains(siteConfig.getSiteNumber());
    }
}
