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

import javax.enterprise.context.ApplicationScoped;

import ca.uhn.hl7v2.HL7Exception;

import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Structure;

import ca.uhn.hl7v2.model.v251.datatype.CX;

import ca.uhn.hl7v2.model.v251.segment.PID;

import ca.uhn.hl7v2.protocol.ReceivingApplicationException;

import com.qbase.onevapharm.model.FillRequest;

import com.qbase.onevapharm.webapp.model.QueryContext;
import com.qbase.onevapharm.webapp.model.RefillContext;

import com.qbase.onevapharm.webapp.util.MessageType;
import com.qbase.onevapharm.webapp.util.OrderControlType;
import com.qbase.onevapharm.webapp.util.SegmentType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-03-16
 * @author         Jim Horner
 */
@ApplicationScoped
public class MessageValidator {

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Method description
     *
     *
     * @param context
     *
     * @throws ReceivingApplicationException
     */
    public void validateContext(QueryContext context)
            throws ReceivingApplicationException {

        if ((context.getSitePatientIDs() == null)
                || (context.getSitePatientIDs().length == 0)) {

            throw new ReceivingApplicationException(
                "Patient identifiers are non-existent in PID segment field 3.");
        }
    }

    /**
     * Method description
     *
     *
     * @param context
     *
     * @throws ReceivingApplicationException
     */
    public void validateContext(RefillContext context)
            throws ReceivingApplicationException {

        StringBuilder sb = new StringBuilder();

        if (context.getRemoteSiteNumber() == null) {

            throw new ReceivingApplicationException("Site number is missing.");
        }

        if (OrderControlType.Refill.equals(context.getOrderControlCode())
                || OrderControlType.PartialFill.equals(context.getOrderControlCode())) {

            FillRequest request = context.getFillRequest();

            if (request.getRxNumber() == null) {

                sb.append(" ");
                sb.append("Rx Number is missing.");
            }

            if (request.getFillDate() == null) {

                sb.append(" ");
                sb.append("Fill date is missing.");
            }

            if (request.getLocation() == null) {

                sb.append(" ");
                sb.append("Mail/Window location is missing or not valid.");
            }

            if (request.getRequestingSite() == null) {

                sb.append(" ");
                sb.append("Requesting site is missing.");
            }

            if (OrderControlType.PartialFill.equals(context.getOrderControlCode())) {

                if (request.getDaysSupply() < 1) {

                    sb.append(" ");
                    sb.append("Days Supply is missing or less than 1.");
                }

                if (request.getQuantity() < 1) {

                    sb.append(" ");
                    sb.append("Quantity is missing or less than 1.");
                }
            }

        } else {

            throw new ReceivingApplicationException(
                "ORC.1 - Order control code is blank or not recognized.");
        }

        if (sb.length() > 0) {

            throw new ReceivingApplicationException(sb.toString().trim());
        }

    }

    /**
     * Method description
     *
     *
     * @param message
     * @param type
     *
     *
     * @return
     * @throws ReceivingApplicationException
     */
    public String validateMessageType(Message message, MessageType type)
            throws ReceivingApplicationException {

        String result = message.getName();
        String msgName = type.getValue();

        if (msgName.equals(result) == false) {

            String errstr =
                String.format("Incorrect HL7 message type, should be %s :: found %s.",
                              msgName, result);

            throw new ReceivingApplicationException(errstr);
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param message
     *
     *
     * @return
     * @throws ReceivingApplicationException
     */
    public PID validatePID(Group message) throws ReceivingApplicationException {

        PID result = (PID) validateSegment(message, SegmentType.PID);

        String msgName = message.getName();

        if (result != null) {

            CX[] ids = result.getPid3_PatientIdentifierList();

            if ((ids == null) || (ids.length == 0)) {

                String errstr =
                    String.format("%s message must contain a populated PID-3 field.",
                                  msgName);

                throw new ReceivingApplicationException(errstr);

            }

        } else {

            String errstr = String.format("%s message must contain a PID segment.",
                                          msgName);

            throw new ReceivingApplicationException(errstr);
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param message
     * @param type
     *
     *
     * @return
     * @throws ReceivingApplicationException
     */
    public Structure validateSegment(Group message, SegmentType type)
            throws ReceivingApplicationException {

        Structure result = null;

        try {

            result = message.get(type.getValue());

        } catch (HL7Exception e) {

            // will be handled below
            logger.error("Error reading segment {}", type.getValue(), e);
        }

        if (result == null) {

            String errstr = String.format("%s message must contain a %s segment.",
                                          message.getName(), type.getValue());

            throw new ReceivingApplicationException(errstr);
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param message
     * @param version
     *
     *
     * @return
     * @throws ReceivingApplicationException
     */
    public String validateVersion(Message message, String version)
            throws ReceivingApplicationException {

        String result = message.getVersion();

        if (version.equals(result) == false) {

            String errstr =
                String.format("Incorrect HL7 version, should be %s :: found %s.",
                              version, message.getVersion());

            throw new ReceivingApplicationException(errstr);
        }

        return result;
    }
}
