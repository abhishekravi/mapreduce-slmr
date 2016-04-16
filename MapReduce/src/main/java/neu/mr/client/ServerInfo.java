package neu.mr.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neu.mr.commons.Command;

/**
 * Class that holds server information.
 * 
 * @author Abhishek Ravichandran
 *
 */
public class ServerInfo {

	private static Logger LOGGER = LoggerFactory.getLogger(ServerInfo.class);

	public InetAddress address;
	public int portNumber;
	public boolean alive;
	private ServerSocket serverSocket;
	private Socket connection;
	private BufferedInputStream in;
	private BufferedOutputStream out;
	private Thread commandListener;

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
			in = new BufferedInputStream(connection.getInputStream());
			out = new BufferedOutputStream(connection.getOutputStream());

			LOGGER.info("Client created input/output streams to the client on tcp");
			commandListener = new Thread(new CommandListener());
			commandListener.start();
		} catch (IOException e) {
			LOGGER.error("Exception starting a TCP connection with the server", e);
		}
	}

	/**
	 * Runnable class that hears to the commands that the server would send on
	 * the tcp connection
	 * 
	 * @author chintanpathak
	 *
	 */
	private class CommandListener implements Runnable {
		Command command;
		byte[] packet = new byte[512];

		@Override
		public void run() {
			while (alive) {
				try {
					in.read(packet);
					command = (Command) SerializationUtils.deserialize(packet);
					LOGGER.info("Received command from server " + command);
					command.getName().parameters.add(getserver());
					command.getName().run();
				} catch (IOException e) {
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
			out.write(SerializationUtils.serialize(command));
			out.flush();
		} catch (IOException e) {
			LOGGER.error("IOException while writing to output stream in ConnectedClient", e);
		}
	}
}
