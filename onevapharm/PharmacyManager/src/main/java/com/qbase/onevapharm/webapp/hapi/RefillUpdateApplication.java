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

import java.io.IOException;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import ca.uhn.hl7v2.HL7Exception;

import ca.uhn.hl7v2.model.Message;

import ca.uhn.hl7v2.model.v251.group.RDS_O13_ORDER;
import ca.uhn.hl7v2.model.v251.group.RDS_O13_ORDER_DETAIL;

import ca.uhn.hl7v2.model.v251.message.RDS_O13;

import ca.uhn.hl7v2.model.v251.segment.ORC;
import ca.uhn.hl7v2.model.v251.segment.PID;
import ca.uhn.hl7v2.model.v251.segment.RXO;

import ca.uhn.hl7v2.protocol.ReceivingApplication;
import ca.uhn.hl7v2.protocol.ReceivingApplicationException;

import com.qbase.onevapharm.config.ManagerConfig;
import com.qbase.onevapharm.config.SiteConfig;

import com.qbase.onevapharm.model.FillResponse;

import com.qbase.onevapharm.service.PharmacyFillService;

import com.qbase.onevapharm.webapp.logging.LoggingEventTag;

import com.qbase.onevapharm.webapp.model.RefillContext;
import com.qbase.onevapharm.webapp.service.TransactionEventTag;

import com.qbase.onevapharm.webapp.transformer.RefillTransformer;

import com.qbase.onevapharm.webapp.util.Constant;
import com.qbase.onevapharm.webapp.util.HapiUtil;
import com.qbase.onevapharm.webapp.util.MessageType;
import com.qbase.onevapharm.webapp.util.OrderControlType;
import com.qbase.onevapharm.webapp.util.SegmentType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-02-25
 * @author         Jim Horner
 */
@ApplicationScoped
public class RefillUpdateApplication implements ReceivingApplication {

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** Field description */
    @Inject
    private ManagerConfig managerConfig;

    /** Field description */
    @Inject
    private PharmacyFillService service;

    /** Field description */
    @Inject
    private RefillTransformer transformer;

    /** Field description */
    @Inject
    private MessageValidator validator;

    /**
     * Constructs ...
     *
     *
     *
     */
    public RefillUpdateApplication() {

        super();
    }

    /**
     * Method description
     *
     *
     *
     * @param message
     *
     * @return
     */
    @Override
    public boolean canProcess(Message message) {

        return MessageType.RDS_O13.getValue().equals(message.getName());
    }

    /**
     * Method description
     *
     *
     *
     * @param message
     * @param metadata
     *
     * @return
     *
     * @throws HL7Exception
     * @throws ReceivingApplicationException
     */
    @Override
    @LoggingEventTag
    @TransactionEventTag
    public Message processMessage(Message message, Map<String, Object> metadata)
            throws ReceivingApplicationException, HL7Exception {

        validateMessage(message);

        RefillContext context = this.transformer.transformIncoming(message);

        logger.debug("Message Structure [\n{}\n]", HapiUtil.printStructure(message));
        logger.debug("Fill Context [\n{}\n]", this.transformer.transformToJSON(context));

        this.validator.validateContext(context);

        String remoteSiteNumber = context.getRemoteSiteNumber();
        SiteConfig remoteSiteConfig = this.managerConfig.findSite(remoteSiteNumber);

        if (remoteSiteConfig == null) {

            String errstr = String.format("Remote site number %s is unknown.",
                                          remoteSiteNumber);

            throw new ReceivingApplicationException(errstr);
        }

        String localSiteNumber = context.getLocalSiteNumber();
        SiteConfig localSiteConfig = this.managerConfig.findSite(localSiteNumber);

        if (localSiteConfig == null) {

            String errstr = String.format("Local site number %s is unknown.",
                                          localSiteNumber);

            throw new ReceivingApplicationException(errstr);
        }

        try {

            FillResponse response = null;

            switch (context.getOrderControlCode()) {

                case Refill :
                    response = this.service.executeRefill(localSiteConfig,
                            remoteSiteConfig, context.getFillRequest());

                    break;

                case PartialFill :
                    response = this.service.executePartialFill(localSiteConfig,
                            remoteSiteConfig, context.getFillRequest());

                    break;
            }

            context.setFillResponse(response);

        } catch (IOException e) {

            throw new ReceivingApplicationException(e);
        }

        return this.transformer.transformOutgoing(context);
    }

    /**
     * Method description
     *
     *
     * @param message
     *
     * @throws ReceivingApplicationException
     */
    private void validateMessage(Message message) throws ReceivingApplicationException {

        this.validator.validateVersion(message, Constant.MessageVersion.getValue());
        this.validator.validateMessageType(message, MessageType.RDS_O13);

        RDS_O13 rds = (RDS_O13) message;

        PID pid = (PID) this.validator.validatePID(rds.getPATIENT());

        RDS_O13_ORDER order = rds.getORDER();
        ORC orc = (ORC) this.validator.validateSegment(order, SegmentType.ORC);

        RDS_O13_ORDER_DETAIL detail = order.getORDER_DETAIL();
        RXO rxo = (RXO) this.validator.validateSegment(detail, SegmentType.RXO);

        // ORC.1 is either RF or PF
        String orc1 = orc.getOrc1_OrderControl().getValue();

        OrderControlType ctype = OrderControlType.toType(orc1);

        if ((OrderControlType.Refill.equals(ctype) == false)
                && (OrderControlType.PartialFill.equals(ctype) == false)) {

            throw new ReceivingApplicationException("ORC.1 must equal RF or PF.");
        }
    }
}
