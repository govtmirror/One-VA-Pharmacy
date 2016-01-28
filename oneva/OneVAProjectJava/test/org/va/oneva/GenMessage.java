package org.va.oneva;

import java.io.IOException;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.v251.message.ADT_A01;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.PID;
import ca.uhn.hl7v2.parser.Parser;

public class GenMessage {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws HL7Exception 
	 */
	public static void main(String[] args) throws HL7Exception, IOException {
		ADT_A01 adt = new ADT_A01();
		adt.initQuickstart("ADT", "A01", "P");

		// Populate the MSH Segment
		MSH mshSegment = adt.getMSH();
		mshSegment.getSendingApplication().getNamespaceID().setValue("TestSendingSystem");
		mshSegment.getSequenceNumber().setValue("123");

		// Populate the PID Segment
		PID pid = adt.getPID(); 
		pid.getPatientName(0).getFamilyName().getSurname().setValue("Doe");
		pid.getPatientName(0).getGivenName().setValue("John");
		pid.getPatientIdentifierList(0).getIDNumber().setValue("123456");
		
		@SuppressWarnings("resource")
		HapiContext context = new DefaultHapiContext();
		Parser parser = context.getPipeParser();
		String encodedMessage = parser.encode(adt);
		System.out.println("Printing ER7 Encoded Message:");
		System.out.println(encodedMessage);

	}

}
