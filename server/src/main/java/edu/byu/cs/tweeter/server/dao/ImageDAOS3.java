package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.util.Base64;

public class ImageDAOS3 implements ImageDAO {

    private final String BUCKET_NAME = "tweeter-carsonbaughan";
    private final String REGION_NAME = "us-west-2";

    AmazonS3 s3 = AmazonS3ClientBuilder
            .standard()
            .withRegion(REGION_NAME)
            .build();

    public void addImage(String image_string, String alias) {
        byte[] byteArray = Base64.getDecoder().decode(image_string);
        ObjectMetadata data = new ObjectMetadata();
        data.setContentLength(byteArray.length);
        data.setContentType("image/jpeg");

        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, alias, new ByteArrayInputStream(byteArray), data).withCannedAcl(CannedAccessControlList.PublicRead);

        s3.putObject(request);

        String link = "https://" + BUCKET_NAME + ".s3." + REGION_NAME + ".amazonaws.com/ " + alias;
    }
}
