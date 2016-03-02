package gov.va.oneva;

import gov.va.med.cds.MedicationInstructionComponent;
import gov.va.med.cds.OutpatientMedicationDispense;
import gov.va.med.cds.OutpatientMedicationPromise;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.GenericPrimitive;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v251.group.RTB_K13_ROW_DEFINITION;
import ca.uhn.hl7v2.model.v251.message.RTB_K13;
import ca.uhn.hl7v2.model.v251.segment.MSA;
import ca.uhn.hl7v2.model.v251.segment.RDF;
import ca.uhn.hl7v2.model.v251.segment.RDT;

public class Transformer {
	
	private static final Logger log = LoggerFactory.getLogger(Transformer.class);
	
	private static final String[] labels = { "Site Number", "Rx Number", "Drug Name", "Quantity", "Refills", "Days Supply", "Expiration Date", "Issue Date",
			"Stop Date", "Last Fill Date", "Sig", "Detail" };

	/**
	 * Transforms the QBP^Q13 HL7 message into an HDR xml request.
	 * 
	 * @param qbpQ13
	 * @return
	 * @throws Exception 
	 */
	public static String transformToHdrRequest(String qbpQ13) throws Exception{
		// 2009-01-01
		String hdrDateFormat = "yyyy-MM-dd";
		@SuppressWarnings("unused")
		String vistaDateFormat = "yyyyMMdd.HHmm00";
		
		String patientIcn = "233ABC";
		String startDate = "1900-01-01";
		String endDate = FastDateFormat.getInstance(hdrDateFormat).format(new Date());
		String requestId = "42";
	
        // quick-n-dirty
        String template = OneVaConfig.getHdrRequestTemplate();
        template = template.replace("${patientIcn}", patientIcn);
        template = template.replace("${startDate}", startDate);
        template = template.replace("${endDate}", endDate);
        template = template.replace("${requestId}", requestId);
        
		return template;
	}
	
	/**
	 * Transforms the list patient prescriptions into an RTB^K13 HL7 message.
	 * 
	 * @param rxs the list of prescriptions to transform
	 * @return the RTB^K13 HL7 message to return to the calling VistA instance
	 * @throws HL7Exception
	 * @throws IOException
	 */
	public static String transformToRTB(List<OutpatientMedicationPromise> rxs)throws HL7Exception, IOException {
		Collections.sort(rxs, new HdrComparator());
		RTB_K13 rtb = new RTB_K13();
		rtb.initQuickstart("RTB", "K13", "P");

		/* Structure for the RTB^K13 message
		 * ---------------------------------------
		 * 1: MSH (Message Header) 
		 * 2: SFT (Software Segment) optional repeating
		 * 3: MSA (Message Acknowledgment) 
		 * 4: ERR (Error) optional 
		 * 5: QAK (Query Acknowledgment) 
		 * 6: QPD (Query Parameter Definition) 
		 * 7: RTB_K13_ROW_DEFINITION (a Group object) optional 
		 * 8: DSC (Continuation Pointer) optional
		 */
		// not sure how to fill out the QAK and the QPD segments...
		
		MSA msa = (MSA) rtb.getMSA();
		msa.getMsa1_AcknowledgmentCode().setValue("AA");
		msa.getMsa2_MessageControlID().setValue("fakenumber"); //TODO need a message control Id 

		RTB_K13_ROW_DEFINITION rd = rtb.getROW_DEFINITION();
		RDF rdf = rd.getRDF();

		// populate the RDF segment with the labels i.e. the RDT segment
		rdf.getNumberOfColumnsPerRow().setValue("" + labels.length);
		for (int i = 0; i < labels.length; i++) {
			String label = labels[i];
			rdf.getColumnDescription(i).getSegmentFieldName().setValue(label);
		}
		
		// populate the patient's prescriptions, 1 row for each prescription with the newest first 
		for(int index = 0; index < rxs.size(); index++){
			OutpatientMedicationPromise prescription = rxs.get(index);
			RDT rdt = rd.insertRDT(index);
			addPrescriptionRow(rdt, prescription);
		}
		return rtb.encode();
	}
	
	/*
	 * Utility class to do all the transformation Will be called by the router
	 * 
	 * sample methods: String toHL7(SomeClass) , SomeClass fromHL7(String
	 * hl7Message)
	 * 
	 * I think the cardinality of this is incorrect. I think one
	 * OutpatientMedicationPromise maps to one RDT row. Need to confirm this
	 * with Narasa Susarla, Program Architect at VA.
	 * 
	 * I confirmed with Narasa that one promise maps to one prescription.
	 */
	private static void addPrescriptionRow(RDT rdt, OutpatientMedicationPromise rx) throws HL7Exception {
		// Site Number
		putValue(rdt, 1, rx.getRecordSource().getUniversalId());

		// Rx Number
		putValue(rdt, 2, rx.getPrescriptionId());

		/*
		 * TODO what do I do with more than 1 refill dispense?!?
		 */
		if (rx.getRefillDispense().isEmpty() == false) {
			OutpatientMedicationDispense refill = rx.getRefillDispense().get(0);

			// Drug Name
			putValue(rdt, 3, refill.getDispensedDrug().getNdc().getDisplayText());

			// Quantity
			putValue(rdt, 4, refill.getQuantityDispensed().getValue());

			// Refills
			putValue(rdt, 5, refill.getNumberOfRefillsRemaining().getValue().toString());

			// Days Supply
			putValue(rdt, 6, refill.getDaysSupply());
		}

		// Expiration Date - TODO check the CDR date format
		putValue(rdt, 7, Util.format(rx.getExpirationDate().getLiteral()));
		
		// Issue Date
		// putValue(rdt, 8, ???);
		
		// Stop Date
		// putValue(rdt, 9, ???);
		
		// Last Fill Date
		putValue(rdt, 10, Util.format(rx.getLastDispenseDate().getLiteral()));

		// Sig
		if (rx.getSig().isEmpty() == false) {
			putValue(rdt, 11, rx.getSig().get(0));
		}

		// Detail
		if(rx.getMedicationInstructions().isEmpty() == false){
			// what do do if there is more than one instruction?
			String detail = getDetail(rx.getMedicationInstructions().get(0));
			putValue(rdt, 12, detail);
		}
	}
	
	// TODO map the instructions into a string...somehow
	private static String getDetail(MedicationInstructionComponent medicationInstructions) {
		// sample: NAPROXEN 250MG TAB Qty: 60 for 30 days
		/*
		    conjunction;
		    dispenseUnitsPerDose;
		    dosageOrdered;
		    noun;
		    route;
		    schedule;
		    verb;
		    giveUnits;
		    intendedDuration;
		    
		    {noun} {dosageOrdered} Qty: {giveUnits} for {intendedDuration}
		 */
		return "still figuring this part out";
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
	private static void putValue(RDT rdt, int index, String value) throws HL7Exception {

		GenericPrimitive msgval = new GenericPrimitive(rdt.getMessage());

		msgval.setValue(value);

		Varies type = (Varies) rdt.getField(index, 0);

		type.setData(msgval);
	}

}
