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

import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;

import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-06-12
 * @author         Jim Horner
 */
@WebServlet(urlPatterns = { "/delay" })
public class DelayServlet extends HttpServlet {

    /** Field description */
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

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

        String delaystr = Strings.nullToEmpty(req.getParameter("delay")).trim();

        if (delaystr.length() == 0) {

            throw new ServletException(
                "The parameter 'delay' is required and is missing.");
        }
        
        // we have to be lenient due to path parameterization
        delaystr = delaystr.replaceAll("/.*", "");

        try {

            int delay = Integer.parseInt(delaystr);

            resp.addHeader("X-Test-Stale-Seconds", delaystr);

            Thread.sleep(delay * 1000);
            
            resp.getWriter().append("{}");

        } catch (NumberFormatException e) {

            String msg = String.format("The parameter 'delay'='%s' is not an integer.",
                                       delaystr);

            throw new ServletException(msg);

        } catch (InterruptedException e) {

            // don't care
            logger.warn("Delay was interrupted.", e);
        }

        resp.flushBuffer();
    }
}
