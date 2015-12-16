package com.qbase.onevapharm.webapp.transformer;

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

import javax.enterprise.context.ApplicationScoped;

import ca.uhn.hl7v2.protocol.ReceivingApplicationException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-03-13
 * @author         Jim Horner
 */
@ApplicationScoped
public class TransformerMapper {

    /** Field description */
    private final ObjectMapper mapper;

    /**
     * Constructs ...
     *
     */
    public TransformerMapper() {

        super();
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    /**
     * Method description
     *
     *
     * @param obj
     *
     * @return
     *
     *
     * @throws ReceivingApplicationException
     */
    public String writeAsString(Object obj) throws ReceivingApplicationException {

        String result = null;

        try {

            result = this.mapper.writeValueAsString(obj);

        } catch (IOException e) {

            throw new ReceivingApplicationException(e);
        }

        return result;
    }
}
