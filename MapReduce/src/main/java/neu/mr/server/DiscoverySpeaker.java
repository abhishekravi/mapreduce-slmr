package neu.mr.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.SerializationUtils;

import neu.mr.commons.Command;
import neu.mr.commons.CommandEnum;
import neu.mr.utils.NetworkUtils;

/**
 * Server side class that is responsible for periodically sending the discovery
 * packets to the possible clients
 * 
 * @author chintanpathak
 *
 */
public class DiscoverySpeaker {

	Thread ackListener;
	TimerTask speakerTask;
	Timer speaker;
	DatagramSocket broadcastSocket;

	/**
	 * Initialize the speaker & ackListener threads. Also create a scheduled task
	 * to periodically send broadcast messages for discovery of clients.
	 */
	public DiscoverySpeaker() {
		createDatagramConnection();
		speaker = new Timer();
		ackListener = new Thread(new ListenerRunnable());
		speakerTask = new TimerTask() {

			Command discoverCommand = new Command();
			DatagramPacket discoverMsg = null;

			{
				discoverCommand.setName(CommandEnum.DISCOVER);
				byte[] buf = SerializationUtils.serialize(discoverCommand);
				discoverMsg = new DatagramPacket(buf, buf.length, NetworkUtils.getBroadcastIpAddress(), 54321);
			}

			@Override
			public void run() {
				try {
					broadcastSocket.send(discoverMsg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

	}

	/**
	 * Start the speaker thread and start sending the broadcast messages at
	 * fixed time intervals
	 */
	public void start() {
		ackListener.start();
		speaker.schedule(speakerTask, 0, 500);
	}

	/**
	 * Runnable class for the ackListener thread.
	 * It listens to the acknowledgement sent
	 * by any possibly discovered client
	 * 
	 * @author chintanpathak
	 *
	 */
	private class ListenerRunnable implements Runnable {

		public void run() {
			try {
				// DatagramSocket socket = new DatagramSocket();
				byte[] buf = new byte[1024];
				DatagramPacket replypacket = new DatagramPacket(buf, buf.length);
				Command replyCommand;
				while(true){
					broadcastSocket.receive(replypacket);
					replyCommand = (Command) SerializationUtils.deserialize(replypacket.getData());
					System.out.println(replypacket.getAddress());
					System.out.println(replypacket.getPort());
					System.out.println(replyCommand);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
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
