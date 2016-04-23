package neu.mr.a3;

import java.io.IOException;

import neu.mr.map.Mapper;

/**
 * this class reads all the records and only selects good records and groups
 * them by carrier and month.
 * 
 * @author Abhishek Ravi Chandran
 *
 */
public class FlightDataMapper extends Mapper<Integer, String, String, String> {

	public void map(Integer key, String value, Context context) throws IOException, InterruptedException {
		RecordData recordData = new RecordData();

		try {
			Parser.getRecordData(value.toString(), recordData);
		} catch (BadDataException e) {
			// skip all bad records
			return;
		}
		if (DataValidator.isGoodRecord(recordData)) {
			// have key as carrier,month
			// passing along values average ticket price
			context.write(String.valueOf(recordData.getMonth() + recordData.getCarrier()),
					String.valueOf(recordData.getAvgTicketPrice()));
		}
	}

}