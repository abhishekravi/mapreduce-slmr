package neu.mr.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import neu.mr.commons.CommandEnum;

/**
 * Server side class that is responsible for periodically sending the discovery
 * packets to the possible clients
 * 
 * @author chintanpathak
 *
 */
public class Discovery {

	Thread listener;
	TimerTask speakerTask;
	Timer speaker;
	DatagramSocket broadcastSocket;

	/**
	 * Initialize the speaker & listener threads. Also create a scheduled task
	 * to periodically send broadcast messages for discovery of clients.
	 */
	public Discovery() {
		speaker = new Timer();
		listener = new Thread(new ListenerRunnable());
		createDatagramConnection();
		try {
			speakerTask = new TimerTask() {

				// To-do: Port the command to use Command object
				byte[] buf = CommandEnum.DISCOVER.toString().getBytes();
				DatagramPacket discovermsg = new DatagramPacket(buf, buf.length, InetAddress.getByName("10.42.0.255"),
						54321);

				@Override
				public void run() {
					try {
						broadcastSocket.send(discovermsg);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Start the speaker thread and start sending the broadcast messages at
	 * fixed time intervals
	 */
	public void start() {
		speaker.schedule(speakerTask, 0, 500);
		listener.start();
	}

	/**
	 * Runnable class for the listener thread
	 * 
	 * @author chintanpathak
	 *
	 */
	private class ListenerRunnable implements Runnable {

		public void run() {
		}

	}

	/**
	 * Initializes the broadcast socket on a dedicated port
	 */
	private void createDatagramConnection() {
		try {
			broadcastSocket = new DatagramSocket();
			broadcastSocket.setBroadcast(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
