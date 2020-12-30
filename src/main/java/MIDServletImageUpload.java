import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.sql.*;
import java.util.Enumeration;

/*
 * This servlet demonstrates how to receive file uploaded from the client
 * without using third-party upload library such as Commons File Upload.
 * @author www.codejava.net
 */

@WebServlet(urlPatterns={"/uploadimage"},loadOnStartup = 1)
public class MIDServletImageUpload extends HttpServlet {

    static final int BUFFER_SIZE = 4096;
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        Connection conn = connecttoDatabase();
        // Gets file name for HTTP header
        String fileName = request.getHeader("fileName");

        File saveFile = new File(fileName);

        // prints out all header values
        System.out.println("===== Begin headers =====");
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String headerName = names.nextElement();
            System.out.println(headerName + " = " + request.getHeader(headerName));
        }
        System.out.println("===== End headers =====\n");
        try {
            Statement s = conn.createStatement();

            String search = "SELECT * FROM MedImages WHERE fileName = '";
            search = search.concat(fileName);
            search = search.concat("';");
            String sqlStr = search;
            System.out.println(search);
            ResultSet rset = s.executeQuery(sqlStr);
            if (rset.next()){
                response.setContentType("html/text");
                response.getWriter().write("Duplicate file");
                rset.close();
                s.close();
                conn.close();
                return;
            }
            rset.close();
            s.close();
            conn.close();
        } catch (Exception e) { }

        // opens input stream of the request for reading data
        InputStream inputStream = request.getInputStream();

        // opens an output stream for writing file
        FileOutputStream outputStream = new FileOutputStream(saveFile);

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;
        System.out.println("Receiving data...");

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        System.out.println("Data received.");
        outputStream.close();
        inputStream.close();

        System.out.println("File written to: " + saveFile.getAbsolutePath());

        //Upload that image to cloud
        System.out.println("Now uploading image to S3");
        AwsS3 s3 =new AwsS3();
        URL images3url= s3.UploadImage(fileName);
        System.out.println("Upload to s3 complete to "+images3url);
        Files.deleteIfExists(saveFile.toPath());

        response.setContentType("html/text");
        response.getWriter().write(String.valueOf(images3url));
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
