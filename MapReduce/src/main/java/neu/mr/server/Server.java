package neu.mr.server;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neu.mr.job.JobScheduler;

/**
 * Main class for the server
 * @author chintanpathak, Abhishek Ravichandran
 *
 */
public class Server {

	private static Logger LOGGER = LoggerFactory.getLogger(Server.class);
	List<ConnectedClient> connectedClients;
	DiscoverySpeaker discovery;
	JobScheduler jobScheduler;
	
	/**
	 * constructor to initialize the server
	 */
	public Server(JobScheduler jobScheduler){
		this.jobScheduler = jobScheduler;
		this.discovery = new DiscoverySpeaker();
		this.connectedClients = new ArrayList<ConnectedClient>();
	}
	
	/**
	 * method to start the server.
	 */
	public void execute () {
		this.discovery.start(connectedClients);
		while(this.connectedClients.isEmpty())
			LOGGER.debug("waiting for clients");
		LOGGER.info("got a client");
		jobScheduler.setConnectedClients(connectedClients);
		jobScheduler.startScheduling(discovery);
	}

}