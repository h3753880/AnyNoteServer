<%-- 
    Document   : home
    Created on : 2012/10/19, 下午 09:07:15
    Author     : Howard
--%>

<%@page import="anynote.server.FavPlace"%>
<%@page import="anynote.server.CityMap"%>
<%@page import="anynote.server.City"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>AnyNote-地理查詢</title>
        <!--for map-->
        <style type="text/css">
              html { height: 100% }
              body { height: 100%; margin: 2em; padding: 0px ; margin-top: 40px;
		font-size: 14px;
		font-family: "Lucida Grande",Helvetica,Arial,Verdana,sans-serif;}
              #map_canvas { height: 100% }    
              #img1{background-image: url(bback01.jpg);border-radius:25px;  }
              h2{font-weight: bolder;font-family: Microsoft JhengHei;
                        text-shadow: 5px 5px 5px black;/*shadow*/
			border-bottom: 4px solid #BE6100;
			border-left:30px solid #BE6100;
			padding-bottom: 10px;
                        padding-left: 1em;
                        width: 95%; }
        </style>
        <!--Load the AJAX API-->
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script src="https://maps.googleapis.com/maps/api/js?sensor=false"></script>
        <script type="text/javascript">       
            
            // Load the Visualization API and the piechart package.
            google.load('visualization', '1.0', {'packages':['corechart']});
            google.load('visualization', '1', {packages: ['table']});
            // Set a callback to run when the Google Visualization API is loaded.
            google.setOnLoadCallback(drawChartPie);
            google.setOnLoadCallback(drawChartCol);
            
            var cityName=new Array();
            var cityNumber=new Array();
            //get data and prepare for map
            <%
                ArrayList<CityMap> cityMap = (ArrayList<CityMap>) session.getAttribute("cityMap");
                ArrayList<CityMap> cityBig = (ArrayList<CityMap>) session.getAttribute("cityBig");
            %>
            //get data and prepare for pie chart
            <%
                String name="";
                int number=0;
                ArrayList<City> city = (ArrayList<City>) session.getAttribute("pieData");
                String prepare="";
                String prepare2="";
                String[] tempName=new String[city.size()];
                int[] tempNumber=new int[city.size()];
                for(int i=0;i<city.size();i++)
                {        
                    name=city.get(i).getCity();
                    number=city.get(i).getNumber();
                    tempName[i]=name;
                    tempNumber[i]=number;
            %>
                    cityName[ <%=i%> ] = "<%=name%>";
                    cityNumber[ <%=i%> ] = <%=number%>;
            <%
                }
            %> 
                
            //prepare data for col chart
            <%
                for (int i = 0; i < tempName.length; i++) {
                        if (i + 1 == tempName.length) {
                            prepare += "'" + tempName[i] + "'";
                            prepare2 += " "+  tempNumber[i] + " ";
                        } else {
                            prepare += "'" + tempName[i] + "', ";
                            prepare2 += " " + tempNumber[i] + ", ";
                        }
                }
                
            %> 
                
            // Callback that creates and populates a data table,
            // instantiates the pie chart, passes in the data and
            // draws it.
            //google chart 繪製pie chart
            function drawChartPie() {

                // Create the data table.
                var data = new google.visualization.DataTable();
                data.addColumn('string', 'Topping');
                data.addColumn('number', 'Slices');
                for(var k=0;k<cityName.length;k++)//input server data & produce pie chart
                {
                    data.addRows([
                        [cityName[k],cityNumber[k]]
                    ]);
                }
                // Set chart options
                var options = {
                    'width':400,
                    'height':500,'title':'城市分析(圓餅圖)' };

                // Instantiate and draw our chart, passing in some options.
                var chart = new google.visualization.PieChart(document.getElementById('chart_div_pie'));
                chart.draw(data, options);
            }
            //google chart 繪製column chart
            function drawChartCol() {
                var data = google.visualization.arrayToDataTable([
                            ['', <%=prepare%>],
                            ['',  <%=prepare2%>]                         
                        ]);

                var options = {'height':500,'width':450,'title':'城市分析(柱狀圖)'};

                var chart = new google.visualization.ColumnChart(document.getElementById('chart_div_col'));
                chart.draw(data, options);
            }
            
            //google map           
            /*initialize google map*/
            var map;
            var marker=new Array();
            function initializeMap() {
                var latlng = new google.maps.LatLng(23.916667, 120.683333 );
                var myOptions = {
                    zoom: 8,
                    center: latlng,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                };
                map = new google.maps.Map(document.getElementById("map_canvas"),myOptions);
                /*大城市提醒點標記*/
                <%
                for(int i=0;i<cityBig.size();i++)
                {
                    String tempCity=cityBig.get(i).getCity();
                    double templat=cityBig.get(i).getLatitude();
                    double templng=cityBig.get(i).getLongitude();
                %>
                    var myLatlng = new google.maps.LatLng( <%=templat%> , <%=templng%> );
                    /*mark point*/
                    marker[ <%=i%> ] = new google.maps.Marker({
                        position: myLatlng, 
                        map: map,
                        title: "<%=tempCity%>"
                    });
                    
                    /*click listener*/
                    google.maps.event.addListener(marker[ <%=i%> ], 'click', function() {
                        map.setZoom(12);
                        var darwin = new google.maps.LatLng( <%=templat%> , <%=templng%> );
                        map.setCenter(darwin);
                        //清除城市標記
                        for(var i=0;i< <%=cityBig.size()%> ; i++ )
                            marker[i].setMap(null);
                        marker.length=0;
                        //標記所有地理提醒
                        markAllPoint();
                    });
                <%
                }%> 
            }
            //標記所有地理提醒
            function markAllPoint(){
                <%
                for(int k=0;k<cityMap.size();k++)
                {
                    String tCity=cityMap.get(k).getCity();
                    double tLat=cityMap.get(k).getLatitude();
                    double tLng=cityMap.get(k).getLongitude();
                    String tTitle=cityMap.get(k).getTitle();
                    String tContent=cityMap.get(k).getContent();
                    String tFrom=cityMap.get(k).userName;
                    String tTo=cityMap.get(k).friendName;
                    tFrom=tFrom.substring(0,tFrom.length()-1);//切除換行
                    tTo=tTo.substring(0,tTo.length()-1);//切除逗點
                %>
                var geoLatlng = new google.maps.LatLng( <%=tLat%> , <%=tLng%> );
                /*mark point*/
                marker[ <%=k%> ] = new google.maps.Marker({
                    position: geoLatlng, 
                    map: map,
                    title: "<%=tTitle%>"
                });
                
                attachSecretMessage(marker[ <%=k%> ],"<%=tTitle%>","<%=tContent%>","<%=tFrom%>","<%=tTo%>");

                <%
                }%>
            }
            //設定地圖上的對話框
            function attachSecretMessage(marker,title,content,from,to) {
                    var infowindow = new google.maps.InfoWindow(
                    { content: "from:"+from+"<br>to:"+to+"<br><br>提醒標題:"+title+"<br>"+"提醒內容:"+content,
                        size: new google.maps.Size(100,100)
                    });
                    google.maps.event.addListener(marker, 'click', function() {
                        infowindow.open(map,marker);
                    });
            }
            /*喜愛點表格*/
            <%
                ArrayList<FavPlace> order = (ArrayList<FavPlace>) session.getAttribute("order");
            %>
            var visualization;
            var data;
            var options = {'showRowNumber': true};
            function drawVisualization() {
              // Create and populate the data table.
              var dataAsJson =
              {cols:[
                {id:'A',label:'喜愛提醒點',type:'string'},
                {id:'B',label:'次數',type:'number'}],
              rows:[
                  <% for(FavPlace item:order){ %>
                        {c:[{v:'<%= item.getPlace() %>'},{v:<%= item.getCount() %>}]},       
                  <%}%>
                   ]
              };
              data = new google.visualization.DataTable(dataAsJson);

              // Set paging configuration options
              // Note: these options are changed by the UI controls in the example.
              options['page'] = 'enable';
              options['pageSize'] = 10;
              options['pagingSymbols'] = {prev: 'prev', next: 'next'};
              options['width'] = 400;
              // Create and draw the visualization.
              visualization = new google.visualization.Table(document.getElementById('table'));
              draw();
              setCustomPagingButtons();
            }
            //draw table
            function draw() {
              visualization.draw(data, options);
            }
            // Sets custom paging symbols "Prev"/"Next"
            function setCustomPagingButtons() {
              options['pagingSymbols'] = {next: 'next', prev: 'prev'};
              draw();  
            }
            google.setOnLoadCallback(drawVisualization);
        </script>
    </head>
    <body onload="initializeMap()">
        <table style="width:100%;" align="center" border="0"><tr>
        <td id="img1"><img src="title.png" width="250" height="130" align="left"/>
            <br/>
            <img src="space.png" width="40" height="100" align="left" />
            <a href="timeNoteCalendar.jsp"><img src="nnewtime2.png" width="100" height="100" align="left" /></a>
            <img src="space.png" width="50" height="100" align="left" />
            <a href="geoNoteMap.jsp"><img src="nnewgeo2.png" width="100" height="100" align="left"/></a>
            <img src="space.png" width="230" height="100" align="left" />
        </td>
    </tr></table>
        <h2 id="name">圖表分析</h2>
        <table border="0" width="85%" align="center">
            <tr>
                <td width="30%">
                    <div id="chart_div_pie"></div>
                </td>
                <td width="30%">
                    <div id="chart_div_col"></div>
                </td>
                <td width="30%">
                    <div id="table"></div>
                </td> 
            </tr>
        </table>
        <h2 id="name">地圖分析</h2>
        <div id="map_canvas" style="height: 90%; top:60px; border: 1px solid black;"></div>
    </body>
</html>
