package gov.va.oneva;

import gov.va.med.cds.OutpatientMedicationPromise;
import gov.va.med.cds.client.ClinicalDataServiceSynchronousInterface_Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.hl7v2.util.Terser;

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.MbBLOB;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;



/**
 * 
 * @author Birali
 * @version v1.0, 2015-01-13
 */

public class Router extends MbJavaComputeNode {

	private static final Logger log = LoggerFactory.getLogger(Router.class);
	
	private Map<String,Host> hosts;
	
	@Override
	public void onInitialize() throws MbException {
		super.onInitialize();
		try {
			hosts = OneVaConfig.getHosts();
		} catch (Exception e) {
			// should convert this to MbException but not what to put into constructor
			e.printStackTrace();
		}
	}
	
	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		
		
		log.debug("++++++ Inside Router+++++++++++++");
		
		MbOutputTerminal out = getOutputTerminal("out");
		@SuppressWarnings("unused")
		MbOutputTerminal alt = getOutputTerminal("alternate");

		MbMessage inMessage = inAssembly.getMessage();
		MbMessageAssembly outAssembly = null;
		try {
			// create new message as a copy of the input
//			MbMessage outMessage = new MbMessage(inMessage);
//			outAssembly = new MbMessageAssembly(inAssembly, outMessage);
			// ----------------------------------------------------------
			// Add user code below

					
			log.debug("++++++ Router got a message!");
			String inMsg = Util.getString(inMessage);			
			log.debug(inMsg);
		
			Terser terser = Util.getTerser(inMsg);
			String msgType = terser.get("/MSH-9-3");
			String hl7Response = inMsg;
			
			// Query the HDR?
			if("QBP_Q13".equalsIgnoreCase(msgType)){
				// TODO call the HDRClient with the query values for HDR 
				// Get the results from HDR
				// Call the Transformer From HDR -> HL7
				// Create outputTerminal to store the HL7

				@SuppressWarnings("unused")
				ClinicalDataServiceSynchronousInterface_Service client = new ClinicalDataServiceSynchronousInterface_Service();
				@SuppressWarnings("unused")
				String hdrRequest = Transformer.transformToHdrRequest(inMsg);
				
				List<OutpatientMedicationPromise> prescriptions = new ArrayList<OutpatientMedicationPromise>();
				
				// GET THE RESPONSE TURN INTO A LIST TO TRANSFORM...THIS IS TEMP CODE!
				prescriptions.add(new OutpatientMedicationPromise());
				
				
				hl7Response = Transformer.transformToRTB(prescriptions);
			}
			else{
				// send message to destination Vista and send response to outputTerminal
				String receivingFacility = terser.get("/MSH-6");
				log.debug("===>MSH-6=" + receivingFacility);
				String subMsgType = terser.get("/MSH-9-2");
				log.debug("===>MSH-9=" + subMsgType);
				
				if (StringUtils.isNotBlank(receivingFacility)) {
					Host host = hosts.get(receivingFacility);
					if(host != null){
						MLLPClient client = new MLLPClient(host);
						hl7Response = client.sendMessage(inMsg);
					}
				} 
				else {
					log.debug("===>No routing information found. MSH-6 is empty");
				}				
			}	
			
			MbMessage outMessage = createOutMessage(hl7Response);
			outAssembly = new MbMessageAssembly(inAssembly, outMessage);
			// End of user code
			// ----------------------------------------------------------
		} catch (MbException e) {
			// Re-throw to allow Broker handling of MbException
			throw e;
		} catch (RuntimeException e) {
			// Re-throw to allow Broker handling of RuntimeException
			throw e;
		} catch (Exception e) {
			// Consider replacing Exception with type(s) thrown by user code
			// Example handling ensures all exceptions are re-thrown to be handled in the flow
			throw new MbUserException(this, "evaluate()", "", "", e.toString(),
					null);
		}
		// The following should only be changed
		// if not propagating message to the 'out' terminal
		out.propagate(outAssembly);

	}

	private MbMessage createOutMessage(String hl7Response) throws MbException {
		MbMessage outMessage = new MbMessage();
		MbElement outRoot = outMessage.getRootElement();
		MbElement outParser = outRoot.createElementAsLastChild(MbBLOB.PARSER_NAME);
		
		
		@SuppressWarnings("unused")
		MbElement outBody = outParser.createElementAsLastChild(MbElement.TYPE_NAME_VALUE, "BLOB", hl7Response.getBytes());
			
		return outMessage;
	}

}
