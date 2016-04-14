package neu.mr.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neu.mr.commons.Command;

/**
 * This class represents a connected client.
 * 
 * @author Abhishek Ravichandran
 *
 */
public class ConnectedClient {

	private static Logger LOGGER = LoggerFactory.getLogger(ConnectedClient.class);
	
	public InetAddress address;
	public int portNumber;
	public boolean alive;
	private Socket socket;
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
	 * Start a tcp connection with the new discovered client
	 */
	public void startTcpConnectionWithClient() {
		try {
			socket = new Socket(address, portNumber);
			
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			commandListener = new Thread(new CommandListener());
			commandListener.start();
		} catch (IOException e) {
			LOGGER.error("Exception starting a TCP connection with the server", e);
		}
	}

	/**
	 * Runnable class for the listener thread that hears to the 
	 * commands that the client would send on the tcp connection
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
					System.out.println("Got command " + command.getName().toString());
					command.getName().run();
				} catch (IOException e) {
					LOGGER.error("IOException while reading from input stream in ConnectedClient", e);
				} catch (ClassNotFoundException e) {
					LOGGER.error("ClassNotFoundException while reading from input stream in ConnectedClient", e);
				}
			}
		}
	}

	/**
	 * Send a command to the client
	 * @param command
	 */
	public void writeToOutputStream(Command command) {
		try {
			out.writeObject(command);
			out.flush();
		} catch (IOException e) {
			LOGGER.error("IOException while writing to output stream in ConnectedClient", e);
		}
	}

}
