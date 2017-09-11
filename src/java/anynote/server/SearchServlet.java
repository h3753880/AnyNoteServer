package anynote.server;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
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
 * @author tony
 */
public class SearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");
            HttpSession session = request.getSession();
            String search = new String(request.getParameter("search").getBytes("ISO-8859-1"), "UTF-8");
            //session.setAttribute("webUserId", webUserId);
            //System.out.print(user);

            JSONArray jArray = (JSONArray) session.getAttribute("jArray");
            jArray.clear();
            ArrayList<TimeNote> DB = (ArrayList<TimeNote>) session.getAttribute("DB");


            //簡單的搜尋只做對title和content的比較
            for (TimeNote element : DB) {


                JSONObject jObj = new JSONObject();
                try {
                    jObj.put("id", element.userId + element.noteId);
                    jObj.put("title", element.title);
                    jObj.put("start", element.time);
                    jObj.put("noteId", element.noteId);
                    if (element.content.indexOf(search) != -1 || element.title.indexOf(search) != -1) {
                        jObj.put("color", "red");
                    } else {
                        jObj.put("color", "#BE6100");
                    }
                    jObj.put("userName", element.userName);
                    jObj.put("userId", element.userId);
                    jObj.put("friendId", element.friends);
                    jObj.put("content", element.content);
                    jObj.put("cycle", element.cycle);
                    jObj.put("noteType", element.noteType);
                    jObj.put("status", element.status);
                    jObj.put("img", element.img);
                } catch (JSONException ex) {
                    // Logger.getLogger(GetTimeNote.class.getName()).log(Level.SEVERE, null, ex);
                }
                jArray.put(jObj);


            }
            //把比較好的jArray存入
            session.setAttribute("jArray", jArray);

            RequestDispatcher view = request.getRequestDispatcher("timeNoteCalendar.jsp");
            view.forward(request, response);
        } catch (NullPointerException ex) {
            PrintWriter out = response.getWriter();
            out.println("error");
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
}
