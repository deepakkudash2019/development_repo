package com.ms.WebPlatform.controller;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@ComponentScan
public class IndexController
{
  @RequestMapping({"/init"})
  public String index() {
    System.out.println("WelcomeController this is working ::::::  ");
    try {
    	System.out.println("WelcomeController this is working inside try block ::::::  ");
      return "inittest";
    }
    catch (Exception e) {
      
      e.printStackTrace();
      return null;
    } 
  }

 // PackingDashboardGRID
  
  @RequestMapping({"/testPage"})
  public String PackingDashboardGRID() {
    System.out.println("WelcomeController this is working ::::::  ");
    try {
    	System.out.println("WelcomeController this is working inside try block ::::::  ");
      return "inittest2";
    }
    catch (Exception e) {
      
      e.printStackTrace();
      return null;
    } 
  }

}
