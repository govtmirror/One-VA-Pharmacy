package com.qbase.onevapharm.transformer;

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

import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import com.google.common.collect.Iterables;

import com.google.common.primitives.Ints;

import com.qbase.onevapharm.model.MedicationOrder;
import com.qbase.onevapharm.model.StatusType;

import com.qbase.onevapharm.util.ConversionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-03-14
 * @author         Jim Horner
 */
@ApplicationScoped
public class PsrxTransformer implements Function<JsonNode, MedicationOrder> {

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** Field description */
    @Inject
    private ZeroNodeTransformer zeroNodeTransformer;

    /**
     * Method description
     *
     *
     * @param json
     *
     * @return
     */
    @Override
    public MedicationOrder apply(JsonNode json) {

        MedicationOrder result = new MedicationOrder();

        // node0
        String node0 = Strings.nullToEmpty(json.path("0").asText());
        List<String> vals0 = Splitter.on("^").splitToList(node0);

        result.setRxNumber(Iterables.get(vals0, 0, null));
        result.setPatientId(Iterables.get(vals0, 1, null));
        result.setDaysSupply(ConversionUtils.asInt(vals0, 7, 0));

        result.setDrugId(Iterables.get(vals0, 5, null));
        result.setIssueDate(ConversionUtils.asFMDate(vals0, 12));
        result.setQuantity(ConversionUtils.asInt(vals0, 6, 0));

        int refills = ConversionUtils.asInt(vals0, 8, 0);

        result.setRefills(refills);

        // node 1 - refills used
        if (refills > 0) {

            // we are just going to count the refills
            int refillsUsed = 0;

            JsonNode node1 = json.path("1");

            if ((node1.isMissingNode() == false) && node1.isObject()) {

                Iterator<String> fieldNames = node1.fieldNames();

                while (fieldNames.hasNext()) {

                    String fieldName = fieldNames.next();
                    Integer index = Ints.tryParse(fieldName);

                    if ((index != null) && (index > 0)) {

                        ++refillsUsed;
                    }
                }

            }

            result.setRefillsRemaining(refills - refillsUsed);
        }

        // node 2
        String node2 = Strings.nullToEmpty(json.path("2").asText());
        List<String> vals2 = Splitter.on("^").splitToList(node2);

        result.setExpirationDate(ConversionUtils.asFMDate(vals2, 5));

        // node 3
        String node3 = Strings.nullToEmpty(json.path("3").asText());
        List<String> vals3 = Splitter.on("^").splitToList(node3);

        result.setLastFillDate(ConversionUtils.asFMDate(vals3, 0));

        String or1node = Strings.nullToEmpty(json.path("OR1").asText());
        List<String> or1vals = Splitter.on("^").splitToList(or1node);

        result.setOrderNumber(Iterables.get(or1vals, 1, null));

        // sig1
        List<String> znodes = this.zeroNodeTransformer.apply(json.path("SIG1"));

        result.setSig(Joiner.on("").join(znodes));

        // sta
        Integer staint = Ints.tryParse(Strings.nullToEmpty(json.path("STA").asText()));

        if (staint != null) {

            result.setStatus(StatusType.toType((staint)));
        }

        return result;
    }
}
