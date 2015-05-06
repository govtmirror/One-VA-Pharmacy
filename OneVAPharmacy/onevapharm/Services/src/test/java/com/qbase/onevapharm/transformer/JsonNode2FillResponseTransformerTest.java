
package com.qbase.onevapharm.transformer;

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

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.qbase.onevapharm.model.FillResponse;
import com.qbase.onevapharm.model.GlobalLocation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.junit.Assert.assertTrue;
import org.junit.Test;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-08-27
 * @author         Jim Horner    
 */
public class JsonNode2FillResponseTransformerTest {

    /**
     * Method description
     *
     *
     * @throws Exception
     */
    @Test
    public void testApply() throws Exception {

        String node0 =
            "1^501084^404305^1^3140827^VERAPAMIL HCL 120MG SA CAP^30^30^RADIOLOGIST,ONE^3140827^2^VEHU SITE^3140827^00378-6320-01^COPE,TJ^(703)222-2222^2303^XTMP(\"PSORLBL\",21790,500205)";
        String node1 = "Rx # 501084 refilled.";

        ObjectNode json = JsonNodeFactory.instance.objectNode();

        json.put("0", node0);
        json.put("1", node1);

        JsonNode2FillResponseTransformer transformer =
            new JsonNode2FillResponseTransformer();

        FillResponse resp = transformer.apply(json);

        assertTrue(resp.isSuccess());
        assertEquals("501084", resp.getRxNumber());
        assertEquals("404305", resp.getPrescriptionId());
        assertEquals("1", resp.getRefillId());
        assertEquals("VERAPAMIL HCL 120MG SA CAP", resp.getDrugName());
        assertEquals(30, resp.getQuantity());
        assertEquals(30, resp.getDaysSupply());
        assertEquals("RADIOLOGIST,ONE", resp.getClerk());
        assertEquals("2", resp.getDivisionId());
        assertEquals("VEHU SITE", resp.getDivisionName());
        assertEquals("00378-6320-01", resp.getNationalDrugCode());
        assertEquals("COPE,TJ", resp.getPharmacist());
        assertEquals("(703)222-2222", resp.getPhoneNumber());
        assertEquals("2303", resp.getRequestingSite());       
        
        GlobalLocation labelLocation = resp.getLabelDataLocation();
        assertNotNull(labelLocation);
        assertEquals("XTMP", labelLocation.getGlobal());
        assertEquals(3, labelLocation.getSubscripts().size());
        assertEquals("PSORLBL", labelLocation.getSubscripts().get(0));
        assertEquals("21790", labelLocation.getSubscripts().get(1));
        assertEquals("500205", labelLocation.getSubscripts().get(2));
        
        assertTrue(resp.getComments().size() == 1);
    }
}
