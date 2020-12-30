import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import DatabaseTools.AwsS3;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;

public class FinalS3TestClass {
    private static final String BUCKET_NAME = "bucket_name";
    private static final String KEY_NAME = "key_name";

    public AmazonS3 s3;
    public AwsS3 service;

    @Before
    public void setUp() {
        s3 = mock(AmazonS3.class);
        service = new AwsS3(s3);
    }

    @Test
    public void whenVerifyingPutObject_thenCorrect() {
        File file = mock(File.class);
        PutObjectResult result = mock(PutObjectResult.class);
        when(s3.putObject(anyString(), anyString(), (File) any())).thenReturn(result);
        assertSame(result,service.putObject(BUCKET_NAME, KEY_NAME, file) );
        verify(s3).putObject(BUCKET_NAME, KEY_NAME,file);
    }

    @Test
    public void whenVerifyingDeleteObject_thenCorrect() {
        service.deleteObject(BUCKET_NAME, KEY_NAME);
        verify(s3).deleteObject(BUCKET_NAME, KEY_NAME);
    }
}
