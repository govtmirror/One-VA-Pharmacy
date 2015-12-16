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

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import com.google.common.base.Function;

import com.qbase.onevapharm.support.model.Patient;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-05-20
 * @author         Jim Horner
 */
@ApplicationScoped
public class Patient2MapTransformer implements Function<Patient, Map<String, String>> {

    /**
     * Method description
     *
     *
     *
     * @param patient
     *
     * @return
     */
    @Override
    public Map<String, String> apply(Patient patient) {

        Map<String, String> result = new HashMap<>();

        result.put("name", patient.getName());
        result.put("gender", patient.getGender());
        result.put("birthDate", TransUtils.toDateString(patient.getBirthDate()));
        result.put("ssn", patient.getSsn());
        result.put("icn", patient.getIcn());
        result.put("patientType", patient.getPatientType());
        result.put("veteran", TransUtils.toYesNo(patient.isVeteran()));
        result.put("serviceConnected", TransUtils.toYesNo(patient.isServiceConnected()));
        result.put("multipleBirth", TransUtils.toYesNo(patient.isMultipleBirth()));

        return result;
    }
}
