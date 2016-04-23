package neu.mr.a3;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.opencsv.CSVParser;

/**
 * 
 * @author Abhishek Ravi Chandran
 * @author Chnimayee Vaidya
 *
 */
public final class Util {

	final static CSVParser PARSER = new CSVParser();
	
	/**
	 * method to parse csv data.
	 * 
	 * @param value
	 *            csv data
	 * @return string array
	 */
	public static String[] parseCSV(String value) {
		String parsed[] = null;
		try {
			parsed = PARSER.parseLine(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return parsed;
	}
	
	/**
	 * method to read the directory and get the list of all files.
	 * 
	 * @param dirPath
	 *            directory path
	 * @return list of file names with path
	 */
	public static List<String> processDir(String dirPath) {
		// get all filename in the directory
		List<String> files = new ArrayList<String>();
		try (DirectoryStream<Path> directoryStream = Files
				.newDirectoryStream(Paths.get(dirPath))) {
			for (Path path : directoryStream) {
				files.add(path.toString());
			}
		} catch (IOException ex) {
			new Error(ex);
		}
		return files;
	}
	
	/**
	 * util method to round double values to any precision.
	 * 
	 * @param value
	 *            value
	 * @param places
	 *            places to round to
	 * @return double
	 */
	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
	/**
	 * method to calculate median of prices by sorting.
	 * 
	 * @param prices
	 *            list of prices
	 * @return median of the list
	 */
	public static float calculateMedian(List<Float> prices) {
		Collections.sort(prices);
		return prices.get(prices.size() / 2);
	}

	/**
	 * method that uses quick select to calculate the median.
	 * 
	 * @param prices
	 *            list of prices
	 * @return median of the list
	 */

	public static float fastMedian(List<Float> values) {
		int arr_size = values.size();
		int mid_element = (arr_size / 2);
		float median = getMedian(values, mid_element, 0, arr_size);
		return median;
	}

	/**
	 * method to partition the array based on comparison of pivot.
	 * 
	 * @param values
	 *            array
	 * @param start
	 *            start pos of array
	 * @param end
	 *            end pos of array
	 * @return pivot position
	 */
	private static int partition(List<Float> values, int start, int end) {
		int left = start;
		int right = end - 1;
		int pivot = start;
		while (left < right) {
			if (pivot == left) {
				float current = values.get(pivot);
				float right_elem = values.get(right);
				if (right_elem > current) {
					right--;
				} else {
					values.set(right, current);
					values.set(pivot, right_elem);
					pivot = right;
				}
			}
			else {
				float current = values.get(pivot);
				float left_elem = values.get(left);
				if (left_elem <= current) {
					left++;
				}

				else {
					values.set(left, current);
					values.set(pivot, left_elem);
					pivot = left;
				}
			}
		}
		return pivot;
	}

	// median value to be calculated
	static float median = -1;

	/**
	 * method to get median using quick select.
	 * 
	 * @param values
	 *            array
	 * @param mid_element
	 *            middle element pos
	 * @param start
	 *            start pos
	 * @param end
	 *            end pos
	 * @return return the median value
	 */
	private static float getMedian(List<Float> values, int mid_element,
			int start, int end) {
		if (end - start < 1)
			return -1f;
		else if (end - start == 1)
			return values.get(start);

		int random_ind = start + (int) (Math.random() * (end - start));
		float temp = values.get(start);
		values.set(start, values.get(random_ind));
		values.set(random_ind, temp);
		int index = partition(values, start, end);
		if (index == mid_element) {
			median = values.get(index);
		} else if (index < mid_element) {
			median = getMedian(values, mid_element, index + 1, end);
		} else {

			median = getMedian(values, mid_element, start, index);
		}
		return median;
	}
}
