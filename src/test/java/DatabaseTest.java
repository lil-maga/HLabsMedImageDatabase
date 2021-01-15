import DatabaseTools.Database;
import Entities.MedImage;
import Entities.MedicalImageLibrary;
import Entities.SearchParameters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import com.google.gson.Gson;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class DatabaseTest {
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void TestImageOperations(){
        //Creates a test medical image with the following parameters
        MedImage testImage = new MedImage();
        testImage.setFileName("TestFileName");
        testImage.setPatientID("TestID");
        testImage.setBodyPart("TestBodyPart");
        testImage.setModality("TestModality");
        testImage.setDate("2000-01-01");
        testImage.setImageURL("TestURL");

        //Creates test Search parameters for the test image
        SearchParameters testParams = new SearchParameters();
        testParams.setBodyPart(new String[]{"TestBodyPart"});
        testParams.setModality(new String[]{"TestModality"});
        testParams.setDate(new String[]{"2000-01-01",""});
        testParams.setPatientID("TestID");

        //Creates an instance of teh database
        Database testDB = new Database();
        Gson testGson = new Gson();

        String SearchParams = testGson.toJson(testParams); //converts test search parameters to json strong
        String results = testDB.doSearch(SearchParams);//searches the database with test parameters
        MedicalImageLibrary resultLibr = testGson.fromJson(results, MedicalImageLibrary.class); //returns search results
        Assert.assertThat(resultLibr.getSize(), is(equalTo(0)));//checks that 0 results were found

        String ImageData = testGson.toJson(testImage);//saves test image parameters as json string
        testDB.doUpload(ImageData);//calls the database upload method and passes image to it

        results = testDB.doSearch(SearchParams);//searches using the test parameters
        resultLibr = testGson.fromJson(results, MedicalImageLibrary.class);//saves the search results to a med image library

        Assert.assertThat(resultLibr.getSize(), is(equalTo(1)));//checks that 1 result was found that means that image was uploaded
        //tests the found image parameters against the test image parameters that verifies that upload and search work correctly
        Assert.assertThat(resultLibr.get(0).getFileName(), is(equalTo(testImage.getFileName())));
        Assert.assertThat(resultLibr.get(0).getPatientID(), is(equalTo(testImage.getPatientID())));
        Assert.assertThat(resultLibr.get(0).getBodyPart(), is(equalTo(testImage.getBodyPart())));
        Assert.assertThat(resultLibr.get(0).getModality(), is(equalTo(testImage.getModality())));
        Assert.assertThat(resultLibr.get(0).getDate(), is(equalTo(testImage.getDate())));
        Assert.assertThat(resultLibr.get(0).getImageURL(), is(equalTo(testImage.getImageURL())));

        boolean duplicate = testDB.checkDuplicate(testImage.getFileName());//checks for duplicate with test image file name
        Assert.assertThat(duplicate, is(equalTo(true)));//duplicate should be found as there is a test image in the database

        testImage.setID(resultLibr.get(0).getID());//gets the ID of the test image
        testDB.doDelete(testGson.toJson(testImage));//deletes the test image from the database

        duplicate = testDB.checkDuplicate(testImage.getFileName());//checks for the file name of the test image
        Assert.assertThat(duplicate, is(equalTo(false)));//no duplicate should be found if image was successfully deleted
    }
}
