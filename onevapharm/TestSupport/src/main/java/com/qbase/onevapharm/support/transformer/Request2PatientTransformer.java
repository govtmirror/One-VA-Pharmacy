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

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Function;

import com.qbase.onevapharm.support.model.Patient;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-05-20
 * @author         Jim Horner
 */
public class Request2PatientTransformer implements Function<HttpServletRequest, Patient> {

    /**
     * Method description
     *
     *
     * @param req
     *
     * @return
     */
    @Override
    public Patient apply(HttpServletRequest req) {

        Patient result = new Patient();
        
        result.setName(req.getParameter("name"));
        result.setGender(req.getParameter("gender"));
        result.setBirthDate(TransUtils.toDate(req.getParameter("birthDate")));
        result.setSsn(req.getParameter("ssn"));
        result.setIcn(req.getParameter("icn"));
        result.setPatientType(req.getParameter("patientType"));
        result.setVeteran(TransUtils.fromYesNo(req.getParameter("veteran")));
        result.setServiceConnected(
            TransUtils.fromYesNo(req.getParameter("serviceConnected")));
        result.setMultipleBirth(TransUtils.fromYesNo(req.getParameter("multipleBirth")));

        return result;
    }
}
