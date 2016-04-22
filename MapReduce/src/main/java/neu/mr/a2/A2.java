package neu.mr.a2;

import neu.mr.job.Configuration;
import neu.mr.job.Job;

public class A2 {

	public static void main(String[] args){
		Job job = new Job();
		job.setName("My Job");
		job.setInputDirectoryPath("s3://pdmrbucket/performance");
		job.setOutputDirectoryPath("s3://pdmrbucket/performance_output");
		Configuration conf = new Configuration();
		conf.set(Configuration.SEPERATOR, ",");
		job.setConf(conf);
		job.setJar(A2.class);
		job.setMapperClass(UserMapper.class);
		job.setReducerClass(UserReducer.class);
		job.setNumOfMapTasks(10);
		job.setNumOfReduceTasks(8);
		job.setOutputKeyClass(String.class);
		job.setOutputValueClass(String.class);
		job.waitForCompletion();
	}
}
