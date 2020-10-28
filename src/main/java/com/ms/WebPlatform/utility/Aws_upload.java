package com.ms.WebPlatform.utility;

import java.io.File;
import java.io.InputStream;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.common.io.Files;

public class Aws_upload { 
	 
	

	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir"));		
	}
	

	
	public  static boolean directUploadTOs3SIde(String localUrl,String bucketName,String serverUrl) {
		BasicAWSCredentials credentials = new BasicAWSCredentials("AKIAI5MIHGU3AZAB5F2A", "ye9WE2ZoZGM8wlPG8lSqOUc1qqvbgYK4yIJgu+VE");
		
		String rootUrlPath = "";
		rootUrlPath = serverUrl;//sdkdta.getMerchantid().equals("na")  ?  "RegisteredUser/"+sdkdta.getUserId()+"/"+sdkdta.getImageName()   :   sdkdta.getMerchantid()+"/"+sdkdta.getUserId()+"/"+sdkdta.getImageName();
	   
		System.out.println("DirectUpload side img to s3 :::::::::  "); 
		
		
		try { 
			
			File initialFile = new File(localUrl);
		    InputStream targetStream = Files.asByteSource(initialFile).openStream();
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(targetStream.available());
			@SuppressWarnings("deprecation")
			AmazonS3 s3 = new AmazonS3Client(credentials);
			
			s3.putObject(new PutObjectRequest(bucketName, rootUrlPath, targetStream, meta));
			System.out.println("Done ::::::::: success");  
			return true;
			
		} catch (Exception e) {
		   e.printStackTrace();
		}
		
		System.out.println("Done side img::::::::: failed");
		return false;
	}

	
	

}
