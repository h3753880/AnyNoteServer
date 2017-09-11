package anynote.server;

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.io.IOException;
import java.io.PrintWriter;
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



public class GetGeoNote extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String userTemp=readString.read(request);
        
       String user = null;
           user=userTemp.substring(0, userTemp.length()-1);//把\n刪掉
       //System.out.print(user);
           
        
       
        ServletContext sc=getServletContext();
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String id=sc.getInitParameter("account");
        String password=sc.getInitParameter("password");
        String web=sc.getInitParameter("web");
        ArrayList<GeoNote> DB=new<GeoNote> ArrayList();//存取的陣列
        
        Connection connection = null; // manages connection
        Statement statement = null; // query statement
        Statement statementUpdate = null; // query statement
        
        
        
    try {
        Class.forName( JDBC_DRIVER ); // load database driver class
 
                // establish connection to database
                connection = (Connection) DriverManager.getConnection(web, id, password);
            
	// create Statement for querying database
	statement = (Statement) connection.createStatement();
        statementUpdate=(Statement) connection.createStatement();
        // query database
	//noteId userId title content longitude latitude timeStart timeEnd range getIn getOut img
	//下query去比較geo_note和compare表格之中的noteId,userId一樣的和friendId等於user和狀態不等於下載
        String stmt="SELECT user.userName,geo_note.noteId,geo_note.userId,friendId,title,content,geo_note.longitude,geo_note.latitude,geo_note.timeStart,geo_note.timeEnd,geo_note.range,geo_note.getIn,geo_note.getOut,status,noteType,geo_note.img,geo_note.sound FROM geo_note,compare,user WHERE geo_note.userId=user.userId and geo_note.noteId=compare.noteId and geo_note.userId=compare.userId and status!=0 and compare.noteType=1 and friendId="+user;
        //把狀態設成1
        //noteType 0 TimeNote 1GeoNote
        //status  0不變 1下載 2修改 3刪除
        ResultSet resultSet = statement.executeQuery(stmt);
        String stmtUpdate="UPDATE compare SET status=0 WHERE status!=0 and noteType=1 and friendId="+user;
        //statementUpdate.executeUpdate(stmtUpdate);
	while ( resultSet.next() )
	{
                int noteId=resultSet.getInt("noteId");
                String friends=resultSet.getString("friendId");
                String userName=resultSet.getString("userName");
                String userId=resultSet.getString("userId");
                String title=resultSet.getString("title");
                String content=resultSet.getString("content");
                double longtitude=resultSet.getDouble("longitude");
                double latitude=resultSet.getDouble("latitude");
                String startTime=resultSet.getString("timeStart");
                String finishTime=resultSet.getString("timeEnd");
                double range=resultSet.getDouble("range");
                boolean getIn=resultSet.getBoolean("getIn");
                boolean getOut=resultSet.getBoolean("getOut");
                int status=resultSet.getInt("status");
                int noteType=resultSet.getInt("noteType");
                String img=resultSet.getString("img");
                String sound=resultSet.getString("sound");
                String city="";
                
                statementUpdate.executeUpdate(stmtUpdate);
           //GeoNote(int noteId,String userId,String friends,String title,
//String content,double longitude,double latitude,String timeStart,String timeEnd,int range,boolean getIn,boolean getOut,int status,int noteType)
               
                GeoNote geoNote=new GeoNote(noteId,userId ,friends, title, content, longtitude,latitude,startTime,finishTime,range,getIn,getOut,status,noteType,img,userName,sound,city);
                DB.add(geoNote);
	}		
    }   
        catch ( SQLException sqlException ) 
        {
	sqlException.printStackTrace();
	System.exit( 1 );
        } // end catch
	catch ( ClassNotFoundException classNotFound ) 
	{
            classNotFound.printStackTrace();            
            System.exit( 1 );
	} // end catch
	finally // ensure statement and connection are closed properly
	{                                                             
            try                                                        
            {                                                                                          
		statement.close();
                statementUpdate.close();
		connection.close();                                     
            } // end try                                               
            catch ( Exception exception )                        
            {                                                          
		exception.printStackTrace();                                     
		System.exit( 1 );                                       
            } // end catch                                             
        } // end finally    
                //取得DB所有資料
        
        JSONArray jArray = new JSONArray();
        
        for(GeoNote element:DB)
        {
            JSONObject jObj = new JSONObject();
            try {
                /*double range=resultSet.getDouble("range");
                boolean getIn=resultSet.getBoolean("getIn");
                boolean getOut=resultSet.getBoolean("getOut");
                int status=resultSet.getInt("status");
                int noteType=resultSet.getInt("noteType");
                String img=resultSet.getString("img");*/
                jObj.put("noteId",element.noteId);
                jObj.put("userId",element.userId);
                jObj.put("friends", element.friends);
                jObj.put("userName",element.userName);
                jObj.put("title", element.title);
                jObj.put("content", element.content);
                jObj.put("latitude", element.latitude);
                jObj.put("longitude", element.longitude);
                jObj.put("startTime", element.timeStart);
                jObj.put("finishTime", element.timeEnd);
                jObj.put("range", element.range);
                jObj.put("getIn",element.getIn);
                jObj.put("getOut", element.getOut);
                
                jObj.put("status", element.status);
                jObj.put("img", element.img);
                jObj.put("sound", element.sound);
            } catch (JSONException ex) {
                Logger.getLogger(GetGeoNote.class.getName()).log(Level.SEVERE, null, ex);
            }
            jArray.put(jObj);
        }
        
        System.out.print(jArray.toString()+"\n");
        out.write(jArray.toString());
        out.flush();
        out.close();
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        doGet(request,response);
    }

    
}