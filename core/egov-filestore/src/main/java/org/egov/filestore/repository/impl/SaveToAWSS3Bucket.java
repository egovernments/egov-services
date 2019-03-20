package org.egov.filestore.repository.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.egov.filestore.domain.model.Artifact;
import org.egov.filestore.repository.AWSClientFacade;
import org.egov.filestore.repository.SaveFiles;
import org.egov.tracer.model.CustomException;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@ConditionalOnProperty(value = "isS3Enabled", havingValue = "true", matchIfMissing = true)
public class SaveToAWSS3Bucket implements SaveFiles {

	@Value("${aws.key}")
	private String key;

	@Value("${aws.secretkey}")
	private String secretKey;

	@Value("${aws.region}")
	private String awsRegion;

	@Value("${image.small}")
	private String _small;

	@Value("${image.medium}")
	private String _medium;

	@Value("${image.large}")
	private String _large;

	@Value("${image.small.width}")
	private Integer smallWidth;

	@Value("${image.medium.width}")
	private Integer mediumWidth;

	@Value("${image.large.width}")
	private Integer largeWidth;

	@Value("${is.bucket.fixed}")
	private Boolean isBucketFixed;

	@Value("${presigned.url.expiry.time}")
	private Long presignedUrlExpirytime;

	private AmazonS3 s3Client;
	
	@Autowired
	private AWSClientFacade awsFacade;
	
	private static final String TEMP_FILE_PATH_NAME = "TempFolder/localFile";

	
	@Override
	public void save(List<Artifact> artifacts) {
		if (null == s3Client)
			s3Client = awsFacade.getS3Client();
		
		artifacts.forEach(artifact -> {
			String completeName = artifact.getFileLocation().getFileName();
			int index = completeName.indexOf('/');
			String bucketName = completeName.substring(0, index);
			String fileNameWithPath = completeName.substring(index + 1, completeName.length());
			if (!isBucketFixed && !s3Client.doesBucketExistV2(bucketName))
				s3Client.createBucket(bucketName);
			if (artifact.getMultipartFile().getContentType().startsWith("image/")) {
				writeImage(artifact.getMultipartFile(), bucketName, fileNameWithPath);
			} else {
				writeFile(artifact.getMultipartFile(), bucketName, fileNameWithPath);
			}
		});
	}
	
	private void writeFile(MultipartFile file, String bucketName, String fileName) {
		InputStream is = null;
		long contentLength = file.getSize();
		try {
			is = file.getInputStream();
		} catch (IOException e) {
			log.error(" exception occured while reading input stream from file {}", e);
			throw new RuntimeException(e);
		}
		ObjectMetadata objMd = new ObjectMetadata();
		objMd.setContentLength(contentLength);

		s3Client.putObject(bucketName, fileName, is, objMd);
	}

	private void writeImage(MultipartFile file, String bucketName, String fileName) {

		try {
			log.info(" the file name " + file.getName());
			log.info(" the file size " + file.getSize());
			log.info(" the file content " + file.getContentType());

			BufferedImage originalImage = ImageIO.read(file.getInputStream());

			if (null == originalImage) {
				Map<String, String> map = new HashMap<>();
				map.put("Image Source Unavailable", "Image File present in upload request is Invalid/Not Readable");
				throw new CustomException(map);
			}

			BufferedImage largeImage = Scalr.resize(originalImage, Method.QUALITY, Mode.AUTOMATIC, mediumWidth, null,
					Scalr.OP_ANTIALIAS);
			BufferedImage mediumImg = Scalr.resize(originalImage, Method.QUALITY, Mode.AUTOMATIC, mediumWidth, null,
					Scalr.OP_ANTIALIAS);
			BufferedImage smallImg = Scalr.resize(originalImage, Method.QUALITY, Mode.AUTOMATIC, smallWidth, null,
					Scalr.OP_ANTIALIAS);

			int lastIndex = fileName.length();
			String replaceString = fileName.substring(fileName.lastIndexOf('.'), lastIndex);
			String extension = FilenameUtils.getExtension(file.getOriginalFilename());
			String largePath = fileName.replace(replaceString, _large + replaceString);
			String mediumPath = fileName.replace(replaceString, _medium + replaceString);
			String smallPath = fileName.replace(replaceString, _small + replaceString);

			s3Client.putObject(getPutObjectRequest(bucketName, fileName, originalImage, extension));
			s3Client.putObject(getPutObjectRequest(bucketName, largePath, largeImage, extension));
			s3Client.putObject(getPutObjectRequest(bucketName, mediumPath, mediumImg, extension));
			s3Client.putObject(getPutObjectRequest(bucketName, smallPath, smallImg, extension));

			smallImg.flush();
			mediumImg.flush();
			originalImage.flush();

		} catch (Exception ioe) {

			Map<String, String> map = new HashMap<>();
			map.put("Image Source Invalid", "Image File present in upload request is Invalid/Not Readable");
			throw new CustomException(map);
		}
	}
	
	private PutObjectRequest getPutObjectRequest(String bucketName, String key, BufferedImage originalImage,
			String extension) {

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(originalImage, extension, os);
		} catch (IOException e) {
			log.error(" error while writing image to stream : {}", e);
			throw new RuntimeException(e);
		}
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(os.size());
		return new PutObjectRequest(bucketName, key, new ByteArrayInputStream(os.toByteArray()), metadata);
	}

}
