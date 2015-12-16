package com.qbase.onevapharm.webapp.hapi;

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

import java.util.Map;

import ca.uhn.hl7v2.HL7Exception;

import ca.uhn.hl7v2.protocol.ReceivingApplicationExceptionHandler;

import com.qbase.onevapharm.webapp.util.Constant;

import org.apache.log4j.MDC;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-02-25
 * @author         Jim Horner
 */
public class HL7ExceptionHandler implements ReceivingApplicationExceptionHandler {

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Constructs ...
     *
     *
     */
    public HL7ExceptionHandler() {

        super();
    }

    /**
     * Method description
     *
     *
     * @param inMessage
     * @param metadata
     * @param outMessage
     * @param exception
     *
     * @return
     *
     * @throws HL7Exception
     */
    @Override
    public String processException(String inMessage, Map<String, Object> metadata,
                                   String outMessage, Exception exception)
            throws HL7Exception {

        if (outMessage != null) {

            logger.debug("Response Message [\n{}\n]", outMessage.trim());
        }

        logger.error("Exception occurred {} : {}",
                     MDC.get(Constant.TransactionId.toString()), exception.getMessage());

        return outMessage;
    }
}
