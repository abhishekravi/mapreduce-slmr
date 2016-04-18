package neu.mr.map;

import java.io.IOException;

public class Mapper<K1, V1, K2, V2>  {

	public abstract class Context {

		protected Context() {
			
		}
		
		public void write(K2 key, V2 value)
		{
			
		}
		

	}

	@SuppressWarnings("unchecked")
	public void map(K1 key, V1 value, 
			Context context) throws IOException, InterruptedException {
		context.write((K2) key, (V2) value);
	}
}
