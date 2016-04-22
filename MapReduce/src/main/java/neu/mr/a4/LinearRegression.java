package neu.mr.a4;

public class LinearRegression {

	public static double calculateSlope(long NumberOfPeriods,
			double SumOfXValues, double SumOfYValues, double SumOfXYValues,
			double SumOfXXValues) {
		double slope = ((NumberOfPeriods * SumOfXYValues) - (SumOfXValues * SumOfYValues))
				/ ((NumberOfPeriods * SumOfXXValues) - (Math.pow(SumOfXValues,
						2)));
		return slope;
	}

	public static double calculateIntercept(long NumberOfPeriods,
			double SumOfXValues, double SumOfYValues, double Slope) {

		double intercept = (SumOfYValues - (Slope * SumOfXValues))
				/ NumberOfPeriods;
		return intercept;
	}

	public static double calculateProjectedScoreForTargetPeriod(double Slope,
			double Intercept, double TargetPeriod) {
		double ProjectedScore = Intercept + (Slope * TargetPeriod);
		return ProjectedScore;
	}
}
