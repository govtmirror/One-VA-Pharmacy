package gov.va.oneva;


/**
 * This is the POJO to hold all the host information to allow a MLLP or TCP connection.
 * 
 * @author Tony Burleson
 * @since 12/04/2015
 */
public class Host {
	private String ip;
	private String hostname;
	private int port = -1;
	private boolean useHostname;
	
	/**
	 * Creates a Host instance with ip, hostname and port to connect to.
	 * 
	 * @param ip the IP of the machine to connect to
	 * @param hostname the host name of the machine to connect to
	 * @param port the specific port to connect to
	 */
	public Host(String ip, String hostname, int port) throws IllegalArgumentException{
		this.ip = ip;
		this.hostname = hostname;
		this.port = port;
		// both can NOT be null
		if(ip == null && hostname == null){
			throw new IllegalArgumentException("IP or hostname is required");
		}
		if(port < 0){
			throw new IllegalArgumentException("A valid port number greater than 0 is required [" + port + "]");
		}
	}
	
	public String getIpOrHostname(){
		return (ip != null ? ip : hostname);
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public boolean isUseHostname() {
		return useHostname;
	}

	public void setUseHostname(boolean useHostname) {
		this.useHostname = useHostname;
	}

	@Override
	public String toString() {
		return "Host [ip=" + ip + ", hostname=" + hostname + ", port=" + port + "]";
	}	
}
