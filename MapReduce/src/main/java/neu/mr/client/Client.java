package neu.mr.client;

/**
 * Main class for the client
 * @author chintanpathak
 *
 */
public class Client {

	public static void main(String[] args) {
		DiscoveryListener discoveryListener = new DiscoveryListener();
		discoveryListener.start();
	}

}
