package com.ms.WebPlatform.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ms.WebplatformApplication;
import com.ms.WebPlatform.services.Importer;
import com.ms.WebPlatform.services.UptimeReport;
@Component
public class Sceduler_Class  extends TimerTask{

	@Autowired private UptimeReport uptimeReport;
	@Autowired private Importer imp;
	 private static final Logger logger = LoggerFactory.getLogger(WebplatformApplication.class);
	 
	public Sceduler_Class() {
	     System.out.println("--");
	      Timer t=new Timer();
	      t.scheduleAtFixedRate(this, 5000,9*1000);
	     
	}

	@Override
	public void run() {
		LocalDateTime localDateTime = new LocalDateTime();
		//System.out.println("scheduler task Outer --- >>> "+localDateTime.getHourOfDay()+"  "+localDateTime.getMinuteOfHour()+" ");
		if(localDateTime.getHourOfDay() == 23 && localDateTime.getMinuteOfHour() ==35) {
			logger.info("it will start in few second "+localDateTime.getHourOfDay()+"  "+localDateTime.getMinuteOfHour()+" ");
			if(localDateTime.getSecondOfMinute() > 50) {
				String startTimre = new LocalDateTime().toString();
				logger.info("\n\nStart data saving --- "+new Date());
				
				String result = uptimeReport.downLoadUptimeReport();
				logger.info("result after Method Call --- >>> "+result);	
				String result2 = uptimeReport.createDowntime();
				logger.info("method Called DownTime  --- >>> "+result2);
				String result3 = uptimeReport.StoreUpDownCount();
				String result4 = uptimeReport.StoreUpDownCount_divisionwise();				
				String result5 = uptimeReport.StoreData_depot();
				logger.info("method Called StoreUpDownCount  --- >>> "+result3);
				//db_backup();
				logger.info("creating CSV ");
				
				logger.info("\n\nEnd JSON data saving --- "+new Date());
				imp.getService().csvenerator();
				logger.info("\n\nEnd CSV data saving --- "+new Date());
				
				String endTimre = new LocalDateTime().toString();
				String msgBody = "Executed start Time "+startTimre + "  End time  -- " + endTimre+"  system path is  "+System.getProperty("user.dir");
				GoogleMail.sendMails("uptime Status", msgBody);
				
				
			}
		}
		
	}
	
	
	
	
	//--------------------------getter Setter-------------------------//
	
	
	


	    public static void db_backup() {

	        
	        String propertyFilePath = System.getProperty("user.dir")+"//"+"db_backup.bat";
			
	        ProcessBuilder processBuilder = new ProcessBuilder(propertyFilePath);			
	        try {

	            Process process = processBuilder.start();

	            StringBuilder output = new StringBuilder();

	            BufferedReader reader = new BufferedReader(
	                    new InputStreamReader(process.getInputStream()));

	            String line;
	            while ((line = reader.readLine()) != null) {
	                output.append(line + "\n");
	            }

	            int exitVal = process.waitFor();
	            if (exitVal == 0) {
	                System.out.println(output);
	                System.exit(0);
	            } else {
	                //abnormal...
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }

	    }

	
	

}
