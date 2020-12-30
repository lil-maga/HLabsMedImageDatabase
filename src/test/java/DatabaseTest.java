import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
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
        MedImage testImage = new MedImage();
        testImage.setFileName("TestFileName");
        testImage.setPatientID("TestID");
        testImage.setBodyPart("TestBodyPart");
        testImage.setModality("TestModality");
        testImage.setDate("2000-01-01");
        testImage.setImageURL("TestURL");

        SearchParameters testParams = new SearchParameters();
        testParams.setBodyPart(new String[]{"TestBodyPart"});
        testParams.setModality(new String[]{"TestModality"});
        testParams.setDate(new String[]{"2000-01-01",""});
        testParams.setPatientID("TestID");

        Database testDB = new Database();
        Gson testGson = new Gson();

        String SearchParams = testGson.toJson(testParams);
        String results = testDB.doSearch(SearchParams);
        MedicalImageLibrary resultLibr = testGson.fromJson(results, MedicalImageLibrary.class);
        Assert.assertThat(resultLibr.getSize(), is(equalTo(0)));//checks that 0 results were found

        String ImageData = testGson.toJson(testImage);
        testDB.doUpload(ImageData);
        System.out.println(ImageData);

        results = testDB.doSearch(SearchParams);
        resultLibr = testGson.fromJson(results, MedicalImageLibrary.class);
        System.out.println(results);

        Assert.assertThat(resultLibr.getSize(), is(equalTo(1)));//checks that 1 result was found
        Assert.assertThat(resultLibr.get(0).getFileName(), is(equalTo(testImage.getFileName())));
        Assert.assertThat(resultLibr.get(0).getPatientID(), is(equalTo(testImage.getPatientID())));
        Assert.assertThat(resultLibr.get(0).getBodyPart(), is(equalTo(testImage.getBodyPart())));
        Assert.assertThat(resultLibr.get(0).getModality(), is(equalTo(testImage.getModality())));
        Assert.assertThat(resultLibr.get(0).getDate(), is(equalTo(testImage.getDate())));
        Assert.assertThat(resultLibr.get(0).getImageURL(), is(equalTo(testImage.getImageURL())));

        boolean duplicate = testDB.checkDuplicate(testImage.getFileName());
        Assert.assertThat(duplicate, is(equalTo(true)));

        testImage.setID(resultLibr.get(0).getID());
        testDB.doDelete(testGson.toJson(testImage)); //problem with AWS object needs to be fixed

        duplicate = testDB.checkDuplicate(testImage.getFileName());
        Assert.assertThat(duplicate, is(equalTo(false)));
    }
}
