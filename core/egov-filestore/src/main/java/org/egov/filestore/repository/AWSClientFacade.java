package org.egov.filestore.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Component
public class AWSClientFacade {
	
	@Value("${aws.key}")
	private String key;

	@Value("${aws.secretkey}")
	private String secretKey;

	@Value("${aws.region}")
	private String awsRegion;
	
	public AmazonS3 getS3Client() {
		return AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(key, secretKey)))
				.withRegion(Regions.valueOf(awsRegion)).build();
	}

}
