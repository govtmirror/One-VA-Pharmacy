
package com.qbase.onevapharm.webapp.hapi;

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

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import ca.uhn.hl7v2.HL7Exception;

import ca.uhn.hl7v2.model.Message;

import ca.uhn.hl7v2.protocol.ReceivingApplication;
import ca.uhn.hl7v2.protocol.ReceivingApplicationException;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-09-08
 * @author         Jim Horner
 */
@ApplicationScoped
public class QuerySwitchApplication implements ReceivingApplication {

    /** Field description */
    @Inject
    private MdwsQueryApplication mdwsQueryService;

    /** Field description */
    @Inject
    private RestQueryApplication queryService;

    /**
     * Method description
     *
     *
     * @param msg
     *
     * @return
     */
    @Override
    public boolean canProcess(Message msg) {

        return this.queryService.canProcess(msg) || this.mdwsQueryService.canProcess(msg);
    }

    /**
     * Method description
     *
     *
     * @param msg
     * @param map
     *
     * @return
     *
     * @throws HL7Exception
     * @throws ReceivingApplicationException
     */
    @Override
    public Message processMessage(Message msg, Map<String, Object> map)
            throws ReceivingApplicationException, HL7Exception {

        Message result = null;

        if (this.queryService.isEnabled()) {

            result = this.queryService.processMessage(msg, map);
            
        } else if (this.mdwsQueryService.isEnabled()) {

            result = this.mdwsQueryService.processMessage(msg, map);
        }

        return result;
    }
}
