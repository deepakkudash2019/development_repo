package com.ms.WebPlatform.utility;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import org.apache.commons.codec.binary.Base64;

public class Imageprocessor {
	
	
  public static final String imageRootPath = "https://miiror-vijay.s3.amazonaws.com/images/public/document/";
	
	public static byte[] decodeImage(String imageDataString) {
		return Base64.decodeBase64(imageDataString);
	}
	
	
	public void save(String imgObj)throws Exception{
			
			
		/*
					 
					 byte[] newImage = decodeImage(imgObj);
					
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
					outputStream.write(newImage);
					img =  outputStream.toByteArray( );			
					
			}
			
			if(imgObj.getAction()!= null &&  imgObj.getAction() == 999)
				commFunc.saveYourImage2(img, "BYTE ARRAY TO IMAGE");
			
			imageObj.setImage_ByteArray(img);
			imageDao.update(imageObj);
		     
		}		
		*/
		
	}
	
	
	public static String saveImage(byte[] imageArray ,  String imageName , String merchantId)throws Exception{
		String imageUrl="";
		String imageUrlrtn = "";  
		try{ 
			
			byte[] imageByteArray = imageArray;
			String midstr = null;
			
			String path =  System.getProperty("user.dir")+"/tempStorage/";//ConstantData.checkingSystem.get("system") == 1 ? ConstantData.localPath : ConstantData.serverPathimage; 			
			String finalPath = path+imageName;	
			
			imageUrl = "image/"+imageName;
			FileOutputStream fos = new FileOutputStream(finalPath);				
		
			fos.write(imageByteArray);
			fos.close();
			
			//-----------copyfileFrom test/deepak to aws s3------//
			String aswPath = "image/"+midstr+"/"+imageName;
			
			//-----------END ------------------------------------//
			imageUrlrtn =  imageRootPath+"image/"+midstr+"/"+imageName;
			
		}catch(Exception e){
			e.printStackTrace();
		}
			
		    
			return imageUrlrtn;			
			
		}
	
	
	public static String storeImage(String imageString,String imageName) {
		
		String finalPath = "E:/Deepak/file_storage_test/";
		try {
			byte[] imageByteArray = decodeImage(imageString);
			//String path = null;
			 finalPath = imageName;
			 
				FileOutputStream fos = new FileOutputStream(finalPath);	
				fos.write(imageByteArray);
				fos.close();			
			
		} catch (Exception e) {
		  e.printStackTrace();
		}
		
		return finalPath;
	}
	
	public static void main(String[] args) {
		
		String imageStr = "jsdhshfowefelwfsdhfsfos";
		try {
			storeImage(imageStr, "myimg.jpg");
			//Imageprocessor.saveImage(decodeImage(imageStr), "testImg","awspath");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	

}
