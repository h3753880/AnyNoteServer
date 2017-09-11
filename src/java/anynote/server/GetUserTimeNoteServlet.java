package anynote.server;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import anynote.server.TimeNote;
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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author tony
 */
@WebServlet(name = "GetUserTimeNoteServlet", urlPatterns = {"/GetUserTimeNoteServlet"})
public class GetUserTimeNoteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");
            HttpSession session = request.getSession();
            PrintWriter out = response.getWriter();
            //String webUserId = new String(request.getParameter("webUserId").getBytes("ISO-8859-1"), "UTF-8");
            String webUserId="100000062901382";
            session.setAttribute("webUserId", webUserId);
            


            ServletContext sc = getServletContext();
            String JDBC_DRIVER = "com.mysql.jdbc.Driver";
            String id = sc.getInitParameter("account");
            String password = sc.getInitParameter("password");
            String web = sc.getInitParameter("web");
            ArrayList<TimeNote> DB = new <TimeNote> ArrayList();//存取的陣列

            Connection connection = null; // manages connection
            Statement statement = null; // query statement
            Statement statement2 = null; // query statement



            try {
                Class.forName(JDBC_DRIVER); // load database driver class

                // establish connection to database
                connection = (Connection) DriverManager.getConnection(web, id, password);
                // create Statement for querying database
                statement = (Statement) connection.createStatement();
                statement2 = (Statement) connection.createStatement();
                // query database
                System.out.println("TimeUserID:"+session.getAttribute("webUserId"));
                //下query去比較time_note和compare表格之中的noteId,userId一樣的和friendId一樣的下載
                String stmt = "SELECT user.userName,"
                        + "time_note.noteId,"
                        + "time_note.userId,"
                        + "friendId,"
                        + "title,"
                        + "content,"
                        + "time,"
                        + "cycle,"
                        + "status,"
                        + "noteType,"
                        + "time_note.img "
                        + "FROM time_note,"
                        + "compare,"
                        + "user "
                        + "WHERE time_note.userId=user.userId "
                        + "and time_note.noteId=compare.noteId "
                        + "and time_note.userId=compare.userId "
                        + "and compare.noteType=0 and friendId=" + session.getAttribute("webUserId");

                ResultSet resultSet = statement.executeQuery(stmt);
                System.out.println("finish");
                //解析query並把資料放入ArrayList中
                while (resultSet.next()) {
                    System.out.println("TimeResult");
                    int noteId = resultSet.getInt("noteId");
                    String userName = resultSet.getString("userName");
                    System.out.println(userName);
                    String friends = resultSet.getString("friendId");
                    String userId = resultSet.getString("userId");
                    String title = resultSet.getString("title");
                    String content = resultSet.getString("content");
                    String time = toTime(resultSet.getString("time"));
                    int cycle = resultSet.getInt("cycle");
                    int status = resultSet.getInt("status");
                    int noteType = resultSet.getInt("noteType");
                    String img = resultSet.getString("img");
                    TimeNote timeNote = new TimeNote(noteId, userName, userId, friends, title, content, time, cycle, status, noteType, img, "");
                   
                    timeNote.friendName+="me,";
                    DB.add(timeNote);
                }
                statement2 = (Statement) connection.createStatement();
                String stmt2 = "SELECT user.userName,"
                        + "time_note.noteId,"
                        + "time_note.userId "
                        + "FROM time_note,"
                        + "compare,"
                        + "user "
                        + "WHERE friendId=user.userId "
                        + "and time_note.noteId=compare.noteId "
                        + "and time_note.userId=compare.userId "
                        + "and compare.noteType=0 and time_note.userId=" + session.getAttribute("webUserId");

                ResultSet resultSet2 = statement2.executeQuery(stmt2);
                while (resultSet2.next()) 
                {
                   
                   for(TimeNote temp:DB)
                    {
                       if(temp.noteId==resultSet2.getInt("noteId")&&temp.userId.equals(resultSet2.getString("userId"))&&!temp.userName.equals(resultSet2.getString("userName")))
                        {
                           if(temp.friendName.equals("me,"))temp.friendName="";
                          temp.friendName+=resultSet2.getString("userName").substring(0,resultSet2.getString("userName").length()-1)+",";   
                        }
                        
                    }

            
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
                    statement2.close();
                    connection.close();
                } // end try                                               
                catch (Exception exception) {
                    exception.printStackTrace();
                    System.exit(1);
                } // end catch                                             
            } // end finally    
            //把資料放成能json被fullcalendar解析的格式
            //重要的有title
            //ID
            //start 格式為2012-10-12
            //其他就放一些日後需要使用的資料
            JSONArray jArray = new JSONArray();
            for (TimeNote element : DB) {
                JSONObject jObj = new JSONObject();
                try {
                    jObj.put("id", element.userId + element.noteId);
                    jObj.put("title", element.title);
                    
                    jObj.put("start", element.time);
                    System.out.println("title:"+element.title);
             
                    jObj.put("noteId", element.noteId);
                    jObj.put("color", "#BE6100");
                    jObj.put("userName", element.userName);
                    jObj.put("userId", element.userId);
                    jObj.put("friendId", element.friends);
                    jObj.put("content", element.content);
                    jObj.put("cycle", element.cycle);
                    jObj.put("noteType", element.noteType);
                    jObj.put("status", element.status);
                    jObj.put("img", element.img);
                    jObj.put("friendName", element.friendName.substring(0, element.friendName.length()-1));
                } catch (JSONException ex) {
                    // Logger.getLogger(GetTimeNote.class.getName()).log(Level.SEVERE, null, ex);
                }
                jArray.put(jObj);

            }
            //把資料放入以便日後好存取
            session.setAttribute("jArray", jArray);
            session.setAttribute("DB", DB);

            RequestDispatcher view = request.getRequestDispatcher("GetUserGeoNoteServlet");
            view.forward(request, response);
        } catch (NullPointerException ex) {
            RequestDispatcher view = request.getRequestDispatcher("fbLogin.jsp");
            view.forward(request, response);
            // Logger.getLogger(GetTimeNote.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        doGet(request, response);
    }
   public String toTime(String input){
       String temp[]=input.split("-");
    
       String output;
       //System.out.println(temp[1].length());
       temp[1]=Integer.toString(Integer.parseInt(temp[1])+1);
       if(temp[1].length()<2)temp[1]="0"+temp[1];
        if(temp[2].length()<2)temp[2]="0"+temp[2];
         if(temp[3].length()<2)temp[3]="0"+temp[3]; 
         if(temp[4].length()<2)temp[4]="0"+temp[4];
               output=temp[0]+"-"+temp[1]+"-"+temp[2]+" "+temp[3]+":"+temp[4];
    
                  //System.out.println(output);
       return output;
   }
}
