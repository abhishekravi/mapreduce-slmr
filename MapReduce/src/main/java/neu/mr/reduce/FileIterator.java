package neu.mr.reduce;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * custom Iterator class for reading a file.
 * @author Abhishek Ravichandran
 *
 */
public class FileIterator implements Iterator<String> {

	private static Logger LOGGER = LoggerFactory.getLogger(FileIterator.class);
	BufferedReader reader;

	/**
	 * Iterator constructor.
	 * 
	 * @param myReader
	 * BufferedReader
	 */
	FileIterator(BufferedReader myReader) {
		reader = myReader;
	};

	@Override
	public boolean hasNext() {
		try {
			return reader.ready();
		} catch (IOException e) {
			LOGGER.error("error checking end of file", e);
		}
		return false;
	};

	@Override
	public String next() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			LOGGER.error("error reading files", e);
		}
		return null;
	};
	

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Remove not supported!");
	};

}
