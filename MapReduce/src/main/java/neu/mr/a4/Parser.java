package neu.mr.a4;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Class to perform csv parsing and set custom data structure.
 * 
 * @author Abhishek Ravi Chandran
 *
 */
public final class Parser {

	/**
	 * method to parse csv data and get RecordData object
	 * 
	 * @param record
	 *            csv data
	 * @param recordData
	 *            RecordData object
	 * @throws BadDataException
	 */
	public static void getRecordData(String record, RecordData recordData)
			throws BadDataException {
		String[] flightRecord = Util.parseCSV(record);
		setRecordValues(recordData, flightRecord);
	}


	/**
	 * method to convert the record data into a pojo.
	 * 
	 * @param recordData
	 *            record object
	 * @param flightRecord
	 *            record array
	 * @throws BadDataException
	 */
	private static void setRecordValues(RecordData recordData,
			String[] flightRecord) throws BadDataException{
		try {
			recordData.setYear(Integer.parseInt(flightRecord[Constants.YEAR]));
			recordData
					.setMonth(Integer.parseInt(flightRecord[Constants.MONTH]));
			recordData.setDay(Integer.parseInt(flightRecord[Constants.DAY]));
			recordData.setCarrier(flightRecord[Constants.CARRIER]);
			if (flightRecord[Constants.CANCELLED].equals("0")) {
				recordData.setCancelled(false);
				recordData.setAvgTicketPrice(Float
						.parseFloat(flightRecord[Constants.AVGPRICE]));
				recordData.setActualElapsedTime(Integer
						.parseInt(flightRecord[Constants.ACTELAPTIME]));
				recordData.setArrDel15(Float
						.parseFloat(flightRecord[Constants.ARRDEL15]));
				recordData.setArrDelay(Float
						.parseFloat(flightRecord[Constants.ARRDEL]));
				recordData.setArrivalDelayNew(Float
						.parseFloat(flightRecord[Constants.ARRDELNEW]));
			} else {
				recordData.setCancelled(true);
			}
			settingDates(recordData, flightRecord);
			recordData.setCrsElapsedTime(Integer
					.parseInt(flightRecord[Constants.CRSELAPSED]));
			recordData.setDest(flightRecord[Constants.DEST]);
			recordData.setDestAirportId(Integer
					.parseInt(flightRecord[Constants.DESTAIRPORTID]));
			recordData.setDestAirportSeqId(Integer
					.parseInt(flightRecord[Constants.DESTAIRPORTSEQID]));
			recordData.setDestCityMArketId(Integer
					.parseInt(flightRecord[Constants.DESTMARKETID]));
			recordData.setDestCityName(flightRecord[Constants.DESTCITYNAME]);
			recordData.setDestStateAbr(flightRecord[Constants.DESTSTABR]);
			recordData.setDestStateFips(Integer
					.parseInt(flightRecord[Constants.DESTSTATEFIPS]));
			recordData.setDestStateNm(flightRecord[Constants.DESTSTATENM]);
			recordData.setDestWac(Integer
					.parseInt(flightRecord[Constants.DESTWAC]));
			recordData.setOrigin(flightRecord[Constants.ORIGIN]);
			recordData.setOriginAirportId(Integer
					.parseInt(flightRecord[Constants.ORIGINAIRPORTID]));
			recordData.setOriginAirportSeqId(Integer
					.parseInt(flightRecord[Constants.ORIGINAIRPORTSEQID]));
			recordData.setOriginCityMarketId(Integer
					.parseInt(flightRecord[Constants.ORIGINMARKETID]));
			recordData
					.setOriginCityName(flightRecord[Constants.ORIGINCITYNAME]);
			recordData
					.setOriginStateAbr(flightRecord[Constants.ORIGINSTATEABR]);
			recordData.setOriginStateFips(Integer
					.parseInt(flightRecord[Constants.ORIGINSTATEFIPS]));
			recordData.setOriginStateNm(flightRecord[Constants.ORIGINSTATENM]);
			recordData.setOriginWac(Integer
					.parseInt(flightRecord[Constants.ORIGINWAC]));
		} catch (NumberFormatException ne) {
			throw new BadDataException(ne.getMessage());
		}
	}

	/**
	 * method to set the dates.
	 * 
	 * @param recordData
	 *            record object
	 * @param flightRecord
	 *            record map
	 * @throws Exception
	 */
	private static void settingDates(RecordData recordData,
			String[] flightRecord) throws BadDataException {
		// setting the scheduled arrival and departure date times
		int cArrTime = Integer
				.parseInt(flightRecord[Constants.CRSARRTIME]);
		int cDepTime = Integer
				.parseInt(flightRecord[Constants.CRSDEPTIME]);
		// exit if these times are zero
		if (cArrTime == 0 || cDepTime == 0) {
			throw new BadDataException("time epmty");
		}
		final Calendar cal = GregorianCalendar.getInstance();
		cal.set(recordData.getYear(), recordData.getMonth(),
				recordData.getDay());
		Date cDepDate;
		Date cArrDate;

		// converting to 00:00 standard time
		if (cArrTime == 2400) {
			cArrTime = 0000;
		}
		// converting to 00:00 standard time
		if (cDepTime == 2400) {
			cDepTime = 0000;
		}
		cal.set(Calendar.HOUR_OF_DAY, (int) cDepTime / 100);
		cal.set(Calendar.MINUTE, (int) cDepTime % 100);
		cDepDate = cal.getTime();

		// incrementing by 1 day if the arrival time is the next day
		if (cArrTime < cDepTime) {
			cal.add(Calendar.DATE, 1);
		}
		cal.set(Calendar.HOUR_OF_DAY, (int) cArrTime / 100);
		cal.set(Calendar.MINUTE, (int) cArrTime % 100);
		cArrDate = cal.getTime();

		// setting the actual arrival and departure date times
		Date depDate = null;
		Date arrDate = null;
		if (!recordData.isCancelled()) {
			// set calendar to date from record
			cal.set(recordData.getYear(), recordData.getMonth(),
					recordData.getDay());
			int depTime = Integer
					.parseInt(flightRecord[Constants.DEPTIME]);
			// converting to 00:00 standard time
			if (depTime == 2400) {
				depTime = 0000;
			}
			cal.set(Calendar.HOUR_OF_DAY, (int) depTime / 100);
			cal.set(Calendar.MINUTE, (int) depTime % 100);
			depDate = cal.getTime();

			int arrTime = Integer
					.parseInt(flightRecord[Constants.ARRTIME]);
			// converting to 00:00 standard time
			if (arrTime == 2400) {
				arrTime = 0000;
			}
			if (arrTime < depTime) {
				cal.add(Calendar.DATE, 1);
			}
			cal.set(Calendar.HOUR_OF_DAY, (int) arrTime / 100);
			cal.set(Calendar.MINUTE, (int) arrTime % 100);
			arrDate = cal.getTime();

		}

		recordData.setCrsArrTime(cArrDate);
		recordData.setCrsDepTime(cDepDate);
		recordData.setDepTime(depDate);
		recordData.setArrTime(arrDate);
	}

}
