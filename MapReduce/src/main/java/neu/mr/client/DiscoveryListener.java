package neu.mr.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;

import neu.mr.commons.Command;
import neu.mr.commons.CommandEnum;

/**
 * The ackListener class for our client that listens to the discovery packet that
 * the server sends
 * 
 * @author chintanpathak
 *
 */
public class DiscoveryListener {

	Thread listener;

	public DiscoveryListener() {
		listener = new Thread(new ListenerRunnable());
	}

	/**
	 * Start the ackListener
	 */
	public void start() {
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

		/**
		 * Initialize the socket and a packet to receive message into
		 */
		public ListenerRunnable() {
			try {
				socket = new DatagramSocket(54321);
			} catch (SocketException e) {
				e.printStackTrace();
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
						System.out.println(command);
						sendDiscoveryAck(receivedPacket);
						socket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
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
			InetAddress address;
			try {
				address = packet.getAddress();
				int port = packet.getPort();
				System.out.println("Address - " + address.getHostAddress());
				System.out.println("Port - " + port);
				
				Command discoveryAck = new Command();
				discoveryAck.setName(CommandEnum.DISCOVER_ACK);
				List<String> params = new ArrayList<String>();
				params.add("54321");
				discoveryAck.setParams(params);
				byte[] reply = SerializationUtils.serialize(discoveryAck);
				DatagramPacket discoveryAckPacket = new DatagramPacket(reply, reply.length, address, port);
				socket.send(discoveryAckPacket);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
