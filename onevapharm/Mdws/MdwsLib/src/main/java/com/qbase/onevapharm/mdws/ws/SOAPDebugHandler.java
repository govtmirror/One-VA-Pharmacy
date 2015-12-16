package com.qbase.onevapharm.mdws.ws;

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

import java.io.ByteArrayOutputStream;

import java.util.Properties;
import java.util.Set;

import javax.xml.namespace.QName;

import javax.xml.soap.SOAPMessage;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;

import javax.xml.transform.stream.StreamResult;

import javax.xml.ws.handler.MessageContext;

import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2013-12-27
 * @author         Jim Horner    
 */
public class SOAPDebugHandler implements SOAPHandler<SOAPMessageContext> {

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(SOAPDebugHandler.class);

    /**
     * Constructs ...
     *
     *
     *
     */
    public SOAPDebugHandler() {
        super();
    }

    /**
     * Method description
     *
     *
     * @param context
     */
    @Override
    public void close(MessageContext context) {

        // throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
    public Set<QName> getHeaders() {

        return null;
    }

    /**
     * Method description
     *
     *
     * @param context
     *
     * @return
     */
    @Override
    public boolean handleFault(SOAPMessageContext context) {

        // throw new UnsupportedOperationException("Not supported yet.");
        return true;
    }

    /**
     * Method description
     *
     *
     * @param smc
     *
     * @return
     */
    @Override
    public boolean handleMessage(SOAPMessageContext smc) {

        Boolean outboundProperty =
            (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        SOAPMessage message = smc.getMessage();

        try {

            if (Boolean.TRUE.equals(outboundProperty)) {

                logger.debug(
                    "\n\n===============outgoing SOAP message===========================");

            } else {

                // Print out the outbound SOAP message to System.out
                logger.debug(
                    "\n\n===============incoming SOAP message===========================");
            }

            logger.debug(xmlToString(message.getSOAPPart().getContent()));

        } catch (Exception ex) {

            logger.debug("Error in the client handler", ex);
        }

        return true;
    }

    /**
     * Method description
     *
     *
     *
     * @return
     */
    private String xmlToString(Source source) {

        String result = "Unable to pretty print";

        Properties props = new Properties();

        props.setProperty(OutputKeys.INDENT, "yes");
        props.setProperty(OutputKeys.STANDALONE, "yes");
        props.setProperty(OutputKeys.METHOD, "xml");
        props.setProperty("{http://xml.apache.org/xslt}indent-amount", "3");

        try {

            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            transformer.setOutputProperties(props);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            StreamResult stream = new StreamResult(bos);

            transformer.transform(source, stream);
            result = bos.toString();

        } catch (TransformerFactoryConfigurationError | TransformerException e) {

            logger.debug("Exception", e);
        }

        return result;
    }
}
