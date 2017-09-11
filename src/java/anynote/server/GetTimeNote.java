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



public class GetTimeNote extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String userTemp=readString.read(request);
        
       String user = null;
           user=userTemp.substring(0, userTemp.length()-1);//把\n刪掉
     System.out.print(user);
           
        
       
        ServletContext sc=getServletContext();
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String id=sc.getInitParameter("account");
        String password=sc.getInitParameter("password");
        String web=sc.getInitParameter("web");
        ArrayList<TimeNote> DB=new<TimeNote> ArrayList();//存取的陣列
        
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
	
	//下query去比較time_note和compare表格之中的noteId,userId一樣的和friendId等於user和狀態不等於下載
        String stmt="SELECT user.userName,time_note.noteId,time_note.userId,friendId,title,content,time,cycle,status,noteType,time_note.img,time_note.sound FROM time_note,compare,user WHERE time_note.userId=user.userId and time_note.noteId=compare.noteId and time_note.userId=compare.userId and status!=0 and compare.noteType=0 and friendId="+user;
        //把狀態設成1
        //noteType 0 TimeNote 1GeoNote
        //status  0不變 1下載 2修改 3刪除
        ResultSet resultSet = statement.executeQuery(stmt);
        String stmtUpdate="UPDATE compare SET status=0 WHERE status!=0 and noteType=0 and friendId="+user;
        //statementUpdate.executeUpdate(stmtUpdate);
	while ( resultSet.next() )
	{
                int noteId=resultSet.getInt("noteId");
                String userName=resultSet.getString("userName");
                System.out.println(userName);
                String friends=resultSet.getString("friendId");
                String userId=resultSet.getString("userId");
                String title=resultSet.getString("title");
                String content=resultSet.getString("content");
                String time=resultSet.getString("time");
                int cycle=resultSet.getInt("cycle");
                int status=resultSet.getInt("status");
                int noteType=resultSet.getInt("noteType");
                String img=resultSet.getString("img");
                String sound=resultSet.getString("sound");
                
                statementUpdate.executeUpdate(stmtUpdate);
               
                TimeNote timeNote=new TimeNote(noteId,userName,userId ,friends, title, content, time, cycle,status,noteType,img,sound);
                DB.add(timeNote);
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
        
        for(TimeNote element:DB)
        {
            JSONObject jObj = new JSONObject();
            try {
                
                jObj.put("noteId",element.noteId);
                jObj.put("userName",element.userName);
                jObj.put("userId",element.userId);
                jObj.put("friendId", element.friends);
                jObj.put("title", element.title);
                jObj.put("content", element.content);
                jObj.put("time", element.time);
                jObj.put("cycle", element.cycle);
                jObj.put("noteType",element.noteType);
                jObj.put("status", element.status);
                jObj.put("img", element.img);
                jObj.put("sound", element.sound);
            } catch (JSONException ex) {
                Logger.getLogger(GetTimeNote.class.getName()).log(Level.SEVERE, null, ex);
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
