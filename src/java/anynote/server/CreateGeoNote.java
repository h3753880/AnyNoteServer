package anynote.server;

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Howard
 */
public class CreateGeoNote extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request,response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        String json = readString.read(request);
        JSONArray jsonArray = null;
        String responseText = null;
        ArrayList<GeoNote> DB=new<GeoNote> ArrayList();//存取的陣列
        ArrayList<FavPlace> favPlace = new ArrayList<FavPlace>();//更改喜愛點排名陣列
        
        try {
            jsonArray=new JSONArray(json);
        } catch (JSONException ex) {
            Logger.getLogger(CreateGeoNote.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(int count=0;count<jsonArray.length();count++)
        {
            //status  0不變 1下載 2修改 3刪除
            //noteType 0 TimeNote 1GeoNote
            GeoNote geoNote;
            try {
                geoNote = new GeoNote(
                  jsonArray.getJSONObject(count).getInt("noteId")
                  ,jsonArray.getJSONObject(count).getString("userId")      
                  ,jsonArray.getJSONObject(count).getString("friends")
                  ,jsonArray.getJSONObject(count).getString("title")
                  ,jsonArray.getJSONObject(count).getString("content")
                  ,jsonArray.getJSONObject(count).getDouble("longitude")
                  ,jsonArray.getJSONObject(count).getDouble("latitude")
                  ,jsonArray.getJSONObject(count).getString("timeStart")
                  ,jsonArray.getJSONObject(count).getString("timeEnd")
                  ,jsonArray.getJSONObject(count).getInt("range")
                  ,jsonArray.getJSONObject(count).getBoolean("getIn")
                  ,jsonArray.getJSONObject(count).getBoolean("getOut")
                  ,1,1,jsonArray.getJSONObject(count).getString("img")
                  ,jsonArray.getJSONObject(count).getString("sound")
                  ,jsonArray.getJSONObject(count).getString("city"));
                System.out.println(jsonArray.getJSONObject(count).getString("timeStart"));
                DB.add(geoNote);
                //存入喜愛點array
                FavPlace tFav=new FavPlace(jsonArray.getJSONObject(count).getString("userId") , jsonArray.getJSONObject(count).getString("favPlace"));
                favPlace.add(tFav);
            } catch (JSONException ex) {
                Logger.getLogger(CreateGeoNote.class.getName()).log(Level.SEVERE, null, ex);
            }catch(NullPointerException ex){
                
            }
        }
        //以上是把json轉成字串
        //以下是與database連結
        ServletContext sc=getServletContext();
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String id=sc.getInitParameter("account");
        String password=sc.getInitParameter("password");
        String web=sc.getInitParameter("web");
        Connection connection = null; // manages connection
        Statement statement = null; // query statement
        Statement statementcompare = null; // query statement
        Statement statementFav = null; // query statement
        Statement statementInsert = null; // query statement
        Statement statementUp = null; // query statement
        Statement statementInsert2 = null; // query statement
        
        try {
            Class.forName(JDBC_DRIVER); // load database driver class

            // establish connection to database
            connection = (Connection) DriverManager.getConnection(web, id, password);

            // create Statement for querying database
            //喜愛點 存入
            for(FavPlace item: favPlace)
            {
                if(item.getPlace().equals("") || item.getPlace()==null)//字串是空的(沒有存喜愛點)
                    continue;
                System.out.println("喜愛點:"+item.getPlace());
                statementFav=(Statement) connection.createStatement();
                String stmtSelect = "SELECT place,count FROM favorite_place WHERE userId=" + item.getUserId();
                ResultSet resultSet = statementFav.executeQuery(stmtSelect);
                if(resultSet.first()==false)//favorite_place為空
                {
                    System.out.println("first FavPlace");
                    statementInsert=(Statement) connection.createStatement();
                    String stmtInsert = "INSERT INTO favorite_place VALUES ('" + item.getPlace() + "'," + "1" + ",'" + item.getUserId() + "')";
                    statementInsert.executeUpdate(stmtInsert);
                    statementInsert.close();
                }
                resultSet.beforeFirst();//將指標移回
                while ( resultSet.next() )
                {
                    int count=resultSet.getInt("count");
                    String place=resultSet.getString("place");
                    if(place.equals(item.getPlace()))//資料庫內已經有此喜愛點 次數+1
                    {
                        System.out.println("repeat FAV");
                        statementUp=(Statement) connection.createStatement();
                        String stmt="UPDATE favorite_place SET count="+(count+1)+" WHERE userId="+ item.getUserId() + " and place='" + item.getPlace()+"'";
                        statementUp.executeUpdate(stmt);
                        statementUp.close();
                        break;
                    }
                    if(resultSet.isLast() && !place.equals(item.getPlace()))//最後一次比對發現無此喜愛點 新增
                    {
                        System.out.println("last FavPlace");
                        statementInsert2=(Statement) connection.createStatement();
                        String stmtInsert2 = "INSERT INTO favorite_place VALUES ('" + item.getPlace() + "'," + "1" + ",'" + item.getUserId() + "')";
                        statementInsert2.executeUpdate(stmtInsert2);
                        statementInsert2.close();
                    }
                }
            }
            //geonote 存入
            for (GeoNote bean : DB) {
                statement = (Statement) connection.createStatement();
                // query database
                //把要存入的值趙有的筆數存入time_note中
                //INSERT INTO geo_note VALUES (...............)
                System.out.println(bean.timeStart);
                String stmt = "INSERT INTO geo_note VALUES (" + bean.noteId + ",'" + bean.userId + "', '" + bean.title + "', '" + bean.content + "', " + bean.longitude + ", " 
                        + bean.latitude + ",'" + bean.timeStart + "','" + bean.timeEnd + "'," + bean.range + "," + bean.getIn + "," + bean.getOut + ",'"+bean.img+ "','"
                        + bean.sound+ "','" + bean.city + "')";
                statement.executeUpdate(stmt);
                statement.close();
                String[] friendId = bean.friends.split("_");//把朋友切割
                for (String friendBean : friendId)//把切割的朋友一個一個存入compare表格中
                {
                    statementcompare = (Statement) connection.createStatement();
                    //INSERT INTO time_note VALUES (noteId, 'userId', 'friendId', 'content', noteType, status)"
                    String stmtcompare = null;
                    if (bean.userId.equals(friendBean)) {
                        stmtcompare = "INSERT INTO compare VALUES (" + bean.noteId + ",'" + bean.userId + "', '" + friendBean + "'," + bean.noteType + "," + 0 
                                +")";
                    } else {
                        stmtcompare = "INSERT INTO compare VALUES (" + bean.noteId + ",'" + bean.userId + "', '" + friendBean + "'," + bean.noteType + "," + bean.status
                                + ")";
                    }
                    statementcompare.executeUpdate(stmtcompare);
                    statementcompare.close();
                }
            }

        } catch (NullPointerException ex) {
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
                connection.close();
            } // end try                                               
            catch (Exception exception) {
                exception.printStackTrace();
                System.exit(1);
            } // end catch                                             
        } // end finally    
    }
}
