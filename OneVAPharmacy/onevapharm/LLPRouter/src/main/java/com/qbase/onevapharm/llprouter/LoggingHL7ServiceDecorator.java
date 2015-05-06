
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

import javax.enterprise.inject.Alternative;

import javax.faces.bean.ApplicationScoped;

import ca.uhn.hl7v2.app.HL7Service;
import us.hornerscorners.lollipop.hapi.IHL7ServiceDecorator;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-09-07
 * @author         Jim Horner
 */
@Alternative
@ApplicationScoped
public class LoggingHL7ServiceDecorator implements IHL7ServiceDecorator {

    /**
     * Method description
     *
     *
     * @param service
     */
    @Override
    public void decorate(HL7Service service) {

        service.setExceptionHandler(new HL7ExceptionHandler());
        service.registerConnectionListener(new LLPConnectionListener());
    }
}
