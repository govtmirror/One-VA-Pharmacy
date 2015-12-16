package com.qbase.onevapharm.config;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import javax.enterprise.inject.Alternative;


/**
 * Class description
 *
 *
 * @version        v1.0, 2013-12-28
 * @author         Jim Horner
 */
@Alternative
@ApplicationScoped
public class ManagerConfig {

    /** Field description */
    private Map<PathType, String> paths;

    /** Field description */
    private List<SiteConfig> sites;

    /**
     * Constructs ...
     *
     */
    public ManagerConfig() {

        super();
        this.sites = new ArrayList<>();
        this.paths = new HashMap<>();
    }

    /**
     * Method description
     *
     *
     * @param siteNumber
     *
     * @return
     */
    public SiteConfig findSite(String siteNumber) {

        SiteConfig result = null;

        for (SiteConfig site : this.sites) {

            if (site.getSiteNumber().equals(siteNumber)) {

                result = site;

                break;
            }
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param type
     *
     * @return
     */
    public String getPath(PathType type) {

        return this.paths.get(type);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public Map<PathType, String> getPaths() {
        return paths;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public List<SiteConfig> getSites() {
        return sites;
    }

    /**
     * Method description
     *
     *
     * @param paths
     */
    public void setPaths(Map<PathType, String> paths) {
        this.paths = paths;
    }

    /**
     * Method description
     *
     *
     * @param sites
     */
    public void setSites(List<SiteConfig> sites) {
        this.sites = sites;
    }
}
