package neu.mr.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neu.mr.client.Client;

/**
 * Main execution point of the MapReduceClient framework.
 * 
 * @author Abhishek Ravichandran
 *
 */
class MapReduceClient {

	private static Logger LOGGER = LoggerFactory.getLogger(MapReduceClient.class);

	/**
	 * main method.
	 * 
	 * @param args
	 *            mode, either start the framework as a server or client.
	 */
	public static void main(String[] args) {
		 Client client = new Client();
		 LOGGER.info("Starting the client");
		 client.execute();
	}

}
