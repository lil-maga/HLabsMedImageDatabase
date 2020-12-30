package DatabaseTools;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
// Class that handles connection to Aws S3
public class AwsS3 {

    private AmazonS3 s3Client;
    private static final String bucketName = "codeimperial-mib";

    //Constructor1
    public AwsS3(AmazonS3 S3client) {
        this.s3Client = S3client;
    }
    //Constructor2
    public AwsS3() {
        s3Client = AmazonS3ClientBuilder
                .standard()
                .withRegion("eu-west-2")
                //.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .build();
    }


    //Upload Image to s3, return its url
    public URL UploadImage(String filepath){

        URL S3ImageUrl;
        String S3fileKey; // Name of the file at the end of filepath
        Path path = Paths.get(filepath);
        Path fileName=path.getFileName();
        S3fileKey=fileName.toString();
        File f = new File(filepath);
        /* Reference №   :https://docs.aws.amazon.com/AmazonS3/latest/dev/UploadObjSingleOpJava.html */
        try {
            PutObjectRequest request = new PutObjectRequest(bucketName, S3fileKey, f);
            s3Client.putObject(request);
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
        /* end of Reference №  */
        return S3ImageUrl=s3Client.getUrl(bucketName, S3fileKey);
    }

    //Deletes an image from s3
    public void DeleteImage(String fileName){

        /* Reference №   :https://docs.aws.amazon.com/AmazonS3/latest/dev/DeletingOneObjectUsingJava.html */
        try {
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }

         /* end of Reference №  */
    }
    ///////////////////////////////////////////////
    //Classes for tests:

    //uploading object general method - to be tested
    public PutObjectResult putObject(String bucketName, String key, File file) {
        return s3Client.putObject(bucketName, key, file);
    }

    //deleting an object general method - to be tested
    public void deleteObject(String bucketName, String objectKey) {
        s3Client.deleteObject(bucketName, objectKey);
    }

}