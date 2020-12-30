import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet(urlPatterns={"/main"},loadOnStartup = 1)
public class MIDServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Database postgres = new Database();
        String reqBody=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        String jsonString = postgres.doSearch(reqBody);
        resp.setContentType("application/json");
        resp.getWriter().write(jsonString);
        System.out.println(jsonString);
    }
}