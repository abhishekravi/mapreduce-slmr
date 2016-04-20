package neu.mr.commons;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neu.mr.client.ServerInfo;
import neu.mr.job.Job;
import neu.mr.job.JobRunner;
import neu.mr.job.JobScheduler;
import neu.mr.server.ConnectedClient;

/**
 * Enumeration class for different commands
 * 
 * @author chintanpathak, Abhishek Ravichandran
 *
 */
public enum CommandEnum implements CommandExecutor {

	/**
	 * Send back a discovery ack, populate the serverInfo object and start a TCP
	 * connection with the server
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
	 * Create a new connected client, start a TCP connection with it and add it
	 * to the connected-clients list inside the server
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
			synchronized (connectedClients) {
				connectedClients.add(client);
			}
			LOGGER.info("discovery client address:" + replyPacket.getAddress().getHostAddress());
			LOGGER.info("client port:" + replyPacket.getPort());
		}
	},

	/**
	 * Heartbeat command send by server to check status of client.
	 */
	HEARTBEAT("status_check") {
		@Override
		public void run() {
			ServerInfo server = (ServerInfo) parameters.get(0);
			Command c = new Command();
			c.setName(HEARTBEAT_ACK);
			server.writeToOutputStream(c);
		}
	},

	/**
	 * Heartbeat acknowledgement command from the client.
	 */
	HEARTBEAT_ACK("status_ack") {
		@Override
		public void run() {
			ConnectedClient c = (ConnectedClient) parameters.get(0);
			c.setLastCommTime(System.currentTimeMillis());
			LOGGER.info("client alive");
		}
	},
	/**
	 * Command for job execution
	 */
	EXECUTE("execute_job") {

		@Override
		public void run() {
			LOGGER.info("Execute command with parameter list:" + parameters);
			ServerInfo server = (ServerInfo) parameters.get(0);
			@SuppressWarnings("unchecked")
			List<Job> list = (List<Job>) parameters.get(1);
			Job jobToRun = list.get(0);
			Command c = new Command();
			c.setName(EXECUTE_ACK);
			server.writeToOutputStream(c);
			LOGGER.info("Job list size:" + list.size());
			LOGGER.info("Job type:" + jobToRun.getType().name().toString());
			LOGGER.info("Executing job with list of files" + jobToRun.getListOfInputFiles());
			Set<String> returnValues = JobRunner.runJob(jobToRun);
			c = new Command();
			c.setName(EXECUTE_COMPLETE);
			c.getParams().add(String.valueOf(jobToRun.getId()));
			c.getParams().addAll(returnValues);
			LOGGER.info("Completed job: sending back keys-" + returnValues);
			server.writeToOutputStream(c);
		}
	},
	EXECUTE_ACK("execute_ack") {
		@Override
		public void run() {
			ConnectedClient c = (ConnectedClient) parameters.get(0);
			c.running = true;
		}

	},
	EXECUTE_COMPLETE("execute_complete") {
		@Override
		public void run() {
			ConnectedClient c = (ConnectedClient) parameters.get(0);
			parameters.remove(0);
			JobScheduler.markJobAsComplete(Long.parseLong(String.valueOf(parameters.get(0))));
			parameters.remove(0);
			String stringKey;
			LOGGER.info("Client " + c.getAddress().getHostAddress() + " wrote files -");
			for (Object keyFromClient : parameters) {
				stringKey = String.valueOf(keyFromClient);
				stringKey = stringKey.substring(0, stringKey.lastIndexOf("~"));
				JobScheduler.reduceKeys.add(stringKey);
				LOGGER.info(stringKey);
			}
			c.running = false;
			c.busy = false;
			c.assignedJobs = new ArrayList<Job>();
			LOGGER.info("updating client " + c.address.getHostAddress() + " busy:" + c.busy);
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
