<%@ page import="java.util.ArrayList" %>
<%@ page import="com.ms.WebPlatform.utility.ConstantData" %>

<%@ page language="java" import="java.util.*" %> 
<%@ page import = "java.util.ResourceBundle" %>
  




<!DOCTYPE html>
<html lang="en" >
<head>
  <meta charset="UTF-8">
  
  
   <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script> 
  <title>MSRTC RAW DATA</title>


<!------------------------------- Css for the table page ---------------------------------->
    <style>
        table {
            width: 100%;
        }
        
        table,
        td {
            border: solid black 1px;
        }
        
        table.menu {
            width: auto;
            margin-right: 0px;
            margin-left: auto;
        }
        
        #table-scroll {
            height: 550px;
            overflow: auto;
            margin-top: 20px;
            width: 950px;
        }
 
 
 
 .loading {display: flex;
            justify-content: center;
            align-content: center;
            align-items: center;
            display:none;
}

.loading img {flex:1; margin: auto}
      
    </style>

    <!--------------------------------------------------- end of css table --------------------------------------------->


    <!-- Script to get data for table -->

  
</head>

<body>


<!-- -------------------------HTML For table Starts------------------------- -->

  <div class="container py-5">
        <div class="row">
            <div class="text-center col-lg-12 col-md-12 col-sm-12">
                <h3> Raw Data For MSRTC </h3>
            </div>
            
            <div class="col-md-12 col-md-12 col-sm-12">

 					<div class="form-group col-lg-2 col-md-2 col-sm-6">
                        <label for="deviceIMEIID">ActivityName</label>
                        <input type="text" class="form-control" id="activity_id" placeholder="name">
                    </div>

                     <div class="form-group col-lg-2 col-md-2 col-sm-6">
                        <label for="deviceIMEIID">technicianNumber</label>
                        <input type="number" class="form-control" id="technician_id" placeholder="12345678">
                    </div>
                    
                    <div class="form-group col-lg-2 col-md-2 col-sm-6">
                        <label for="deviceIMEIID">DivisionCode</label>
                        <input type="text" class="form-control" id="division_id" placeholder="division">
                    </div>
                    <div class="form-group col-lg-2 col-md-2 col-sm-6">
                        <label for="deviceIMEIID">StartDate</label>
                        <input type="date" class="form-control" id="start_id_date" placeholder="2020-08-07">
                    </div>
                    
                     <div class="form-group col-lg-2 col-md-2 col-sm-6">
                        <label for="deviceIMEIID">EndDate</label>
                        <input type="date" class="form-control" id="end_id_date" placeholder="2020-08-08">
                    </div><br>
                   
                    
                    <div class="form-group col-lg-2 col-md-2 col-sm-6">
                    <label for="noOfRows">&nbsp;&nbsp;</label>
                    <button type="button" class="btn btn-primary form-control" onclick="fetchRawData()">Submit</button>
                    </div>
                   <div style="col-md-12 col-sm-12">
                   			
                        <table id = "table_id">
                            <thead>

                                <tr>
                                    <td>
                                        <span style="font-size:15px;">Slno.</span>
                                    </td>
                                    <td>
                                        <span style="font-size:15px;">ActivityDate</span>
                                    </td>
                                    <td>
                                        <span style="font-size:15px;">technician</span>
                                    </td>

                                    <td>
                                        <span style="font-size:15px;">ActivityName</span>
                                    </td>
                                     <td>
                                        <span style="font-size:15px;">Division</span>
                                    </td>
                                    <td>
                                        <span style="font-size:15px;">Depot</span>
                                    </td>
                                </tr>
                            </thead>
                            <tbody id="container">
                            </tbody>
                        </table>
                        <div class="loading" id='div_loader'>
								<img src="https://upload.wikimedia.org/wikipedia/commons/b/b1/Loading_icon.gif">
							</div>
                    </div> 
                    
                    

            </div>
        </div>
    </div>

</body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>



    <script >
    function show_hide(value){
        if(value == 1){        	
        	document.getElementById('date_div_id').style.visibility = "visible";
       }else{
    	   document.getElementById('date_div_id').style.visibility = "hidden";
    	   document.getElementById('id_date').value = '';
       }
    }
    
    function getDevice_ping(){ 

        
    }

    function fetchRawData_datewise(){

    	var startdate=document.getElementById("start_id_date").value;  
    	var enddate=document.getElementById("end_id_date").value;  
    	var technicianName=document.getElementById("technician_id").value;  
    	var ActivityName=document.getElementById("technician_id").value;  
    	var division=document.getElementById("imei").value; 
    	
    	var restURL="http://103.197.121.86:8020/api/rawdata/readCsv/?path="+dynamicPath;
    	console.log('dynamic Url --  '+restURL);
    	
    	if(imeiNumber == 0 || imeiNumber.length < 10){
    		swal("IMEI not valid", "Please enter a valid imei");
    		myFunction_loader_stop();
    		return;
    	}    	
    	 var lTable = document.getElementById("container");
    	 lTable.style.display = "none";
    	myFunction_loader();

    	console.log("fetchRawdataMethod--");
    	$.ajax({
            type: "GET",
            dataType: "json",
            async: false,
            url: restURL,
            success: function (data) {              

            	if(data.code != 1){
            		 swal("Data not available", "Data not available with this Date "+dateValue); 
					 myFunction_loader_stop();
               }
                var objArr = data.data;
                var html='';
               

                for (var i=0; i<objArr.length; i++) {
					  var count = (i+1);
					 

					  var rawDataArr = objArr[i].rawData.split(",")[5];
					  var msgStatus = rawDataArr == 'L' ? 'Live':'History';
					  
				    html += "<tr>"+
				        "<td>"+count+ "</td>"+
					    "<td>"+ objArr[i].recevedTime+ "</td>"+
					    "<td>"+ objArr[i].pingTime+ "</td>"+
					
					    "<td width='30%'>"+ objArr[i].rawData+ "</td>"+
					    "<td>"+ msgStatus + "</td>"+
				    "</tr>";
				    // Break into next row

				    console.log('inside loop  '+count+"  "+e);
				    if(count == SelectedVal){
				    	 break;
				  }
				    
				  }				

				  // ATTACH HTML TO CONTAINER
				  //console.log("this is inner html --->>> "+html);
				  lTable.style.display = "contents";
				  document.getElementById("container").innerHTML = html;
				  myFunction_loader_stop();  
				    
			 
               
            },
            error: function (error) {

                jsonValue = jQuery.parseJSON(error.responseText);
                alert("error" + error.responseText);
            }
        });

    }
    
    function fetchRawData(){    
//{"name":"maintenance","startDt":"2020-07-21","endDt":"2020-07-23","divisionCode":"","userLogin":"8816046208"}
    	var startdate=document.getElementById("start_id_date").value;  
    	var endDt=document.getElementById("end_id_date").value;  
    	var technicianName=document.getElementById("technician_id").value;  
    	var ActivityName=document.getElementById("activity_id").value;  
    	var divisionCode=document.getElementById("division_id").value; 	
    	
    	var jsonObj = {"name":ActivityName,"startDt":startdate,"endDt":endDt,"divisionCode":divisionCode,"userLogin":technicianName};
    	console.log("test ----  >>>  "+jsonObj);
    	
    	 $.ajax({
				type:'POST',
				 dataType: 'json',
				 contentType: "application/json; charset=utf-8",
				 url : "http://103.197.121.83:8010/api/admin/activitySearch/f76hkjds6767jhd90jj/",
				 data: JSON.stringify(jsonObj),
				success : function(data) 
				{	
					console.log("payLoad -- >> "+jsonObj);
					console.log("result---->> "+JSON.stringify(data.data));
					var objArr = data.data;
					
					var count = 0;				 
					 
					 var i;
					 if(data.code == 0 || objArr.length == 0){						
							 swal("Data not available", "Data not available with this IMEI"); 
							 myFunction_loader_stop(); 
						return;
					 }else{					    
						    
						    var perrow = 3,html = "";

						  for (var i=0; i<objArr.length; i++) {
							  var count = (i+1);
							  var msgStatus = objArr[i].msgStatus == 'L' ? 'Live':'History';
						    html += "<tr>"+
						        "<td>"+count+ "</td>"+
							    "<td>"+ objArr[i].receivedTime+ "</td>"+
							    "<td>"+ objArr[i].messageTime+ "</td>"+
							
							    "<td width='30%'>"+ objArr[i].rawData+ "</td>"+
							    "<td>"+ msgStatus + "</td>"+
						    "</tr>";
						    // Break into next row
						    
						  }				

						  // ATTACH HTML TO CONTAINER
						  console.log("this is inner html --->>> "+html);
						  lTable.style.display = "contents";
						  document.getElementById("container").innerHTML = html;
						  myFunction_loader_stop();  
						    
					 }   
					 
					 
			    },
				error : function(emp) 
				{
					swal("Error", "Cant call services try again");
					location.reload();
				}
			});
    	
    	
    	
    	
    }
    

    function myFunction_loader() {
    	  var x = document.getElementById("div_loader");    	 
    	    x.style.display = "contents";    	  
    }
    function myFunction_loader_stop() {
  	  var x = document.getElementById("div_loader");    	 
  	    x.style.display = "none";    	  
  }
    function sweetAlert(){
    	Swal.fire({
    		  position: 'top-end',
    		  icon: 'success',
    		  title: 'Your work has been saved',
    		  showConfirmButton: false,
    		  timer: 1500
    		})
    }
    </script>

</body>
</html>
