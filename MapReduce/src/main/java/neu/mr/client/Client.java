package neu.mr.client;

import neu.mr.utils.AwsUtil;

/**
 * Main class for the client.
 * 
 * @author chintanpathak, Abhishek Ravichandran
 *
 */
public class Client {

	DiscoveryListener discoveryListener;
	ServerInfo serverInfo;

	/**
	 * constructor to initialize a client.
	 * 
	 * @param address
	 *            server address
	 * @param awskey
	 *            aws key
	 * @param awsid
	 *            aws id
	 */
	public Client(String address, String awsid, String awskey) {
		this.discoveryListener = new DiscoveryListener();
		this.serverInfo = new ServerInfo(address, new AwsUtil(awsid, awskey));
	}

	/**
	 * method to start client's execution.
	 */
	public void execute() {
		this.discoveryListener.start(this.serverInfo);

	}

}