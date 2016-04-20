package neu.mr.job;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Mania Abdi
 *
 */
public class Configuration implements Serializable {

	private static final long serialVersionUID = 1L;
	public final static String SEPERATOR = "slmr.seperator";
	public final static String INPUT_BUCKET = "slmr.input.buket";
	public final static String INPUT_FOLDER = "slmr.input.folder";
	public final static String OUTPUT_BUCKET = "slmr.output.bucket";
	public final static String OUTPUT_FOLDER = "slmr.output.folder";
	
	Map<String, Object> map = new HashMap<String, Object>();

	
	public void set(String name, Object value) {
		map.put(name, value);
	}

	public Object getValue(String key) {
		return map.containsKey(key) ? map.get(key) : null;
	}

}