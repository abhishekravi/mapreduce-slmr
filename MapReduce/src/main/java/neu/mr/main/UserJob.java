package neu.mr.main;

import neu.mr.job.Configuration;
import neu.mr.job.Job;

public class UserJob {

	public static void main(String[] args){
		Job job = new Job();
		job.setName("My Job");
		job.setInputDirectoryPath("s3://pdmrbucket/test");
		job.setOutputDirectoryPath("s3://pdmrbucket/output1");
		Configuration conf = new Configuration();
		conf.set(Configuration.SEPERATOR, ",");
		job.setConf(conf);
		job.setJar(UserJob.class);
		job.setMapperClass(UserMapper.class);
		job.setReducerClass(UserReducer.class);
		job.setNumOfMapTasks(2);
		job.setNumOfReduceTasks(8);
		job.setOutputKeyClass(String.class);
		job.setOutputValueClass(String.class);
		//defining next job
		Job job1 = new Job();
		job1.setName("My Job");
		job1.setInputDirectoryPath("s3://pdmrbucket/test2");
		job1.setOutputDirectoryPath("s3://pdmrbucket/output2");
		Configuration conf1 = new Configuration();
		conf1.set(Configuration.SEPERATOR, ",");
		job1.setConf(conf1);
		job1.setJar(UserJob.class);
		job1.setMapperClass(UserMapper.class);
		job1.setReducerClass(UserReducer.class);
		job1.setNumOfMapTasks(2);
		job1.setNumOfReduceTasks(8);
		job1.setOutputKeyClass(String.class);
		job1.setOutputValueClass(String.class);
		//pipelining the next job
		job.setNextJob(job1);
		job.waitForCompletion();
	}
}
