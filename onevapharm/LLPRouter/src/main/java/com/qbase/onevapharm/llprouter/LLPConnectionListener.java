
package com.qbase.onevapharm.llprouter;

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

import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.ConnectionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-09-07
 * @author         Jim Horner
 */
public class LLPConnectionListener implements ConnectionListener {

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Constructs ...
     *
     *
     */
    public LLPConnectionListener() {

        super();
    }

    /**
     * Method description
     *
     *
     * @param conn
     */
    @Override
    public void connectionDiscarded(Connection conn) {

        logger.debug("Connection closed.");
    }

    /**
     * Method description
     *
     *
     * @param conn
     */
    @Override
    public void connectionReceived(Connection conn) {

        logger.debug("Connection created.");
    }
}
