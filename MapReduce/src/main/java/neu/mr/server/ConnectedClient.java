package neu.mr.server;

import java.net.InetAddress;

import neu.mr.commons.CommanExecutor;

/**
 * This class represents a connected client.
 * @author Abhishek Ravichandran
 *
 */
public class ConnectedClient {

	InetAddress address;
	int portNumber;
	boolean alive;
	Thread executor = new Thread(new CommanExecutor());
	

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

}
