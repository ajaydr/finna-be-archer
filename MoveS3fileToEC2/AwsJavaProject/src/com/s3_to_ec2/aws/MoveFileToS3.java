/*
 * Open source. 
 *
 * @author Ajay Rajgure
 *  
 */
package com.s3_to_ec2.aws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

/**
 * This code demonstrates how to move file(generated in code) to Amazon S3 using
 * the AWS SDK for Java.
 * <p>
 * <b>Prerequisites:</b> You must have a valid Amazon Web Services developer
 * account, and be signed up to use Amazon S3. 
 * <p>
 *
 */
public class MoveFileToS3 {

    public static void main(String[] args) throws IOException {
      
        AmazonS3 s3 = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
		Region usEast1 = Region.getRegion(Regions.US_EAST_1);
		s3.setRegion(usEast1);
		
     	/*
         * Read S3 bucket name and file name from properties file.
         */
		String bucketName = null;
        String fileName = null;
		try{
			ResourceBundle rb = ResourceBundle.getBundle("AwsCustomProperties");
			bucketName = rb.getString("s3BucketName");
	        fileName = rb.getString("s3FileName");
	        System.out.println("bucketName="+bucketName);
	        System.out.println("Read bucket name and file name from properties file.");
		} catch (MissingResourceException mre) {
		   System.out.println("Caught an MissingResourceException while reading AwsCustomProperties file. Error Message: " + mre.getMessage());	
		   
		   if(bucketName == null) {
	    	   bucketName = Constants.defaultS3BucketName;
	    	   System.out.println("Using default S3 Bucket Name from Constants.");
	       }
	       if(fileName == null) {
	    	   fileName = Constants.defaultS3FileName;
	    	   System.out.println("Using default File Name from Constants.");
	       }
	    }
		
        try {
            /*
             * Create a new S3 bucket, if does not already exists.
             */
        	
        	try{
        		s3.getBucketLocation(bucketName);
        	}	
        	catch(Exception e){
        	    System.out.println("Creating new bucket " + bucketName + "\n");
        	    s3.createBucket(bucketName);
        	}
        	
        	/*
             * Upload a new file to S3.
             */
            System.out.println("Uploading a new file to S3\n");
            s3.putObject(new PutObjectRequest(bucketName, fileName, createS3File()));
            
            System.out.println("Successfully copied file to S3\n");
            
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

    /**
     * Creates a temporary file with required text data
     * 
     * @return A newly created temporary file with text data.
     *
     * @throws IOException
     */
    private static File createS3File() throws IOException {
        File file = File.createTempFile(Constants.tmpFileName, ".txt");
        file.deleteOnExit();

        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write("Sample\n");
        writer.write("File\n");
        writer.write("To be moved to\n");
        writer.write("AWS EC2 Instance\n");
        writer.close();

        return file;
    }
    
}
