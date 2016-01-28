package gov.va.oneva;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.NumberUtils;

public class OneVaConfig {
	
	private static Configuration config;
	private static String hdrRequestTemplate;
	
	/**
	 * Initializes the configuration for OneVA.
	 * 
	 * @throws ConfigurationException if the properties file is not found or if the environment variable, 'ONEVA_TIER' is not set
	 * @throws IOException 
	 */
	public static void initConfig() throws ConfigurationException, IOException{
		config = new PropertiesConfiguration("config.properties");
		if(config.getString("tier") == null){
			throw new ConfigurationException("Failed to find environment variable 'ONEVA_TIER' set to 'dev', 'test' or 'prod'");
		}
		hdrRequestTemplate = FileUtils.readFileToString(new File(config.getString("hdr.request.template")));
	}
	
	/**
	 * Gets the configuration for OneVA, calls {@link #initConfig()} if it doesn't exist yet.
	 * 
	 * @return the OneVA configuration
	 * @throws ConfigurationException
	 * @throws IOException 
	 */
	public static Configuration getConfig() throws ConfigurationException, IOException{
		if(config == null){
			initConfig();
		}
		return config;
	}
	
	/**
	 * Gets a tier-specific configuration the pre-pends the environment variable 'ONEVA_TIER'.
	 * 
	 * @param key the <i>generic</i> key to use to get the config value.
	 * @return the tier-specific value for the given key.
	 * @throws ConfigurationException
	 * @throws IOException 
	 */
	public static String getTierConfig(String key) throws ConfigurationException, IOException{
		getConfig();
		String tier = config.getString("tier");
		
		return config.getString(tier + "." + key);
	}

	/**
	 * converts the tier-specific csv file into a map of hosts for easy lookup.
	 * 
	 * 
	 * @return a map of the site ID to hosts. [see {@link Host}]
	 * @throws IOException
	 * @throws ConfigurationException 
	 */
	public static Map<String,Host> getHosts() throws IOException, ConfigurationException{
		String fname = getConfig().getString("hosts.filename");
		List<String> hosts = FileUtils.readLines(new File(fname));
		
		// remove the metadata
		hosts.remove(0); 
		Map<String,Host> theHosts = new HashMap<String,Host>();
		// create the IP to Host lookup collection
		for(String host: hosts){
			String[] row = host.split(",");
			Host theHost = new Host(row[1], row[2], NumberUtils.toInt(row[3]));
			theHost.setUseHostname(getConfig().getBoolean("use.hostname"));
			theHosts.put(row[0], theHost);
		}
		// all done, return collection
		return theHosts;
	}

	public static String getHdrRequestTemplate() throws Exception {
		if(config == null){
			initConfig();
		}
		return hdrRequestTemplate;
	}
}
