/**
 * Mrp Configuration .jsp  Page Java script Code Starts From here 
 * 
 */

 var global = new com.example.MrpConfig.MrpPrinting.GlobalConstants();



$(document).ready(function () {
    $( "#sortable" ).sortable({
      revert: true,
      stop: function(event,ui){display();}
    }
    );
    $( "#draggable" ).draggable({
      connectToSortable: "#sortable",
      helper: "clone",
      revert: "invalid"
    });
    $( "ul, li" ).disableSelection();
    
    $(".daterange").daterangepicker({
	        locale: {
   	format: 'YYYY-MM-DD'
      }
     }); 
    
  } );
var  day =  '<li class="ui-state-default"> <label style=" text-align: center;" class="label1"><input type="checkbox" class="prefix" checked onchange="display()"/> Prefix</label>'+
'<h2 style="padding:10px; background:#33b5e5; text-align:center;color:white;" class="key">Day</h2>'+
'<h2 style="display:none;" class="prefixvalue"> D</h2>'+
'<h2 style="display:none;" class="onlyvalue"> 16</h2>'+
'</li>';
			
var month='<li class="ui-state-default"><label style=" text-align: center;"class="label1"><input type="checkbox" class="prefix" checked onchange="display()"/> Prefix</label>'+
'<h2 style="padding:10px; background:#ffbb33; text-align:center;color:white;" class="key"  >Month</h2>'+
'<h2 style="display:none;" class="prefixvalue"> M</h2>'+
'<h2 style="display:none;" class="onlyvalue"> 04</h2>'+
'</li>';
var year ='<li class="ui-state-default"><label style=" text-align: center;"class="label1"><input type="checkbox" class="prefix" checked onchange="display()"/> Prefix</label>'+
'<h2 style="padding:10px; background:#ff5252; text-align:center;color:white;" class="key"  >Year</h2>'+
'<h2 style="display:none;" class="prefixvalue"> Y</h2>'+
'<h2 style="display:none;" class="onlyvalue"> 17</h2>'+
'</li>';
var week=	'<li class="ui-state-default"><label style=" text-align: center;"class="label1"><input type="checkbox" class="prefix" checked onchange="display()"/> Prefix</label>'+
'<h2 style="padding:10px; background:#ff5252; text-align:center;color:white;" class="key"  >Week</h2>'+
'<h2 style="display:none;" class="prefixvalue"> W</h2>'+
'<h2 style="display:none;" class="onlyvalue"> 15</h2>'+
'</li>';

var mrp=	'<li class="ui-state-default"><label style=" text-align: center;"class="label1"><input type="checkbox" class="prefix" checked onchange="display()"/> Prefix</label>'+
'<h2 style="padding:10px; background:#aa66cc; text-align:center;color:white;" class="key"  >Mrp</h2>'+
'<h2 style="display:none;" class="prefixvalue"> Mrp</h2>'+
'<h2 style="display:none;" class="onlyvalue"> 400</h2>'+
'</li>';
var productcode = 	'<li class="ui-state-default"><label style=" text-align: center;"class="label1"><input type="checkbox" class="prefix" checked onchange="display()"/> Prefix</label>'+
'<h2 style="padding:10px; background:#00b8d4; text-align:center;color:white;" class="key"  >Product Code</h2>'+
'<h2 style="display:none;" class="prefixvalue"> PC</h2>'+
'<h2 style="display:none;" class="onlyvalue"> ppc</h2>'+
'</li>';
 
var flyash= '<li class="ui-state-default"><label style=" text-align: center;"class="label1"><input type="checkbox" class="prefix" checked onchange="display()"/> Prefix</label>'+
			'<h2 style="padding:10px; background:#ff5252; text-align:center;color:white;" class="key">Flyash %</h2>'+
			'<h2 style="display:none;" class="prefixvalue">FA</h2>'+
			'<h2 style="display:none;" class="onlyvalue"> 15%</h2>'+
			'</li>';
			
			
			
	function addString()
	{
		var value=$("#type").val();
		switch(value)
		{
			case "day":
			$("#sortable").append(day);
				$('#type option[value="day"]').hide();
				$("#type").val("select");
				break;
			case "month":
				$("#sortable").append(month);
				$('#type option[value="month"]').hide();
				$("#type").val("select");
				break;
			case "year":
				$("#sortable").append(year);
				$('#type option[value="year"]').hide();
				$("#type").val("select");
				break;
			case "mrp":
				$("#sortable").append(mrp);
				$('#type option[value="mrp"]').hide();
				$("#type").val("select");
				break;
			case "week":
				$("#sortable").append(week);
				$('#type option[value="week"]').hide();
				$("#type").val("select");
				break;
			case "product_code":
				$("#sortable").append(productcode);
				$('#type option[value="product_code"]').hide();
				$("#type").val("select");
				break;
			
			case "flyash":
				$("#sortable").append(flyash);
				$('#type option[value="flyash"]').hide();
				$("#type").val("select");
				break;
			}
		
		display();
		
		
		
	}
	
	function display()
	{
				var abc="";
				var checkbox=$(".prefix");
				var prefix=$(".prefixvalue");
				var onlyvalue=$(".onlyvalue");
				if($('#prefix').is(':checked'))
				{
					$(".label1").show();
					
					for(var i=0;i<prefix.length;i++)
					{
						if(!checkbox[i].checked)
						{
							abc+=onlyvalue[i].innerHTML;
						}
						else{
						abc+=prefix[i].innerHTML+onlyvalue[i].innerHTML;
						}
					}
					$("#samplestring").html(abc);
				
				}
				else
				{
					$(".label1").hide();
					for(var i=0;i<onlyvalue.length;i++)
					{
						abc+=onlyvalue[i].innerHTML;
					}
					
					$("#samplestring").html(abc);
				}
				
				
	}
	function submit()
	{
		
	
	var mrpCommand=[];
		var array= new Array();
		var checkbox=$(".prefix");
		var prefix=$(".prefixvalue");
		var onlyvalue=$(".onlyvalue");
		var key= $(".key");
		var abc="";
		if($('#prefix').is(':checked'))
		{
			
			var counter=0;
			for(var i=0;i<prefix.length;i++)
			{
				
				counter= i+1;
				var jsonObject= new Object();
				if(checkbox[i].checked)
				{
					
					
					mrpCommand.push(""+(prefix[i].innerHTML+":"+1).trim());
					
				}
				else
				{
					
					mrpCommand.push(""+(prefix[i].innerHTML+":"+0).trim());
					
				}
				
				
				
			}
			
		
		}
		else
		{
			$(".label1").hide();
			var onlyvalue=$(".onlyvalue");
			for(var i=0;i<onlyvalue.length;i++)
			{
				/* var jsonObject= new Object();
				jsonObject={
				"keyvalue":""+key[i].value,
				"is_prefix_applied":"N",
				"sequence_number":""+counter};
				array.push(jsonObject); */
				mrpCommand.push(""+(prefix[i].innerHTML+":"+0).trim());
				
			}
			
			
		}
			console.log(JSON.stringify(mrpCommand));
		
  	var jsonobject={"mrpCommand":mrpCommand};
  	
    	$.ajax({
             type: "POST",
             url:""+global.updateMrp,
             data:JSON.stringify(jsonobject),
             contentType: "application/json",
             success: function(data)
             {
             	alert("Data Submitted");
             }
             	
			 });
			 
	}