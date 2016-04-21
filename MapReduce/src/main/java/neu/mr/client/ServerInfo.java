package neu.mr.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neu.mr.commons.Command;
import neu.mr.utils.AwsUtil;

/**
 * Class that holds server information.
 * 
 * @author Abhishek Ravichandran
 *
 */
public class ServerInfo {

	/**
	 * initializing server info.
	 * 
	 * @param serverAddress
	 *            address if available
	 * @param awsUtil
	 *            aws util class
	 */
	public ServerInfo(String serverAddress, AwsUtil awsUtil) {
		this.awsUtil = awsUtil;
		if (!serverAddress.isEmpty())
			try {
				this.address = InetAddress.getByName(serverAddress);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
	}

	private static Logger LOGGER = LoggerFactory.getLogger(ServerInfo.class);

	public InetAddress address;
	public int portNumber;
	public boolean alive;
	private ServerSocket serverSocket;
	private Socket connection;
	private BufferedInputStream in;
	private ObjectInputStream oin;
	private BufferedOutputStream out;
	private ObjectOutputStream oout;
	private Thread commandListener;
	public AwsUtil awsUtil;

	public ServerInfo getserver() {
		return this;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	/**
	 * Start a tcp connection with the server
	 */
	public void startTcpConnectionWithServer() {
		try {
			serverSocket = new ServerSocket(54322);
			connection = serverSocket.accept();
			out = new BufferedOutputStream(connection.getOutputStream());
			oout = new ObjectOutputStream(out);
			oout.flush();
			in = new BufferedInputStream(connection.getInputStream());
			oin = new ObjectInputStream(in);
			LOGGER.info("Client created input/output streams to the client on tcp");
			commandListener = new Thread(new CommandListener());
			commandListener.start();
		} catch (IOException e) {
			LOGGER.error("Exception starting a TCP connection with the server", e);
		}
	}

	/**
	 * Runnable class that hears to the commands that the server would send on
	 * the tcp connection.
	 * 
	 * @author chintanpathak
	 *
	 */
	private class CommandListener implements Runnable {
		Command command;

		@Override
		public void run() {
			while (alive) {
				try {
					command = (Command) oin.readObject();
					LOGGER.info("Received command from server " + command);
					command.getName().parameters = new ArrayList<Object>();
					command.getName().parameters.add(getserver());
					if (null != command.getJobs())
						command.getName().parameters.add(command.getJobs());
					command.getName().run();
				} catch (IOException | ClassNotFoundException e) {
					LOGGER.error("IOException while reading from input stream in ServerInfo", e);
				}
			}
		}
	}

	/**
	 * Send a command to the client
	 * 
	 * @param command
	 */
	public void writeToOutputStream(Command command) {
		try {
			oout.writeObject(command);
			oout.flush();
		} catch (IOException e) {
			LOGGER.error("IOException while writing to output stream in ConnectedClient", e);
		}
	}

	/**
	 * cleanup method for termination.
	 */
	public void cleanUp() {
		try {
			alive = false;
			oin.close();
			oout.close();
			connection.close();
			serverSocket.close();
		} catch (IOException e) {
			LOGGER.error("when terminating client", e);
		}
	}
}
