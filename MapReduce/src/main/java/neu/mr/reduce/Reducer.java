package neu.mr.reduce;

import java.io.IOException;

/**
 * Reducer class implementation.
 * @author Abhishek Ravichandran
 *
 * @param <K1>
 * @param <V1>
 * @param <K2>
 * @param <V2>
 */
public class Reducer<K1, V1, K2, V2> {

	public abstract class Context {
		/**
		 * The <code>Context</code> passed on to the {@link Reducer}
		 * implementations.
		 */
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
