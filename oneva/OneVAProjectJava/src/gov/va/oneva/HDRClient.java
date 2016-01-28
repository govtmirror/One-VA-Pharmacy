package gov.va.oneva;

import gov.va.med.cds.client.ClinicalDataServiceSynchronousInterface_Service;

public class HDRClient {
	
	/*
	 *   Utility class that connects to the HDR. Will be called by the Router class
	 */
	
	public String getPatientPrescriptions(String patientID, String locationID){
		@SuppressWarnings("unused")
		ClinicalDataServiceSynchronousInterface_Service client = new ClinicalDataServiceSynchronousInterface_Service();
		return null;
	}
	

}
