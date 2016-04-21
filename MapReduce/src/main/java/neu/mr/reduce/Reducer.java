package neu.mr.reduce;

import java.io.IOException;

/**
 * Reducer class to be extended by user to implement reducer.
 * @author Abhishek Ravichandran
 *
 * @param <K1>
 * @param <V1>
 * @param <K2>
 * @param <V2>
 */
public class Reducer<K1, V1, K2, V2> {

	public abstract class Context {
		protected Context() {

		}

		public void write(K2 key, V2 value) {

		}
	}

	/**
	 * default reducer implementation.
	 */
	@SuppressWarnings("unchecked")
	protected void reduce(K1 key, Iterable<V1> values, Context context) throws IOException, InterruptedException {
		for (V1 value : values) {
			context.write((K2) key, (V2) value);
		}
	}
}
