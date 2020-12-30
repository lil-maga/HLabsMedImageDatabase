import org.junit.Assert;
import org.junit.Test;

public class TestSearchParameters {
    @Test
    public void testPatientIDAssign(){
        SearchParameters par=new SearchParameters();
        String id="123";
        par.setPatientID(id);
        Assert.assertEquals(par.getPatientID(),id);
    }
    @Test
    public void testModalityAssign(){
        SearchParameters par=new SearchParameters();
        String[] mod={"ct","mri"};
        par.setModality(mod);
        Assert.assertEquals(par.getModality(),mod);
    }
    @Test
    public void testBodyPartAssign(){
        SearchParameters par=new SearchParameters();
        String[] body={"leg","arm"};
        par.setBodyPart(body);
        Assert.assertEquals(par.getBodyPart(),body);
    }
    @Test
    public void testDateAssign(){
        SearchParameters par=new SearchParameters();
        String[] date={"today","yesterday"};
        par.setDate(date);
        Assert.assertEquals(par.getDate(),date);
    }
}
