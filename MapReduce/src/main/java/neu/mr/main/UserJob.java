package neu.mr.main;

import neu.mr.job.Configuration;
import neu.mr.job.Job;

public class UserJob {

	public static void main(String[] args){
		Job job = new Job();
		job.setName("My Job");
		job.setInputDirectoryPath("blah");
		job.setJar(UserJob.class);
		job.setConf(new Configuration());
		job.setMapperClass(UserMapper.class);
		job.setReducerClass(UserReducer.class);
		job.setNumOfMapTasks(1);
		job.setOutputKeyClass(String.class);
		job.setOutputValueClass(String.class);
		job.waitForCompletion();
	}
}
