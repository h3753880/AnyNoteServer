<%-- 
    Document   : testjsp
    Created on : Oct 20, 2012, 4:43:29 PM
    Author     : tony
--%>

<%@page import="atg.taglib.json.util.JSONArray"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <title>AnyNote-時間查詢</title>
<link rel='stylesheet' type='text/css' href='fullcalendar/fullcalendar.css' />
<link rel='stylesheet' type='text/css' href='fullcalendar/fullcalendar.print.css' media='print' />
<script type='text/javascript' src='jquery/jquery-1.8.1.min.js'></script>
<script type='text/javascript' src='jquery/jquery-ui-1.8.23.custom.min.js'></script>
<script type='text/javascript' src='fullcalendar/fullcalendar.js'></script>
		<!-- Core files -->
		<script src="jquery.alerts.js" type="text/javascript"></script>
		<link href="jquery.alerts.css" rel="stylesheet" type="text/css" media="screen" />
		
		<!-- Example script -->
<script type='text/javascript'>
    
	$(document).ready(function() {
	
		var date = new Date();
		var d = date.getDate();
		var m = date.getMonth();
		var y = date.getFullYear();
           
		$('#calendar').fullCalendar(
              {
                
			header: {
				left: 'prev,next today',
				center: 'title',
				right: 'month,basicWeek,basicDay'
                               
			},
			editable: false,
			events:${sessionScope.jArray},
                        
   
                        /*提醒詳細內容*/
                        eventClick: function(event) { 
                               jAlert("&nbsp;&nbsp;from:"+event.userName+
                                      "&nbsp;&nbsp;to:"+event.friendName+
                                      "\n&nbsp;&nbsp;標題:"+event.title+"\n"+
                                      "&nbsp;&nbsp;內容:"+event.content+"\n"+
                                      "&nbsp;&nbsp;時間:"+String(event.start).slice(16, 25), '時間提醒詳細內容');
                        }
		});
                
                var year=-1; 
                var month=-1;
                <%
                String temp[];
                JSONArray jArray = (JSONArray) session.getAttribute("jArray");
                System.out.println(jArray.length());
                if(jArray.length()!=0)
                    temp = new String[2]; 
                else
                {
                    temp = new String[2];
                }
                    for(int i=0; i< jArray.length()  ; i++)
                    {
                        
                        if(  jArray.getJSONObject( i ).getString("color").equals("red")   )
                        {
                            temp=jArray.getJSONObject( i).getString("start").split("-"); 
                           break;
                        }              

                    }
                
                %> 
                        year=<%= temp[0]%>;
                        month=<%= temp[1]%>;
                        
                        if(year==null) 
                            $('#calendar').fullCalendar('today');
                        
                        else 
                            $('#calendar').fullCalendar( 'gotoDate',year,month-1);
                 
                 
	});
        
</script>
<style type='text/css'>

	body {  margin: 2em; 
		margin-top: 40px;
		font-size: 14px;
		font-family: "Lucida Grande",Helvetica,Arial,Verdana,sans-serif;
		}

	#calendar {
		width: 900px;
		margin: 0 auto;
		}
                
        #img1{background-image: url(bback01.jpg) ;border-radius:25px; }
        #img2{background-image: url(titleBack.png) ;background-repeat:no-repeat; }
        p{font-weight: bolder;font-family: Microsoft JhengHei; }
        #leftslidebarwrapper{
            background-image:url(../images/leftslidebg.jpg);
            background-repeat:repeat-y;
        }
        #name{font-weight: bolder;font-family: Microsoft JhengHei;
                        text-shadow: 5px 5px 5px black;/*shadow*/
			border-bottom: 4px solid #BE6100;
			border-left:30px solid #BE6100;
			padding-bottom: 10px;
                        padding-left: 1em;
                        width: 95%; }
</style>
</head>
<body>
    <table style="width:100%;" align="center" border="0"><tr>
        <td id="img1"><img src="title.png" width="250" height="130" align="left"/>
            <br/>
            <img src="space.png" width="40" height="100" align="left" />
            <a href="timeNoteCalendar.jsp"><img src="nnewtime2.png" width="100" height="100" align="left" /></a>
            <img src="space.png" width="50" height="100" align="left" />
            <a href="geoNoteMap.jsp"><img src="nnewgeo2.png" width="100" height="100" align="left"/></a>
            <img src="space.png" width="230" height="100" align="left" />
            <form method="post" action="SearchServlet" >
              
                     <img src="search2.png" width="206" height="50" /><br/><img src="space.png" width="150" height="1" align="left" /><input type="context" name="search" style="font-size:140%;"/>
                    <span style="position:relative;top:7px"><input  TYPE=IMAGE SRC="button.png" /></span>
               
            </form>
        </td>
    </tr></table>
    <h2 id="name">時間分析</h2>
    <div id='calendar'></div>
</body>
</html>


