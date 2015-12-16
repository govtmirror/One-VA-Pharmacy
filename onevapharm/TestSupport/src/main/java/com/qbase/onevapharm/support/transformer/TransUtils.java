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

import org.joda.time.DateTime;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-05-20
 * @author         Jim Horner
 */
public class TransUtils {

    /** Field description */
    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

    /**
     * Method description
     *
     *
     * @param str
     *
     * @return
     */
    public static boolean fromYesNo(String str) {

        return ("Y".equals(str) || "y".equals(str));
    }

    /**
     * Method description
     *
     *
     * @param strdob
     *
     * @return
     */
    public static DateTime toDate(String strdob) {

        DateTime result = null;

        if (strdob != null) {

            result = formatter.parseDateTime(strdob);
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param dateToConvert
     *
     * @return
     */
    public static String toDateString(DateTime dateToConvert) {

        String result = null;

        if (dateToConvert != null) {

            result = formatter.print(dateToConvert);
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param dateToConvert
     *
     * @return
     */
    public static String toVistaDate(DateTime dateToConvert) {

        String result = null;

        if (dateToConvert != null) {

            int year = dateToConvert.getYear() - 1700;

            DateTimeFormatter df = null;

            if ((dateToConvert.getHourOfDay() > 0)
                    && (dateToConvert.getMinuteOfHour() > 0)) {

                // drop time
                df = DateTimeFormat.forPattern("MMdd");

            } else {

                df = DateTimeFormat.forPattern("MMdd.HHmm");
            }

            // 3100525.1755 == yyymmdd.hhmm
            result = String.format("%d%s", year, df.print(dateToConvert));
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param bool
     *
     * @return
     */
    public static String toYesNo(boolean bool) {

        return bool ? "Y" : "N";
    }
}
