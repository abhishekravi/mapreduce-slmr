package neu.mr.a4;

import neu.mr.job.Configuration;
import neu.mr.job.Job;

/**
 * driver class for processing flight data using map reduce.
 * 
 * @author Abhishek Ravi chandran
 * @author Chinmayee Vaidya
 *
 */
public class A4 {

	/**
	 * main method to process the data
	 * 
	 * @param args
	 *            file location
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		/**
		 * method that runs the mapreduce job
		 */
		Job job1 = new Job();
		job1.setName("Job 1");
		job1.setInputDirectoryPath("s3://pdmrbucket/performance3/");
		job1.setOutputDirectoryPath("s3://pdmrbucket/a4out");
		Configuration conf = new Configuration();
		conf.set(Configuration.SEPERATOR, ",");
		job1.setConf(conf);
		job1.setJar(A4.class);
		job1.setMapperClass(RegressionMapper.class);
		job1.setReducerClass(RegressionReducer.class);
		job1.setNumOfMapTasks(2);
		job1.setNumOfReduceTasks(8);
		job1.setOutputKeyClass(String.class);
		job1.setOutputValueClass(String.class);

		Job job2 = new Job();
		job2.setName("Job 2");
		job2.setInputDirectoryPath("s3://pdmrbucket/performance3/");
		job2.setOutputDirectoryPath("s3://pdmrbucket/a4out" + "_final/");
		conf = new Configuration();
		conf.set(Configuration.SEPERATOR, ",");
		job2.setConf(conf);
		job2.setJar(A4.class);
		job2.setMapperClass(PlotMapper.class);
		job2.setReducerClass(PlotReducer.class);
		job2.setNumOfMapTasks(2);
		job2.setNumOfReduceTasks(8);
		job2.setOutputKeyClass(String.class);
		job2.setOutputValueClass(String.class);

		job1.setNextJob(job2);
		job1.waitForCompletion();
	}
}
