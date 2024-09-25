package com.ecom.admin.reponsitory.s3;

import com.ecom.admin.AmazonS3Utility;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ecom.admin.AmazonS3Utility.listFolder;

public class S3Test {
    @Test
    public void testListObject(){
        String folderName = "product-image/22";
        List<String> listObject = AmazonS3Utility.listFolder(folderName);
        listObject.forEach(System.out::println);
    }
}
