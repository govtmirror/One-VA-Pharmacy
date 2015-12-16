package com.qbase.onevapharm.webapp.util;

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

import java.io.IOException;

import ca.uhn.hl7v2.HL7Exception;

import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Message;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-02-25
 * @author         Jim Horner
 */
public class HapiUtil {

    /**
     * Method description
     *
     *
     * @param message
     *
     * @return
     *
     * @throws HL7Exception
     */
    public static Message generateAck(Message message) throws HL7Exception {

        Message result = null;

        try {

            result = message.generateACK();

        } catch (IOException e) {

            throw new HL7Exception(e);
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param message
     *
     * @return
     */
    public static String printStructure(Message message) {

        String result = null;

        try {

            result = message.printStructure();

        } catch (HL7Exception e) {

            // WTF
            result = e.getMessage();
        }

        return result;
    }
}
