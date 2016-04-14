package neu.mr.commons;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neu.mr.client.ServerInfo;
import neu.mr.server.ConnectedClient;

/**
 * Enumeration class for different commands
 * 
 * @author chintanpathak
 *
 */
public enum CommandEnum implements CommandExecutor {

	/**
	 * Send back a discovery ack, populate the serverInfo object
	 * and start a TCP connection with the server
	 */
	DISCOVER("discover") {
		@Override
		public void run() {
			DatagramPacket packet = (DatagramPacket) parameters.get(0);
			ServerInfo serverInfo = (ServerInfo) parameters.get(1);
			DatagramSocket socket = (DatagramSocket) parameters.get(2);
			CommandExecutorUtils.initializeServer(serverInfo, packet);
			CommandExecutorUtils.sendDiscoveryAck(serverInfo, socket);
			socket.close();
			serverInfo.startTcpConnectionWithServer();
		}
	},
	/**
	 * Create a new connected client, start a TCP connection with it and
	 * add it to the connected-clients list inside the server
	 */
	DISCOVER_ACK("discover_ack") {
		@Override
		public void run() {
			DatagramPacket replyPacket = (DatagramPacket) parameters.get(0);
			@SuppressWarnings("unchecked")
			List<ConnectedClient> connectedClients = (List<ConnectedClient>) parameters.get(1);

			ConnectedClient client = new ConnectedClient();
			Command replyCommand = (Command) SerializationUtils.deserialize(replyPacket.getData());
			client.address = replyPacket.getAddress();
			client.portNumber = Integer.parseInt(replyCommand.params.get(0));
			client.alive = true;
			client.startTcpConnectionWithClient();
			connectedClients.add(client);
			LOGGER.info("client address:" + replyPacket.getAddress().getHostAddress());
			LOGGER.info("client port:" + replyPacket.getPort());
		}
	};

	private static Logger LOGGER = LoggerFactory.getLogger(CommandEnum.class);
	public List<Object> parameters;
	private final String text;

	/**
	 * @param text
	 */
	private CommandEnum(final String text) {
		this.text = text;
		parameters = new ArrayList<Object>();
	}

	@Override
	public String toString() {
		return text;
	}

}
