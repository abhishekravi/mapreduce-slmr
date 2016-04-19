package neu.mr.reduce;

import java.io.BufferedReader;
import java.util.Iterator;

/**
 * custom Itrable class for File.
 * @author Abhishek Ravichandran
 *
 */
public class FileItrable implements Iterable<String> {

	BufferedReader reader;

	/**
	 * constructor, setting buffered reader for file.
	 * @param myReader
	 * buffered reader
	 */
	public FileItrable(BufferedReader myReader) {
		reader = myReader;
	};

	@Override
	public Iterator<String> iterator() {
		return new FileIterator(reader);
	}

}
