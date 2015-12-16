package com.qbase.onevapharm.webapp.util;

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

/**
 * Enum description
 *
 */
public enum MessageType {

    QBP_Q13("QBP_Q13"), RDE_O26("RDE_O25"), RDS_O13("RDS_O13");

    /** Field description */
    private final String value;

    /**
     * Constructs ...
     *
     *
     * @param value
     */
    private MessageType(String value) {
        this.value = value;
    }

    /**
     * Method description
     *
     *
     * @param str
     *
     * @return
     */
    public static MessageType toType(String str) {

        MessageType result = null;

        for (MessageType type : MessageType.values()) {

            if (type.value.equals(str)) {

                result = type;

                break;
            }
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getValue() {

        return this.value;
    }
}
