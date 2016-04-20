package neu.mr.commons;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neu.mr.client.ServerInfo;

/**
 * Helper methods for command execution
 * 
 * @author chintanpathak
 *
 */
public class CommandExecutorUtils {

	private static Logger LOGGER = LoggerFactory.getLogger(CommandExecutorUtils.class);

	/**
	 * Sends back the discovery ack with the port on which this client is
	 * hearing for commands from the server
	 * 
	 * @param packet
	 */
	public static void sendDiscoveryAck(ServerInfo serverInfo, DatagramSocket socket) {
		try {

			LOGGER.info("Address - " + serverInfo.address.getHostAddress());
			LOGGER.info("Port - " + serverInfo.portNumber);

			Command discoveryAck = new Command();
			discoveryAck.setName(CommandEnum.DISCOVER_ACK);
			List<String> params = new ArrayList<String>();
			params.add("54322");
			discoveryAck.setParams(params);
			byte[] reply = SerializationUtils.serialize(discoveryAck);
			DatagramPacket discoveryAckPacket = new DatagramPacket(reply, reply.length, serverInfo.address,
					serverInfo.portNumber);
			socket.send(discoveryAckPacket);
		} catch (IOException e) {
			LOGGER.error("exception when sending ack", e);
		}
	}

	/**
	 * Initializes the serverInfo object with the address and port number of the
	 * server
	 * 
	 * @param serverInfo
	 * @param packet
	 */
	public static void initializeServer(ServerInfo serverInfo, DatagramPacket packet) {
		if(serverInfo.address == null)
			serverInfo.address = packet.getAddress();
		serverInfo.alive = true;
		serverInfo.portNumber = 9001;
	}
}
