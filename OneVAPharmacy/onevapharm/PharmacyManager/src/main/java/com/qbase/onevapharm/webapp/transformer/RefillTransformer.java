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

import java.io.IOException;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import ca.uhn.hl7v2.AcknowledgmentCode;
import ca.uhn.hl7v2.HL7Exception;

import ca.uhn.hl7v2.model.Message;

import ca.uhn.hl7v2.model.v251.datatype.CE;
import ca.uhn.hl7v2.model.v251.datatype.FT;
import ca.uhn.hl7v2.model.v251.datatype.XCN;

import ca.uhn.hl7v2.model.v251.group.RDS_O13_ORDER;
import ca.uhn.hl7v2.model.v251.group.RDS_O13_ORDER_DETAIL;
import ca.uhn.hl7v2.model.v251.group.RDS_O13_ORDER_DETAIL_SUPPLEMENT;
import ca.uhn.hl7v2.model.v251.group.RRD_O14_DISPENSE;
import ca.uhn.hl7v2.model.v251.group.RRD_O14_ORDER;

import ca.uhn.hl7v2.model.v251.message.RDS_O13;
import ca.uhn.hl7v2.model.v251.message.RRD_O14;

import ca.uhn.hl7v2.model.v251.segment.ERR;
import ca.uhn.hl7v2.model.v251.segment.NTE;
import ca.uhn.hl7v2.model.v251.segment.ORC;
import ca.uhn.hl7v2.model.v251.segment.RXD;
import ca.uhn.hl7v2.model.v251.segment.RXO;

import ca.uhn.hl7v2.protocol.ReceivingApplicationException;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import com.google.common.collect.Iterables;

import com.qbase.onevapharm.model.FillErrorType;
import com.qbase.onevapharm.model.FillResponse;

import com.qbase.onevapharm.webapp.model.RefillContext;

import com.qbase.onevapharm.webapp.util.OrderControlType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-02-27
 * @author         Jim Horner
 */
@ApplicationScoped
public class RefillTransformer extends AbstractOrderTransformer
        implements ITransformer<Message, RefillContext> {

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** Field description */
    @Inject
    private TransformerMapper mapper;

    /**
     * Method description
     *
     *
     *
     * @param message
     *
     * @return
     *
     * @throws HL7Exception
     */
    @Override
    public RefillContext transformIncoming(Message message) throws HL7Exception {

        RDS_O13 rds = (RDS_O13) message;

        RDS_O13_ORDER order = rds.getORDER();
        ORC orc = order.getORC();

        RDS_O13_ORDER_DETAIL detail = order.getORDER_DETAIL();
        RXO rxo = detail.getRXO();

        RDS_O13_ORDER_DETAIL_SUPPLEMENT supplement = detail.getORDER_DETAIL_SUPPLEMENT();
        NTE nte = supplement.getNTE();

        return transformOrder(message, orc, rxo, nte);
    }

    /**
     * Method description
     *
     *
     *
     * @param context
     *
     * @return
     *
     * @throws HL7Exception
     */
    @Override
    public Message transformOutgoing(RefillContext context) throws HL7Exception {

        RRD_O14 result = new RRD_O14();

        RDS_O13 request = (RDS_O13) context.getMessage();

        FillResponse resp = context.getFillResponse();

        if (resp == null) {
            throw new HL7Exception("Fill Response is null.");
        }

        if (FillErrorType.Missing0Node.equals(resp.getFillError())) {

            throw new HL7Exception(
                "Remote system did not return a computable response: [missing '0' node].");
        }

        if (FillErrorType.MissingPieces.equals(resp.getFillError())) {

            throw new HL7Exception("Remote system did not return enough pieces.");
        }

        try {

            boolean success = resp.isSuccess();
            AcknowledgmentCode ackcode = AcknowledgmentCode.AR;

            if (success) {

                ackcode = AcknowledgmentCode.AA;
            }

            request.fillResponseHeader(result, ackcode);

            RDS_O13_ORDER requestOrder = request.getORDER();
            ORC requestOrc = requestOrder.getORC();

            // PID
            result.getRESPONSE().getPATIENT().getPID().parse(
                request.getPATIENT().getPID().encode());

            RRD_O14_ORDER resultOrder = result.getRESPONSE().insertORDER(0);

            // ORC
            resultOrder.getORC().parse(requestOrc.encode());

            if (success) {

                resultOrder.getORC().getOrc1_OrderControl().setValue(
                    OrderControlType.OrderRefilled.getValue());

                RRD_O14_DISPENSE resultDispense = resultOrder.getDISPENSE();
                RXD rxd = resultDispense.getRXD();

                // rxd 1 - refill count
                rxd.getRxd1_DispenseSubIDCounter().setValue("1");

                // rxd 2 - dispense give code
                CE rxd2 = rxd.getRxd2_DispenseGiveCode();

                rxd2.getCe1_Identifier().setValue(resp.getNationalDrugCode());
                rxd2.getCe2_Text().setValue(resp.getDrugName());
                rxd2.getCe3_NameOfCodingSystem().setValue("NDC");

                // rxd 3 - date/time dispensed
                rxd.getRxd3_DateTimeDispensed().getTime().setValue(resp.getFillDate());

                // rxd 4 - actual dispense amount
                rxd.getRxd4_ActualDispenseAmount().setValue(
                    Integer.toString(resp.getQuantity()));

                // rxd 7 - prescription number / PSOIEN^REFIEN
                String presnum =
                    String.format("%s::%s",
                                  Strings.nullToEmpty(resp.getPrescriptionId()),
                                  Strings.nullToEmpty(resp.getRefillId()));

                rxd.getRxd7_PrescriptionNumber().setValue(presnum);

                // rxd 9 - dispensing notes (global location of label data)
                if (resp.getLabelDataLocation() != null) {

                    rxd.getDispenseNotes(0).setValue(
                        resp.getLabelDataLocation().getUnparsedString());
                }

                // rxd 10 - dispensing provider
                List<String> names = Splitter.on(",").splitToList(resp.getPharmacist());
                XCN rxd10 = rxd.getRxd10_DispensingProvider(0);

                rxd10.getXcn2_FamilyName().getFn1_Surname().setValue(Iterables.get(names,
                        0, null));
                rxd10.getXcn3_GivenName().setValue(Iterables.get(names, 1, null));

                CE rxd10_16 = rxd10.getXcn16_NameContext();

                rxd10_16.getCe1_Identifier().setValue(resp.getDivisionId());
                rxd10_16.getCe2_Text().setValue(resp.getDivisionName());

                rxd10.getXcn19_EffectiveDate().getTs1_Time().setValue(
                    resp.getLoginDate());

                // rxd 12 - total daily dose
                rxd.getRxd12_TotalDailyDose().getCq1_Quantity().setValue(
                    Integer.toString(resp.getDaysSupply()));

            } else {

                resultOrder.getORC().getOrc1_OrderControl().setValue(
                    OrderControlType.UnableToRefill.getValue());
            }

            // grab any other nodes after zero and put into ERR

            int idx = 0;

            if (resp.isSuccess()) {

                for (String comment : resp.getComments()) {

                    NTE nte = result.getNTE(idx);

                    nte.getNte1_SetIDNTE().setValue(Integer.toString(idx + 1));

                    FT ft = nte.getNte3_Comment(0);

                    ft.setValue(comment);

                    ++idx;
                }

            } else {

                for (String comment : resp.getComments()) {

                    ERR err = result.getERR(idx);

                    err.getErr2_ErrorLocation(0).getErl1_SegmentID().setValue(
                        context.getRemoteSiteNumber());

                    err.getErr3_HL7ErrorCode().getCwe1_Identifier().setValue("300");
                    err.getErr4_Severity().setValue("E");

                    err.getErr8_UserMessage().setValue(comment);

                    ++idx;
                }
            }

        } catch (IOException e) {

            throw new HL7Exception(e);
        }

        return result;
    }

    /**
     * Method description
     *
     *
     *
     * @param context
     *
     * @return
     *
     *
     * @throws ReceivingApplicationException
     */
    @Override
    public String transformToJSON(RefillContext context)
            throws ReceivingApplicationException {

        return this.mapper.writeAsString(context);
    }
}
