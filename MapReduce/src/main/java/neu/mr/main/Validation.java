package neu.mr.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author chintanpathak
 *
 */

// This class provides different methods to validate the flight data
// while reading the csv files and helps apply different constraints
// to filter out the noisy and insane data to avoid further miscalculations
public class Validation {

	// Checks for the sanity of a row of flight data from the csv file
	// by validating various constraints against different values
	public static Boolean isCorrupt(String[] tokens) throws NumberFormatException, ParseException {

		SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
		Date crsArrTime = null, arrTime = null, crsDepTime = null, depTime = null;

		try {
			crsArrTime = formatter.parse(tokens[FlightDataIndexes.CRSARRTIME]);
			crsDepTime = formatter.parse(tokens[FlightDataIndexes.CRSDEPTIME]);
		} catch (ParseException e) {
			System.err.println("Cannot parse the given date using HHmm format");
		}

		// Constraint 1)
		// CRSArrTime and CRSDepTime should not be zero
		if ((tokens[FlightDataIndexes.CRSARRTIME].trim().isEmpty()
				|| (Double.parseDouble(tokens[FlightDataIndexes.CRSARRTIME]) == 0))
				|| (tokens[FlightDataIndexes.CRSDEPTIME].trim().isEmpty()
						|| (Double.parseDouble(tokens[FlightDataIndexes.CRSDEPTIME]) == 0))) {
			return true;
		}

		Double timeZone = ((double) (crsArrTime.getTime() - crsDepTime.getTime()) / (1000 * 60))
				- Double.valueOf(tokens[FlightDataIndexes.CRSELAPSEDTIME]);

		// Constraint 2)
		// timeZone = CRSArrTime - CRSDepTime - CRSElapsedTime;
		// timeZone % 60 should be 0
		if ((timeZone % 60) != 0) {
			return true;
		}

		// Constraint 3)
		// AirportID, AirportSeqID, CityMarketID, StateFips, Wac should be
		// larger than 0
		if (Double.valueOf(tokens[FlightDataIndexes.DESTAIRPORTID]) <= 0
				|| Double.valueOf(tokens[FlightDataIndexes.ORIGINAIRPORTID]) <= 0
				|| Double.valueOf(tokens[FlightDataIndexes.DESTAIRPORTSEQID]) <= 0
				|| Double.valueOf(tokens[FlightDataIndexes.ORIGINAIRPORTSEQID]) <= 0
				|| Double.valueOf(tokens[FlightDataIndexes.DESTCITYMARKETID]) <= 0
				|| Double.valueOf(tokens[FlightDataIndexes.ORIGINCITYMARKETID]) <= 0
				|| Double.valueOf(tokens[FlightDataIndexes.DESTSTATEFIPS]) <= 0
				|| Double.valueOf(tokens[FlightDataIndexes.ORIGINSTATEFIPS]) <= 0
				|| Double.valueOf(tokens[FlightDataIndexes.DESTWAC]) <= 0
				|| Double.valueOf(tokens[FlightDataIndexes.ORIGINWAC]) <= 0) {
			return true;
		}

		// Constraint 4)
		// Origin, Destination, CityName, State, StateName should not be empty
		if (tokens[FlightDataIndexes.ORIGIN].trim().isEmpty() || tokens[FlightDataIndexes.DEST].trim().isEmpty()
				|| tokens[FlightDataIndexes.DESTCITYNAME].trim().isEmpty()
				|| tokens[FlightDataIndexes.ORIGINCITYNAME].trim().isEmpty()
				|| tokens[FlightDataIndexes.DESTSTATEABR].trim().isEmpty()
				|| tokens[FlightDataIndexes.ORIGINSTATEABR].trim().isEmpty()
				|| tokens[FlightDataIndexes.DESTSTATENM].trim().isEmpty()
				|| tokens[FlightDataIndexes.ORIGINSTATENM].trim().isEmpty()) {
			return true;
		}

		/*
		 * For flights that are not Cancelled:
		 */
		if (Integer.parseInt(tokens[FlightDataIndexes.CANCELLED]) != 1) {

			Double arrDelayMinutes = Double.parseDouble(tokens[FlightDataIndexes.ARRDELAYNEW]);
			Double minutesInADay = (double) (24 * 60);

			try {
				arrTime = formatter.parse(tokens[FlightDataIndexes.ARRTIME]);
				depTime = formatter.parse(tokens[FlightDataIndexes.DEPTIME]);
			} catch (ParseException e) {
				System.err.println("Cannot parse the given date using HHmm format");
			}

			// Constraint 5)
			// ArrTime - DepTime - ActualElapsedTime - timeZone should be zero
			if ((((double) (arrTime.getTime() - depTime.getTime()) / (1000 * 60))
					- Double.parseDouble(tokens[FlightDataIndexes.ACTUALELAPSEDTIME]) - timeZone)
					% minutesInADay != 0) {
				return true;
			}

			if (Double.parseDouble(tokens[FlightDataIndexes.ARRDELAY]) > 0) {

				// Constraint 6)
				// if ArrDelay > 0 then ArrDelay should equal to arrDelayMinutes
				if (((Double.parseDouble(tokens[FlightDataIndexes.ARRDELAY])) != arrDelayMinutes)) {
					return true;
				}

			} else if (Double.parseDouble(tokens[FlightDataIndexes.ARRDELAY]) < 0) {
				// Constraint 7)
				// if ArrDelay < 0 then ArrDelayMinutes should be zero
				if (arrDelayMinutes != 0) {
					return true;
				}
			}

			// Constraint 8)
			// if ArrDelayMinutes >= 15 then ArrDel15 should be true
			if (arrDelayMinutes >= 15) {
				if (Double.parseDouble(tokens[FlightDataIndexes.ARRDELAY15]) != 1) {
					return true;
				}
			}
		}

		// Constraint 9)
		// As we are dealing with average ticket price of each flight,
		// this field shouldn't be null or empty
		if (tokens[FlightDataIndexes.AVGTICKETPRICE] == null
				|| tokens[FlightDataIndexes.AVGTICKETPRICE].trim().isEmpty()) {
			return true;
		}

		return false;
	}

	// Checks if the given flight was flying during January 2015
	// Use: To mark if the carrier was active during that month or otherwise
	public static Boolean isActiveDuringJanuary2015(String[] tokens) {
		return (Integer.parseInt(tokens[FlightDataIndexes.YEAR]) == 2015
				&& Integer.parseInt(tokens[FlightDataIndexes.MONTH]) == 1) ? true : false;
	}
}
