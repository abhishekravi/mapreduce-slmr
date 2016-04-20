package neu.mr.job;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neu.mr.map.Mapper;
import neu.mr.reduce.Reducer;
import neu.mr.server.Server;

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
@SuppressWarnings("rawtypes")
public class Job implements Serializable {

	private static Logger LOGGER = LoggerFactory.getLogger(Job.class);
	private static final long serialVersionUID = 1L;

	private Class<?> jar;
	private long Id;
	private Class<? extends Mapper> mapperClass;
	private Class<? extends Reducer> reducerClass;
	private Class<?> outputKeyClass;
	private Class<?> outputValueClass;
	private String inputDirectoryPath;
	private String outputDirectoryPath;
	private int numOfMapTasks;
	private int numOfReduceTasks;
	private List<String> listOfInputFiles;
	private Configuration conf;
	private String name;
	private JobType type;

	public Job() {
		listOfInputFiles = new ArrayList<String>();
		type = JobType.UNASSIGNED;
		this.conf = new Configuration();
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
		String[] s3info = inputDirectoryPath.split("/");
		this.conf.map.put(Configuration.INPUT_BUCKET, s3info[2]);
		this.conf.map.put(Configuration.INPUT_FOLDER, s3info[3]);
		s3info = outputDirectoryPath.split("/");
		this.conf.map.put(Configuration.OUTPUT_BUCKET, s3info[2]);
		this.conf.map.put(Configuration.OUTPUT_FOLDER, s3info[3]);
		Properties prop = new Properties();
		InputStream input;
		String filename = "config.properties";
		input = getClass().getClassLoader().getResourceAsStream(filename);
		try {
			prop.load(input);
			JobScheduler jobScheduler = new JobScheduler(this, prop.getProperty("awsid"), prop.getProperty("awskey"));
			Server server = new Server(jobScheduler);
			server.execute();
		} catch (IOException e){
			LOGGER.error("error when loading properties file", e);
		}
		return 0;
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

	public int getNumOfReduceTasks() {
		return numOfReduceTasks;
	}

	public void setNumOfReduceTasks(int numOfReduceTasks) {
		this.numOfReduceTasks = numOfReduceTasks;
	}
	
	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	@Override
	public String toString() {
		return listOfInputFiles.toString();
	}

	public String getOutputDirectoryPath() {
		return outputDirectoryPath;
	}

	public void setOutputDirectoryPath(String outputDirectoryPath) {
		this.outputDirectoryPath = outputDirectoryPath;
	}
}
