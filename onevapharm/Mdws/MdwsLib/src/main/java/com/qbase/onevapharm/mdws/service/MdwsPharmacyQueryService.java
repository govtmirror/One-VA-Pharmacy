package com.qbase.onevapharm.mdws.service;

import java.io.IOException;

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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import com.google.common.primitives.Ints;

import com.qbase.onevapharm.config.ManagerConfig;
import com.qbase.onevapharm.config.SiteConfig;

import com.qbase.onevapharm.mdws.ws.EmrSvcEndpoint;

import com.qbase.onevapharm.model.MedicationOrder;
import com.qbase.onevapharm.model.StatusType;

import com.qbase.onevapharm.service.AbstractPharmacyQueryService;

import gov.va.medora.mdws.emrsvc.DataSourceArray;
import gov.va.medora.mdws.emrsvc.EmrSvcSoap;
import gov.va.medora.mdws.emrsvc.FaultTO;
import gov.va.medora.mdws.emrsvc.MedicationTO;
import gov.va.medora.mdws.emrsvc.TaggedMedicationArray;
import gov.va.medora.mdws.emrsvc.TaggedMedicationArrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slf4j.profiler.Profiler;


/**
 * Class description
 *
 *
 * @version        v1.0, 2013-12-27
 * @author         Jim Horner
 */
@ApplicationScoped
public class MdwsPharmacyQueryService extends AbstractPharmacyQueryService {

    /** Field description */
    @Inject
    private ManagerConfig config;

    /** Field description */
    @Inject
    private EmrSvcEndpoint emrEndpoint;

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Constructs ...
     *
     *
     */
    public MdwsPharmacyQueryService() {

        super();
    }

    /**
     * Method description
     *
     *
     * @param date
     *
     * @return
     */
    private Date asDate(String date) {

        Date result = null;

        if (date != null) {

            DateFormat df = new SimpleDateFormat("yyyyMMdd.HHmmss");

            try {

                result = df.parse(date);

            } catch (ParseException e) {

                logger.warn("Invalid date string {}", date);
            }
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param val
     *
     * @return
     */
    private Integer asInteger(String val) {

        Integer result = null;

        try {

            result = Ints.stringConverter().convert(val);

        } catch (NumberFormatException e) {

            logger.warn("Value is not an integer {}.", val);
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param siteConfig
     * @param dfn
     *
     * @return
     *
     * @throws IOException
     */
    @Override
    public Collection<MedicationOrder> retrieveActiveMedications(SiteConfig siteConfig,
            String dfn)
            throws IOException {

        List<MedicationOrder> result = new ArrayList<>();

        Profiler profiler = new Profiler(getClass().getSimpleName());

        profiler.setLogger(logger);
        profiler.start("EmrSvc->getEmrSvcSoap12");

        EmrSvcSoap port = this.emrEndpoint.createPortType();

        DataSourceArray connectResult = null;

        try {

            // connect
            profiler.start("EmrSvcSoap->connect");
            connectResult = port.connect(siteConfig.getSiteNumber());

            // login
            profiler.start("EmrSvcSoap->login");
            port.login(siteConfig.getUserName(), siteConfig.getPassword(), "");

            // select
            profiler.start("EmrSvcSoap->selectPatient");
            port.select(dfn);

            // retrieve meds
            profiler.start("EmrSvcSoap->getOutpatientMeds");

            TaggedMedicationArrays meds = port.getOutpatientMeds();

            // TODO figure out why this is an array of arrays
            // login and select functions prohibit gathering from multiple sites
            // v2.8.12 throws exception on second, third, etc site
            List<TaggedMedicationArray> medArrays =
                meds.getArrays().getTaggedMedicationArray();

            for (TaggedMedicationArray medArray : medArrays) {

                if (medArray.getCount() > 0) {

                    result.addAll(transform(dfn, siteConfig.getSiteNumber(),
                                            medArray.getMeds().getMedicationTO()));

                } else {

                    FaultTO fault = medArray.getFault();

                    if (fault != null) {

                        throw new IOException(fault.getMessage());
                    }
                }
            }

        } finally {

            if (connectResult != null) {

                // disconnect
                profiler.start("EmrSvcSoap->disconnect");
                port.disconnect();
            }

            profiler.stop().log();
        }

        return result;
    }

    /**
     * Method description
     *
     *
     *
     *
     * @param dfn
     * @param siteNumber
     * @param medtolist
     *
     * @return
     */
    private Collection<MedicationOrder> transform(String dfn, String siteNumber,
            Collection<MedicationTO> medtolist) {

        List<MedicationOrder> result = new ArrayList<>();

        for (MedicationTO medto : medtolist) {

            String status = medto.getStatus();

            if ("ACTIVE".equals(status)) {

                MedicationOrder med = new MedicationOrder();

                med.setDaysSupply(asInteger(medto.getDaysSupply()));
                med.setDetail(medto.getDetail());
                med.setDrugId(medto.getDrug().getTag());
                med.setDrugName(medto.getDrug().getText());
                med.setExpirationDate(asDate(medto.getExpirationDate()));
                med.setId(medto.getId());
                med.setIssueDate(asDate(medto.getIssueDate()));
                med.setLastFillDate(asDate(medto.getLastFillDate()));
                med.setOrderNumber(medto.getOrderId());
                med.setPatientId(dfn);
                med.setQuantity(asInteger(medto.getQuantity()));
                med.setRefills(asInteger(medto.getRefills()));
                med.setRefillsRemaining(asInteger(medto.getRemaining()));
                med.setRxNumber(medto.getRxNum());
                med.setSiteNumber(siteNumber);
                med.setSig(medto.getSig());
                med.setStatus(StatusType.Active);
                med.setStopDate(asDate(medto.getStopDate()));

                if (med.getRefillsRemaining() == null) {

                    // MDWS AFAIK passes back refillsRemaining in refills
                    med.setRefillsRemaining(med.getRefills());
                }

                result.add(med);
            }
        }

        return result;

    }
}
