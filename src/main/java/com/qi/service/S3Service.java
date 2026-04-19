package com.qi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner presigner;

    @Value("${AWS_S3_BUCKET_NAME}")
    private String bucketName;

    public String uploadFile(MultipartFile multipartFile){
        try{
            if(multipartFile.isEmpty()){
                throw new RuntimeException("File is empty");
            }
            String contentType = multipartFile.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new RuntimeException("Only image files are allowed");
            }
            long maxSize = 10 * 1024 * 1024; // 10MB
            if (multipartFile.getSize() > maxSize) {
                throw new RuntimeException("File size should not exceed 10MB");
            }
             String key = UUID.randomUUID()+"_"+multipartFile.getOriginalFilename();
            PutObjectRequest request = PutObjectRequest.builder().bucket(bucketName).key(key).contentType(multipartFile.getContentType()).build();
            s3Client.putObject(request, RequestBody.fromInputStream(multipartFile.getInputStream(),multipartFile.getSize()));
            return key;
        } catch (Exception e) {
            throw new RuntimeException("S3 upload failed: " + e.getMessage());
        }
    }
    public String generateUrl(String key){
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getObjectRequest).build();

        return presigner.presignGetObject(presignRequest).url().toString();
    }

    public  void deleteFile(String key){
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName).key(key).build();
        s3Client.deleteObject(deleteObjectRequest);
    }
}
