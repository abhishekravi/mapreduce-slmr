package neu.mr.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
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
		speakerTask = new TimerTask() {

			Command discoverCommand = new Command();
			DatagramPacket discoverMsg = null;

			{
				discoverCommand.setName(CommandEnum.DISCOVER);
				List<String> params = new ArrayList<String>();
				params.add(NetworkUtils.getIpAddress().getHostAddress());
				params.add("12345");
				discoverCommand.setParams(params);
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
