package neu.mr.server;

/**
 * Main class for the server
 * @author chintanpathak
 *
 */
public class Server {

	public static void main(String[] args) {
		DiscoverySpeaker discovery = new DiscoverySpeaker();
		discovery.start();
	}

}
