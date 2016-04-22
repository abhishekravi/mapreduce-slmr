package neu.mr.a4;

import java.io.IOException;
import java.util.Calendar;

import neu.mr.map.Mapper;

public class PlotMapper extends Mapper<Integer, String, String, String> {

	@Override
	public void map(Integer key, String value, Mapper<Integer, String, String, String>.Context context)
			throws IOException, InterruptedException {
		RecordData recordData = new RecordData();
		String car = "AS";
		try {
			Parser.getRecordData(value.toString(), recordData);
		} catch (BadDataException e) {
			// skip all bad records
			return;
		}
		if (DataValidator.isGoodRecord(recordData) && recordData.getCarrier().equals(car)) {
			// have key as year, month, week, carrier
			// passing along values average ticket price
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, recordData.getYear());
			c.set(Calendar.MONTH, recordData.getMonth() - 1);
			c.set(Calendar.DATE, recordData.getDay());
			context.write(recordData.getYear() + "," + c.get(Calendar.WEEK_OF_YEAR) + " " + recordData.getCarrier(),
					String.valueOf(recordData.getAvgTicketPrice()));
		}
	}

}