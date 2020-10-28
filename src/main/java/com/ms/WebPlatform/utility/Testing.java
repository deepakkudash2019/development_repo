package com.ms.WebPlatform.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.Gson;

public class Testing {
	
	public static void main2(String[] args) {
		
		try {
			System.out.println("checking port ");
			Process process = Runtime.getRuntime().exec("netstat -aon | find 8020");
			
			InputStream ips = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(isr);
			String value = null;
			while ((value = br.readLine()) != null) {
			    System.out.println("process --->> "+value);
			}

			
			System.out.println("---END--"+value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
	}
	
	
	public static void main(String args[]) throws Exception {  
			
		Double res =   (double) (100*6 / 76);	
		System.out.println(" --- >>>  "+res);
		
		
    } 
	
	
}