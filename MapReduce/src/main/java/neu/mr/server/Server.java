package neu.mr.server;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class for the server
 * @author chintanpathak, Abhishek Ravichandran
 *
 */
public class Server {

	List<ConnectedClient> connectedClients;
	DiscoverySpeaker discovery;
	
	/**
	 * constructor to initialize the server
	 */
	public Server(){
		this.discovery = new DiscoverySpeaker();
		this.connectedClients = new ArrayList<ConnectedClient>();
	}
	
	/**
	 * method to start the server.
	 */
	public void execute () {
		this.discovery.start(connectedClients);
		// Now start sending the required commands to execute 
		// jobs on the client machine here
		// --------- What comes here?
		// Job queue comes here 
		// Scheduling jobs
		// Heartbeat checking
		// etc...
		// --------------------------
		// Use connectedClients.writeToOutputStream to
		// send a command and add command implementations on
		// client side to decide what is executed when this 
		// command is received
		// - Clients hear automatically and executes the command's
		// run method whenever they hear a command 
		
		// To-do: Refactor command execution parameters in the enum 
		// to take directly from command object
		
		// Possible options:
		// 1) map command with mapper obj and file in it's parameters
		// 2) reduce command with reducer obj and file in it's parameters
		// 3) shuffle command with the location of files and keys to shuffle in 
		// 	  it's parameters
	}

}