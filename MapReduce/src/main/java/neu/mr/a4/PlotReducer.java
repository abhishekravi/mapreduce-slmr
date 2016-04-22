package neu.mr.a4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import neu.mr.reduce.Reducer;

public class PlotReducer extends Reducer<String, String, String, String> {

	@Override
	protected void reduce(String key, Iterable<String> values,
			Reducer<String, String, String, String>.Context context) throws IOException, InterruptedException {
		// float median = 0;
		List<Float> prices = new ArrayList<Float>();
		for (String v : values) {
			prices.add(Float.parseFloat(v));
		}
		Float median = Util.fastMedian(prices);
		// op: year,month, week, carrier median
		context.write(key, String.valueOf(median));
	}
}