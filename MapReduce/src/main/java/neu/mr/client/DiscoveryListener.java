package neu.mr.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import neu.mr.commons.CommandEnum;

/**
 * The listener class for our client that listens to the
 * discovery packet that the server sends
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
	 * Start the listener
	 */
	public void start() {
		listener.start();
	}

	/**
	 * Runnable class for the listener with the run() method
	 * 
	 * @author chintanpathak
	 *
	 */
	private class ListenerRunnable implements Runnable {

		DatagramPacket receivedPacket;
		byte[] buf = new byte[256];
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
			String stringPacket;

			while (!discovered) {
				try {
					socket.receive(receivedPacket);
					stringPacket = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
					System.out.println(stringPacket);
					if (CommandEnum.DISCOVER.toString().equals(stringPacket)) {
						discovered = true;
						socket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
