import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

@WebServlet(urlPatterns={"/delete"},loadOnStartup = 1)
public class MIDServletDelete extends HttpServlet {
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = connecttoDatabase();
        Gson gson = new Gson();
        String reqBody=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        MedImage removeImage = gson.fromJson(reqBody,MedImage.class);
        try {
            Statement s=conn.createStatement();
            String request = "DELETE FROM MedImages WHERE id = ";
            request = request.concat(String.valueOf(removeImage.getID()));
            request = request.concat(";");
            System.out.println(request);
            String sqlStr = request;
            s.execute(sqlStr);

            s.close();
            conn.close();
        }
        catch (Exception e){ }

        AwsS3 s3 = new AwsS3();
        s3.DeleteImage(removeImage.getFileName());

        resp.setContentType("html/text");
        resp.getWriter().write("The image was successfully deleted from the database.");
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


