package org.va.oneva;

import static org.junit.Assert.assertTrue;
import gov.va.med.cds.DispensedDrugInstance;
import gov.va.med.cds.HL72CodedElementLite;
import gov.va.med.cds.HL72FacilityIdentifier;
import gov.va.med.cds.MedicationInstructionComponent;
import gov.va.med.cds.ObjectFactory;
import gov.va.med.cds.OutpatientMedicationDispense;
import gov.va.med.cds.OutpatientMedicationPromise;
import gov.va.med.cds.PhysicalQuantity;
import gov.va.med.cds.PointInTime;
import gov.va.oneva.Transformer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.hl7v2.HL7Exception;

public class TestTransformer {
	private String[] labels = {
			"Site Number",
			"Rx Number",
			"Drug Name",
			"Quantity",
			"Refills",
			"Days Supply",
			"Expiration Date",
			"Issue Date",
			"Stop Date",
			"Last Fill Date",
			"Sig",
			"Detail"
		};
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

//	@Test
//	public void testTransformToRTB() throws HL7Exception, IOException {
//		String rtb = Transformer.transformToRTB(getData());
//		System.out.println(rtb);
//		assertTrue("must have an RTB message", StringUtils.isNotBlank(rtb));
//	}
	
	@Test
	public void testTransformToHdrRequest() throws Exception{
		String request = Transformer.transformToHdrRequest("");
		System.out.println(request);
		assertTrue("must have an HDR request message", StringUtils.isNotBlank(request));
	}
	
	private OutpatientMedicationPromise getData(){
		OutpatientMedicationPromise d = new OutpatientMedicationPromise();
		
		Map<String, String> row = getRow(0);
		
		// "Site Number" - 2302
		HL72FacilityIdentifier source = new HL72FacilityIdentifier();
		source.setUniversalId(row.get("Site Number"));
		d.setRecordSource(source);
		
		// "Rx Number" - 501109
		d.setPrescriptionId(row.get("Rx Number"));
		
		// "Drug Name" - NAPROXEN 250MG TAB 
		OutpatientMedicationDispense disp = new OutpatientMedicationDispense();
		d.getRefillDispense().add(disp);		
		DispensedDrugInstance drug = new DispensedDrugInstance();
		disp.setDispensedDrug(drug);
		HL72CodedElementLite ndc = new HL72CodedElementLite();
		ndc.setDisplayText(row.get("Drug Name"));
		drug.setNdc(ndc);
		
		// "Quantity" - 60
		PhysicalQuantity quantity = new PhysicalQuantity();
		disp.setQuantityDispensed(quantity);
		quantity.setValue("60");
		
		// "Refills" - 11
		Integer numRefills = Integer.parseInt(row.get("Refills"));		
		ObjectFactory fact = new ObjectFactory();  
		JAXBElement<Integer> num = fact.createOutpatientMedicationDispenseNumberOfRefillsRemaining(numRefills);
		disp.setNumberOfRefillsRemaining(num);
		
		// "Days Supply" - 30
		disp.setDaysSupply(row.get("Days Supply"));
		
		// "Expiration Date" - 20150517.000000
		PointInTime expDate = fact.createPointInTime();
		expDate.setLiteral(row.get("Expiration Date"));
		d.setExpirationDate(expDate);
		
		
		// "Issue Date" - 20140516.000000
		
		// "Stop Date" - 20150517.000000
		
		// "Last Fill Date" - 20140516.000000
		PointInTime lastFillDate = fact.createPointInTime();
		lastFillDate.setLiteral(row.get("Last Fill Date"));
		d.setLastDispenseDate(lastFillDate);
		
		// "Sig" - TAKE ONE TABLET BY MOUTH TWICE A DAY
		d.getSig().add(row.get("Sig"));
		
		// "Detail" - NAPROXEN 250MG TAB Qty: 60 for 30 days
		MedicationInstructionComponent inst = fact.createMedicationInstructionComponent();
		d.getMedicationInstructions().add(inst);
		return d;
	}
	
	private Map<String, String> getRow(int index){
		String dat = getRdt()[index];
		String[] rdt = dat.split("\\|");
		System.out.println("rdt length = " + rdt.length);
		System.out.println("labels length = " + labels.length);
		Map<String,String> row = new HashMap<String, String>();
		for(int i = 0; i< rdt.length; i++){
			row.put(labels[i], rdt[i]);
		}
		
		return row;
	}
	
	private String[] getRdt(){
		return new String[]{
			"2302|501109|NAPROXEN 250MG TAB|60|11|30|20150517.000000|20140516.000000|20150517.000000|20140516.000000|TAKE ONE TABLET BY MOUTH TWICE A DAY|NAPROXEN 250MG TAB Qty: 60 for 30 days",
			"2302|501110|RANITIDINE HCL 25MG EFFER TAB|60|6|30|20150517.000000|20140516.000000|20150517.000000|20140516.000000|DISSOLVE 1 MOUTH TWICE A DAY|RANITIDINE HCL 25MG EFFER TAB Qty: 60 for 30 days"
		};
	}

}
