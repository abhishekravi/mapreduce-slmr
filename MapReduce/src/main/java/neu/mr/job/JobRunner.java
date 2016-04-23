package neu.mr.job;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neu.mr.map.MapRunner;
import neu.mr.reduce.ReduceRunner;
import neu.mr.utils.AwsUtil;

/**
 * Class to run the user job. It delegates map and reduce jobs to MapRunner and
 * ReduceRunner classes respectively where they are executed using the run()
 * method
 * 
 * @author Chintan Pathak, Abhishek Ravichandran
 *
 */
public abstract class JobRunner {

	private static Logger LOGGER = LoggerFactory.getLogger(JobRunner.class);

	@SuppressWarnings("rawtypes")
	private static MapRunner mapRunner;
	@SuppressWarnings("rawtypes")
	private static ReduceRunner reduceRunner;

	/**
	 * Run the job encapsulated within the "Job" object.
	 * This method detects the type of job and sends it
	 * to the respective Map/Reduce runner classes
	 * @param job
	 * @param awsutil
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Set<String> runJob(Job job, AwsUtil awsutil) {
		mapRunner = new MapRunner(awsutil);
		reduceRunner = new ReduceRunner(awsutil, String.valueOf(job.getConf().getValue(Configuration.SEPERATOR)));
		if (job.getType().equals(JobType.MAP)) {
			return mapRunner.run(job);
		} else if (job.getType().equals(JobType.REDUCE)) {
			return reduceRunner.run(job);
		} else {
			LOGGER.error("Cannot execute job that is neither of the type map or reduce");
		}
		return null;
	}

	public abstract Set<String> run(Job job);
}
