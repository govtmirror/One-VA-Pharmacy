package com.qbase.onevapharm.webapp.transformer;

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

import ca.uhn.hl7v2.HL7Exception;

import ca.uhn.hl7v2.model.Message;

import ca.uhn.hl7v2.model.v251.datatype.EI;
import ca.uhn.hl7v2.model.v251.datatype.FN;
import ca.uhn.hl7v2.model.v251.datatype.LA1;
import ca.uhn.hl7v2.model.v251.datatype.NM;
import ca.uhn.hl7v2.model.v251.datatype.XCN;
import ca.uhn.hl7v2.model.v251.datatype.XTN;

import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.NTE;
import ca.uhn.hl7v2.model.v251.segment.ORC;
import ca.uhn.hl7v2.model.v251.segment.RXO;

import com.google.common.primitives.Ints;

import com.qbase.onevapharm.model.FillLocationType;
import com.qbase.onevapharm.model.FillRequest;

import com.qbase.onevapharm.webapp.model.RefillContext;

import com.qbase.onevapharm.webapp.util.OrderControlType;
import com.qbase.onevapharm.webapp.util.SegmentType;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-03-17
 * @author         Jim Horner
 */
public abstract class AbstractOrderTransformer {

    /**
     * Method description
     *
     *
     * @param message
     * @param orc
     * @param rxo
     * @param nte
     *
     * @return
     *
     * @throws HL7Exception
     */
    protected RefillContext transformOrder(Message message, ORC orc, RXO rxo, NTE nte)
            throws HL7Exception {

        RefillContext result = new RefillContext(message);

        FillRequest request = new FillRequest();

        result.setFillRequest(request);

        // msh
        MSH msh = (MSH) message.get(SegmentType.MSH.getValue());

        request.setRequestingSite(
            msh.getMsh4_SendingFacility().getHd1_NamespaceID().getValue());
        
        result.setLocalSiteNumber(request.getRequestingSite());

        // orc1 - orderControl
        result.setOrderControlCode(
            OrderControlType.toType(orc.getOrc1_OrderControl().getValue()));

        // orc2 - rxNumber^siteNumber
        EI orc2 = orc.getOrc2_PlacerOrderNumber();
        String rxNumber = orc2.getEi1_EntityIdentifier().getValue();
        String siteNumber = orc2.getEi2_NamespaceID().getValue();

        request.setRxNumber(rxNumber);
        result.setRemoteSiteNumber(siteNumber);

        // orc9 - fill date
        request.setFillDate(
            orc.getOrc9_DateTimeOfTransaction().getTs1_Time().getValueAsDate());

        // orc10 - pharmacist
        XCN orc10 = orc.getEnteredBy(0);

        if ((orc10 == null) || orc10.isEmpty()) {

            throw new HL7Exception("ORC.10 must contain values for a pharmacist.");
        }

        FN familyName = orc10.getFamilyName();

        String pharmacist = String.format("%s,%s",
                                          familyName.getFn1_Surname().getValue(),
                                          orc10.getGivenName().getValue());

        request.setPharmacist(pharmacist);

        // orc13 - siteNumber
//      PL orc13 = orc.getOrc13_EntererSLocation();
//
//      request.setRequestingSite(
//          orc13.getPl4_Facility().getHd1_NamespaceID().getValue());

        // orc14 - callback phone number
        XTN orc14 = orc.getOrc14_CallBackPhoneNumber(0);

        request.setPhoneNumber(orc14.getXtn1_TelephoneNumber().getValue());

        // rxo2 - fill quantity
        NM rxo2_1 = rxo.getRxo2_RequestedGiveAmountMinimum();

        if ((rxo2_1 != null) && (rxo2_1.getValue() != null)) {

            String intval = rxo2_1.getValue();

            Integer qty = Ints.tryParse(intval);

            if (qty != null) {
                request.setQuantity(qty.intValue());
            }
        }

        // rxo8 - mail or window
        LA1 rxo8 = rxo.getRxo8_DeliverToLocation();

        if ((rxo8 != null) && (rxo8.isEmpty() == false)) {

            FillLocationType ltype =
                FillLocationType.toType(rxo8.getLa11_PointOfCare().getValue());

            request.setLocation(ltype);
        }

        // rxo11 - days supply
        NM rxo11_1 = rxo.getRxo11_RequestedDispenseAmount();

        if ((rxo11_1 != null) && (rxo11_1.getValue() != null)) {

            Integer supply = Ints.tryParse(rxo11_1.getValue());

            if (supply != null) {
                request.setDaysSupply(supply.intValue());
            }
        }

        // nte - remarks
        if ((nte != null) && (nte.getCommentReps() > 0)) {

            request.setRemarks(nte.getNte3_Comment(0).getValue());
        }

        return result;
    }
}
