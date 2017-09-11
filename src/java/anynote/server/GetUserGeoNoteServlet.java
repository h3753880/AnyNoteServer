/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package anynote.server;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Howard
 */
public class GetUserGeoNoteServlet extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
       

        try {
            HttpSession session = request.getSession();
            ServletContext sc = getServletContext();
            String JDBC_DRIVER = "com.mysql.jdbc.Driver";
            String id = sc.getInitParameter("account");
            String password = sc.getInitParameter("password");
            String web = sc.getInitParameter("web");
            
            Connection connection = null; // manages connection
            Statement statement = null; // query statement
            Statement statement2 = null; // query statement
            Statement statementOrder = null; // query statement
            ArrayList<String> cityName = new ArrayList<String>();
            ArrayList<Integer> cityNumber = new ArrayList<Integer>();
            ArrayList<CityMap> cityMap = new ArrayList<CityMap>();// for showing map
            ArrayList<CityMap> cityBig = new ArrayList<CityMap>(); // for showing big map
            ArrayList<FavPlace> order=new ArrayList<FavPlace>();//for ordering favorite place
            
            try {
                Class.forName(JDBC_DRIVER); // load database driver class

                // establish connection to database
                connection = (Connection) DriverManager.getConnection(web, id, password);

                // create Statement for querying database
                statement = (Statement) connection.createStatement();
                // query database 別人寄給我
                //String stmt = "SELECT noteId,city,longitude,latitude,title,content FROM geo_note WHERE userId="+session.getAttribute("webUserId");
                String stmt= "SELECT user.userName,geo_note.userId,geo_note.noteId,city,longitude,latitude,title,content " + "FROM geo_note,compare,user "
                        + "WHERE geo_note.userId=user.userId "
                        + "and geo_note.noteId=compare.noteId "
                        + "and geo_note.userId=compare.userId "
                        + "and compare.noteType=1 and friendId=" + session.getAttribute("webUserId");
                ResultSet resultSet = statement.executeQuery(stmt);
                System.out.println("GeoServ");
                while (resultSet.next()) {
                    String userName=resultSet.getString("userName");
                    String userId=resultSet.getString("userId");
                    int noteId=resultSet.getInt("noteId");
                    
                    String title = resultSet.getString("title");
                    String content = resultSet.getString("content");
                    String city = resultSet.getString("city");
                    double longitude = resultSet.getDouble("longitude");
                    double latitude = resultSet.getDouble("latitude");
                    CityMap perCityMap = new CityMap(city,latitude,longitude,title,content,userName,noteId,userId);
                    perCityMap.friendName+="me,";
                    cityMap.add(perCityMap);
                    /*for (int i = 0; i < cityName.size(); i++) {
                        if (cityName.get(i).equals(city)) {
                            cityNumber.set(i, cityNumber.get(i) + 1);
                            break;
                        }
                        if (i + 1 == cityName.size()) {
                            cityName.add(city);
                            cityNumber.add(1);
                            cityBig.add(new CityMap(city,latitude,longitude));//for big map
                            break;
                        }
                    }
                    if (cityName.isEmpty()) {
                        cityName.add(city);
                        cityNumber.add(1);
                        cityBig.add(new CityMap(city,latitude,longitude));//for big map
                    }*/
                }
                
                //我寄給別人
                statement2 = (Statement) connection.createStatement();
                String stmt2 = "SELECT user.userName,"
                        + "geo_note.noteId,"
                        + "geo_note.userId "
                        + "FROM geo_note,"
                        + "compare,"
                        + "user "
                        + "WHERE friendId=user.userId "
                        + "and geo_note.noteId=compare.noteId "
                        + "and geo_note.userId=compare.userId "
                        + "and compare.noteType=1 and geo_note.userId=" + session.getAttribute("webUserId");
                ResultSet resultSet2 = statement2.executeQuery(stmt2);
                while (resultSet2.next()) //比對 刪掉自己
                {
                    for(CityMap temp:cityMap)
                    {
                        if(temp.noteId==resultSet2.getInt("noteId")&&temp.userId.equals(resultSet2.getString("userId"))&&!temp.userName.equals(resultSet2.getString("userName")))
                        {
                           if(temp.friendName.equals("me,"))
                               temp.friendName="";
                           temp.friendName+=resultSet2.getString("userName").substring(0,resultSet2.getString("userName").length()-1)+",";   
                        }
                    }
                }
                for(CityMap item:cityMap)//prepare map data
                {
                    System.out.println("ㄎㄎ"+item.userName+"testes"+item.friendName);
                    for (int i = 0; i < cityName.size(); i++) {
                        if (cityName.get(i).equals(item.getCity())) {
                            cityNumber.set(i, cityNumber.get(i) + 1);
                            break;
                        }
                        if (i + 1 == cityName.size()) {
                            cityName.add(item.getCity());
                            cityNumber.add(1);
                            cityBig.add(new CityMap(item.getCity(),item.getLatitude(),item.getLongitude()));//for big map
                            break;
                        }
                    }
                    if (cityName.isEmpty()) {
                        cityName.add(item.getCity());
                        cityNumber.add(1);
                        cityBig.add(new CityMap(item.getCity(),item.getLatitude(),item.getLongitude()));//for big map
                    }
                }
                
                
                /*following is getting the favorite places*/
                statementOrder = (Statement) connection.createStatement();
                String stmtOrder = "SELECT place,count FROM favorite_place WHERE userId="+session.getAttribute("webUserId");
                ResultSet resultSetPlace = statement.executeQuery(stmtOrder);
                while (resultSetPlace.next()) {
                    String place=resultSetPlace.getString("place");
                    int count=resultSetPlace.getInt("count");
                    order.add(new FavPlace(place,count));
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                System.exit(1);
            } // end catch
            catch (ClassNotFoundException classNotFound) {
                classNotFound.printStackTrace();
                System.exit(1);
            } // end catch
            finally // ensure statement and connection are closed properly
            {
                try {
                    statement.close();
                    statementOrder.close();
                    connection.close();
                } // end try                                               
                catch (Exception exception) {
                    exception.printStackTrace();
                    System.exit(1);
                } // end catch                                             
            } // end finally    

            /*傳送DATA至主頁*/
            ArrayList<City> data = new ArrayList<City>();
            for (int i = 0; i < cityName.size(); i++) {
                System.out.println("GeoCity");
                City temp = new City(cityName.get(i), cityNumber.get(i));
                data.add(temp);
            }
            RequestDispatcher view;
            session.setAttribute("pieData", data);
            session.setAttribute("cityMap", cityMap);
            session.setAttribute("cityBig", cityBig);
            session.setAttribute("order", order);
            view = request.getRequestDispatcher("timeNoteCalendar.jsp");
            view.forward(request, response);

        } catch (NullPointerException ex) {
            RequestDispatcher view = request.getRequestDispatcher("fbLogin.jsp");
            view.forward(request, response);
        }
    }
}
