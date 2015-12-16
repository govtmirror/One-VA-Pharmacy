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

import java.util.List;

import javax.inject.Inject;

import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.base.Strings;

import com.google.common.collect.Lists;

import com.qbase.onevapharm.config.ManagerConfig;
import com.qbase.onevapharm.config.SiteConfig;

import com.qbase.onevapharm.support.service.GatewayService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-06-10
 * @author         Jim Horner
 */
@WebServlet(urlPatterns = { "/gateway" })
public class GatewayServlet extends HttpServlet {

    /** Field description */
    private final List<String> VALID_ACTIONS = Lists.newArrayList("start", "stop",
                                                   "status", "delay");

    /** Field description */
    @Inject
    private ManagerConfig config;

    /** Field description */
    private final JsonNodeFactory factory;

    /** Field description */
    @Inject
    private GatewayService gatewayService;

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** Field description */
    private final ObjectMapper mapper;

    /**
     * Constructs ...
     *
     */
    public GatewayServlet() {

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

        String action = Strings.nullToEmpty(req.getParameter("action")).trim();

        if (action.length() == 0) {

            throw new ServletException(
                "The parameter 'action' is required and is missing.");
        }

        if (VALID_ACTIONS.contains(action) == false) {

            String msg = String.format("Action '%s' is invalid; must be start or stop.",
                                       action);

            throw new ServletException(msg);
        }

        String site = Strings.nullToEmpty(req.getParameter("site")).trim();

        if (site.length() == 0) {

            throw new ServletException(
                "The parameter 'site' is required and is missing.");
        }

        SiteConfig siteConfig = this.config.findSite(site);

        if (siteConfig == null) {

            String gwSite = String.format("Gateway%s", site);

            siteConfig = this.config.findSite(gwSite);
        }

        if (siteConfig == null) {

            String msg = String.format("Site '%s' is not known and/or configured.", site);

            throw new ServletException(msg);
        }

        Integer delay = null;
        String delaystr = Strings.nullToEmpty(req.getParameter("delay")).trim();

        if ("delay".equals(action)) {

            if (delaystr.length() == 0) {

                throw new ServletException(
                    "The parameter 'delay' is required and is missing.");
            }

            try {

                delay = Integer.valueOf(delaystr);

            } catch (NumberFormatException e) {

                String msg =
                    String.format("The parameter 'delay'='%s' is not an integer.",
                                  delaystr);

                throw new ServletException(msg);
            }
        }

        String result = "";

        try {

            JsonNode jsonNode = this.gatewayService.execute(action, site, delay);

            result = this.mapper.writeValueAsString(jsonNode);

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
