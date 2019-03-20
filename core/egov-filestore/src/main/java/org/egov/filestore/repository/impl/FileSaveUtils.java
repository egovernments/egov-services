package org.egov.filestore.repository.impl;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.egov.tracer.model.CustomException;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileSaveUtils {
	
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
	
	public Map<String, BufferedImage> createVersionsOfImage(MultipartFile file, String fileName){
		Map<String, BufferedImage> mapOfImagesAndPaths = new HashMap<>();
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
			
			mapOfImagesAndPaths.put(fileName,  originalImage);
			mapOfImagesAndPaths.put(fileName.replace(replaceString, _large + replaceString), largeImage);
			mapOfImagesAndPaths.put(fileName.replace(replaceString, _medium + replaceString), mediumImg);
			mapOfImagesAndPaths.put(fileName.replace(replaceString, _small + replaceString), smallImg);			
		}catch(Exception e) {
			log.error("Error while creating different versions of the image: ", e);
		}
		
		return mapOfImagesAndPaths;
	}

}
