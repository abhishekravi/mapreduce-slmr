package neu.mr.job;

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
import neu.mr.utils.AwsUtil;

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
		jobMap = new HashMap<String, Job>();
	}

	public JobScheduler(List<ConnectedClient> connectedClients) {
		this.connectedClients = connectedClients;
		jobQueue = new LinkedList<Job>();
		jobMap = new HashMap<String, Job>();
	}

	public void startScheduling() {
		schedulerThread = new Thread(new JobSchedulerRunnable());
		schedulerThread.start();
	}

	private class JobSchedulerRunnable implements Runnable {

		@Override
		public void run() {
			while (true) {
				synchronized (connectedClients) {
					LOGGER.info("num of clients:" + connectedClients.size());
					LOGGER.info("num of jobs:" + jobQueue.size());
					for (ConnectedClient client : connectedClients) {
						LOGGER.info("client " + client.address.getHostAddress() + " busy?:" + client.busy);
						if (!client.busy && !jobQueue.isEmpty()) {
							Job job = jobQueue.poll();
							client.assignedJobs.add(job);
							client.busy = true;
							jobMap.put(client.address.getHostAddress(), job);
							client.sendExecuteCommand();
						}
					}
					removeDeadClients();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		/**
		 * method to remove dead clients.
		 */
		private void removeDeadClients() {
			Iterator<ConnectedClient> it = connectedClients.iterator();
			while (it.hasNext()) {
				ConnectedClient client = it.next();
				if (!client.alive) {
					jobQueue.add(jobMap.get(client.address.getHostAddress()));
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
		listOfInputFiles = new ArrayList<String>();
		AwsUtil awsUtil = new AwsUtil("AKIAJG5UIGP6SQUW7OBA", "+fIVd3W1Ou5Jsal/8cV9TI+h341FJN2mF3Vr9fpD");
		listOfInputFiles.addAll(awsUtil.getFileList("pdmrbucket", "blah"));
	}

	public List<ConnectedClient> getConnectedClients() {
		return connectedClients;
	}

	public void setConnectedClients(List<ConnectedClient> connectedClients) {
		this.connectedClients = connectedClients;
	}
}
