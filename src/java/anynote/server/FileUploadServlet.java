package anynote.server;

// Modified from org.kodejava.example.commons.fileupload;


import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.File;
import java.util.List;
import java.util.Iterator;
public class FileUploadServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                System.out.println("Start upload!!!!");
                response.setContentType("text/html;charset=utf-8"); 
		PrintWriter out = response.getWriter();
                String id = request.getParameter("id");
                System.out.println(id);
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (isMultipart) {
            System.out.println("isMultipart!!!!");
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);

            try {
                List items = upload.parseRequest(request);
                Iterator iterator = items.iterator();
                while (iterator.hasNext()) {
                    FileItem item = (FileItem) iterator.next();

                    if (!item.isFormField()) {
                        String fileName = item.getName();

                        String root = getServletContext().getRealPath("/");
                        File path = new File(root + "/uploads/"+id); //place files in the uploads directory
                        if (!path.exists()) {
                            boolean status = path.mkdirs();
                        }
						
                        File uploadedFile = new File(path + "/" + fileName);
                        System.out.println(uploadedFile.getAbsolutePath());
                        item.write(uploadedFile);
			//out.print("Upload: " + fileName);
                        System.out.println("Upload: " + fileName);
                        out.write(1);
                        out.flush();
                        out.close();
                                               
                    }
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("notMultipart!!!!");
        }
		
    }
}