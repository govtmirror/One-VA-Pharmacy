package org.va.oneva;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.va.oneva.Host;
import gov.va.oneva.MLLPClient;
import gov.va.oneva.OneVaConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.llp.LLPException;

public class TestClient {

	private String msg;
	private MLLPClient client;
	private Host host;

	@Before
	public void init() throws IOException, ConfigurationException {
		msg = FileUtils.readFileToString(new File("test/sample-HL7.txt"));
		Map<String, Host> hosts = OneVaConfig.getHosts();
		host = hosts.get("0");
		client = new MLLPClient(host);

		int timeout = 3000;
		System.setProperty("ca.uhn.hl7v2.app.initiator.timeout", Integer.toString(timeout));
	}

//	@Test
	public void testMessage() throws HL7Exception, LLPException, IOException {
		assertTrue("Must have an HL7 message to send!", StringUtils.isNotEmpty(msg));
		try {
			String response = client.sendMessage(msg);
			System.out.println("MLLP response: " + response);
			assertTrue(StringUtils.isNotEmpty(response));
		} catch (Exception e) {
			e.printStackTrace();
			fail("failed to connect to: " + host + " -- " +  e.getMessage());
		}
	}

	@Test
	public void tcpMessage() throws Exception {
		char END_OF_BLOCK = '\u001c';
		char START_OF_BLOCK = '\u000b';
		char CR = 13;

		StringBuffer mb = new StringBuffer();
		mb.append(START_OF_BLOCK);
		mb.append("MSH|^~\\&|TestSendingSystem||||20151209191100.85-0500||ADT^A01^ADT_A01|1|P|2.5.1|123");
		mb.append(CR);
		mb.append("PID|||123456||Doe^John");
		mb.append(CR);
		mb.append(END_OF_BLOCK);
		mb.append(CR);

//		byte[] buff =  mb.toString().getBytes();
		byte[] buff =  gov.va.oneva.Util.addLLPbytes(msg).getBytes();
		
		String ip = host.getIp();
		int port = host.getPort();

		Socket socket = null;
		try {
			socket = new Socket(ip, port);
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			socket.setSoTimeout(3000);
			out.write(buff);
			byte[] response = new byte[800];
			in.read(response);

			String vistaResponse = StringUtils.trimToEmpty(new String(response));
			System.out.println("TCP response: " + vistaResponse);
			assertTrue("Must have a non-empty response", StringUtils.isNotEmpty(vistaResponse));

		} catch (Exception e) {
			fail("failed to connect to: " + host + " -- " +e.getMessage());
		} finally {
			IOUtils.closeQuietly(socket);
		}
	}
}
