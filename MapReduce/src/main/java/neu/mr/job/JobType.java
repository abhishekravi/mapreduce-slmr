package neu.mr.job;

import java.io.Serializable;

/**
 * Holds the different types of jobs.
 * @author Chintan Pathak
 *
 */
public enum JobType implements Serializable{

	MAP("map_job"),
	REDUCE("reduce_job"),
	UNASSIGNED("unassigned");
	
	private final String text;

	/**
	 * @param text
	 */
	private JobType(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}

}
