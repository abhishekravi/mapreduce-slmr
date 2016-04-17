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

	Map<String, Object> map = new HashMap<String, Object>();

	public void set(String name, Object value) {
		map.put(name, value);
	}

	public Object getValue(String key) {
		return map.containsKey(key) ? map.get(key) : null;
	}

}