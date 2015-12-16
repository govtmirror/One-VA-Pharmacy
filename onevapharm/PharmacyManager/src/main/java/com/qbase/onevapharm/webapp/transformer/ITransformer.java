package com.qbase.onevapharm.webapp.transformer;

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

import ca.uhn.hl7v2.HL7Exception;

import ca.uhn.hl7v2.protocol.ReceivingApplicationException;


/**
 * Interface description
 *
 *
 * @param <X>
 * @param <Y>
 *
 * @version        v1.0, 2014-02-26
 * @author         Jim Horner
 */
public interface ITransformer<X, Y> {

    /**
     * Method description
     *
     *
     * @param x
     *
     * @return
     *
     * @throws HL7Exception
     */
    public abstract Y transformIncoming(X x) throws HL7Exception;

    /**
     * Method description
     *
     *
     * @param y
     *
     * @return
     *
     * @throws HL7Exception
     */
    public abstract X transformOutgoing(Y y) throws HL7Exception;

    /**
     * Method description
     *
     *
     * @param y
     *
     * @return
     *
     *
     * @throws ReceivingApplicationException
     */
    public abstract String transformToJSON(Y y) throws ReceivingApplicationException;
}
