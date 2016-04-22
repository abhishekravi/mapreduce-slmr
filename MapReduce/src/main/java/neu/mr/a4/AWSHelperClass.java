package neu.mr.a4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class AWSHelperClass {
	AmazonS3 s3;
	String accessid;
	String secretKey;

	AWSHelperClass(String accessid, String secretKey){
		this.accessid = accessid;
		this.secretKey = secretKey;
	}
	
	private Log LOGGER = LogFactory.getLog(AWSHelperClass.class);
	
	public String findCheapest(String bucket, String output) throws NumberFormatException, IOException, URISyntaxException{
		s3 = new AmazonS3Client(new BasicAWSCredentials(accessid, secretKey));
		Map<String,Integer> carrCount = new HashMap<String,Integer>();
		Map<String,YearData> yearDatas = new HashMap<String,YearData>();
		ObjectListing listing = s3.listObjects(new ListObjectsRequest()
		.withBucketName(bucket).withPrefix(output.split("/")[3]+"/"));
		BufferedReader buffer = null;
		String line="";
		YearData year;
		Carrier car;
		for (S3ObjectSummary objectSummary : listing.getObjectSummaries()) {
			S3Object s3object = s3.getObject(new GetObjectRequest(
					bucket, objectSummary.getKey()));
			buffer = new BufferedReader(new BufferedReader(new InputStreamReader(s3object.getObjectContent())));
			while((line=buffer.readLine()) != null){
				String keys[] = line.split("\\s+");
				carrCount.put(keys[1], 0);
				if(yearDatas.get(keys[0]) == null){
					yearDatas.put(keys[0],new YearData());
				}
				year = yearDatas.get(keys[0]);
				car = new Carrier();
				car.name = keys[1];
				car.price = Float.parseFloat(keys[2]);
				year.vals.add(car);
			}
			buffer.close();
			}
		
		for(Entry<String, YearData> e: yearDatas.entrySet()){
			float min = Float.MAX_VALUE;
			String ca = "";
			for(Carrier c : e.getValue().vals){
				if(c.price<min){
					min = c.price;
					ca = c.name;
				}
			}
			carrCount.put(ca,carrCount.get(ca) + 1);
		}
		return getCheapest(carrCount);
	}

	private String getCheapest(Map<String, Integer> carrCount) {
		String carr = "";
		int max = 0;
		for (Entry<String, Integer> e : carrCount.entrySet()) {
			if (e.getValue() > max) {
				carr = e.getKey();
				max = e.getValue();
			}
		}
		LOGGER.info("carr:" + carr);
		return carr;
	}

	class YearData {
		YearData() {
			vals = new ArrayList<Carrier>();
		}

		List<Carrier> vals;
	}

	class Carrier {
		String name;
		float price;
	}
}