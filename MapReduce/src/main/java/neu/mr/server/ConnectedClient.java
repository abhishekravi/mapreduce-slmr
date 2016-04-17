package neu.mr.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neu.mr.commons.Command;
import neu.mr.commons.CommandEnum;
import neu.mr.job.Job;

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
	private BufferedInputStream in;
	private ObjectInputStream oin;
	private BufferedOutputStream out;
	private ObjectOutputStream oout;
	private Thread commandListener;
	private long lastCommTime = System.currentTimeMillis();
	private TimerTask heartBeatTask;
	private Timer heartBeatTimer;
	private Command heartBeat;
	public boolean busy = false;
	public boolean running = false;
	public List<Job> assignedJobs;

	public ConnectedClient() {
		assignedJobs = new ArrayList<Job>();
	}

	/**
	 * method to start heart beat mechanism.
	 */
	public void startHeartBeat() {
		heartBeat = new Command();
		heartBeat.setName(CommandEnum.HEARTBEAT);
		heartBeatTimer = new Timer();
		heartBeatTask = new TimerTask() {
			@Override
			public void run() {
				long timeSinceComm = (System.currentTimeMillis() - lastCommTime) / 1000;
				LOGGER.info("time:" + timeSinceComm);
				if (timeSinceComm >= 35) {
					alive = false;
					heartBeatTask.cancel();
				}
				writeToOutputStream(heartBeat);
				LOGGER.info("heartbeat");
			}
		};
		heartBeatTimer.schedule(heartBeatTask, 5000, 30000);
	}

	public ConnectedClient getClient() {
		// TODO Auto-generated method stub
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

	public long getLastCommTime() {
		return lastCommTime;
	}

	public void setLastCommTime(long lastCommTime) {
		this.lastCommTime = lastCommTime;
	}

	/**
	 * Start a tcp connection with the new discovered client
	 */
	public void startTcpConnectionWithClient() {
		try {
			socket = new Socket(address, portNumber);
			out = new BufferedOutputStream(socket.getOutputStream());
			oout = new ObjectOutputStream(out);
			oout.flush();
			in = new BufferedInputStream(socket.getInputStream());
			oin = new ObjectInputStream(in);
			LOGGER.info("Server created input/output streams to the client on tcp");
			commandListener = new Thread(new CommandListener());
			commandListener.start();
			this.startHeartBeat();
		} catch (IOException e) {
			LOGGER.error("Exception starting a TCP connection with the server", e);
		}
	}

	/**
	 * Runnable class for the listener thread that hears to the commands that
	 * the client would send on the tcp connection
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
					lastCommTime = System.currentTimeMillis();
					LOGGER.info("Received command from client " + command);
					command.getName().parameters.add(getClient());
					command.getName().run();
				} catch (IOException | ClassNotFoundException e) {
					alive = false;
					LOGGER.error("IOException while reading from input stream in ConnectedClient", e);
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
			if (alive) {
				oout.writeObject(command);
				oout.flush();
			}
		} catch (IOException e) {
			LOGGER.error("IOException while writing to output stream in ConnectedClient", e);
		}
	}

	/**
	 * Send the execute command to this client with the job object as it's
	 * payload
	 */
	public void sendExecuteCommand() {
		if (!assignedJobs.isEmpty()) {
			Command execute = new Command(CommandEnum.EXECUTE);
			execute.setJobs(assignedJobs);
			writeToOutputStream(execute);
		} else {
			LOGGER.error("Cannot send execute command to client - " + address.getHostAddress()
					+ " as the assignedJobs list is empty for it");
		}
	}

}
