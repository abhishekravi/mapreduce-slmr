package neu.mr.commons;

import java.io.Serializable;
import java.util.List;

/**
 * Class to hold the command information that a server and client would use for
 * communication
 * 
 * @author chintanpathak
 *
 */
public class Command implements Serializable {

	private static final long serialVersionUID = 1L;

	CommandEnum name;
	List<String> params;

	public Command(CommandEnum name) {
		this.name = name;
	}
	
	public Command() {
	}

	public CommandEnum getName() {
		return name;
	}

	public void setName(CommandEnum name) {
		this.name = name;
	}

	public List<String> getParams() {
		return params;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return name + " : " + params;
	}
}
