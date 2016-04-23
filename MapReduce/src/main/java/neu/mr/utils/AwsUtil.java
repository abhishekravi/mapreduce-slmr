package neu.mr.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//package utils.aws.s3;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * Util class for interaction with s3.
 * 
 * @author Abhishek Ravi Chandran, Chintan Pathak
 *
 */
public class AwsUtil {

	private static Logger LOGGER = LoggerFactory.getLogger(AwsUtil.class);
	AmazonS3 s3;

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
	 * to download file from s3.
	 * 
	 * @param filename
	 *            name of file to download
	 * @param bucket
	 *            bucket name
	 * @param output
	 *            directory to put the file in
	 */
	public void download(String filename, String bucket, String output) {
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
	 * method to write to s3.
	 * 
	 * @param filename
	 *            name of the file
	 * @param file
	 *            DataInputStream of file to write
	 * @param folder
	 *            folder in s3
	 */
	public void writeToS3(String bucket, String fileToUpload, String folder) {
		LOGGER.info("uploading " + fileToUpload + "to s3");
		File file = new File(fileToUpload);
		s3.putObject(new PutObjectRequest(bucket, folder + "/" + fileToUpload, file));
	}

	/**
	 * method to get list of files from s3 folder.
	 * 
	 * @param bucket
	 * @param folder
	 * @return file list
	 */
	public List<String> getFileList(String bucket, String folder) {
		List<String> fileList = new ArrayList<String>();
		ObjectListing objectListing = s3
				.listObjects(new ListObjectsRequest().withBucketName(bucket).withPrefix(folder + "/"));
		for (S3ObjectSummary s : objectListing.getObjectSummaries()) {
			if (!s.getKey().endsWith("/"))
				fileList.add(s.getKey());
			LOGGER.info("file:" + s.getKey());
		}
		return fileList;
	}

	/**
	 * method to download files and append it to a single file.
	 * 
	 * @param bucketname
	 * @param key
	 * @param fileName
	 */
	public void writeToFile(String bucketname, String key, String fileName) {
		try {
			S3Object s3object = s3.getObject(new GetObjectRequest(bucketname, key));
			InputStream input = s3object.getObjectContent();
			OutputStream out = new FileOutputStream(fileName, true);
			IOUtils.copy(input, out);
		} catch (Exception e) {
			LOGGER.error("error when merging from s3", e);
		}
	}
}
