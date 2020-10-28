package com.ms.WebPlatform.utility;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import com.google.gson.Gson;
 
public class HitAndTrail {
	
	 
	public static void main(String[] args) {   
		
		
		Date today = new Date();
		Instant instant = Instant.ofEpochMilli(today.getTime());
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		LocalDate currentDate = localDateTime.toLocalDate();
		
		
		//LocalDate currentDate = LocalDate.now();
	    DayOfWeek dow = currentDate.getDayOfWeek();
	    int dom = currentDate.getDayOfMonth(); 
	    int doy = currentDate.getDayOfYear();
	    Month m = currentDate.getMonth();
	    int y = currentDate.getYear();	
	    
	    System.out.println("java month ::: "+y+" "+m.getValue()+" " +dom+" "+doy+" "+dow+" "+currentDate);
		
	}
	 
	
	public static List<Date> getDatesBetweenUsingJava7(Date startDate, Date endDate) {
			    List<Date> datesInRange = new ArrayList<>();
			    Calendar calendar = new GregorianCalendar();
			    calendar.setTime(startDate);
			     
			    Calendar endCalendar = new GregorianCalendar(); 
			    endCalendar.setTime(endDate);
			 
			    while (calendar.before(endCalendar)) {
			        Date result = calendar.getTime();
			        datesInRange.add(result);
			        calendar.add(Calendar.DATE, 1);
			    }
			    return datesInRange;
			}

	  
 
	  
		  
		  private static String changeFormat(Date date ) {
			  // Date date = new Date();  
			    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
			    String strDate= formatter.format(date);  
			    System.out.println(strDate);  
			    
			    strDate = strDate.replace("/", "-");
			    return strDate;
		  }
		  
}
