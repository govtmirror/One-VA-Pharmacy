package com.qbase.onevapharm.webapp.hapi;

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

/**
 * Interface description
 *
 *
 * @version        v1.0, 2014-04-21
 * @author         Jim Horner    
 */
public interface IEnableAwareService {

    /**
     * Method description
     *
     *
     * @return
     */
    public abstract boolean isEnabled();

    /**
     * Method description
     *
     *
     * @param enable
     */
    public abstract void setEnabled(boolean enable);
}
