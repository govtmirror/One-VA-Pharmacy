package gov.va.oneva;

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

public class VistaSender extends MbJavaComputeNode {

	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
				
		System.out.println("++++++>Inside VistaSender");
				
		MbOutputTerminal out = getOutputTerminal("out");
		MbOutputTerminal alt = getOutputTerminal("alternate");

		MbMessage inMessage = inAssembly.getMessage();
		MbMessageAssembly outAssembly = null;
		try {
			// create new message as a copy of the input
//			MbMessage outMessage = new MbMessage(inMessage);
//			outAssembly = new MbMessageAssembly(inAssembly, outMessage);
			// ----------------------------------------------------------
			// Add user code below

			String inMsg = Util.getString(inMessage);			
			System.out.println("===> Results HL7 to be sent to VistA ===>" + inMsg + "]");
			
			String outMsg = Util.addLLPbytes(inMsg) ;
			
			// send msg to vista in MSH section
			
			MbMessage outMessage = new MbMessage();
			MbElement outRoot = outMessage.getRootElement();
			MbElement outParser = outRoot.createElementAsLastChild(MbBLOB.PARSER_NAME);
			MbElement outBody = outParser.createElementAsLastChild(MbElement.TYPE_NAME_VALUE, "BLOB", outMsg.getBytes());
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
