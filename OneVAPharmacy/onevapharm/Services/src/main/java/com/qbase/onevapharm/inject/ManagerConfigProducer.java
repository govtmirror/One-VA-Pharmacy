package com.qbase.onevapharm.inject;

import java.io.InputStream;

import javax.enterprise.context.ApplicationScoped;

import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;

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

import com.qbase.onevapharm.config.ActionType;
import com.qbase.onevapharm.config.EndpointConfig;
import com.qbase.onevapharm.config.ManagerConfig;
import com.qbase.onevapharm.config.PathType;
import com.qbase.onevapharm.config.TimeoutConfig;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;

import org.yaml.snakeyaml.constructor.Constructor;


/**
 * Class description
 *
 *
 * @version        v1.0, 2013-12-28
 * @author         Jim Horner
 */
@Alternative
public class ManagerConfigProducer {

    /**
     * Method description
     *
     *
     * @param is
     *
     * @return
     */
    public static ManagerConfig createConfig(InputStream is) {

        Constructor c = new Constructor();
        TypeDescription managerDescription = new TypeDescription(ManagerConfig.class);

        managerDescription.putMapPropertyType("paths", PathType.class, String.class);
        c.addTypeDescription(managerDescription);

        TypeDescription endpointDescription = new TypeDescription(EndpointConfig.class);

        endpointDescription.putMapPropertyType("timeouts", ActionType.class,
                TimeoutConfig.class);

        c.addTypeDescription(endpointDescription);

        Yaml yaml = new Yaml(c);
        ManagerConfig result = null;

        try {

            result = yaml.loadAs(is, ManagerConfig.class);

        } catch (Exception e) {

            throw new IllegalStateException("Unable to read ManagerConfig.yml", e);
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    @Produces
    @ApplicationScoped
    public ManagerConfig createConfig() {

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream("META-INF/ManagerConfig.yml");

        if (is == null) {

            String msg = "Missing META-INF/ManagerConfig.yml";

            throw new IllegalStateException(msg);
        }

        return createConfig(is);
    }
}
