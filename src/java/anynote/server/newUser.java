/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package anynote.server;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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
@WebServlet(name = "newUser", urlPatterns = {"/newUser"})
public class newUser extends HttpServlet {
    static int test=0;
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
        
        String userIdAndName = readString.read(request);
        String responseText = null;
        String[] user=userIdAndName.split("_");
        //user[0]是userId,user[1]是userName
        //以上是把json轉成字串
       //以下是與database連結
        
     ServletContext sc=getServletContext();
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String id=sc.getInitParameter("account");
        String password=sc.getInitParameter("password");
        String web=sc.getInitParameter("web");
        //ArrayList<TimeNote> DB=new<TimeNote> ArrayList();//存取的陣列
        
        Connection connection = null; // manages connection
        Statement statement = null; // query statement
        Statement statementCheck=null;
        
    try {
        Class.forName( JDBC_DRIVER ); // load database driver class
            
                // establish connection to database
        connection = (Connection) DriverManager.getConnection(web, id, password);
           
	// create Statement for querying database
	statement = (Statement) connection.createStatement();
        statementCheck=(Statement) connection.createStatement();
        // query database
	//int count = 0;
        String stmtCheck="SELECT userId FROM user WHERE userId="+user[0];
        ResultSet resultSet = statementCheck.executeQuery(stmtCheck);
	if(!resultSet.next())
        {
            String stmt="INSERT INTO user VALUES ('"+ user[0] +"','"+ user[1] +"')";
            statement.executeUpdate(stmt);			
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
                statementCheck.close();
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
        
        
        
        
        
        
        
        
        
        
        
        /*
        try {
            jsonObject = new JSONObject(json);
            responseText = "帳號:" + jsonObject.getString("number")+ " 名子:"+jsonObject.getString("name");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }*/
        
       
        
  
    }

   
}
