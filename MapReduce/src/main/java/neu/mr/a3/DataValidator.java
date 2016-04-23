package neu.mr.a3;
import java.util.concurrent.TimeUnit;

/**
 * 
 * Class to perform all data validations.
 * @author Abhishek Ravi Chandran
 * @author Chinmayee Vaidya
 *	
 */
public final class DataValidator {

	/**
	 * method to check if the record read is good.
	 * 
	 * @param recordData
	 *            record read
	 * @return boolean
	 */
	public static boolean isGoodRecord(RecordData recordData) {

		boolean goodRecord = true;
		// sanity checks to be performed
		if (!checkTimes(recordData) || !checkOrigDestData(recordData)
				|| !checkIDData(recordData)) {
			goodRecord = false;
		}
		// sanity checks for flights that are not Cancelled:
		if (!recordData.isCancelled()) {
			if (!checkNonCancelledData(recordData))
				goodRecord = false;
		}
		return goodRecord;
	}

	/**
	 * method to check conditions for non cancelled flights.
	 * 
	 * @param recordData
	 *            record read
	 * @return boolean
	 */
	private static boolean checkNonCancelledData(RecordData recordData) {
		boolean dataGood = true;
		long duration  = recordData.getArrTime().getTime()
				- recordData.getDepTime().getTime();
		long timeDiff = TimeUnit.MILLISECONDS.toMinutes(duration);

		double timeZone = timeDiff - recordData.getActualElapsedTime();

		// ArrTime - DepTime - ActualElapsedTime - timeZone should be zero
		if ((timeDiff - recordData.getActualElapsedTime() - timeZone) != Constants.ZERO) {
			dataGood = false;
		}
		// if ArrDelay > 0 then ArrDelay should equal to ArrDelayMinutes
		if (recordData.getArrDelay() > Constants.ZERO) {
			if (recordData.getArrDelay() != recordData.getArrivalDelayNew())
				dataGood = false;
		}
		// if ArrDelay < 0 then ArrDelayMinutes should be zero
		if (recordData.getArrDelay() < Constants.ZERO) {
			if (recordData.getArrivalDelayNew() != Constants.ZERO)
				dataGood = false;
		}
		// if ArrDelayMinutes >= 15 then ArrDel15 should be false
		if (recordData.getArrivalDelayNew() >= Constants.FIFTEEN) {
			if (recordData.getArrDel15() == Constants.ZERO)
				dataGood = false;
		}
		return dataGood;
	}

	/**
	 * method to check times.
	 * 
	 * @param recordData
	 *            record read
	 * @return boolean
	 */
	private static boolean checkTimes(RecordData recordData) {
		boolean dataGood = true;

		// getting time difference in minutes
		long duration  = recordData.getCrsArrTime().getTime()
				- recordData.getCrsDepTime().getTime();
		long timeDiff = TimeUnit.MILLISECONDS.toMinutes(duration);

		double timeZone = timeDiff - recordData.getCrsElapsedTime();

		// timeZone % 60 should be 0
		if (timeZone % Constants.NUMOFMINS != Constants.ZERO) {
			dataGood = false;
		}
		return dataGood;
	}

	/**
	 * method to check airport ids.
	 * 
	 * @param recordData
	 *            record read
	 * @return boolean
	 */
	private static boolean checkIDData(RecordData recordData) {
		boolean dataGood = true;
		// AirportID, AirportSeqID, CityMarketID, StateFips, Wac should be
		// larger than 0
		if (recordData.getDestAirportId() <= Constants.ZERO
				|| recordData.getOriginAirportId() <= Constants.ZERO
				|| recordData.getDestAirportSeqId() <= Constants.ZERO
				|| recordData.getOriginAirportSeqId() <= Constants.ZERO
				|| recordData.getDestCityMArketId() <= Constants.ZERO
				|| recordData.getOriginCityMarketId() <= Constants.ZERO
				|| recordData.getDestStateFips() <= Constants.ZERO
				|| recordData.getOriginStateFips() <= Constants.ZERO
				|| recordData.getDestWac() <= Constants.ZERO
				|| recordData.getOriginWac() <= Constants.ZERO) {
			dataGood = false;
		}
		return dataGood;
	}

	/**
	 * method to check origin and destination data.
	 * 
	 * @param recordData
	 *            record read
	 * @return boolean
	 */
	private static boolean checkOrigDestData(RecordData recordData) {
		boolean dataGood = true;
		// Origin, Destination, CityName, State, StateName should not be
		// empty
		if (recordData.getOrigin().isEmpty()
				|| recordData.getDest().isEmpty()
				|| recordData.getOriginCityName().isEmpty()
				|| recordData.getDestCityName().isEmpty()
				|| recordData.getOriginStateNm().isEmpty()
				|| recordData.getOriginStateAbr().isEmpty()
				|| recordData.getDestStateNm().isEmpty()
				|| recordData.getDestStateAbr().isEmpty()) {
			dataGood = false;
		}
		return dataGood;
	}
}
