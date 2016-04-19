package neu.mr.job;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neu.mr.map.MapRunner;
import neu.mr.reduce.ReduceRunner;
import neu.mr.utils.AwsUtil;

/**
 * Class to run the user job.
 * It delegates map and reduce jobs to MapRunner and ReduceRunner 
 * classes respectively where they are executed using the run()
 * method
 * @author chintanpathak
 *
 */
public abstract class JobRunner {

	private static Logger LOGGER = LoggerFactory.getLogger(JobRunner.class);

	@SuppressWarnings("rawtypes")
	private static MapRunner mapRunner;
	private static ReduceRunner reduceRunner;
	private static AwsUtil awsutil;

	static {
		awsutil = new AwsUtil("AKIAJG5UIGP6SQUW7OBA", "+fIVd3W1Ou5Jsal/8cV9TI+h341FJN2mF3Vr9fpD");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Set<String> runJob(Job job) {
		mapRunner = new MapRunner(awsutil);
		reduceRunner = new ReduceRunner(awsutil);
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
