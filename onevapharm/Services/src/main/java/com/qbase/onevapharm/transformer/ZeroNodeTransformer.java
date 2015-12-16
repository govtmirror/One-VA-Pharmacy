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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import com.google.common.base.Function;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-07-03
 * @author         Jim Horner    
 */
public class ZeroNodeTransformer implements Function<JsonNode, List<String>> {

    /**
     * Method description
     *
     *
     * @param json
     *
     * @return
     */
    @Override
    public List<String> apply(JsonNode json) {

        List<String> result = new ArrayList<>();

        Iterator<JsonNode> chnodes = json.elements();

        while (chnodes.hasNext()) {

            JsonNode znode = chnodes.next().path("0");

            if (znode.isMissingNode() == false) {
                result.add(znode.asText());
            }
        }

        return result;
    }
}
