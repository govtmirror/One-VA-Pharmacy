package com.qbase.onevapharm.transformer;

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

import com.fasterxml.jackson.databind.JsonNode;

import com.google.common.base.Function;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-07-03
 * @author         Jim Horner
 */
public class JsonNode2ValueTransformer implements Function<JsonNode, String> {

    /**
     * Method description
     *
     *
     * @param json
     *
     * @return
     */
    @Override
    public String apply(JsonNode json) {

        String result = null;

        JsonNode valueNode = json.path("_value");

        if (valueNode.isMissingNode() == false) {

            result = valueNode.asText();
        }

        return result;
    }
}
