package neu.mr.a3;

import neu.mr.job.Configuration;
import neu.mr.job.Job;

/**
 * driver class for processing flight data using map reduce.
 * 
 * @author Abhishek Ravi chandran
 * @author Chinmayee Vaidya
 *
 */
public class A3 {

	static String MODE;

	/**
	 * main method to process the data
	 * 
	 * @param args
	 *            file location
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Job job = new Job();
		job.setName("Job");
		job.setInputDirectoryPath("s3://pdmrbucket/performance");
		job.setOutputDirectoryPath("s3://pdmrbucket/a3out");
		Configuration conf = new Configuration();
		conf.set(Configuration.SEPERATOR, ",");
		job.setConf(conf);
		job.setJar(A3.class);
		job.setMapperClass(FlightDataMapper.class);
		job.setReducerClass(FlightDataReducer.class);
		job.setNumOfMapTasks(2);
		job.setNumOfReduceTasks(8);
		job.setOutputKeyClass(String.class);
		job.setOutputValueClass(String.class);
		job.waitForCompletion();
	}

}
