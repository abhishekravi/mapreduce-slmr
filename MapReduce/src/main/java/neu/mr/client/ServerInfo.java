package neu.mr.client;

import java.net.InetAddress;

/**
 * Class that holds server information.
 * @author Abhishek Ravichandran
 *
 */
public class ServerInfo {

	InetAddress address;
	int portNumber;
	boolean alive;

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
