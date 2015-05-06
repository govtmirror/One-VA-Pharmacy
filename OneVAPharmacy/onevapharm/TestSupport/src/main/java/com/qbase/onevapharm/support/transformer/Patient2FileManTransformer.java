package com.qbase.onevapharm.support.transformer;

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

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.base.Function;

import com.qbase.onevapharm.support.model.Patient;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-05-22
 * @author         Jim Horner    
 */
public class Patient2FileManTransformer implements Function<Patient, JsonNode> {

    /** Field description */
    private final JsonNodeFactory jsonFactory = JsonNodeFactory.instance;

    /**
     * Method description
     *
     *
     * @param patient
     *
     * @return
     */
    @Override
    public JsonNode apply(Patient patient) {

        ObjectNode result = this.jsonFactory.objectNode();

        result.put(".01", patient.getName());
        result.put(".02", patient.getGender());
        result.put(".03", TransUtils.toVistaDate(patient.getBirthDate()));
        result.put(".09", patient.getSsn());
        result.put("991.01", patient.getIcn());
        result.put("391", patient.getPatientType());
        result.put("1901", TransUtils.toYesNo(patient.isVeteran()));
        result.put(".301", TransUtils.toYesNo(patient.isServiceConnected()));
        result.put("994", TransUtils.toYesNo(patient.isMultipleBirth()));

        return result;
    }
}
