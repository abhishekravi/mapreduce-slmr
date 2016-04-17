package neu.mr.job;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neu.mr.server.ConnectedClient;

/**
 * 
 * @author chintanpathak
 *
 */
public class JobScheduler {
	
	private static Logger LOGGER = LoggerFactory.getLogger(JobScheduler.class);

	private List<ConnectedClient> connectedClients;
	private Map<String, Job> jobMap;
	public static Queue<Job> jobQueue;
	private Thread schedulerThread;
	private List<String> listOfInputFiles;

	public JobScheduler() {
		jobQueue = new LinkedList<Job>();
		jobMap = new HashMap<String,Job>();
	}

	public JobScheduler(List<ConnectedClient> connectedClients) {
		this.connectedClients = connectedClients;
		jobQueue = new LinkedList<Job>();
		jobMap = new HashMap<String,Job>();
	}

	public void startScheduling() {
		schedulerThread = new Thread(new JobSchedulerRunnable());
		schedulerThread.start();
	}

	private class JobSchedulerRunnable implements Runnable {

		@Override
		public void run() {
			while (true) {
				removeDeadClients();
				for (ConnectedClient client : connectedClients) {
					if (!client.busy && !jobQueue.isEmpty()) {
						Job job = jobQueue.poll();
						client.assignedJobs.add(job);
						jobMap.put(client.address.getHostAddress(), job);
						client.sendExecuteCommand();
					}
				}
			}
		}

		/**
		 * method to remove dead clients.
		 */
		private void removeDeadClients() {
			Iterator<ConnectedClient> it = connectedClients.iterator();
			while(it.hasNext()){
				if(!it.next().alive){
					LOGGER.info("removing client");
					it.remove();
				}
			}
		}
	}

	public void populateJobQueue(Job userJob, int numOfMapTasks) {
		Job job;
		job = new Job(userJob);
		List<Job> jobs = new ArrayList<Job>();
		populateListOfInputFiles(job.getInputDirectoryPath());
		for (int i = 0; i < numOfMapTasks; i++) {
			jobs.add(new Job(userJob));
		}
		for (int i = 0; i < listOfInputFiles.size(); i++) {
			jobs.get(i % jobs.size()).getListOfInputFiles().add(listOfInputFiles.get(i));
		}
		for (Job splitJob : jobs) {
			jobQueue.add(splitJob);
		}
	}

	private void populateListOfInputFiles(String inputDirectoryPath) {
		try {
			listOfInputFiles = new ArrayList<String>();
			File inputDirectory = new File(inputDirectoryPath);
			if (inputDirectory.exists()) {
				File[] listOfFiles = inputDirectory.listFiles(new FileFilter() {

					@Override
					public boolean accept(File pathname) {
						return !pathname.getName().startsWith(".");
					}
				});
				for (File file : listOfFiles) {
					listOfInputFiles.add(file.getCanonicalPath());
				}
			}
		} catch (IOException e) {
			// Put logger error message
		}
	}

	public List<ConnectedClient> getConnectedClients() {
		return connectedClients;
	}

	public void setConnectedClients(List<ConnectedClient> connectedClients) {
		this.connectedClients = connectedClients;
	}
}
