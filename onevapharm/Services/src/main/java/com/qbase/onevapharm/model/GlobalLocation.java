
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

import java.util.List;

import com.google.common.collect.Lists;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-08-27
 * @author         Jim Horner
 */
public class GlobalLocation {

    /** Field description */
    private final String global;

    /** Field description */
    private final List<String> subscripts;

    /** Field description */
    private String unparsedString;

    /**
     * Constructs ...
     *
     *
     * @param global
     * @param subscripts
     */
    public GlobalLocation(String global, List<String> subscripts) {

        super();
        this.global = global;
        this.subscripts = Lists.newArrayList(subscripts);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getGlobal() {
        return global;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public List<String> getSubscripts() {
        return subscripts;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getUnparsedString() {
        return unparsedString;
    }

    /**
     * Method description
     *
     *
     * @param unparsedString
     */
    public void setUnparsedString(String unparsedString) {
        this.unparsedString = unparsedString;
    }
}
