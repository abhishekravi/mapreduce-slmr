package neu.mr.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import neu.mr.reduce.Reducer;

public class UserReducer extends Reducer {

	public Set<String> activeCarriers;
	public Map<String, List<Float>> output;

	@Override
	protected void setup(Reducer<FlightDataCompositeKey, FloatWritable, Text, FloatWritable>.Context context)
			throws IOException, InterruptedException {
		output = new TreeMap<String, List<Float>>();
		activeCarriers = new HashSet<String>();
	}

	@Override
	protected void cleanup(Reducer<FlightDataCompositeKey, FloatWritable, Text, FloatWritable>.Context context)
			throws IOException, InterruptedException {
		Float total;
		Long count;

		System.err.println("Cleaning up now");
		
		for (String key : output.keySet()) {
			total = 0f;
			count = 0l;
			
			if (activeCarriers.contains(key.substring(0, 2))) {
				System.err.println("Processing active carrier=" + key.substring(0, 2));
				for (Float value : output.get(key)) {
					total += value;
					count++;
				}
				
				total = Float.parseFloat(String.format("%.2f", total));
				System.err.println("Writing its value to output file");
				context.write(new Text(key), new FloatWritable(total / count));
			}
		}
	}

	@Override
	protected void reduce(FlightDataCompositeKey key, Iterable<FloatWritable> values,
			Reducer<FlightDataCompositeKey, FloatWritable, Text, FloatWritable>.Context context)
					throws IOException, InterruptedException {

		String carrier = key.carrierCode;
		Integer month = key.month;
		Integer year = key.year;
		
		System.err.println("Processing=" + key);
		
		if (year == 2015) {
			System.err.println(carrier + " is active");
			activeCarriers.add(carrier);
		}

		Float total = 0f;
		Long count = 0l;

		for (FloatWritable value : values) {
			total += value.get();
			count++;
		}

		String key1 = carrier + "," + month + "";

		if (output.containsKey(key1)) {
			output.get(key1).add(total / count);
		} else {
			System.err.println("Putting key in output map =" + key1);
			output.put(key1, new ArrayList<Float>());
			output.get(key1).add(total / count);
		}
	}
}
