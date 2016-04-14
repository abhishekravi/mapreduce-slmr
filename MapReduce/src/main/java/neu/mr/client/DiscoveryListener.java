package neu.mr.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neu.mr.commons.Command;
import neu.mr.commons.CommandEnum;

/**
 * The ackListener class for our client that listens to the discovery packet that
 * the server sends
 * 
 * @author chintanpathak, Abhishek Ravichandran
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
						LOGGER.info("command received:" + command.getName().toString());
						sendDiscoveryAck(receivedPacket);
						discovered = true;
						socket.close();
					}
				} catch (IOException e) {
					LOGGER.error("exception when getting discovery packet", e);
				}
			}
		}
		
		/**
		 * Sends back the discovery ack with the port
		 * on which this client is hearing for commands from
		 * the server
		 * @param packet
		 */
		private void sendDiscoveryAck(DatagramPacket packet) {
			try {
				serverInfo.address = packet.getAddress();
				serverInfo.portNumber = packet.getPort();
				LOGGER.info("Address - " + serverInfo.address.getHostAddress());
				LOGGER.info("Port - " + serverInfo.portNumber);
				
				Command discoveryAck = new Command();
				discoveryAck.setName(CommandEnum.DISCOVER_ACK);
				List<String> params = new ArrayList<String>();
				params.add("54321");
				discoveryAck.setParams(params);
				byte[] reply = SerializationUtils.serialize(discoveryAck);
				DatagramPacket discoveryAckPacket = 
						new DatagramPacket(reply, reply.length, serverInfo.address, serverInfo.portNumber);
				socket.send(discoveryAckPacket);
			} catch (IOException e) {
				LOGGER.error("exception when sending ack", e);
			}
		}

	}
}
