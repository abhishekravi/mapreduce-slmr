package neu.mr.server;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class for the server
 * @author chintanpathak, Abhishek Ravichandran
 *
 */
public class Server {

	List<ConnectedClient> connectedClients;
	DiscoverySpeaker discovery;
	
	/**
	 * constructor to initialize the server
	 */
	public Server(){
		this.discovery = new DiscoverySpeaker();
		this.connectedClients = new ArrayList<ConnectedClient>();
	}
	
	/**
	 * method to start the server.
	 */
	public void execute () {
		this.discovery.start(connectedClients);
	}

}