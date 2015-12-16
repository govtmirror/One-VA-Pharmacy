package com.qbase.onevapharm.model;

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
public enum StatusType {

    Active(0), NonVerified(1), Refill(2), Hold(3), DrugInteractions(4), Suspended(5),
               Done(10), Expired(11), Discontinued(12), Deleted(13),
               DiscontinuedByProvider(14), DiscontinedEdited(15), ProviderHold(16),
               PrintedFaxed(9);

    /** Field description */
    private final int code;

    /**
     * Constructs ...
     *
     *
     * @param c
     */
    private StatusType(int c) {
        this.code = c;
    }

    /**
     * Method description
     *
     *
     * @param other
     *
     * @return
     */
    public static StatusType toType(int other) {

        StatusType result = null;

        for (StatusType type : StatusType.values()) {

            if (type.code == other) {

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
    public int getCode() {

        return this.code;
    }
}
