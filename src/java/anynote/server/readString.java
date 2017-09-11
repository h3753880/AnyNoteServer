/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package anynote.server;

import java.io.BufferedReader;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author tony
 */
public class readString {
     public static String read(HttpServletRequest request) {
        StringBuffer json = new StringBuffer();
        
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                json.append(line+"\n");
                //System.out.println(json.toString());
            }
            reader.close();
            //line=reader.readLine();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return json.toString();
        //return line;
    }
    
}
