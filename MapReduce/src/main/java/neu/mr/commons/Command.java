package neu.mr.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import neu.mr.job.Job;

/**
 * Class to hold the command information that a server and client would use for
 * communication
 * 
 * @author chintanpathak, Abhsihek Ravichandran
 *
 */
public class Command implements Serializable {

	private static final long serialVersionUID = 1L;

	CommandEnum name;
	List<String> params;
	List<Job> jobs;

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public Command(CommandEnum name) {
		this.name = name;
		jobs = new ArrayList<Job>();
		params = new ArrayList<String>();
	}

	public Command() {
		jobs = new ArrayList<Job>();
		params = new ArrayList<String>();
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
