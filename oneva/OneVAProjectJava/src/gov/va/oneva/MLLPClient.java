package gov.va.oneva;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;

public class MLLPClient {
	private Host host;
	boolean useTls = false; // Should we use TLS/SSL?

	/**
	 * Initializes the client with the destination host.
	 * 
	 * @param destHost
	 *            The host to send a message to
	 */
	public MLLPClient(Host destHost) {
		this.host = destHost;
	}

	/**
	 * Sends the message to the host provided
	 * 
	 * @param msg
	 *            the message to send.
	 * @return the response from the destination host.
	 * @throws HL7Exception
	 * @throws LLPException
	 * @throws IOException
	 */
	public String sendMessage(String msg) throws HL7Exception, LLPException, IOException {
		if (this.host == null) {
			throw new IOException("Unable to send a message to unknown destination!");
		}

		HapiContext context = null;
		Connection connection = null;
		try {
			// connect to the host
			context = new DefaultHapiContext();
			System.out.println(host);
			connection = context.newClient(host.getIp(), host.getPort(), useTls);			
			// parse the HL7 message to send
			Parser p = context.getPipeParser();
			Message adt = p.parse(msg);

			// send the message and get a response
			Initiator initiator = connection.getInitiator();
			Message response = initiator.sendAndReceive(adt);

			// now return the response
			return p.encode(response);
		} finally {
			if (connection != null && connection.isOpen()) {
				connection.close();
			}
			if (context != null) {
				context.close();
			}
		}
	}
	
	public String sendTcpMessage(String msg){
		byte[] buff =  gov.va.oneva.Util.addLLPbytes(msg).getBytes();
		
		String ip = host.getIp();
		int port = host.getPort();

		Socket socket = null;
		try {
			socket = new Socket(ip, port);
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			socket.setSoTimeout(30000);
			
			out.write(buff);
			byte[] response = new byte[800];
			in.read(response);

			String vistaResponse = StringUtils.trimToEmpty(new String(response));
			System.out.println("TCP response: " + vistaResponse);
			return vistaResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			IOUtils.closeQuietly(socket);
		}
		
	}
}
