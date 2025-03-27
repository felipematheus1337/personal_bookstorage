package com.bookstorage.app.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bookstorage.app.config.AMQPConfiguration;
import com.bookstorage.app.utils.BusinessHelperUtil;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class S3Service {

    private final AmazonS3 s3;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public S3Service(AmazonS3 s3) {
        this.s3 = s3;
    }

    public CompletableFuture<String> uploadFile(byte[] fileBytes) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String fileName = "uploads/" + System.currentTimeMillis() + ".jpg";
                InputStream fileInputStream = new ByteArrayInputStream(fileBytes);

                PutObjectRequest objRequest = new PutObjectRequest(BusinessHelperUtil.BUCKET_NAME, fileName,
                        fileInputStream, null);

                s3.putObject(objRequest);

                return s3.getUrl(BusinessHelperUtil.BUCKET_NAME, fileName).toString();
            } catch(Exception e) {
               throw new RuntimeException("Failed to upload.");
            }
        }, executorService);


    }
}
