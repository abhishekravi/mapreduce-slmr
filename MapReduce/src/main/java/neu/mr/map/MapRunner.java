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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import neu.mr.job.Job;
import neu.mr.job.JobRunner;
import neu.mr.utils.AwsUtil;

/**
 * Class to run the map jobs
 * 
 * @author chintanpathak, Mania Abdi
 *
 */
public class MapRunner<K1, V1, K2, V2> extends JobRunner {

	private AwsUtil awsutil;

	class Context extends Mapper<Integer, String, K2, V2>.Context {

		Map<String, BufferedWriter> bufwrs = new HashMap<String, BufferedWriter>();

		Context(Mapper<Integer, String, K2, V2> mapper) {
			mapper.super();
		}

		@Override
		public void write(K2 key, V2 value) {

			try {
				String stringKey = key.toString() + ".gz";
				String stringValue = value.toString();
				if (!bufwrs.containsKey(stringKey)) {
					GZIPOutputStream zip = new GZIPOutputStream(new FileOutputStream(new File(stringKey)));

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

		public Set<String> keySet() {
			return bufwrs.keySet();
		}

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

	public MapRunner(AwsUtil awsUtil) {
		this.awsutil = awsUtil;
	}

	@Override
	public void run(Job job) {

		try {
			@SuppressWarnings("unchecked")
			Mapper<Integer, String, K2, V2> mapper = job.getMapperClass().getConstructor().newInstance();
			Context context = new Context(mapper);

			for (String file : job.getListOfInputFiles()) {
				/* get file from aws */
				awsutil.readFromS3("pdmrbucket", file, "blah");
				InputStream fileStream = new FileInputStream(file);
				Reader reader = new InputStreamReader(new GZIPInputStream(fileStream));
				BufferedReader bufread = new BufferedReader(reader);

				String dl;
				Integer i = 0;

				while ((dl = bufread.readLine()) != null) {
					mapper.map(i, dl, context);
				}

				bufread.close();
			}

			Set<String> keyset = context.keySet();
			for (String key : keyset) {
				awsutil.writeToS3("pdmrbucket", key, "tmp");
			}

			context.close();

		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
