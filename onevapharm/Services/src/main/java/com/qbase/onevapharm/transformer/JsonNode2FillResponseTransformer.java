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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import com.qbase.onevapharm.model.FillErrorType;
import com.qbase.onevapharm.model.FillResponse;
import com.qbase.onevapharm.model.GlobalLocation;

import com.qbase.onevapharm.util.ConversionUtils;
import com.qbase.onevapharm.util.VistaDateUtil;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-07-03
 * @author         Jim Horner
 */
public class JsonNode2FillResponseTransformer
        implements Function<JsonNode, FillResponse> {

    /**
     * Method description
     *
     *
     * @param json
     *
     * @return
     */
    @Override
    public FillResponse apply(JsonNode json) {

        FillResponse result = new FillResponse();

        List<String> pieces = new ArrayList<>();

        JsonNode node0 = json.path("0");

        if (node0.isMissingNode() || node0.isNull() || (node0.isValueNode() == false)) {

            result.setFillError(FillErrorType.Missing0Node);

        } else {

            pieces.addAll(Splitter.on("^").trimResults().splitToList(node0.asText()));

            if (pieces.size() < 17) {

                result.setFillError(FillErrorType.MissingPieces);
            }
        }

        if (pieces.isEmpty() == false) {

            if ("1".equals(pieces.get(0))) {

                result.setSuccess(true);
            }

            result.setRxNumber(pieces.get(1));
            result.setPrescriptionId(pieces.get(2));
            result.setRefillId(pieces.get(3));
            result.setFillDate(VistaDateUtil.parseVistaDate(pieces.get(4)));
            result.setDrugName(pieces.get(5));
            result.setQuantity(ConversionUtils.asInt(pieces.get(6), 0));
            result.setDaysSupply(ConversionUtils.asInt(pieces.get(7), 0));
            result.setClerk(pieces.get(8));
            result.setLoginDate(VistaDateUtil.parseVistaDate(pieces.get(9)));
            result.setDivisionId(pieces.get(10));
            result.setDivisionName(pieces.get(11));
            result.setDispenseDate(VistaDateUtil.parseVistaDate(pieces.get(12)));
            result.setNationalDrugCode(pieces.get(13));
            result.setPharmacist(pieces.get(14));
            result.setPhoneNumber(pieces.get(15));
            result.setRequestingSite(pieces.get(16));

            if (pieces.size() > 17) {

                GlobalLocation labelLocation = null;

                String original = Strings.nullToEmpty(pieces.get(17)).trim();

                List<String> locpieces = Splitter.on("(").splitToList(original);

                if (locpieces.size() == 2) {

                    String global = locpieces.get(0);
                    String subscsv = locpieces.get(1).replaceAll("[\")]", "");

                    labelLocation =
                        new GlobalLocation(global, Splitter.on(",").splitToList(subscsv));
                    labelLocation.setUnparsedString(original);
                }

                result.setLabelDataLocation(labelLocation);
            }

            Iterator<JsonNode> chnodes = json.elements();

            // skip node "0"
            chnodes.next();

            while (chnodes.hasNext()) {

                result.addComment(chnodes.next().asText());
            }
        }

        return result;
    }
}
