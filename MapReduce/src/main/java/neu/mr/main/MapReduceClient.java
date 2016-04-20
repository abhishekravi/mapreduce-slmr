package neu.mr.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
		String serverAddress = "";
		if(args.length != 0){
			serverAddress = args[0];
		}
		Properties prop = new Properties();
		InputStream input;
		String filename = "config.properties";
		input = MapReduceClient.class.getClassLoader().getResourceAsStream(filename);
		try {
			prop.load(input);
			Client client = new Client(serverAddress, prop.getProperty("awsid"), prop.getProperty("awskey"));
			LOGGER.info("Starting the client");
			client.execute();
		} catch (IOException e){
			LOGGER.error("error when loading properties file", e);
		}
	}

}
