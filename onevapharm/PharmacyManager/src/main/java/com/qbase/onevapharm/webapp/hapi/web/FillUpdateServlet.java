
package com.qbase.onevapharm.webapp.hapi.web;

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

import javax.inject.Inject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;

import ca.uhn.hl7v2.hoh.hapi.server.HohServlet;

import com.qbase.onevapharm.webapp.hapi.RefillUpdateApplication;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-09-08
 * @author         Jim Horner    
 */
@WebServlet(urlPatterns = { "/hoh/v251/fill" })
public class FillUpdateServlet extends HohServlet {

    /** Field description */
    @Inject
    private RefillUpdateApplication fillApplication;

    /**
     * Method description
     *
     *
     * @param theConfig
     *
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig theConfig) throws ServletException {

        setApplication(fillApplication);
    }
}
