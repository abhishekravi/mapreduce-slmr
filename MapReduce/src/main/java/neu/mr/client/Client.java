package neu.mr.client;

/**
 * Main class for the client
 * @author chintanpathak, Abhishek Ravichandran
 *
 */
public class Client {
	
	DiscoveryListener discoveryListener;
	ServerInfo serverInfo;
	
	/**
	 * constructor to initialize a client.
	 */
	public Client(String address){
		this.discoveryListener = new DiscoveryListener();
		this.serverInfo = new ServerInfo(address);
	}
	
	/**
	 * method to start client's execution.
	 */
	public void execute(){
		this.discoveryListener.start(this.serverInfo);
		
	}

}