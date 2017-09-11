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
public class DeleteNote extends HttpServlet {
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
        int noteType = 0;
        int noteId=0;
        String userId=null;
        try {
            jsonObject=new JSONObject(json);
            noteType=jsonObject.getInt("noteType");
            noteId=jsonObject.getInt("noteId");
            userId=jsonObject.getString("userId");
        } catch (atg.taglib.json.util.JSONException ex) {
            Logger.getLogger(CreateTimeNote.class.getName()).log(Level.SEVERE, null, ex);
        }
        //public TimeNote(int noteId, String friends, String title, 
        //String content, String time, int cycle,int status,int noteType)
        //把db轉成timenote陣列方便存取
        
    
        //以上是把json轉成字串
       //以下是與database連結
     	
        
     ServletContext sc=getServletContext();
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String id=sc.getInitParameter("account");
        String password=sc.getInitParameter("password");
        String web=sc.getInitParameter("web");
        Connection connection = null; // manages connection
        Statement statement = null; // query statement
  
        
    try {
        Class.forName( JDBC_DRIVER ); // load database driver class
            
                // establish connection to database
                connection = (Connection) DriverManager.getConnection(web, id, password);
                
            
	// create Statement for querying database
           
	statement = (Statement) connection.createStatement();
        // query database
        //把要存入的值趙有的筆數存入time_note中
	//INSERT INTO time_note VALUES (noteId, 'userId', 'title', 'content', 'time', cycle)"
        //status  0不變 1下載 2修改 3刪除
        String stmt="UPDATE compare SET status=3 WHERE  noteType="+noteType+" and userId="+userId+" and noteId="+noteId;
	statement.executeUpdate(stmt);			
        statement.close();
     

        
		
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