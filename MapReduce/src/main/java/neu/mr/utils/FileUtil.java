package neu.mr.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/**
 * Util function 
 * @author Mania Abdi
 *
 */
public class FileUtil {

	public static void gunzip (String gzfname, String ofname)
	{
		byte[] buffer = new byte[1024];
		int readsize = 0;
		GZIPInputStream gzistrm = null;
		FileOutputStream ostrm = null;
		try{
			gzistrm = new GZIPInputStream(new FileInputStream(gzfname));
			ostrm = new FileOutputStream(ofname);

			while ((readsize = gzistrm.read(buffer)) > 0) {
				ostrm.write(buffer, 0, readsize);
			}

			gzistrm.close();
			ostrm.close();

		}catch(IOException ex){
			ex.printStackTrace();   
		}
	}
}