package neu.mr.job;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import neu.mr.map.Mapper;
import neu.mr.reduce.Reducer;
import neu.mr.server.Server;
import neu.mr.utils.AwsUtil;
import neu.mr.utils.FileUtil;

/**
 * Main job class in which the user would configure their job, including the
 * mapper class, reducer class, input folder and output folder
 * 
 * It'll also have an object of JobConfiguration that holds a map of properties
 * that every client would get along with the job itself
 * 
 * @author Chintan Pathak, Mania Abdi
 *
 */
public class Job implements Serializable {

	private static final long serialVersionUID = 1L;

	private Class<?> jar;
	private Class<? extends Mapper> mapperClass;
	private Class<? extends Reducer> reducerClass;
	private Class<?> outputKeyClass;
	private Class<?> outputValueClass;
	private String inputDirectoryPath;
	private int numOfMapTasks;
	private List<String> listOfInputFiles;
	private Configuration conf;
	private String name;
	private JobType type;

	public Job() {
		listOfInputFiles = new ArrayList<String>();
		type = JobType.UNASSIGNED;
	}

	public Job(Job other) {
		jar = other.getJar();
		mapperClass = other.getMapperClass();
		reducerClass = other.getReducerClass();
		outputKeyClass = other.getOutputKeyClass();
		outputValueClass = other.getOutputValueClass();
		inputDirectoryPath = other.getInputDirectoryPath();
		numOfMapTasks = other.getNumOfMapTasks();
		listOfInputFiles = new ArrayList<String>();
		listOfInputFiles.addAll(other.getListOfInputFiles());
		conf = other.getConf();
		name = other.getName();
		type = other.getType();
	}

	public int waitForCompletion() {
		JobScheduler jobScheduler = new JobScheduler();
		jobScheduler.populateJobQueue(this, numOfMapTasks);
		Server server = new Server(jobScheduler);
		server.execute();
		return 0;
	}

	public void helloWorld(){
		System.out.println("Hello world!");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void runMapper () {

		class Context extends Mapper<Object, String, String, String>.Context {

			Map<String, BufferedWriter> bufwrs = new HashMap<String, BufferedWriter>();

			Context(Mapper<Object, String, String, String> mapper) {
				mapper.super();
			}

			public void write(String key, String value) {

				try {

					if (!bufwrs.containsKey(key))
					{
						BufferedWriter bw;
						bw = new BufferedWriter(new FileWriter(new File(key), true));
						bufwrs.put(key, bw);
					}

					bufwrs.get(key).write(value);
					bufwrs.get(key).newLine();
					bufwrs.get(key).flush();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			public void close()
			{
				for (BufferedWriter bw : bufwrs.values())
				{
					try {
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}

		try {
			@SuppressWarnings("unchecked")
			Mapper<Object, String, String, String> mapper = getMapperClass().getConstructor().newInstance();
			Context context = new Context(mapper);
			AwsUtil awsutil = new AwsUtil("AKIAJG5UIGP6SQUW7OBA","+fIVd3W1Ou5Jsal/8cV9TI+h341FJN2mF3Vr9fpD");

			listOfInputFiles.add("blah/55.csv.gz");
			for (String file : listOfInputFiles)
			{
				/* get file from aws */
				awsutil.readFromS3("pdmrbucket", file, "blah");
				FileUtil.gunzip (file, file.replace(".gz", ""));
				//GZIPInputStream gzistrm = new GZIPInputStream(new FileInputStream(file));

				@SuppressWarnings("resource")
				BufferedReader bufread = new BufferedReader(new FileReader(file.replace(".gz", "")));
				String dl;

				while ((dl = bufread.readLine()) != null)
				{
					mapper.map ("0", dl, context);
				}

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

	public void runReducer ()
	{

	}

	public Class<?> getJar() {
		return jar;
	}

	public void setJar(Class<?> jar) {
		this.jar = jar;
	}

	public Class<? extends Mapper> getMapperClass() {
		return mapperClass;
	}

	public void setMapperClass(Class<? extends Mapper> mapperClass) {
		this.mapperClass = mapperClass;
	}

	public Class<? extends Reducer> getReducerClass() {
		return reducerClass;
	}

	public void setReducerClass(Class<? extends Reducer> reducerClass) {
		this.reducerClass = reducerClass;
	}

	public Class<?> getOutputKeyClass() {
		return outputKeyClass;
	}

	public void setOutputKeyClass(Class<?> outputKeyClass) {
		this.outputKeyClass = outputKeyClass;
	}

	public Class<?> getOutputValueClass() {
		return outputValueClass;
	}

	public void setOutputValueClass(Class<?> outputValueClass) {
		this.outputValueClass = outputValueClass;
	}

	public String getInputDirectoryPath() {
		return inputDirectoryPath;
	}

	public void setInputDirectoryPath(String inputDirectoryPath) {
		this.inputDirectoryPath = inputDirectoryPath;
	}

	public int getNumOfMapTasks() {
		return numOfMapTasks;
	}

	public void setNumOfMapTasks(int numOfMapTasks) {
		this.numOfMapTasks = numOfMapTasks;
	}

	public List<String> getListOfInputFiles() {
		return listOfInputFiles;
	}

	public void setListOfInputFiles(List<String> listOfInputFiles) {
		this.listOfInputFiles = listOfInputFiles;
	}

	public Configuration getConf() {
		return conf;
	}

	public void setConf(Configuration conf) {
		this.conf = conf;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JobType getType() {
		return type;
	}

	public void setType(JobType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return listOfInputFiles.toString();
	}
}
