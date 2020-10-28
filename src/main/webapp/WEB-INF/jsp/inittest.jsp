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
<marquee>RTL</marquee>

<!-- -------------------------HTML For table Starts------------------------- -->

  <div class="container py-5">
        <div class="row">
            <div class="text-center col-lg-12 col-md-12 col-sm-12">
                <h3> Raw Data For MSRTC </h3>
            </div>
            
            <div class="col-md-12 col-md-12 col-sm-12">



                
                    <div class="form-group col-lg-2 col-md-2 col-sm-6">
                        <label for="deviceIMEIID">Device IMEI</label>
                        <input type="number" class="form-control" id="imei" placeholder="12345678">
                    </div>                   
                    
                    <div class="form-group col-lg-2 col-md-2 col-sm-6">
                        <label for="noOfRows">No of Rows</label>
                        <select class="form-control" id="rowCount">
                        <option>5</option>
                        <option>10</option>
                        <option>25</option>
                        <option>100</option>
                        <option>200</option>
                      </select>
                    </div>
                    
                  <!--    <div class="form-group col-lg-2 col-md-2 col-sm-6" id = "date_div_id">
                        <label for="deviceIMEIID">Date</label>
                        <input type="date" class="form-control" id="id_date" placeholder="12345678">
                    </div><br>
                    
                    <div class="form-group col-lg-3 col-md-2 col-sm-6">
	                    <input type="radio" id="showId" name="gender" value="show"  onchange="show_hide(1)">
						<label for="male">Show Date</label>
						
						<input type="radio" id="hideId" name="gender" value="hide" onchange="show_hide(0)">
						<label for="male">Hide Date</label><br>
                    </div> -->
                    
                    <div class="form-group col-lg-2 col-md-2 col-sm-6">
                    <label for="noOfRows">&nbsp;&nbsp;</label>
                    <button type="button" class="btn btn-primary form-control" onclick="getDevice_ping()">Submit</button>
                    </div>
                    
                    <div class="form-group col-lg-2 col-md-2 col-sm-6">
                    <label for="noOfRows">&nbsp;&nbsp;</label>
                    <button type="button" class="btn btn-primary form-control" onclick="getDevice_excel(1)">RawData Excel</button>
                    </div>
                    
                    <div class="form-group col-lg-2 col-md-2 col-sm-6">
                    <label for="noOfRows">&nbsp;&nbsp;</label>
                    <button type="button" class="btn btn-primary form-control" onclick="getDevice_excel(0)">Mapped Excel</button>
                    </div>
                   <div style="col-md-12 col-sm-12">
                   			
                        <table id = "table_id">
                            <thead>

                                <tr>
                                    <td>
                                        <span style="font-size:15px;">Slno.</span>
                                    </td>
                                    <td>
                                        <span style="font-size:15px;">Received-Time</span>
                                    </td>
                                    <td>
                                        <span style="font-size:15px;">Message-Time</span>
                                    </td>

                                    <td>
                                        <span style="font-size:15px;">Raw Data</span>
                                    </td>
                                    <td>
                                        <span style="font-size:15px;">Message Status</span>
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
    	 fetchRawData();
    	/*var dateValue=document.getElementById("id_date").value;  
    	if(dateValue.length > 5){   		
    		
    		fetchRawData_datewise();
          }else{
        	  fetchRawData();
        }
    	*/
    }

    function fetchRawData_datewise(){

    	var imeiNumber=document.getElementById("imei").value;  
    	var dateValue=document.getElementById("id_date").value;
    	var e = document.getElementById("rowCount"); 
    	var SelectedVal = e.options[e.selectedIndex].value;

    	var dynamicPath = imeiNumber+"/"+imeiNumber+"_"+dateValue+".csv";
    	
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

    var globalTokenForExcel = "";
    var excelIO = new GC.Spread.Excel.IO();

    //http://103.197.121.83:8010/api/data/generateMappUnmappExcel/kfjdsfoiu949/

    function getDevice_excel(type) {  
       // var excelUrl = "./test.xlsx"; 
       myFunction_loader();        
       if(type == 1 && globalTokenForExcel == ""){
            alert("please click on submit first");
            return;
       }
       
        var restURL = type == 1 ? "http://103.197.121.83:8010/api/data/generateExcelbytoken/"+globalTokenForExcel+"/" : "http://103.197.121.83:8010/api/data/generateMappUnmappExcel/kfjdsfoiu949/";
        window.open(restURL, "_blank");
        globalTokenForExcel = "";
        myFunction_loader_stop();

    } 

    
   
    
    function fetchRawData(){    	
    	
    	var e = document.getElementById("rowCount"); 
    	var SelectedVal = e.options[e.selectedIndex].value;
    	
    	var imeiNumber=document.getElementById("imei").value;  
    	
    	var jsonObj = {"deviceImei":imeiNumber,"count":SelectedVal};
    	
    	console.log("the value is --->> "+imeiNumber);
    	
    	if(imeiNumber == 0 || imeiNumber.length < 10){
    		swal("IMEI not valid", "Please enter a valid imei");
    		myFunction_loader_stop();
    		return;
    	}    	
    	 var lTable = document.getElementById("container");
    	 lTable.style.display = "none";
    	myFunction_loader();
    	 $.ajax({
				type:'POST',
				 dataType: 'json',
				 contentType: "application/json; charset=utf-8",
				 url : "http://103.197.121.83:8010/api/data/lastPing/",
				 data: JSON.stringify(jsonObj),
				success : function(data) 
				{	
					
					console.log("result---->> "+JSON.stringify(data.data));
					var objArr = data.data;
					
					var count = 0;				 
					 
					 var i;
					 if(data.code == 0 || objArr.length == 0){						
							 swal("Data not available", "Data not available with this IMEI"); 
							 myFunction_loader_stop(); 
						return;
					 }else{					    
						 globalTokenForExcel =  data.token;
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
