
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

import ca.uhn.hl7v2.HL7Exception;

import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Type;

import ca.uhn.hl7v2.model.v251.datatype.NM;
import ca.uhn.hl7v2.model.v251.datatype.ST;

import ca.uhn.hl7v2.parser.ModelClassFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-06-06
 * @author         Jim Horner
 */
public class ZAK extends AbstractSegment {

    /**
     * Constructs ...
     *
     *
     * @param parent
     * @param factory
     */
    public ZAK(Group parent, ModelClassFactory factory) {

        super(parent, factory);
        init(factory);
    }

    /**
     * Method description
     *
     *
     * @param field
     *
     * @return
     */
    @Override
    protected Type createNewTypeWithoutReflection(int field) {
        return null;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public ST getComment() {

        return getTypedField(4, 0);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public NM getCount() {

        return getTypedField(2, 0);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public ST getSiteNumber() {

        return getTypedField(1, 0);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public NM getSuccess() {

        return getTypedField(3, 0);
    }

    /**
     * Method description
     *
     *
     * @param factory
     */
    private void init(ModelClassFactory factory) {

        try {

            add(ST.class, true, 1, 36, new Object[] { getMessage() }, "Site Number");
            add(NM.class, true, 1, 4, new Object[] { getMessage() }, "Count");
            add(NM.class, true, 1, 1, new Object[] { getMessage() }, "Success/Failure");
            add(ST.class, false, 1, 120, new Object[] { getMessage() }, "Comment");

        } catch (HL7Exception e) {

            throw new IllegalStateException(e);
        }
    }
}
