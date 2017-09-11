/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package anynote.server;

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;





/**
 *
 * @author Howard
 */
@WebServlet(name = "Create", urlPatterns = {"/Create"})
public class CreateTimeNote extends HttpServlet {
    //static int test=0;
@Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    
            doGet(request,response);
        }
    
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        String json = readString.read(request);
        JSONArray jsonArray = null;
        String responseText = null;
        ArrayList<TimeNote> DB=new<TimeNote> ArrayList();//存取的陣列
        try {
            jsonArray=new JSONArray(json);
        } catch (atg.taglib.json.util.JSONException ex) {
            Logger.getLogger(CreateTimeNote.class.getName()).log(Level.SEVERE, null, ex);
        }
        //public TimeNote(int noteId, String friends, String title, 
        //String content, String time, int cycle,int status,int noteType)
        //把db轉成timenote陣列方便存取
        for(int count=0;count<jsonArray.length();count++)
        {
            //status  0不變 1下載 2修改 3刪除
            //noteType 0 TimeNote 1GeoNote
            //TimeNote timeNote = null;
        
            try {
                TimeNote timeNote = new TimeNote(
                 jsonArray.getJSONObject(count).getInt("noteId")
                 ,jsonArray.getJSONObject(count).getString("userId")      
                 ,jsonArray.getJSONObject(count).getString("friends")
                 ,jsonArray.getJSONObject(count).getString("title")
                 ,jsonArray.getJSONObject(count).getString("content")
                 ,jsonArray.getJSONObject(count).getString("time")
                 ,jsonArray.getJSONObject(count).getInt("cycle")
                 ,1
                 ,0
                  , jsonArray.getJSONObject(count).getString("img")
                  , jsonArray.getJSONObject(count).getString("sound")
                 );
                
            DB.add(timeNote);
            } 
            catch (JSONException ex) {
                Logger.getLogger(CreateTimeNote.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch(NullPointerException ex)
            {
                
            }
            //DB.add(timeNote);
        }
      
    
        //以上是把json轉成字串
       //以下是與database連結
     System.out.print(jsonArray.length()); 	
        
     ServletContext sc=getServletContext();
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String id=sc.getInitParameter("account");
        String password=sc.getInitParameter("password");
        String web=sc.getInitParameter("web");
        Connection connection = null; // manages connection
        Statement statement = null; // query statement
        Statement statementcompare = null; // query statement
        
    try {
        Class.forName( JDBC_DRIVER ); // load database driver class
 
                // establish connection to database
                connection = (Connection) DriverManager.getConnection(web, id, password);
            
	// create Statement for querying database
        for(TimeNote bean:DB)
        {        
	statement = (Statement) connection.createStatement();
        // query database
        //把要存入的值趙有的筆數存入time_note中
	//INSERT INTO time_note VALUES (noteId, 'userId', 'title', 'content', 'time', cycle)"
        String stmt="INSERT INTO time_note VALUES ("+ bean.noteId +",'"+bean.userId+"', '"+bean.title+"', '"+bean.content+"', '"+bean.time+"', "+bean.cycle+", '"+bean.img+"', '"+bean.sound+"' )";
	statement.executeUpdate(stmt);			
        statement.close();
        String[] friendId=bean.friends.split("_");//把朋友切割
            for(String friendBean:friendId)//把切割的朋友一個一個存入compare表格中
            {
                statementcompare = (Statement) connection.createStatement();
                //INSERT INTO time_note VALUES (noteId, 'userId', 'friendId', 'content', noteType, status)"
                String stmtcompare=null;
                 if(bean.userId.equals(friendBean)) 
                    stmtcompare="INSERT INTO compare VALUES ("+ bean.noteId +",'"+bean.userId+"', '"+friendBean+"',"+bean.noteType+","+0+")";
                else stmtcompare="INSERT INTO compare VALUES ("+ bean.noteId +",'"+bean.userId+"', '"+friendBean+"',"+bean.noteType+","+bean.status+")";
                statementcompare.executeUpdate(stmtcompare);			
                statementcompare.close(); 
            }
        }
		
    }
     catch(NullPointerException ex)
            {
                
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
		connection.close();                                     
            } // end try                                               
            catch ( Exception exception )                        
            {                                                          
		exception.printStackTrace();                                     
		System.exit( 1 );                                       
            } // end catch                                             
        } // end finally    
                //取得DB所有資料
        
        
       
        
  
    }

   


}
