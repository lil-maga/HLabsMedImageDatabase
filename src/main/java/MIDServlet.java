import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

@WebServlet(urlPatterns={"/main"},loadOnStartup = 1)
public class MIDServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = connecttoDatabase();
        Gson gson = new Gson();
        MedicalImageLibrary Libr = new MedicalImageLibrary();
        String reqBody=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        SearchParameters pars = gson.fromJson(reqBody,SearchParameters.class);
        try {
            Statement s=conn.createStatement();
            String request = "SELECT * FROM MedImages WHERE modality in ('";
            String delim = "','";
            request = request.concat(String.join(delim,(pars.getModality())));
            request = request.concat("')");
            request = request.concat(" AND bodyPart in ('");
            request = request.concat(String.join(delim,(pars.getBodyPart())));
            request = request.concat("')");
            //request = request.concat(" AND date BETWEEN'");
            //request = request.concat(String.join("' AND '",(pars.getDate())));
            //request = request.concat("'");
            String part = "";
            if (String.join(delim,(pars.getPatientID()))==("not null")){
                part = part.concat(" AND patientid is not null;");
            }
            else{
                part = part.concat(" AND patientid is not null;");
                //part = part.concat(String.join(delim,(pars.getPatientID())));
                //part = part.concat("';");
            }
            request = request.concat(part);
            String sqlStr = request;
            System.out.println(request);
            ResultSet rset=s.executeQuery(sqlStr);

            while(rset.next()){
                MedImage result = new MedImage();
                result.setPatientID(rset.getString("patientid"));
                result.setBodyPart(rset.getString("bodyPart"));
                result.setModality(rset.getString("modality"));
                result.setDate(rset.getString("date"));
                result.setImageURL(rset.getString("imageURL"));
                Libr.AddNewImage(result);
            }

            rset.close();
            s.close();
            conn.close();
        }
        catch (Exception e){ }
        Gson gson2 = new Gson();
        String jsonString = gson2.toJson(Libr);
        resp.setContentType("application/json");
        resp.getWriter().write(jsonString);
        System.out.println(jsonString);
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
