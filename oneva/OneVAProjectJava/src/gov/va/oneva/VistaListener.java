package gov.va.oneva;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class VistaListener extends MbJavaComputeNode {
	
	private static final Logger log = LoggerFactory.getLogger(VistaListener.class);

	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		log.debug("~~~~~~~ In VistaListener ~~~~~~~~~~");
		MbOutputTerminal out = getOutputTerminal("out");
		@SuppressWarnings("unused")
		MbOutputTerminal alt = getOutputTerminal("alternate");

		MbMessage inMessage = inAssembly.getMessage();
		MbMessageAssembly outAssembly = null;
		try {
			// create new message as a copy of the input
			//MbMessage outMessage = new MbMessage(inMessage);
			//outAssembly = new MbMessageAssembly(inAssembly, outMessage);
			// ----------------------------------------------------------
			// Add user code below
			
			String inMsg = Util.getString(inMessage);
						
			String hl7Msg = Util.removeLLPbytes(inMsg); 
			log.debug("===hl7 payload msg===\n" + hl7Msg);
			
						
			MbMessage outMessage = new MbMessage();
			MbElement outRoot = outMessage.getRootElement();
			MbElement outParser = outRoot.createElementAsLastChild(MbBLOB.PARSER_NAME);
			@SuppressWarnings("unused")
			MbElement outBody = outParser.createElementAsLastChild(MbElement.TYPE_NAME_VALUE, "BLOB", hl7Msg.getBytes());
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

}
