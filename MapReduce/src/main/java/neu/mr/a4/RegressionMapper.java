package neu.mr.a4;

import java.io.IOException;

import neu.mr.map.Mapper;

public class RegressionMapper extends Mapper<Integer, String, String, String> {

	@Override
	public void map(Integer key, String value, Mapper<Integer, String, String, String>.Context context)
			throws IOException, InterruptedException {
		RecordData recordData = new RecordData();

		try {
			Parser.getRecordData(value.toString(), recordData);
		} catch (BadDataException e) {
			// skip all bad records
			return;
		}
		if (DataValidator.isGoodRecord(recordData)) {
			// have key as year, carrier
			// passing along values average ticket price
			context.write(recordData.getYear() + "," + recordData.getCarrier(),
					recordData.getCrsElapsedTime() + "," + recordData.getAvgTicketPrice());
		}

	}
}
