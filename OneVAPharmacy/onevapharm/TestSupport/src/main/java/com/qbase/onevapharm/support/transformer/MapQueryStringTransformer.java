package com.qbase.onevapharm.support.transformer;

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

import com.google.common.base.Strings;

import com.google.common.collect.Maps;

import com.google.common.escape.Escaper;

import com.google.common.net.UrlEscapers;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-05-20
 * @author         Jim Horner    
 */
public class MapQueryStringTransformer
        implements Maps.EntryTransformer<String, String, String> {

    /** Field description */
    private static final Escaper escaper = UrlEscapers.urlPathSegmentEscaper();

    /**
     * Method description
     *
     *
     * @param k
     * @param v
     *
     * @return
     */
    @Override
    public String transformEntry(String k, String v) {

        return String.format("%s=%s", escaper.escape(k),
                             escaper.escape(Strings.nullToEmpty(v)));
    }
}
