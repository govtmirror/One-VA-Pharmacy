package gov.va.oneva;

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.MbBLOB;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;

public class GenerateACK extends MbJavaComputeNode {

	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		System.out.println("#########  In GenerateACK #############");
		MbOutputTerminal out = getOutputTerminal("out");
		@SuppressWarnings("unused")
		MbOutputTerminal alt = getOutputTerminal("alternate");

		MbMessage inMessage = inAssembly.getMessage();
		MbMessageAssembly outAssembly = null;
		try {
			
			// ----------------------------------------------------------
			// Add user code below
			
			String inMsg = Util.getString(inMessage);			
				
			String hl7Msg = Util.removeLLPbytes(inMsg); 
			System.out.println("===hl7 payload msg===\n" + hl7Msg);			
			
			MbMessage outMessage = new MbMessage();
			MbElement outRoot = outMessage.getRootElement();
			MbElement outParser = outRoot.createElementAsLastChild(MbBLOB.PARSER_NAME);
			
			String ack = Util.generateACK(hl7Msg);
			
			String ackllp = Util.addLLPbytes(ack);
			System.out.println("===ack llp msg===\n" + ackllp);			
			
			@SuppressWarnings("unused")
			MbElement outBody = outParser.createElementAsLastChild(MbElement.TYPE_NAME_VALUE, "BLOB", ackllp.getBytes());
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
