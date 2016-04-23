package neu.mr.a3;
/**
 * custom exception class for bad data.
 * @author Abhishek Ravi Chandran
 *
 */
public class BadDataException extends Exception{

	private static final long serialVersionUID = 1L;

	/**
	 * constructor to pass error message.
	 * @param msg
	 * error message
	 */
	BadDataException(String msg){
		super(msg);
	}
}
