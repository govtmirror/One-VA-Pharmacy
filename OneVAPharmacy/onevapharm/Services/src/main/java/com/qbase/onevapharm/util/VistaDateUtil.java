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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import com.google.common.base.Strings;

import com.google.common.primitives.Ints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author jim
 */
public class VistaDateUtil {

    /** Field description */
    private static final Logger logger = LoggerFactory.getLogger(VistaDateUtil.class);

    /**
     * Method description
     *
     *
     * @param dateToConvert
     *
     * @return
     */
    public static String createVistaDate(Date dateToConvert) {

        String result = null;

        if (dateToConvert != null) {

            Calendar cal = Calendar.getInstance();

            cal.setTime(dateToConvert);

            int year = cal.get(Calendar.YEAR) - 1700;

            SimpleDateFormat df = null;

            if ((cal.get(Calendar.HOUR_OF_DAY) == 0) && (cal.get(Calendar.MINUTE) == 0)) {

                // drop time
                df = new SimpleDateFormat("MMdd");

            } else {

                df = new SimpleDateFormat("MMdd.HHmm");
            }

            // 3100525.1755 == yyymmdd.hhmm
            result = String.format("%d%s", year, df.format(dateToConvert));
        }

        return result;
    }

    /**
     * Method description
     *
     *
     *
     * @param datestr
     *
     * @return
     */
    public static Date parseVistaDate(String datestr) {

        Date result = null;

        if (Strings.isNullOrEmpty(datestr) == false) {

            String ystr = datestr.substring(0, 3);

            Integer year = Ints.tryParse(ystr);

            if (year != null) {

                year = year + 1700;

                DateFormat df = null;
                String noyear = datestr.substring(3);
                String tstr = "";

                if (noyear.contains(".")) {

                    df = new SimpleDateFormat("yyyyMMdd.HHmm");
                    tstr = Strings.padEnd(noyear, 9, '0');

                } else {

                    df = new SimpleDateFormat("yyyyMMdd");
                    tstr = noyear;
                }

                String finstr = String.format("%d%s", year, tstr);

                try {

                    result = df.parse(finstr);

                } catch (ParseException e) {

                    logger.warn("Unable to parse date.", e);
                }
            }
        }

        return result;
    }
}
