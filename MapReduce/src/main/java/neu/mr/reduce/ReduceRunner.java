package neu.mr.reduce;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neu.mr.job.Configuration;
import neu.mr.job.Job;
import neu.mr.job.JobRunner;
import neu.mr.utils.AwsUtil;

/**
 * Class to execute the reduce jobs
 * 
 * @author Chintan Pathak, Abhishek Ravichandran, Chinmayee Vaidya
 *
 */
public class ReduceRunner<K1, V1, K2, V2> extends JobRunner {

	private static Logger LOGGER = LoggerFactory.getLogger(ReduceRunner.class);
	private AwsUtil awsutil;
	private static String seperator;
	private Map<String, List<String>> taskMap = new HashMap<String, List<String>>();

	/**
	 * initializing reducer.
	 * @param awsUtil
	 * @param seperator
	 * seperator for output.
	 */
	public ReduceRunner(AwsUtil awsUtil, String seperator) {
		this.awsutil = awsUtil;
		ReduceRunner.seperator = seperator;
	}

	/**
	 * Context class that will have the method to write data to file.
	 * 
	 * @author Abhishek Ravichandran
	 *
	 */
	class Context extends Reducer<String, String, K2, V2>.Context {

		BufferedWriter bw;

		Context(Reducer<String, String, K2, V2> reducer) {
			reducer.super();
		}

		/**
		 * method to write the output to a file.
		 */
		@Override
		public void write(K2 key, V2 value) {
			try {
				String file = key.toString() + ".gz";
				String stringValue = value.toString();
				GZIPOutputStream zip = new GZIPOutputStream(new FileOutputStream(new File("reduceoutput/" + file)));
				bw = new BufferedWriter(new OutputStreamWriter(zip));
				bw.write(key.toString() + ReduceRunner.seperator + stringValue);
				bw.newLine();
				bw.flush();
			} catch (IOException e) {
				LOGGER.error("error when writing file", e);
			}

		}

		/**
		 * reducer cleanup method
		 */
		public void close() {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * method to run the job, here temporary files created by mapper are read.
	 */
	@Override
	public Set<String> run(Job job) {

		//creating temporary directories
		File f = new File("reduceoutput");
		f.mkdir();
		f = new File("shuffle");
		f.mkdir();
		shuffleTask(job.getListOfInputFiles(), job.getConf());
		LOGGER.info("starting reducer");
		//creating threads for each key
		Thread[] reducerThreads = new Thread[taskMap.size()];
		int i = 0;
		for (Entry<String, List<String>> entry : taskMap.entrySet()) {
			reducerThreads[i] = new Thread(new ReducerThread(job, entry.getKey()));
			reducerThreads[i].start();
			i++;
		}
		for (Thread t : reducerThreads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				LOGGER.error("error in reducer multithreading", e);
			}
		}
		try {
			//cleaning up temporary directories
			FileUtils.deleteDirectory(new File("reduceoutput"));
			FileUtils.deleteDirectory(new File("shuffle"));
		} catch (IOException e) {
			LOGGER.error("error in reducer cleanup", e);
		}
		return new HashSet<String>();
	}

	/**
	 * reducer task thread, for each key.
	 * @author Abhishek Ravichandran
	 *
	 */
	class ReducerThread implements Runnable {

		Job job;
		String key;

		/**
		 * initializing reducer thread.
		 * @param job
		 * @param key
		 */
		ReducerThread(Job job, String key) {
			this.job = job;
			this.key = key;
		}

		/**
		 * run method for the thread.
		 */
		public void run() {
			try {
				LOGGER.info("in reduce for key:" + key);
				@SuppressWarnings("unchecked")
				Reducer<String, String, K2, V2> reducer = job.getReducerClass().getConstructor().newInstance();
				Context context = new Context(reducer);
				/* get file from aws */
				InputStream fileStream = new FileInputStream("shuffle/" + key + ".gz");
				Reader reader = new InputStreamReader(new GZIPInputStream(fileStream));
				BufferedReader bufread = new BufferedReader(reader);
				FileItrable fileItrable = new FileItrable(bufread);
				reducer.reduce(key, fileItrable, context);
				bufread.close();
				context.close();
				awsutil.writeToS3(String.valueOf(job.getConf().getValue(Configuration.OUTPUT_BUCKET)),
						"reduceoutput/" + key + ".gz",
						String.valueOf(job.getConf().getValue(Configuration.OUTPUT_FOLDER)));
			} catch (NoSuchMethodException | SecurityException | IOException | InterruptedException
					| InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				LOGGER.error("error when reading file", e);
			}
		}
	}


	/**
	 * method to start the shuffle task. Here we download all files with the
	 * same key and merge them into a single files.
	 * 
	 * @param configuration
	 * 
	 * @param list
	 *            list of keys
	 */
	private void shuffleTask(List<String> keyList, Configuration conf) {
		String bucket = String.valueOf(conf.getValue(Configuration.OUTPUT_BUCKET));
		List<String> files = awsutil.getFileList(bucket, "tmp");
		for (String key : keyList) {
			taskMap.put(key, getFilesForKey(key, files));
		}
		startMerge(bucket);
		LOGGER.info("beginning shuffle task");
	}

	/**
	 * merge all files needed for same key.
	 * 
	 * @param bucket
	 *            bucket name
	 */
	private void startMerge(String bucket) {
		for (Entry<String, List<String>> entry : taskMap.entrySet()) {
			for (String file : entry.getValue())
				awsutil.writeToFile(bucket, file, "shuffle/" + entry.getKey() + ".gz");
		}
	}

	/**
	 * method to get only the filtered files for a key.
	 * 
	 * @param key
	 *            key to filter by
	 * @param files
	 *            list of files written by mapper
	 * @return list of files for a single key
	 */
	private List<String> getFilesForKey(String key, List<String> files) {
		List<String> filtered = new ArrayList<String>();
		for (String file : files) {
			if (file.split("~")[0].endsWith(key))
				filtered.add(file);
		}
		return filtered;
	}

}
