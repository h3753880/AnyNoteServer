/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package anynote.server;

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.io.IOException;
import java.io.PrintWriter;
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
 * @author tony
 */
@WebServlet(name = "DeleteNote", urlPatterns = {"/DeleteNote"})
public class ModifyTimeNote extends HttpServlet {
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
        JSONObject jsonObject = null;
        String responseText = null;
    TimeNote timeNote=new TimeNote();
        String userId=null;
        try {
            jsonObject=new JSONObject(json);
            timeNote.noteId=jsonObject.getInt("noteId");
                 timeNote.userId=jsonObject.getString("userId") ;    
                 
                 timeNote.title=jsonObject.getString("title");
                 timeNote.content=jsonObject.getString("content");
                 timeNote.time=jsonObject.getString("time");
                timeNote.cycle=jsonObject.getInt("cycle");
                 timeNote.status=2;
                 timeNote.cycle=0;
                        
        } catch (atg.taglib.json.util.JSONException ex) {
            Logger.getLogger(CreateTimeNote.class.getName()).log(Level.SEVERE, null, ex);
        }
        //public TimeNote(int noteId, String friends, String title, 
        //String content, String time, int cycle,int status,int noteType)
        //把db轉成timenote陣列方便存取
        
    System.out.print(json);
        //以上是把json轉成字串
       //以下是與database連結
     	
        
     ServletContext sc=getServletContext();
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String id=sc.getInitParameter("account");
        String password=sc.getInitParameter("password");
        String web=sc.getInitParameter("web");
        Connection connection = null; // manages connection
        Statement statement = null; // query statement
        Statement statementNote = null;
  
        
    try {
        Class.forName( JDBC_DRIVER ); // load database driver class
            
                // establish connection to database
                connection = (Connection) DriverManager.getConnection(web, id, password);
                
            
	// create Statement for querying database
           
	statement = (Statement) connection.createStatement();
        statementNote= (Statement) connection.createStatement();
        // query database
        //把要存入的值趙有的筆數存入time_note中
	//INSERT INTO time_note VALUES (noteId, 'userId', 'title', 'content', 'time', cycle)"
        //status  0不變 1下載 2修改 3刪除
        String stmt="UPDATE compare SET status=2 WHERE  noteType= 0 and userId="+timeNote.userId+" and friendId!="+timeNote.userId+" and noteId="+timeNote.noteId+" and status=0";
	statement.executeUpdate(stmt);			
        statement.close();
        statementNote= (Statement) connection.createStatement();
        stmt="UPDATE time_note SET title='"+timeNote.title+"', content='"+timeNote.content+"', time='"+timeNote.time+"' WHERE userId="+timeNote.userId+" and noteId="+timeNote.noteId;
        statementNote.executeUpdate(stmt);			
        statementNote.close();

        
		
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