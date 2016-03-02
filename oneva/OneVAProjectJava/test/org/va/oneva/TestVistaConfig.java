package org.va.oneva;

import static org.junit.Assert.assertTrue;
import gov.va.oneva.Host;
import gov.va.oneva.OneVaConfig;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.EnvironmentConfiguration;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestVistaConfig {
	
	private static final Logger log = LoggerFactory.getLogger(TestVistaConfig.class);
	
	private static Configuration config;
	
	@BeforeClass
	public static void init() throws Exception{
		config = OneVaConfig.getConfig();
	}

	@Test
	public void testGetConfig() throws IOException, ConfigurationException {
		Map<String,Host> hosts = OneVaConfig.getHosts();
		log.info(hosts.size() + " hosts found");
		assertTrue(hosts != null);
		assertTrue(hosts.size() > 0);
	}
	
	@Test
	public void testInitConfig() throws Exception{
		OneVaConfig.initConfig();
	}
	
	@Test
	public void testTierHdrConfig() throws Exception{
		String tier = config.getString("tier");
		String expected = config.getString(tier + ".hdr.endpoint");
		String actual = OneVaConfig.getTierConfig("hdr.endpoint");
		assertTrue("value should be: " + expected, expected.equalsIgnoreCase(actual));
	}
	
	@Test
	public void testEnvVar() throws Exception{
		log.info("tier = " + config.getString("tier"));
		log.info("hosts.filename = " + config.getString("hosts.filename"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBadConfig(){
		new Host(null, null, 20);
	}
	
	@Test
	public void testTierConfig() throws ConfigurationException{
		Configuration envConfig = new EnvironmentConfiguration();
		log.info("ONEVA_TIER = " + envConfig.getString("ONEVA_TIER"));
	}
	
	@Test
	public void testHdrTemplate() throws Exception{
		String template = OneVaConfig.getHdrRequestTemplate();
		assertTrue("must have a template", template != null);
	}

}
