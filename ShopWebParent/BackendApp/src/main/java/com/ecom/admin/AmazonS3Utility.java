package com.ecom.admin;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class AmazonS3Utility {
    public static final String BUCKET_NAME = "shopify-all-files";

    public static List<String> listFolder(String folderName){
        List<String> listKey = new ArrayList<>();

        S3Client s3Client = S3Client.builder().build(); // create s3 client object with environment variable

        ListObjectsRequest objectsRequest = ListObjectsRequest.builder()
                .bucket(BUCKET_NAME)
                .prefix(folderName)
                .build(); // request get list object of specific bucket and specific folder name matches with prefix

        ListObjectsResponse s3Response = s3Client.listObjects(objectsRequest); // response list object result
        List<S3Object> contents = s3Response.contents(); // list object of response
        ListIterator<S3Object> iterator = contents.listIterator();

        while (iterator.hasNext()){
            S3Object s3Object = iterator.next();
            listKey.add(s3Object.key());
        }

        return listKey;
    }

    public static void uploadFileToS3(String folderName, String fileName, InputStream inputStream) throws IOException {
        S3Client s3Client = S3Client.builder().build();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(folderName + "/" + fileName)
                .acl("public-read")
                .build(); // set parameter for object request
        try{
            int contentLength = inputStream.available(); // get size (byte) form input stream when upload file
            s3Client.putObject(objectRequest, RequestBody.fromInputStream(inputStream, contentLength)); // put file in to s3. Transfer data input stream to s3 object content with size data input stream.
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public static void deleteFile(String fileName) {
        S3Client client = S3Client.builder().build();

        DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(BUCKET_NAME)
                .key(fileName).build();
        client.deleteObject(request);
    }

    public static void removeFolder(String folderName) {
        S3Client s3Client = S3Client.builder().build();
        ListObjectsRequest listRequest = ListObjectsRequest.builder()
                .bucket(BUCKET_NAME).prefix(folderName + "/").build();

        ListObjectsResponse response = s3Client.listObjects(listRequest);

        List<S3Object> contents = response.contents();

        ListIterator<S3Object> listIterator = contents.listIterator();

        while (listIterator.hasNext()) {
            S3Object s3Object = listIterator.next();
            DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(BUCKET_NAME)
                    .key(s3Object.key()).build();
            s3Client.deleteObject(request);
        }
    }
}
