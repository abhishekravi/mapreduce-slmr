package neu.mr.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neu.mr.client.Client;
import neu.mr.server.Server;

/**
 * Main execution point of the MapReduce framework.
 * 
 * @author Abhishek Ravichandran
 *
 */
public class MapReduce {

	private static Logger LOGGER = LoggerFactory.getLogger(MapReduce.class);

	/**
	 * main method.
	 * 
	 * @param args
	 *            mode, either start the framework as a server or client.
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			LOGGER.info("usage <client/server>");
		}
		String mode = args[0];

		switch (mode) {
		case "client":
			Client client = new Client();
			client.execute();
			break;
		case "server":
			Server server = new Server();
			server.execute();
		}
	}

}
