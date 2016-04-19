package neu.mr.main;

import neu.mr.reduce.Reducer;

public class UserReducer extends Reducer<String, String, String, String> {

	@Override
	public void reduce(String key, Iterable<String> values, Reducer<String, String, String, String>.Context context) {

		String keys[] = key.split(",");
		String carrier = keys[0];
		Integer month = Integer.parseInt(keys[1]);
		Integer year = Integer.parseInt(keys[2]);

		System.err.println("Processing=" + key);

		Float total = 0f;
		Long count = 0l;

		for (String value : values) {
			total += Float.parseFloat(value);
			count++;
		}

		String key1 = carrier + "," + month + "," + year;
		context.write(key1, String.valueOf(total/count));
	}
}
