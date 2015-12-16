package com.qbase.onevapharm.mdws.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceRef;

import javax.xml.ws.handler.Handler;

import javax.xml.ws.soap.AddressingFeature;

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

import com.qbase.onevapharm.config.ActionType;
import com.qbase.onevapharm.config.EndpointConfig;
import com.qbase.onevapharm.config.TimeoutConfig;

import com.qbase.onevapharm.mdws.inject.MdwsEndpointTag;

import gov.va.medora.mdws.emrsvc.EmrSvc;
import gov.va.medora.mdws.emrsvc.EmrSvcSoap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2013-12-28
 * @author         Jim Horner
 */

//@ApplicationScoped
public class EmrSvcEndpoint {

    /** Field description */
    @Inject
    @MdwsEndpointTag
    private EndpointConfig config;

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** Field description */
    @WebServiceRef
    private EmrSvc service;

    /**
     * Constructs ...
     *
     *
     *
     */
    public EmrSvcEndpoint() {

        super();
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public EmrSvcSoap createPortType() {

        EmrSvcSoap result = this.service.getEmrSvcSoap12(new AddressingFeature(true));

        BindingProvider bprovider = (BindingProvider) result;

        Map<String, Object> context = bprovider.getRequestContext();

        context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    this.config.getUrl().toString());

        context.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, Boolean.TRUE);

        TimeoutConfig timeoutConfig = this.config.getTimeoutConfig(ActionType.Query);

        context.put("com.sun.xml.ws.request.timeout", timeoutConfig.getRequestTimeout());
        context.put("com.sun.xml.ws.connect.timeout", timeoutConfig.getConnectTimeout());

        Binding binding = bprovider.getBinding();
        List<Handler> handlerList = binding.getHandlerChain();

        if (handlerList == null) {
            handlerList = new ArrayList<>();
        }

        if (this.config.isDebugMode()) {
            handlerList.add(new SOAPDebugHandler());
        }

        bprovider.getBinding().setHandlerChain(handlerList);

        return result;
    }
}
