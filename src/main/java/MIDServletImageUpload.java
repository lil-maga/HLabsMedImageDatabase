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
}
