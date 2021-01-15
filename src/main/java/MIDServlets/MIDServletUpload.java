package MIDServlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;
import DatabaseTools.*;
/* Servlet class that is responsible for receiving POST request to upload an image and sending a confirmation
   response back */
@WebServlet(urlPatterns={"/upload"},loadOnStartup = 1)
public class MIDServletUpload extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Database DB = new Database();//creates a database object
        String reqBody=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));//saves the request as a string
        DB.doUpload(reqBody);//calls an upload method and passes the request string to it
        resp.setContentType("html/text");//sets the response type to text
        resp.getWriter().write("The image was successfully added to the database.");//sends a confirmation to the client
    }
}


