package com.qbase.onevapharm.util;

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

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Iterables;

import com.google.common.primitives.Ints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-07-03
 * @author         Jim Horner
 */
public class ConversionUtils {

    /** Field description */
    private static final Logger logger = LoggerFactory.getLogger(ConversionUtils.class);

    /**
     * Method description
     *
     *
     * @param vals
     * @param idx
     *
     * @return
     */
    public static Date asFMDate(List<String> vals, int idx) {

        Date result = null;
        String dstr = Iterables.get(vals, idx, null);

        if (dstr != null) {

            result = VistaDateUtil.parseVistaDate(dstr);
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param val
     * @param defvalue
     *
     * @return
     */
    public static int asInt(String val, int defvalue) {

        Integer result = asInteger(val);

        if (result == null) {

            result = defvalue;
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param vals
     * @param idx
     * @param defvalue
     *
     * @return
     */
    public static int asInt(Collection<String> vals, int idx, int defvalue) {

        Integer result = asInteger(vals, idx);

        if (result == null) {

            result = defvalue;
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param val
     *
     * @return
     */
    public static Integer asInteger(String val) {

        Integer result = null;

        try {

            result = Ints.stringConverter().convert(val);

        } catch (NumberFormatException e) {

            logger.warn("Value is not an integer {}.", val);
        }

        return result;
    }

    /**
     * Method description
     *
     *
     *
     *
     * @param vals
     * @param idx
     *
     * @return
     */
    public static Integer asInteger(Collection<String> vals, int idx) {

        Integer result = null;

        String val = Iterables.get(vals, idx, null);

        if (val != null) {

            result = asInteger(val);
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param integer
     *
     * @return
     */
    public static String asString(Integer integer) {

        return Ints.stringConverter().reverse().convert(integer);
    }
}
