package neu.mr.map;

import java.io.IOException;

/**
 * Mapper class that should be extended by the user to implement map function.
 * @author Mania Abdi
 *
 * @param <K1>
 * @param <V1>
 * @param <K2>
 * @param <V2>
 */
public class Mapper<K1, V1, K2, V2> {

	public abstract class Context {

		protected Context() {

		}

		public void write(K2 key, V2 value) {

		}
	}

	@SuppressWarnings("unchecked")
	public void map(Integer key, String value, Context context) throws IOException, InterruptedException {
		context.write((K2) key, (V2) value);
	}
}
