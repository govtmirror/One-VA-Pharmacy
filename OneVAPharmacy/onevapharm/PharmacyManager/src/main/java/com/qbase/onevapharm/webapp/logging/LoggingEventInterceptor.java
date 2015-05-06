package com.qbase.onevapharm.webapp.logging;

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

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.MDC;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-03-13
 * @author         Jim Horner
 */
@LoggingEventTag
@Interceptor
public class LoggingEventInterceptor {

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
    public Object logMethodEntry(InvocationContext ctx) throws Exception {

        String name = String.format("%s.%s",
                                    ctx.getMethod().getDeclaringClass().getName(),
                                    ctx.getMethod().getName());

        MDC.put(LoggingEventFilter.MDC_NAME, name);

        Object result = ctx.proceed();

        MDC.remove(LoggingEventFilter.MDC_NAME);

        return result;
    }
}
