
package com.qbase.onevapharm.webapp.model;

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

import java.util.Comparator;

import com.qbase.onevapharm.model.MedicationOrder;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-06-06
 * @author         Jim Horner    
 */
public class RxNumberComparator implements Comparator<MedicationOrder> {

    /**
     * Method description
     *
     *
     * @param o1
     * @param o2
     *
     * @return
     */
    @Override
    public int compare(MedicationOrder o1, MedicationOrder o2) {

        int result = o1.getSiteNumber().compareTo(o2.getSiteNumber());

        if (result == 0) {

            result = o1.getRxNumber().compareTo(o2.getRxNumber());
        }

        return result;
    }
}
