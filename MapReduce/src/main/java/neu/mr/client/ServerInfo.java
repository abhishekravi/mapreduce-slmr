package neu.mr.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

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
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Thread commandListener;

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
			serverSocket = new ServerSocket(54321);
			connection = serverSocket.accept();
			in = new ObjectInputStream(connection.getInputStream());
			out = new ObjectOutputStream(connection.getOutputStream());
			commandListener = new Thread(new CommandListener());
			commandListener.start();
		} catch (IOException e) {
			LOGGER.error("Exception starting a TCP connection with the server", e);
		}
	}

	/**
	 * Runnable class that hears to the commands that 
	 * the server would send on the tcp connection
	 * @author chintanpathak
	 *
	 */
	private class CommandListener implements Runnable {
		Command command;

		@Override
		public void run() {
			while (alive) {
				try {
					command = (Command) in.readObject();
					command.getName().run();
				} catch (IOException e) {
					LOGGER.error("IOException while reading from input stream in ServerInfo", e);
				} catch (ClassNotFoundException e) {
					LOGGER.error("ClassNotFoundException while reading from input stream in ServerInfo", e);
				}
			}
		}
	}
	
	/**
	 * Send a command to the server
	 * @param command
	 */
	public void writeToOutputStream(Command command){
		try {
			out.writeObject(command);
			out.flush();
		} catch (IOException e) {
			LOGGER.error("IOException while writing to output stream in ServerInfo", e);
		}
	}
}
