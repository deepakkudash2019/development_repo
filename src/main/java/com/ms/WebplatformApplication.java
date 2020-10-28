package com.ms;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import com.ms.WebPlatform.services.LoadCashe;
import com.ms.WebPlatform.utility.ConstantData;
    
@SpringBootApplication   
public class WebplatformApplication {     
	
	     private static final Logger logger = LoggerFactory.getLogger(WebplatformApplication.class);
		public static ApplicationContext ctx;
     
	public static void main(String[] args) { 
		System.out.println("\n\nwelcome  system path :::: "+System.getProperty("user.dir"));

		try {
			
			//String propertyFilePath = System.getProperty("user.dir")+"//"+"msrtc_graph_application.properties";
			//SpringApplication.run(DemoApplication.class, args);
			
			InputStream _inputApplicationFile = new FileInputStream("msrtc_graph_application.properties");
		    Properties _prop = new Properties();
		    _prop.load(_inputApplicationFile);
		   
		    
		    ctx = new SpringApplicationBuilder(WebplatformApplication.class).properties(_prop).run(args);
		    
		    LoadCashe load = ctx.getBean(LoadCashe.class);
		    load.setCashe();
		    ConstantData.dbQuery_1 = _prop.getProperty("db.query_1");
		    ConstantData.fileStoragePath = _prop.getProperty("server.storage.path");
		    
		    if(_prop.getProperty("server.check").equals("0")) {
		    	System.out.println("this is local system ");
		    }else {
		    	logger.info("Code running on server now ----------");
		    	ConstantData.is_server = 1;
		    }
		     System.out.println("test git..");
		  ConstantData.thirdParty_Api = _prop.getProperty("server.thirdpartiapi");
		 ConstantData.raw_data_root_path = _prop.getProperty("server.raw.data");
		 createDirectories("json_activity/");
		 createDirectories("msrtc_storage/");
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
	}
	
	
	//-------createDirectory-----------//
	private static void createDirectories(String fileNm) {
		
		
		 System.out.println("Enter the path to create a directory: ");
		// msrtc_storage "json_activity/";
	      String path = fileNm;
	      System.out.println("Enter the name of the desired a directory: ");	     
	      //Creating a File object
	      File file = new File(path);
	      //Creating the directory
	      boolean bool = file.mkdir();
	      if(bool){
	         logger.info("Directory created successfully --> "+fileNm);
	      }else{
	    	  logger.info("Sorry couldn’t create specified directory  "+fileNm);
	      }
	}
	
	

}
