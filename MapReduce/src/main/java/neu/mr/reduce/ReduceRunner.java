package neu.mr.reduce;

import java.util.Set;

import neu.mr.job.Job;
import neu.mr.job.JobRunner;
import neu.mr.utils.AwsUtil;

/**
 * Class to execute the reduce jobs
 * @author chintanpathak
 *
 */
public class ReduceRunner extends JobRunner {

	private AwsUtil awsutil;
	
	public ReduceRunner(AwsUtil awsUtil) {
		this.awsutil = awsUtil;
	}
	
	@Override
	public Set<String> run(Job job) {
		awsutil.toString();
		return null;
	}

}
