package neu.mr.a4;

import java.io.IOException;

import neu.mr.reduce.Reducer;

public class RegressionReducer extends Reducer<String, String, String, String> {

	@Override
	protected void reduce(String key, Iterable<String> values,
			Reducer<String, String, String, String>.Context context) throws IOException, InterruptedException {
		int n = 200;
		// getting number of flights per carrier
		/*
		 * SimpleRegression regression = new SimpleRegression(); for (Text v
		 * : values) { String [] s = Util.parseCSV(v.toString()); float time
		 * = Float.parseFloat(s[0]); float price = Float.parseFloat(s[1]);;
		 * regression.addData(time,price); }
		 */
		double sumx = 0;
		double sumy = 0;
		double sumxy = 0;
		double sumxx = 0;
		long num = 0;
		for (String v : values) {
			String[] s = Util.parseCSV(v.toString());
			float time = Float.parseFloat(s[0]);
			float price = Float.parseFloat(s[1]);
			;
			num++;
			sumx += time;
			sumy += price;
			sumxy += time * price;
			sumxx += time * time;
		}
		double slope = LinearRegression.calculateSlope(num, sumx, sumy, sumxy, sumxx);
		double intercept = LinearRegression.calculateIntercept(num, sumx, sumy, slope);
		// op: year,carrier predicted price for time n
		// context.write(key, new
		// Text(String.valueOf(regression.predict(n))));
		context.write(key, new String(
				String.valueOf(LinearRegression.calculateProjectedScoreForTargetPeriod(slope, intercept, n))));
	}
}