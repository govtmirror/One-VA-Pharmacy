package gov.va.oneva;

//import org.apache.log4j.ConsoleAppender;
//import org.apache.log4j.Level;
//import org.apache.log4j.Logger;
//import org.apache.log4j.PatternLayout;
//import org.apache.log4j.RollingFileAppender;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.message.ACK;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

import com.ibm.broker.plugin.MbBLOB;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessage;

/**
 * 
 * @author Birali
 * @version v1.0, 2015-01-13
 */
public class Util {

	// public static Logger rootLogger = null;

	public final static char VT = 0x0b;
	public final static char FS = 0x1c;
	public final static char CR = 0x0d;
	
	//private final static char END_OF_BLOCK = '\u001c';
	//private final static char START_OF_BLOCK = '\u000b';
	//private final static char CR = 13;

	/**
	 * gets the HL7 message from the Message Broker object
	 * 
	 * @param inMessage
	 *            the incoming message to harvest the HL7 message from
	 * @return the HL7 message as a string.
	 * @throws Exception
	 */
	public static String getString(MbMessage inMessage) throws Exception {
		MbElement inRoot = inMessage.getRootElement();
		byte[] MessageBodyByteArray = (byte[]) (inRoot.getFirstElementByPath("/BLOB/BLOB").getValue());
		String hl7Msg = new String(MessageBodyByteArray);
		return hl7Msg;
	}

	/**
	 * Converts the HL7 message string into a Message Broker object to pass
	 * along the message flow.
	 * 
	 * @param msg
	 *            the HL7 message to convert
	 * @return the outgoing message broker object to send.
	 * @throws MbException
	 */
	public static MbMessage buildMbMsg(String msg) throws MbException {
		MbMessage outMessage = new MbMessage();
		MbElement outRoot = outMessage.getRootElement();
		MbElement outParser = outRoot.createElementAsLastChild(MbBLOB.PARSER_NAME);
		@SuppressWarnings("unused")
		MbElement outBody = outParser.createElementAsLastChild(MbElement.TYPE_NAME_VALUE, "BLOB", msg.getBytes());
		return outMessage;
	}

	/**
	 * Generates and HL7 ACK message to return to the calling VistA to
	 * acknowledge the ESB received the message.
	 * 
	 * @param msg
	 *            the HL7 message to acknowledge
	 * @return the HL7 ACK message as a string or an empty string if the parse fails.
	 */
	public static String generateACK(String msg) {
		ACK ackMsg = null;
		try {
			@SuppressWarnings("resource")
			HapiContext context = new DefaultHapiContext();

			Parser p = context.getGenericParser();

			Message hapiMsg = p.parse(msg);

			ackMsg = (ACK) hapiMsg.generateACK();
			
			return ackMsg.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Removes the first and last byte of the message which "should" consist of
	 * the LLP bytes.
	 * 
	 * @param inMsg
	 *            the HL7 message to strip the LLP bytes from.
	 * @return the stripped HL7 message
	 */
	public static String removeLLPbytes(String inMsg) {
		String outMsg = new String();		
		if (inMsg.length() > 1) {
			String s1 = inMsg.substring(1);
			outMsg = s1.substring(0, s1.length() - 1);
		}

		return outMsg;
	}
	
	public static Terser getTerser(String msg)throws HL7Exception{
		@SuppressWarnings("resource")
		HapiContext context = new DefaultHapiContext();
		context.setValidationContext(new NoValidation());
		PipeParser pipeParser = context.getPipeParser();

		Message hMsg = pipeParser.parse(msg);
		
		if ((hMsg instanceof Message) == false) {
			// this might not even execute if .parse() throws an exception
			throw new HL7Exception(StringUtils.abbreviate(msg, 40) + " is not a valid HL7 message");
		}
		
		return new Terser((Message) hMsg);
	}
	
	public static String getReceivingFacility(String msg) throws HL7Exception{
		@SuppressWarnings("resource")
		HapiContext context = new DefaultHapiContext();
		context.setValidationContext(new NoValidation());
		PipeParser pipeParser = context.getPipeParser();

		Message hMsg = pipeParser.parse(msg);
		
		Terser terser = null;
		if (hMsg instanceof Message) {
			terser = new Terser((Message) hMsg);
		}
		else{
			System.out.println("msg is not an HL7 message");
		}
	
		String receivingFacility = terser.get("/MSH-6");
		System.out.println("===>MSH-6=" + receivingFacility);
		String msgType = terser.get("/MSH-9-2");
		System.out.println("===>MSH-9=" + msgType);
		return (receivingFacility != null ? receivingFacility : "") ;
	}
	
	/**
	 * Adds the beginning and ending LLP bytes needed for sending and HL7 message;
	 * 
	 * @param msg the HL7 message to wrap
	 * @return the wrapped message with the LLP bytes.
	 */
	public static String addLLPbytes(String msg){
		StringBuilder sb = new StringBuilder();
		sb.append(Util.VT);
		sb.append(msg);
		sb.append(Util.FS);
		sb.append(Util.CR);
		
//		sb.append(START_OF_BLOCK);
//		sb.append(msg);
//		sb.append(CR);
//		sb.append(END_OF_BLOCK);
//		sb.append(CR);
		return sb.toString();
	}
	
	/**
	 * Formats the date object to the HL7 string date format. <i>yyyyMMdd.HHmm00</i><br/>
	 * <b>05/17/2015 12:00am</b> is transformed into : <b>20150517.000000</b>
	 * 
	 * @param date the date to format into
	 * @return the formatted date to use
	 */
	public static String format(Date date){
		FastDateFormat fmt = FastDateFormat.getInstance("yyyyMMdd.HHmm00");
		return fmt.format(date);
	}
	
	/**
	 * Re-Formats the CDR date into the HL7 date format {@link #format(Date)}<br>
	 * <h1 style='color:red;font-size:2em;'>Currently this does nothing!</h1> 
	 * <br/>
	 * @param cdrDate the CDR date to format
	 * @return the HL7 date format needed
	 */
	public static String format(String cdrDate){
		return cdrDate;
	}
	
	// Temporary for debugging
	/*
	 * public static Logger getLoggerInstance() {
	 * 
	 * if (rootLogger == null) { rootLogger = Logger.getRootLogger();
	 * 
	 * RollingFileAppender fa = new RollingFileAppender();
	 * fa.setName("RollingFileLogger"); fa.setFile("/temp/mssconsole.log");
	 * fa.setMaxFileSize("10MB"); fa.setLayout(new
	 * PatternLayout("%d %-5p [%c{1}] %m%n")); fa.setThreshold(Level.DEBUG);
	 * fa.setAppend(true); fa.activateOptions(); rootLogger =
	 * Logger.getRootLogger(); rootLogger.addAppender(fa);
	 * 
	 * 
	 * 
	 * ConsoleAppender fa = new ConsoleAppender(); fa.setName("ConsoleLogger");
	 * fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
	 * fa.setThreshold(Level.DEBUG); fa.activateOptions(); rootLogger =
	 * Logger.getRootLogger(); rootLogger.addAppender(fa);
	 * 
	 * 
	 * }
	 * 
	 * return rootLogger;
	 * 
	 * }
	 */
}
