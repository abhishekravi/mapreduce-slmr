package neu.mr.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
//package utils.aws.s3;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

/**
 * Util class to download files from s3.
 * 
 * @author Abhishek Ravi Chandran
 *
 */
public class AwsUtil {

	private static Logger LOGGER = LoggerFactory.getLogger(AwsUtil.class);
	AmazonS3 s3;
	String bucket;

	/**
	 * constructor to create s3 connections.
	 * 
	 * @param accessid
	 *            id
	 * @param secretKey
	 *            secret key
	 */
	public AwsUtil(String accessid, String secretKey) {
		s3 = new AmazonS3Client(new BasicAWSCredentials(accessid, secretKey));
	}

	/**
	 * method to download file from s3.
	 * 
	 * @param filename
	 * @param bucket
	 * @param output
	 *            file name to download
	 * @param fs
	 */
	private void download(String filename, String bucket, String output) {
		S3Object s3object = s3.getObject(new GetObjectRequest(bucket, filename));
		InputStream input = s3object.getObjectContent();
		try {
			String key = s3object.getKey();
			key = key.substring(key.lastIndexOf("/") + 1, key.length());
			Files.copy(input, Paths.get(output + "/" + key));
			input.close();
		} catch (IOException e) {
			LOGGER.error("error when downloading from s3", e);
		}
	}

	/**
	 * read files from s3
	 * 
	 * @param bucketname
	 * @param key
	 * @param output
	 * @throws IOException
	 */
	public void readFromS3(String bucketname, String key, String output) throws IOException {
		download(key, bucketname, output);
	}

	/**
	 * method to write to s3.
	 * 
	 * @param filename
	 *            name of the file
	 * @param file
	 *            DataInputStream of file to write
	 * @param folder
	 *            folder in s3
	 */
	public void writeToS3(String filename, DataInputStream file, String folder) {
		LOGGER.info("uploading " + filename + "to s3");
		TransferManager transferManager = new TransferManager(s3);
		ObjectMetadata objectMetadata = new ObjectMetadata();
		Upload upload = transferManager.upload(bucket, folder + "/" + filename, file, objectMetadata);
		try {
			upload.waitForCompletion();
		} catch (AmazonClientException | InterruptedException e) {
			LOGGER.error("error when writing to s3", e);
		}
	}
}
