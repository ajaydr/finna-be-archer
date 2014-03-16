package com.s3_to_ec2.aws;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.junit.Test;

public class MoveFileToS3Test {

    /**
     * Test of createS3File method, of class MoveFileToS3.
     */
	@Test
    public void testcreateS3File() throws IOException {
		File expectedfile = File.createTempFile(Constants.tmpFileName, ".txt");
		expectedfile.deleteOnExit();

        Writer writer = new OutputStreamWriter(new FileOutputStream(expectedfile));
        writer.write("Sample\n");
        writer.write("File\n");
        writer.write("To be moved to\n");
        writer.write("AWS EC2 Instance\n");
        writer.close();
        
		File resultFile = MoveFileToS3.createS3File();
		
		assertEquals(expectedfile.length(), resultFile.length());
		
	}    
}
