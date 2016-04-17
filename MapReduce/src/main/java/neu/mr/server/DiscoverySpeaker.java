package neu.mr.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neu.mr.commons.Command;
import neu.mr.commons.CommandEnum;
import neu.mr.utils.NetworkUtils;

/**
 * Server side class that is responsible for periodically sending the discovery
 * packets to the possible clients.
 * 
 * @author chintanpathak, Abhishek Ravichandran
 *
 */
public class DiscoverySpeaker {

	private static Logger LOGGER = LoggerFactory.getLogger(DiscoverySpeaker.class);
	Thread ackListener;
	TimerTask speakerTask;
	Timer speaker;
	DatagramSocket broadcastSocket;
	List<ConnectedClient> connectedClients;

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
					LOGGER.error("exception when sending broadcast message", e);
				}
			}
		};

	}

	/**
	 * Start the speaker thread and start sending the broadcast messages at
	 * fixed time intervals
	 * @param clients 
	 */
	public void start(List<ConnectedClient> connectedClients) {
		ackListener.start();
		this.connectedClients = connectedClients;
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
					replyCommand.getName().parameters.add(replypacket);
					replyCommand.getName().parameters.add(connectedClients);
					replyCommand.getName().run();
					LOGGER.info("reply message:" + replyCommand);
				}
			} catch (IOException e) {
				LOGGER.error("exception when sending broadcast message", e);
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
			LOGGER.error("exception when creating datagram socket", e);
		}
	}
}
