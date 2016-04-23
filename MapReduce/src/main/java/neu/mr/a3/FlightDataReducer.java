package neu.mr.a3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import neu.mr.reduce.Reducer;

/**
 * reducer class that calculates the mean/median price for all carriers per
 * month.
 * 
 * @author Abhishek Ravichandran
 *
 */
public class FlightDataReducer extends Reducer<String, String, String, String> {

	public void reduce(String key, Iterable<String> values, Context context)
			throws IOException, InterruptedException {
		long size = Constants.ZERO;
		double total = Constants.ZERO;
		List<Float> prices = new ArrayList<Float>();
		// getting number of flights per carrier
		for (String v : values) {
			size++;
			total += Float.parseFloat(v);
			prices.add(Float.parseFloat(v));
		}
		// statistical variable to hold mean or median
		float statVal = Constants.ZERO;
		// op: month ,carrier, mean(or)median
		statVal = (float) (total / size);
		context.write(key, String.valueOf(statVal));
	}
}
