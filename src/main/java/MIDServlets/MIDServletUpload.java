package MIDServlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;
import DatabaseTools.*;

@WebServlet(urlPatterns={"/upload"},loadOnStartup = 1)
public class MIDServletUpload extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Database DB = new Database();
        String reqBody=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        DB.doUpload(reqBody);
        resp.setContentType("html/text");
        resp.getWriter().write("The image was successfully added to the database.");
    }
}


