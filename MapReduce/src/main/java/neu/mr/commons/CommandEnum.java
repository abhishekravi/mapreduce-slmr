package neu.mr.commons;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

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
			Command blah = new Command();
			blah.setName(CommandEnum.BLAH);
			serverInfo.writeToOutputStream(blah);
		}
	},
	DISCOVER_ACK("discover_ack") {
		@Override
		public void run() {
			DatagramPacket replyPacket = (DatagramPacket) parameters.get(0);
			@SuppressWarnings("unchecked")
			List<ConnectedClient> connectedClients = (List<ConnectedClient>) parameters.get(1);

			ConnectedClient client = new ConnectedClient();
			client.address = replyPacket.getAddress();
			client.portNumber = replyPacket.getPort();
			client.alive = true;
			client.startTcpConnectionWithClient();
			connectedClients.add(client);
			LOGGER.info("client address:" + replyPacket.getAddress().getHostAddress());
			LOGGER.info("client port:" + replyPacket.getPort());
		}
	},
	BLAH("Connected!"){
		@Override
		public void run() {
			System.out.println("Ohk buddy we are connected");
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
