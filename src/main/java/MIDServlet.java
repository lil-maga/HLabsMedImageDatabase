import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.stream.Collectors;

@WebServlet(urlPatterns={"/main"},loadOnStartup = 1)
public class MIDServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.getWriter().write("Hello, World!");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    Connection conn = connecttoDatabase();
    String reqBody=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    MedImage medimg = new MedImage();
        try {
            Statement s=conn.createStatement();
            String sqlStr = "SELECT * FROM MedicalImages WHERE ScanType = 'Microscope';";
            ResultSet rset=s.executeQuery(sqlStr);
            while(rset.next()){
                medimg.setScanType(rset.getString("ScanType"));
                medimg.setLocation(rset.getString("Location"));
                medimg.setBodyPart(rset.getString("BodyPart"));
            }
            rset.close();
            s.close();
            conn.close();
        }
        catch (Exception e){ }
        resp.setContentType("text/html");
        resp.getWriter().write(medimg.getBodyPart() + " " +medimg.getScanType() + " " + medimg.getLocation());
    }

    public static Connection connecttoDatabase(){
        String dbUrl = "jdbc:postgresql://ec2-54-170-123-247.eu-west-1.compute.amazonaws.com:5432/d54rjscdsauvis";
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
        }

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbUrl, "yzsxiugbvxboxx","e4b0b4171911f0f435a274c51f6c037217cba861ffdc3f098feb75bfd00bd77d");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }
}


