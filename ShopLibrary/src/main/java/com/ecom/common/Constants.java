package com.ecom.common;

public class Constants {
    public static final String S3_URI;

    static {
        String bucketName = "shopify-all-files";
        String region = System.getenv("AWS_REGION");
        String parent = "https://%s.s3.%s.amazonaws.com/";

        S3_URI = String.format(parent,bucketName,region);
    }
}
