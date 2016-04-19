package neu.mr.job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

	private static final long serialVersionUID = 1L;

	private Class<?> jar;
	private long Id;
	private Class<? extends Mapper> mapperClass;
	private Class<? extends Reducer> reducerClass;
	private Class<?> outputKeyClass;
	private Class<?> outputValueClass;
	private String inputDirectoryPath;
	private int numOfMapTasks;
	private int numOfReduceTasks;
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
		JobScheduler jobScheduler = new JobScheduler(this);
		Server server = new Server(jobScheduler);
		server.execute();
		return 0;
	}

	public void helloWorld() {
		System.out.println("Hello world!");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
}
