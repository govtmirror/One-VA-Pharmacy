package org.va.oneva;

import static org.junit.Assert.assertTrue;
import gov.va.oneva.Host;
import gov.va.oneva.MLLPClient;
import gov.va.oneva.OneVaConfig;

import java.io.File;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestEsbConnection {

	private static String msg;
	private static MLLPClient client;
	private static Host host;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		msg = FileUtils.readFileToString(new File("test/sample-HL7.txt"));
		Map<String, Host> hosts = OneVaConfig.getHosts();
		host = hosts.get("0");
		client = new MLLPClient(host);
	}

	@Test
	public void test() {
		String response = client.sendTcpMessage(msg);
		assertTrue("failed to send to: " + host, StringUtils.isNotEmpty(response));
	}

}
