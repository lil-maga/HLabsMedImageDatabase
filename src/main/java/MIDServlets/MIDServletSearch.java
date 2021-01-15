package MIDServlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;
import DatabaseTools.*;
/* Servlet class that is responsible for receiving POST request with search parameters
   and sending response with search results */
@WebServlet(urlPatterns={"/search"},loadOnStartup = 1)
public class MIDServletSearch extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Database postgres = new Database();//creates a database object
        String reqBody=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));//saves the request as string
        String jsonString = postgres.doSearch(reqBody);//passes the request string to the Search method and saves the search results to json string
        resp.setContentType("application/json");//sets response type to json format
        resp.getWriter().write(jsonString);//sends search results as a json string to the client
    }
}