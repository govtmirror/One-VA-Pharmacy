package com.qbase.onevapharm.webapp.logging;

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

import javax.websocket.Session;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-03-12
 * @author         Jim Horner
 */
public class WebsocketEvent {

    /** Field description */
    private final Session session;

    /** Field description */
    private final EventType type;

    /**
     * Enum description
     *
     */
    public enum EventType { open, close }

    /**
     * Constructs ...
     *
     *
     * @param session
     * @param type
     */
    public WebsocketEvent(Session session, EventType type) {

        super();

        this.session = session;
        this.type = type;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public Session getSession() {
        return session;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public EventType getType() {
        return type;
    }

    ;
}
