import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


public class MIDServletTest {
    /*@Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    DatabaseTools.Database db;
    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);

    }
    @Test
    public void TestDoPost() throws IOException, ServletException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(request.getReader().lines().collect(Collectors.joining(System.lineSeparator()))).thenReturn();
        when(response.getWriter()).thenReturn(printWriter);
        when(db.doSearch("good")).thenReturn("great");

        MIDServlets.MIDServlet myServlet = new MIDServlets.MIDServlet();
        myServlet.doPost(request, response);

        String output = stringWriter.getBuffer().toString();
        Assert.assertThat(response.getContentType(),is(equalTo("application/json")));
        Assert.assertThat(output, is(equalTo("great")));
    }
*/
}
