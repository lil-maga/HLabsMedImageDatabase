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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;


public class MIDServletTest {

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

        MIDServletSearch myServlet = new MIDServletSearch();
        myServlet.doPost(request, response);

        String output = stringWriter.getBuffer().toString();
        Assert.assertThat(output, is(db.doSearch(gson.toJson(testParams))));
    }
}
