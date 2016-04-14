package neu.mr.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.commons.lang.SerializationUtils;
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
	private InputStream in;
	private OutputStream out;
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
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			LOGGER.info("Server created input/output streams to the client on tcp");
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
		byte[] packet = new byte[1024];
		
		@Override
		public void run() {
			while (alive) {
				try {
					in.read(packet);
					command = (Command) SerializationUtils.deserialize(packet);
					LOGGER.info("Received command from client " + command);
					command.getName().run();
				} catch (IOException e) {
					LOGGER.error("IOException while reading from input stream in ConnectedClient", e);
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
			out.write(SerializationUtils.serialize(command));
			out.flush();
		} catch (IOException e) {
			LOGGER.error("IOException while writing to output stream in ConnectedClient", e);
		}
	}

}
