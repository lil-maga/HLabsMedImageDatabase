import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet(urlPatterns={"/delete"},loadOnStartup = 1)
public class MIDServletDelete extends HttpServlet {
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Database DB = new Database();
        String reqBody=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        DB.doDelete(reqBody);
        resp.setContentType("html/text");
        resp.getWriter().write("The image was successfully deleted from the database.");
    }
}