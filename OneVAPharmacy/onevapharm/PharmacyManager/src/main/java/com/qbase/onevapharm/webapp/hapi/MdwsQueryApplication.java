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

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import ca.uhn.hl7v2.HL7Exception;

import ca.uhn.hl7v2.model.Message;

import ca.uhn.hl7v2.protocol.ReceivingApplication;
import ca.uhn.hl7v2.protocol.ReceivingApplicationException;

import com.qbase.onevapharm.mdws.service.MdwsPharmacyQueryService;

import com.qbase.onevapharm.model.QueryResponse;
import com.qbase.onevapharm.model.SitePatientID;

import com.qbase.onevapharm.webapp.logging.LoggingEventTag;

import com.qbase.onevapharm.webapp.model.QueryContext;
import com.qbase.onevapharm.webapp.service.TransactionEventTag;

import com.qbase.onevapharm.webapp.transformer.QueryTransformer;

import com.qbase.onevapharm.webapp.util.Constant;
import com.qbase.onevapharm.webapp.util.MessageType;

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
public class MdwsQueryApplication implements IEnableAwareService, ReceivingApplication {

    /** Field description */
    private boolean enabled;

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** Field description */
    @Inject
    private MdwsPharmacyQueryService service;

    /** Field description */
    @Inject
    private QueryTransformer transformer;

    /** Field description */
    @Inject
    private MessageValidator validator;

    /**
     * Constructs ...
     *
     *
     */
    public MdwsQueryApplication() {

        super();
        this.enabled = false;
    }

    /**
     * Method description
     *
     *
     * @param message
     *
     * @return
     */
    @Override
    public boolean canProcess(Message message) {

        return this.enabled && MessageType.QBP_Q13.getValue().equals(message.getName());
    }

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
    public boolean isEnabled() {

        return enabled;
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

        QueryContext context = this.transformer.transformIncoming(message);

        this.validator.validateContext(context);

        logger.debug("Query Context [\n{}\n]", this.transformer.transformToJSON(context));

        try {

            Collection<SitePatientID> sites = Arrays.asList(context.getSitePatientIDs());
            Collection<QueryResponse> queryResponses =
                this.service.retrieveActiveMedications(sites);

            context.addAllQueryResponses(queryResponses);

        } catch (Exception e) {

            throw new ReceivingApplicationException(e);
        }

        return this.transformer.transformOutgoing(context);
    }

    /**
     * Method description
     *
     *
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled) {

        this.enabled = enabled;
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
        this.validator.validateMessageType(message, MessageType.QBP_Q13);

        this.validator.validatePID(message);
    }
}
