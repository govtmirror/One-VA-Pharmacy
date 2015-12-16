package com.qbase.onevapharm.support.servlet;

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

import java.io.IOException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import com.qbase.onevapharm.support.model.Patient;

import com.qbase.onevapharm.support.service.PatientSyncService;

import com.qbase.onevapharm.support.transformer.Request2PatientTransformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-05-19
 * @author         Jim Horner
 */
@WebServlet(urlPatterns = { "/createPatient" })
public class CreatePatientServlet extends HttpServlet {

    /** Field description */
    private final JsonNodeFactory factory;

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** Field description */
    private final ObjectMapper mapper;

    /** Field description */
    @Inject
    private PatientSyncService patientService;

    /** Field description */
    @Inject
    private Request2PatientTransformer patientTransformer;

    /**
     * Constructs ...
     *
     */
    public CreatePatientServlet() {

        super();
        this.factory = JsonNodeFactory.instance;
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    /**
     * Method description
     *
     *
     * @param req
     * @param resp
     *
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String result = "";

        String sitescsv = Strings.nullToEmpty(req.getParameter("sites")).trim();

        if (sitescsv.length() == 0) {

            throw new ServletException(
                "The parameter 'sites' is required and is missing.");
        }

        Set<String> sites = new HashSet<>();

        sites.addAll(Splitter.on(",").trimResults().splitToList(sitescsv));

        try {

            Patient patient = this.patientTransformer.apply(req);

            Map<String, String> map = this.patientService.syncPatient(patient, sites);

            result = this.mapper.writeValueAsString(map);

        } catch (Exception e) {

            logger.error("Exception", e);

            ObjectNode jsonNode = this.factory.objectNode();

            jsonNode.put("error", e.getMessage());

            result = jsonNode.asText();
        }

        resp.getOutputStream().write(result.getBytes());
        resp.flushBuffer();
    }
}
