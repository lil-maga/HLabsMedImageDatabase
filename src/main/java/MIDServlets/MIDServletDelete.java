package MIDServlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;
import DatabaseTools.*;
//Servlet class that is responsible for receiving DELETE request and sending a confirmation response
@WebServlet(urlPatterns={"/delete"},loadOnStartup = 1)
public class MIDServletDelete extends HttpServlet {
    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Database DB = new Database();//creates a database object
        String reqBody=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));//saves the request as a string
        DB.doDelete(reqBody);//calls a database delete method and passes the request to it
        resp.setContentType("html/text");//sets response to html/text type
        resp.getWriter().write("The image was successfully deleted from the database.");//returns confirmation message
    }
}