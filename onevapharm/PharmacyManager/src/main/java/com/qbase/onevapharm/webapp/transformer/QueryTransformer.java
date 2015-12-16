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

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import ca.uhn.hl7v2.AcknowledgmentCode;
import ca.uhn.hl7v2.HL7Exception;

import ca.uhn.hl7v2.model.GenericPrimitive;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Varies;

import ca.uhn.hl7v2.model.v251.datatype.CE;
import ca.uhn.hl7v2.model.v251.datatype.CX;
import ca.uhn.hl7v2.model.v251.datatype.RCD;

import ca.uhn.hl7v2.model.v251.group.RTB_K13_ROW_DEFINITION;

import ca.uhn.hl7v2.model.v251.message.QBP_Q13;

import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.PID;
import ca.uhn.hl7v2.model.v251.segment.QAK;
import ca.uhn.hl7v2.model.v251.segment.RDF;
import ca.uhn.hl7v2.model.v251.segment.RDT;

import ca.uhn.hl7v2.protocol.ReceivingApplicationException;

import com.google.common.base.Strings;

import com.qbase.onevapharm.model.MedicationOrder;
import com.qbase.onevapharm.model.QueryResponse;
import com.qbase.onevapharm.model.SitePatientID;

import com.qbase.onevapharm.util.ConversionUtils;

import com.qbase.onevapharm.webapp.model.QueryContext;
import com.qbase.onevapharm.webapp.model.RxNumberComparator;

import com.qbase.onevapharm.webapp.model.hl7.ZAK;
import com.qbase.onevapharm.webapp.model.hl7.ZTB_K13;

import com.qbase.onevapharm.webapp.util.SegmentType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-02-26
 * @author         Jim Horner
 */
@ApplicationScoped
public class QueryTransformer implements ITransformer<Message, QueryContext> {

    /** Field description */
    private final String[] columns = new String[] {

        "Site Number", "Rx Number", "Drug Name", "Quantity", "Refills", "Days Supply",
        "Expiration Date", "Issue Date", "Stop Date", "Last Fill Date", "Sig", "Detail",
        "Status"
    };

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** Field description */
    @Inject
    private TransformerMapper mapper;

    /**
     * Method description
     *
     *
     * @param date
     *
     * @return
     */
    private String asString(Date date) {

        String result = null;

        if (date != null) {

            DateFormat df = new SimpleDateFormat("yyyyMMdd.HHmm00");

            result = df.format(date);
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param rdt
     * @param index
     * @param value
     *
     * @throws HL7Exception
     */
    private void putValue(RDT rdt, int index, String value) throws HL7Exception {

        GenericPrimitive msgval = new GenericPrimitive(rdt.getMessage());

        msgval.setValue(value);

        Varies type = (Varies) rdt.getField(index, 0);

        type.setData(msgval);
    }

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
    public QueryContext transformIncoming(Message message) throws HL7Exception {

        QueryContext result = new QueryContext(message);

        MSH msh = (MSH) message.get(SegmentType.MSH.getValue());
        String siteId = msh.getMsh4_SendingFacility().getHd1_NamespaceID().getValue();

        PID pid = (PID) message.get(SegmentType.PID.getValue());

        for (CX cx : pid.getPid3_PatientIdentifierList()) {

            String id = cx.getCx1_IDNumber().getValue();
            String sta = cx.getCx6_AssigningFacility().getHd1_NamespaceID().getValue();

            // ignore sending site number (local prescriptions)
            if (siteId.equals(sta) == false) {

                result.addSitePatientID(new SitePatientID(sta, id));
            }
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param context
     *
     * @return
     *
     * @throws HL7Exception
     */
    @Override
    public Message transformOutgoing(QueryContext context) throws HL7Exception {

        ZTB_K13 result = new ZTB_K13();

        QBP_Q13 request = (QBP_Q13) context.getMessage();

        try {

            request.fillResponseHeader(result, AcknowledgmentCode.AA);

        } catch (IOException e) {

            throw new HL7Exception(e);
        }

        List<MedicationOrder> medications = new ArrayList<>();
        int zakidx = 0;

        for (QueryResponse response : context.getQueryResponses()) {

            ZAK zak = result.getZAK(zakidx);

            zak.getSiteNumber().setValue(response.getSiteConfig().getSiteNumber());
            zak.getCount().setValue(Integer.toString(response.getCount()));

            if (response.isSuccess()) {

                zak.getSuccess().setValue("1");
                zak.getComment().setValue("Success");

            } else {

                switch (response.getErrorType()) {

                    case connectionTimeout :
                        zak.getSuccess().setValue("-1");
                        zak.getComment().setValue("Connection timeout.");

                        break;

                    case responseTimeout :
                        zak.getSuccess().setValue("-2");
                        zak.getComment().setValue("Response timeout.");

                        break;

                    default :
                        zak.getSuccess().setValue("0");
                        zak.getComment().setValue("Unknown error.");

                        break;
                }
            }

            medications.addAll(response.getOrders());
            ++zakidx;
        }

        int count = medications.size();

        logger.debug("Transforming Query Results {}", count);

        QAK qak = result.getQAK();

        String queryTag = request.getQPD().getQpd2_QueryTag().getValue();

        qak.getQak1_QueryTag().setValue(queryTag);
        qak.getQak2_QueryResponseStatus().setValue(((count > 0) ? "OK" : "NF"));

        CE queryName = request.getQPD().getQpd1_MessageQueryName();

        qak.getQak3_MessageQueryName().parse(queryName.encode());
        qak.getQak4_HitCount().setValue(Integer.toString(count));

        RTB_K13_ROW_DEFINITION rowdef = result.getROW_DEFINITION();
        RDF rdf = rowdef.getRDF();

        rdf.getRdf1_NumberOfColumnsPerRow().setValue(
            Integer.toString(this.columns.length));

        int index = 0;

        for (String column : this.columns) {

            RCD rcd = rdf.insertRdf2_ColumnDescription(index);

            rcd.getRcd1_SegmentFieldName().setValue(column);
            ++index;
        }

        index = 0;

        Collections.sort(medications, new RxNumberComparator());

        for (MedicationOrder medication : medications) {

            String rxNum = medication.getRxNumber();

            if (Strings.isNullOrEmpty(rxNum) == false) {

                RDT rdt = rowdef.insertRDT(index);

                putValue(rdt, 1, medication.getSiteNumber());
                putValue(rdt, 2, rxNum);
                putValue(rdt, 3, medication.getDrugName());
                putValue(rdt, 4, ConversionUtils.asString(medication.getQuantity()));
                putValue(rdt, 5,
                         ConversionUtils.asString(medication.getRefillsRemaining()));
                putValue(rdt, 6, ConversionUtils.asString(medication.getDaysSupply()));
                putValue(rdt, 7, asString(medication.getExpirationDate()));
                putValue(rdt, 8, asString(medication.getIssueDate()));
                putValue(rdt, 9, asString(medication.getStopDate()));
                putValue(rdt, 10, asString(medication.getLastFillDate()));
                putValue(rdt, 11, medication.getSig());
                putValue(rdt, 12, medication.getDetail());
                putValue(rdt, 13,
                         ConversionUtils.asString(medication.getStatus().getCode()));

                ++index;
            }
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
    public String transformToJSON(QueryContext context)
            throws ReceivingApplicationException {

        return this.mapper.writeAsString(context);
    }
}
