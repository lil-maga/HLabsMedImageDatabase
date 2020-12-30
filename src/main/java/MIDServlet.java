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

@WebServlet(urlPatterns={"/search"},loadOnStartup = 1)
public class MIDServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = connecttoDatabase();
        Gson gson = new Gson();
        MedicalImageLibrary Libr = new MedicalImageLibrary();
        String reqBody=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        SearchParameters pars = gson.fromJson(reqBody,SearchParameters.class);
        try {
            Statement s=conn.createStatement();
            String delim = "','";

            String request = "SELECT * FROM MedImages WHERE ";

            if (String.join(delim, pars.getModality()).equals("")){
                request = request.concat("modality is not null AND ");
            }
            else{
                request = request.concat("modality in ('");
                request = request.concat(String.join(delim,(pars.getModality())));
                request = request.concat("') AND ");
            }

            if (String.join(delim, pars.getBodyPart()).equals("")){
                request = request.concat("bodyPart is not null AND ");
            }
            else{
                request = request.concat("bodyPart in ('");
                request = request.concat(String.join(delim,(pars.getBodyPart())));
                request = request.concat("') AND ");
            }

            if (String.join("", pars.getDate()).equals("")){
                request = request.concat("date is not null AND ");
            }
            else if(pars.getDate()[1].equals("")){
                request = request.concat("date >= '");
                request = request.concat((pars.getDate()[0]));
                request = request.concat("' AND ");
            }
            else if(pars.getDate()[0].equals("")){
                request = request.concat("date <= '");
                request = request.concat((pars.getDate()[1]));
                request = request.concat("' AND ");
            }
            else{
                request = request.concat("date BETWEEN '");
                request = request.concat(String.join("' AND '",(pars.getDate())));
                request = request.concat("' AND ");
            }

            if ((pars.getPatientID()).equals("")){
                request = request.concat("patientid is not null;");
            }
            else{
                request = request.concat("patientid = '");
                request = request.concat(pars.getPatientID());
                request = request.concat("';");
            }
            String sqlStr = request;
            System.out.println(request);
            ResultSet rset=s.executeQuery(sqlStr);

            while(rset.next()){
                MedImage result = new MedImage();
                result.setID(rset.getInt("id"));
                result.setFileName(rset.getString("fileName"));
                result.setPatientID(rset.getString("patientid"));
                result.setModality(rset.getString("modality"));
                result.setBodyPart(rset.getString("bodyPart"));
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


