
package com.qbase.onevapharm.webapp.model.hl7;

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

import java.util.Arrays;

import ca.uhn.hl7v2.HL7Exception;

import ca.uhn.hl7v2.model.v251.message.RTB_K13;

import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-06-06
 * @author         Jim Horner
 */
public class ZTB_K13 extends RTB_K13 {

    /**
     * Constructs ...
     *
     *
     * @throws HL7Exception
     */
    public ZTB_K13() throws HL7Exception {
        this(new DefaultModelClassFactory());
    }

    /**
     * Constructs ...
     *
     *
     * @param factory
     *
     * @throws HL7Exception
     */
    public ZTB_K13(ModelClassFactory factory) throws HL7Exception {

        super(factory);

        String[] segmentNames = getNames();
        int idx = 1 + Arrays.asList(segmentNames).indexOf("QAK");

        add(ZAK.class, false, true, idx);
    }

    /**
     * Method description
     *
     *
     * @return
     *
     * @throws HL7Exception
     */
    public ZAK getZAK() throws HL7Exception {

        return getTyped("ZAK", ZAK.class);
    }

    /**
     * Method description
     *
     *
     * @param repidx
     *
     * @return
     *
     * @throws HL7Exception
     */
    public ZAK getZAK(int repidx) throws HL7Exception {

        return getTyped("ZAK", repidx, ZAK.class);
    }
}
