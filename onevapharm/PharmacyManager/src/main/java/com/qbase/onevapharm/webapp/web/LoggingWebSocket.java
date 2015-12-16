package com.qbase.onevapharm.webapp.web;

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

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import javax.websocket.server.ServerEndpoint;

import com.qbase.onevapharm.webapp.logging.WebsocketEvent;

import com.qbase.onevapharm.webapp.util.EventBusHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-03-11
 * @author         Jim Horner
 */
@ServerEndpoint(value = "/logging")
public class LoggingWebSocket {

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Method description
     *
     *
     * @param session
     * @param closeReason
     */
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {

        EventBusHolder.getInstance().post(new WebsocketEvent(session,
                WebsocketEvent.EventType.close));

        logger.info("Session {} closed because of {}", session.getId(), closeReason);
    }

    /**
     * Method description
     *
     *
     * @param message
     * @param session
     *
     * @return
     */
    @OnMessage
    public String onMessage(String message, Session session) {

        return message;
    }

    /**
     * Method description
     *
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {

        EventBusHolder.getInstance().post(new WebsocketEvent(session,
                WebsocketEvent.EventType.open));

        logger.info("Connected {} ... ", session.getId());
    }
}
