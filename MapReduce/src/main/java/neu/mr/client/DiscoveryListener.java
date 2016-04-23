package neu.mr.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neu.mr.commons.Command;
import neu.mr.commons.CommandEnum;

/**
 * The ackListener class for our client that listens to the discovery packet that
 * the server sends.
 * 
 * @author Chintan Pathak, Abhishek Ravichandran
 *
 */
public class DiscoveryListener {
	
	private static Logger LOGGER = LoggerFactory.getLogger(DiscoveryListener.class);
	Thread listener;

	/**
	 * Start the listener
	 */
	public void start(ServerInfo serverInfo) {
		listener = new Thread(new ListenerRunnable(serverInfo));
		listener.start();
	}

	/**
	 * Runnable class for the ackListener with the run() method
	 * 
	 * @author chintanpathak
	 *
	 */
	private class ListenerRunnable implements Runnable {

		DatagramPacket receivedPacket;
		byte[] buf = new byte[2048];
		boolean discovered = false;
		DatagramSocket socket;
		ServerInfo serverInfo;

		/**
		 * Initialize the socket and a packet to receive message into.
		 * 
		 * @param serverInfo
		 *            to store server information
		 */
		public ListenerRunnable(ServerInfo serverInfo) {
			try {
				this.serverInfo = serverInfo;
				socket = new DatagramSocket(54321);
				if(this.serverInfo.address != null){
					this.discovered = true;
					Command command = new Command(CommandEnum.DISCOVER);
					command.getName().parameters.add(receivedPacket);
					command.getName().parameters.add(serverInfo);
					command.getName().parameters.add(socket);
					command.getName().run();
				}
			} catch (SocketException e) {
				LOGGER.error("exception during discovery socket creation:" + e);
			}
			receivedPacket = new DatagramPacket(buf, buf.length);
		}

		/**
		 * Listens for the discover packet
		 */
		public void run() {

			while (!discovered) {
				try {
					socket.receive(receivedPacket);
					Command command = (Command) SerializationUtils.deserialize(receivedPacket.getData());
					if (CommandEnum.DISCOVER.toString().equals(command.getName().toString())) {
						discovered = true;
						LOGGER.info("Command received:" + command.getName().toString());
						command.getName().parameters.add(receivedPacket);
						command.getName().parameters.add(serverInfo);
						command.getName().parameters.add(socket);
						command.getName().run();
					}
				} catch (IOException e) {
					LOGGER.error("Exception while hearing for discovery packet", e);
				}
			}
		}
		
	}
}
