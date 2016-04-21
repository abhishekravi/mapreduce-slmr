package neu.mr.map;

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
import java.util.HashMap;
import java.util.Map;
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
import neu.mr.utils.NetworkUtils;

/**
 * Class to run the map jobs
 * 
 * @author chintanpathak, Mania Abdi, Abhishek Ravichandran
 *
 */
public class MapRunner<K1, V1, K2, V2> extends JobRunner {

	private AwsUtil awsutil;
	private static Logger LOGGER = LoggerFactory.getLogger(JobRunner.class);

	/**
	 * context class used to write the records to output files.
	 * @author chintanpathak, Mania Abdi, Abhishek Ravichandran
	 *
	 */
	class Context extends Mapper<Integer, String, K2, V2>.Context {

		Map<String, BufferedWriter> bufwrs = new HashMap<String, BufferedWriter>();

		Context(Mapper<Integer, String, K2, V2> mapper) {
			mapper.super();
		}

		@Override
		public void write(K2 key, V2 value) {

			try {
				String stringKey = key.toString() + "~" + NetworkUtils.getIpAddress().getHostAddress() + ".gz";
				String stringValue = value.toString();
				if (!bufwrs.containsKey(stringKey)) {
					GZIPOutputStream zip = new GZIPOutputStream(
							new FileOutputStream(new File("mapoutput/"+stringKey)));

					BufferedWriter bw;
					bw = new BufferedWriter(new OutputStreamWriter(zip));
					bufwrs.put(stringKey, bw);
				}

				bufwrs.get(stringKey).write(stringValue);
				bufwrs.get(stringKey).newLine();
				bufwrs.get(stringKey).flush();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		/**
		 * method to get the set of keys.
		 * @return
		 * set of keys
		 */
		public Set<String> keySet() {
			return bufwrs.keySet();
		}

		/**
		 * method to close all the files.
		 */
		public void close() {
			for (BufferedWriter bw : bufwrs.values()) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * initializing map runner.
	 * @param awsUtil
	 */
	public MapRunner(AwsUtil awsUtil) {
		this.awsutil = awsUtil;
	}

	@Override
	public Set<String> run(Job job) {

		try {
			@SuppressWarnings("unchecked")
			Mapper<Integer, String, K2, V2> mapper = job.getMapperClass().getConstructor().newInstance();
			Context context = new Context(mapper);

			LOGGER.info("Processing files : " + job.getListOfInputFiles());
			for (String file : job.getListOfInputFiles()) {
				LOGGER.info("Downloading file : " + file + " from S3");
				//create temporary folders
				File f = new File("tempmap");
				f.mkdir();
				f = new File("mapoutput");
				f.mkdir();
				awsutil.download(file, 
						String.valueOf(job.getConf().getValue(Configuration.INPUT_BUCKET)), "tempmap");
				String key = file.substring(file.lastIndexOf("/") + 1, file.length());
				InputStream fileStream = new FileInputStream("tempmap/" + key);
				Reader reader = new InputStreamReader(new GZIPInputStream(fileStream));
				BufferedReader bufread = new BufferedReader(reader);

				String line;
				Integer i = 0;

				LOGGER.info("Starting mapper on file " + file);
				while ((line = bufread.readLine()) != null) {
					mapper.map(i, line, context);
				}
				LOGGER.info("Finished mapper for the file - " + file);

				bufread.close();
			}

			Set<String> keyset = context.keySet();
			context.close();

			for (String key : keyset) {
				awsutil.writeToS3(String.valueOf(job.getConf().getValue(Configuration.OUTPUT_BUCKET)), 
						"mapoutput/"+key, "tmp");
			}
			//cleanup of temporary folders
			FileUtils.deleteDirectory(new File("mapoutput"));
			FileUtils.deleteDirectory(new File("tempmap"));
			return keyset;
		} catch (NoSuchMethodException | SecurityException | IOException | InterruptedException
				| InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			LOGGER.error("error when reading file", e);
		}
		return null;
	}
}
