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

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.websocket.Session;

import ch.qos.logback.classic.spi.ILoggingEvent;

import com.google.common.eventbus.Subscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-03-12
 * @author         Jim Horner
 */
public class LoggingBroadcaster {

    /** Field description */
    public static final String MDC_NAME = "LoggingEventTag";

    /** Field description */
    private static final Set<Session> _SESSIONS = new HashSet<>();

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Method description
     *
     *
     * @param loggingEvent
     */
    @Subscribe
    public void onLoggingEvent(ILoggingEvent loggingEvent) {

        List<Session> sessions = new ArrayList<>();

        synchronized (_SESSIONS) {
            sessions.addAll(_SESSIONS);
        }

        for (Session session : sessions) {

            try {

                String msg = loggingEvent.getFormattedMessage();

                msg = msg.replaceAll("\n", "<br />");
                session.getBasicRemote().sendText(msg);

            } catch (Exception e) {

                logger.debug("Chat Error: Failed to send message to client", e);

                synchronized (_SESSIONS) {
                    _SESSIONS.remove(session);
                }

                try {

                    session.close();

                } catch (IOException e1) {

                    // Ignore
                }
            }
        }
    }

    /**
     * Method description
     *
     *
     * @param sessionEvent
     */
    @Subscribe
    public void onLoggingSessionEvent(WebsocketEvent sessionEvent) {

        if (WebsocketEvent.EventType.open.equals(sessionEvent.getType())) {

            synchronized (_SESSIONS) {
                _SESSIONS.add(sessionEvent.getSession());
            }

        } else {

            synchronized (_SESSIONS) {
                _SESSIONS.remove(sessionEvent.getSession());
            }
        }
    }
}
