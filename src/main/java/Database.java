import com.google.gson.Gson;
import java.sql.*;

/* Database Class that contains methods to connect to the database, search the database,
upload to the database, delete from the database and check for a duplicate. It is used
by servlet classes to access the database and and retrieve or update needed data. */

public class Database {
    //This method connects to the database using provided credentials and database URL
    public static Connection ConnectToDatabase(){
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
    //This method searches the database using the specified search parameters that are passed in a Json string
    public String doSearch (String Pars){
        Gson gson = new Gson();
        SearchParameters params = gson.fromJson(Pars, SearchParameters.class);//convert search parameters from json to Search Parameters object
        MedicalImageLibrary Library = new MedicalImageLibrary();//Medical Image Library will store an array of results
        Connection conn = ConnectToDatabase();//establishes connection to database
        try {
            Statement s=conn.createStatement();
            String delim = "','";
            //constructs an SQL statement to search the database using the search parameters
            String request = "SELECT * FROM MedImages WHERE ";
            //constructs part of SQL statement that specifies modality
            if (String.join(delim, params.getModality()).equals("")){
                request = request.concat("modality is not null AND ");
            }
            else{
                request = request.concat("modality in ('");
                request = request.concat(String.join(delim,(params.getModality())));
                request = request.concat("') AND ");
            }
            //constructs part of SQL statement that specifies body part
            if (String.join(delim, params.getBodyPart()).equals("")){
                request = request.concat("bodyPart is not null AND ");
            }
            else{
                request = request.concat("bodyPart in ('");
                request = request.concat(String.join(delim,(params.getBodyPart())));
                request = request.concat("') AND ");
            }
            //constructs part of SQL statement that searches between 2 dates or >=/<= a date
            if (String.join("", params.getDate()).equals("")){
                request = request.concat("date is not null AND ");
            }
            else if(params.getDate()[1].equals("")){
                request = request.concat("date >= '");
                request = request.concat((params.getDate()[0]));
                request = request.concat("' AND ");
            }
            else if(params.getDate()[0].equals("")){
                request = request.concat("date <= '");
                request = request.concat((params.getDate()[1]));
                request = request.concat("' AND ");
            }
            else{
                request = request.concat("date BETWEEN '");
                request = request.concat(String.join("' AND '",(params.getDate())));
                request = request.concat("' AND ");
            }
            //constructs part of SQL statement that specifies Patient ID
            if ((params.getPatientID()).equals("")){
                request = request.concat("patientid is not null;");
            }
            else{
                request = request.concat("patientid = '");
                request = request.concat(params.getPatientID());
                request = request.concat("';");
            }
            String sqlStr = request;
            System.out.println(request);
            ResultSet rset=s.executeQuery(sqlStr);//SQL statement is executed and results are stored
            //adds the results to a Medical Image Library until all of the results were stored
            while(rset.next()){
                MedImage result = new MedImage();//creates a MedImage object to store parameters
                result.setID(rset.getInt("id"));
                result.setFileName(rset.getString("fileName"));
                result.setPatientID(rset.getString("patientid"));
                result.setModality(rset.getString("modality"));
                result.setBodyPart(rset.getString("bodyPart"));
                result.setDate(rset.getString("date"));
                result.setImageURL(rset.getString("imageURL"));
                Library.AddNewImage(result);//adds a MedImage to the Medical Image Library
            }
            //releases all the objects
            rset.close();
            s.close();
            conn.close();
        }
        catch (Exception e){ }
        Gson gson2 = new Gson();
        String jsonString = gson2.toJson(Library);//changes the Library to jsonString
        return jsonString;//returns a jsonString
    }
    //This method deletes a selected image from the database
    public void doDelete(String jsonString){
        Gson gson = new Gson();
        Connection conn = ConnectToDatabase();//establishes connection to database
        MedImage removeImage = gson.fromJson(jsonString,MedImage.class);//converts jsonString to MedImage
        try{
            Statement s=conn.createStatement();//creates a statement
            //constructs an SQL query to delete an image using MedImage ID
            String request = "DELETE FROM MedImages WHERE id = ";
            request = request.concat(String.valueOf(removeImage.getID()));
            request = request.concat(";");
            String sqlStr = request;
            s.execute(sqlStr);//executes the query
            //releases the objects
            s.close();
            conn.close();
        }
        catch (Exception e){ }
        //for unit testing this part is skipped as a separate unit test is designed for this
        if (!removeImage.getImageURL().equals("TestURL")){
            AwsS3 s3 = new AwsS3();
            s3.DeleteImage(removeImage.getFileName());//deletes an Image from the S3 storage
        }

    }
    //This method uploads the image data to the database using image data as input
    public void doUpload(String jsonString){
        Connection conn = ConnectToDatabase();//connection to
        Gson gson = new Gson();
        MedImage newImage = gson.fromJson(jsonString,MedImage.class);
        try {
            Statement s=conn.createStatement();
            String request = "INSERT INTO MedImages (fileName, patientid, modality, bodyPart, date, imageURL) values ('";
            request = request.concat(newImage.getFileName());
            request = request.concat("','");
            request = request.concat(newImage.getPatientID());
            request = request.concat("','");
            request = request.concat(newImage.getModality());
            request = request.concat("','");
            request = request.concat(newImage.getBodyPart());
            request = request.concat("','");
            request = request.concat(newImage.getDate());
            request = request.concat("','");
            request = request.concat(newImage.getImageURL());
            request = request.concat("');");
            System.out.println(request);
            String sqlStr = request;
            s.execute(sqlStr);

            s.close();
            conn.close();
        }
        catch (Exception e){ }
    }

    public boolean checkDuplicate(String fileName){
        Connection conn = ConnectToDatabase();
        boolean duplicate = false;
        try {
            Statement s = conn.createStatement();
            String search = "SELECT * FROM MedImages WHERE fileName = '";
            search = search.concat(fileName);
            search = search.concat("';");
            String sqlStr = search;
            System.out.println(search);
            ResultSet rset = s.executeQuery(sqlStr);
            duplicate = rset.next();
            System.out.println(duplicate);
            rset.close();
            s.close();
            conn.close();
        } catch (Exception e) { }
        System.out.println(duplicate);
        return duplicate;
    }

}