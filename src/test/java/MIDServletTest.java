import Entities.*;
import DatabaseTools.*;
import MIDServlets.MIDServletSearch;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

/* Test for the communication between client and servlet. The test was designed for MIDServletSearch class,
   but other servlet classes follow the same communication method and only difference is the database method
   called in them.
 */
public class MIDServletTest {
    //Mocks the request and response of the servlet
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void TestDoPost() throws IOException, ServletException {
        //Create the search parameters to search for all the entries
        SearchParameters testParams = new SearchParameters();
        testParams.setBodyPart(new String[]{""});
        testParams.setModality(new String[]{""});
        testParams.setDate(new String[]{"",""});
        testParams.setPatientID("");
        Database db = new Database();

        Gson gson = new Gson();

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        Stream<String> stream = Stream.of(gson.toJson(testParams));

        BufferedReader reader = Mockito.mock(BufferedReader.class);
        when(reader.lines()).thenReturn(stream);

        when(request.getReader()).thenReturn(reader);
        when(response.getWriter()).thenReturn(printWriter);

        MIDServletSearch myServlet = new MIDServletSearch();//creates an instance of MIDServlet class
        myServlet.doPost(request, response);//passes the mocked request and mocked response

        String output = stringWriter.getBuffer().toString();
        //checks that the response is equal to the search result with the test parameters
        Assert.assertThat(output, is(db.doSearch(gson.toJson(testParams))));
    }
}
