package neu.mr.a2;

import java.io.IOException;
import java.text.ParseException;

import au.com.bytecode.opencsv.CSVParser;
import neu.mr.map.Mapper;

public class UserMapper extends Mapper<Integer, String, String, String> {

	@Override
	public void map(Integer key, String value,
			Mapper<Integer, String, String, String>.Context context){

		String line = value.toString();
		
		CSVParser blah = new CSVParser(',','"',' ');

		String key2;
		String value2;

		try {

			String[] tokens = blah.parseLine(line);
			if (tokens.length == FlightDataIndexes.TOTAL_NUM_OF_COLUMNS
					&& !tokens[FlightDataIndexes.YEAR].startsWith("YEAR") // Ignore
																			// the
																			// header
					&& !Validation.isCorrupt(tokens)) {

				String carrierCode = tokens[FlightDataIndexes.UNIQUECARRIER];
				Integer month = Integer.parseInt(tokens[FlightDataIndexes.MONTH]);
				Integer year = Integer.parseInt(tokens[FlightDataIndexes.YEAR]);
				Float ticketPrice = Float.parseFloat(tokens[FlightDataIndexes.AVGTICKETPRICE]);

				// Construct the required key and value
				key2 = new String(carrierCode + "," + month + "," + year);
				value2 = new String(String.valueOf(ticketPrice));

				// Write the keys and values of the valid rows
				// to the context for the reducer function to consume
				// for further processing
				context.write(key2, value2);
			}

		} catch (NumberFormatException e) {
			System.err.println("Cannot parse the csv lines. NumberFormatException encountered.");
		} catch (ParseException e) {
			System.err.println("Cannot parse the csv lines. ParseException encountered.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
