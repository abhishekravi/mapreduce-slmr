package neu.mr.job;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neu.mr.commons.Command;
import neu.mr.commons.CommandEnum;
import neu.mr.server.ConnectedClient;
import neu.mr.server.DiscoverySpeaker;
import neu.mr.utils.AwsUtil;

/**
 * Job scheduler class that takes care of assigning clients jobs
 * and fault tolerance by making sure all jobs are run. If a client fails
 * the job is reassigned to the next free client.
 * 
 * @author Chintan Pathak, Abhishek Ravichandran, Chinmayee Vaidya
 *
 */
public class JobScheduler {

	private static Logger LOGGER = LoggerFactory.getLogger(JobScheduler.class);

	private List<ConnectedClient> connectedClients;
	private Map<String, Job> assignedJobMap;
	public static List<Job> pipeLine;
	public static Queue<Job> jobQueue;
	private Thread schedulerThread;
	private List<String> listOfInputFiles;
	public static Set<String> reduceKeys;
	public static Set<Long> finishedJobs;
	private boolean jobComplete = false;
	private boolean reducerStarted = false;
	private int numOfMapTasks = 1;
	private int numOfReduceTasks = 1;
	private Job userJob;
	private long idCounter = 0;
	private AwsUtil awsUtil;

	/**
	 * initializing job scheduler.
	 * @param userJob
	 * user created job
	 * @param awsid
	 * awsid
	 * @param awskey
	 * awskey
	 */
	public JobScheduler(Job userJob, String awsid, String awskey) {
		this.userJob = userJob;
		jobQueue = new LinkedList<Job>();
		assignedJobMap = new HashMap<String, Job>();
		reduceKeys = new HashSet<String>();
		finishedJobs = new HashSet<Long>();
		this.awsUtil = new AwsUtil(awsid, awskey);
		setNumOfMapTasks(userJob.getNumOfMapTasks());
		setNumOfReduceTasks(userJob.getNumOfReduceTasks());
		populateMapJobs();
	}

	/**
	 * start distributing the jobs.
	 * @param discovery 
	 */
	public void startScheduling(DiscoverySpeaker discovery) {
		schedulerThread = new Thread(new JobSchedulerRunnable(discovery));
		schedulerThread.start();
	}

	/**
	 * The thread that distributes the jobs.
	 * @author Abhishek Ravichandran
	 *
	 */
	private class JobSchedulerRunnable implements Runnable {

		DiscoverySpeaker discovery;
		public JobSchedulerRunnable(DiscoverySpeaker discovery) {
			this.discovery = discovery;
		}

		@Override
		public void run() {
			while (!jobComplete) {
				if (jobQueue.isEmpty()) {
					populateReduceJobs();
				}
				if (jobQueue.isEmpty() && reducerStarted) {
					jobComplete = true;
				}
				synchronized (connectedClients) {
					LOGGER.info("num of clients:" + connectedClients.size());
					LOGGER.info("num of jobs:" + jobQueue.size());
					for (ConnectedClient client : connectedClients) {
						LOGGER.info("client " + client.address.getHostAddress() + " busy?:" + client.busy);
						if (!client.busy && !jobQueue.isEmpty()) {
							Job job = jobQueue.poll();
							client.assignedJobs = new ArrayList<Job>();
							client.assignedJobs.add(job);
							client.busy = true;
							assignedJobMap.put(client.address.getHostAddress(), job);
							LOGGER.info("Sending client " + client.getAddress().getHostAddress() + " a "
									+ job.getType().name().toString() + " job");
							client.sendExecuteCommand();
						}
					}
					removeDeadClients();
				}
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			LOGGER.info("job completed");
			writeSuccessFile();
			if(userJob.getNextJob() != null){
				startNextPipelineJob();
			}
			for(ConnectedClient c : connectedClients){
				Command com = new Command(CommandEnum.TERMINATE); 
				c.writeToOutputStream(com);
				c.destroy();
			}
			this.discovery.terminate();
			
		}
		
		/**
		 * method to write the success file when a job finishes.
		 */
		private void writeSuccessFile() {
			String bucket = String.valueOf(userJob.getConf().getValue(Configuration.OUTPUT_BUCKET));
			String folder = String.valueOf(userJob.getConf().getValue(Configuration.OUTPUT_FOLDER));
			File f = new File("_SUCCESS");
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			awsUtil.writeToS3(bucket, "_SUCCESS", folder);
		}

		/**
		 * method to start the next job in pipeline.
		 */
		private void startNextPipelineJob(){
			LOGGER.info("stating next job in pipeline");
			userJob = userJob.getNextJob();
			jobQueue.clear();
			assignedJobMap.clear();
			reduceKeys.clear();
			finishedJobs.clear();
			setNumOfMapTasks(userJob.getNumOfMapTasks());
			setNumOfReduceTasks(userJob.getNumOfReduceTasks());
			populateMapJobs();
			jobComplete = false;
			reducerStarted = false;
			run();
		}
		
		/**
		 * method to remove dead clients.
		 */
		private void removeDeadClients() {
			Iterator<ConnectedClient> it = connectedClients.iterator();
			while (it.hasNext()) {
				ConnectedClient client = it.next();
				if (!client.alive) {
					jobQueue.add(assignedJobMap.get(client.address.getHostAddress()));
					LOGGER.info("removing client");
					client.destroy();
					it.remove();
				}
			}
		}
	}

	/**
	 * create map jobs.
	 */
	public void populateMapJobs() {
		Job job = new Job(userJob);
		List<Job> jobs = new ArrayList<Job>();
		populateListOfInputFiles(job.getConf());
		for (int i = 0; i < numOfMapTasks; i++) {
			job = new Job(userJob);
			job.setId(idCounter++);
			job.setType(JobType.MAP);
			jobs.add(job);
		}
		for (int i = 0; i < listOfInputFiles.size(); i++) {
			jobs.get(i % jobs.size()).getListOfInputFiles().add(listOfInputFiles.get(i));
		}
		for (Job splitJob : jobs) {
			jobQueue.add(splitJob);
		}
	}

	/**
	 * get list of input files to calculate the splits.
	 * @param conf
	 * job configuration
	 */
	private void populateListOfInputFiles(Configuration conf) {
		listOfInputFiles = new ArrayList<String>();
		listOfInputFiles.addAll(awsUtil.getFileList(
				String.valueOf(conf.map.get(Configuration.INPUT_BUCKET))
				, String.valueOf(conf.map.get(Configuration.INPUT_FOLDER))));
	}

	/**
	 * create reduce jobs.
	 */
	private void populateReduceJobs() {
		if (finishedJobs.size() == numOfMapTasks) {
			LOGGER.info("Map jobs finished - Now populating reduce jobs");
			Job job;
			List<Job> jobs = new ArrayList<Job>();

			for (int i = 0; i < numOfReduceTasks; i++) {
				LOGGER.info("Creating reduce job with id : " + idCounter);
				job = new Job(userJob);
				job.setListOfInputFiles(new ArrayList<String>());
				job.setId(idCounter++);
				job.setType(JobType.REDUCE);
				jobs.add(job);
			}

			Iterator<String> reduceKeysIterator = reduceKeys.iterator();
			String reduceKey;

			LOGGER.info("Reduce keys are: ");
			for (int i = 0; i < reduceKeys.size(); i++) {
				reduceKey = reduceKeysIterator.next();
				LOGGER.info(reduceKey);
				jobs.get(i % jobs.size()).getListOfInputFiles().add(reduceKey);
			}

			reduceKeys.clear();

			for (Job splitJob : jobs) {
				jobQueue.add(splitJob);
			}
			if(!jobQueue.isEmpty())
				reducerStarted = true;
		}
	}

	/**
	 * mark job as complete.
	 * @param jobId
	 * jobid
	 */
	public static void markJobAsComplete(Long jobId) {
		LOGGER.info("Marking job id " + jobId + " as finished");
		finishedJobs.add(jobId);
	}

	public List<ConnectedClient> getConnectedClients() {
		return connectedClients;
	}

	public void setConnectedClients(List<ConnectedClient> connectedClients) {
		this.connectedClients = connectedClients;
	}

	public int getNumOfMapTasks() {
		return numOfMapTasks;
	}

	public void setNumOfMapTasks(int numOfMapTasks) {
		this.numOfMapTasks = numOfMapTasks;
	}

	public int getNumOfReduceTasks() {
		return numOfReduceTasks;
	}

	public void setNumOfReduceTasks(int numOfReduceTasks) {
		this.numOfReduceTasks = numOfReduceTasks;
	}
}
