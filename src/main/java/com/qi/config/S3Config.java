package com.qi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    @Value("${AWS_ACCESS_KEY_ID}")
    private String awsAccessKey;
    @Value("${AWS_SECRET_ACCESS_KEY}")
    private String awsSecretKey;
    @Value("${AWS_REGION}")
    private String s3Region;

    @Bean
    public S3Client s3Client(){
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
          awsAccessKey,awsSecretKey
        );
        return S3Client.builder().region(Region.of(s3Region)).credentialsProvider(StaticCredentialsProvider.create(credentials)).build();
    }
    @Bean
    public S3Presigner s3Presigner() {
        AwsBasicCredentials credentials =
                AwsBasicCredentials.create(awsAccessKey,awsSecretKey);

        return S3Presigner.builder()
                .region(Region.of(s3Region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(credentials)
                )
                .build();
    }
}
