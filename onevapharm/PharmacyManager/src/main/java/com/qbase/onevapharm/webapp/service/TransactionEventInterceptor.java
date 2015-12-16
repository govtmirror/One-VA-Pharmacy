package com.qbase.onevapharm.webapp.service;

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

import java.util.UUID;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import ca.uhn.hl7v2.model.Message;

import com.qbase.onevapharm.webapp.util.Constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-03-13
 * @author         Jim Horner
 */
@TransactionEventTag
@Interceptor
public class TransactionEventInterceptor {

    /** Field description */
    public static final String MDC_NAME = "TransactionId";

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Method description
     *
     *
     * @param ctx
     *
     * @return
     *
     * @throws Exception
     */
    @AroundInvoke
    public Object transMethodEntry(InvocationContext ctx) throws Exception {

        String key = UUID.randomUUID().toString();

        MDC.put(Constant.TransactionId.name(), key);

        Message request = (Message) ctx.getParameters()[0];

        logger.debug("Request Message [\n{}\n]", request.encode().trim());

        Object result = ctx.proceed();

        if (result != null) {

            Message response = (Message) result;

            logger.debug("Response Message [\n{}\n]", response.encode().trim());
        }

        MDC.remove(Constant.TransactionId.name());

        return result;
    }
}
