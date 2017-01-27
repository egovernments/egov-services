package org.egov.filestore.service;

import static java.io.File.separator;
import static java.util.UUID.randomUUID;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.egov.filestore.exception.ApplicationRuntimeException;
import org.egov.filestore.entity.FileStoreMapper;
import org.egov.filestore.repository.FileStoreMapperRepository;
import org.egov.filestore.config.properties.ApplicationProperties;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FileStoreService {

	private static final Logger LOG = getLogger(FileStoreService.class);

	private String fileStoreBaseDir;
	
	@Autowired
	private FileStoreMapperRepository fileStoreMapperRepository;

	@Autowired
	public FileStoreService(ApplicationProperties applicationProperties) {
		if (applicationProperties.fileStoreBaseDir().isEmpty())
			this.fileStoreBaseDir = FileUtils.getUserDirectoryPath() + separator + "egovfilestore";
		else
			this.fileStoreBaseDir = applicationProperties.fileStoreBaseDir();
	}

	@Transactional
	public FileStoreMapper saveFileStoreMapper(final FileStoreMapper fileStoreMapper) {
		return fileStoreMapperRepository.save(fileStoreMapper);
	}

	public FileStoreMapper findByFileStoreId(final String fileStoreId) {
		return fileStoreMapperRepository.findByFileStoreId(fileStoreId);
	}

	public FileStoreMapper store(File sourceFile, String fileName, String mimeType, String moduleName, String tanent) {
		return store(sourceFile, fileName, mimeType, moduleName, true, tanent);
	}

	public FileStoreMapper store(InputStream sourceFileStream, String fileName, String mimeType, String moduleName,
			String tanent) {
		return store(sourceFileStream, fileName, mimeType, moduleName, true, tanent);
	}

	public FileStoreMapper store(InputStream fileStream, String fileName, String mimeType, String moduleName,
			boolean closeStream, String tanent) {
		try {
			FileStoreMapper fileMapper = new FileStoreMapper(randomUUID().toString(), fileName);
			Path newFilePath = this.createNewFilePath(fileMapper, moduleName);
			Files.copy(fileStream, newFilePath);
			fileMapper.setContentType(mimeType);
			fileMapper.setTenant(tanent);
			if (closeStream)
				fileStream.close();
			return fileMapper;
		} catch (IOException e) {
			throw new ApplicationRuntimeException(String.format("Error occurred while storing files at %s/%s/%s",
					this.fileStoreBaseDir, tanent, moduleName), e);
		}
	}

	public FileStoreMapper store(File file, String fileName, String mimeType, String moduleName, boolean deleteFile,
			String tanent) {
		try {
			FileStoreMapper fileMapper = new FileStoreMapper(randomUUID().toString(),
					defaultString(fileName, file.getName()));
			Path newFilePath = this.createNewFilePath(fileMapper, moduleName);
			Files.copy(file.toPath(), newFilePath);
			fileMapper.setContentType(mimeType);
			fileMapper.setTenant(tanent);
			if (deleteFile && file.delete())
				LOG.info("File store source file deleted");
			return fileMapper;
		} catch (IOException e) {
			throw new ApplicationRuntimeException(String.format("Error occurred while storing files at %s/%s/%s",
					this.fileStoreBaseDir, tanent, moduleName), e);

		}
	}

	private String defaultString(String fileName, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	private Path createNewFilePath(FileStoreMapper fileMapper, String moduleName) throws IOException {
		Path fileDirPath = this.getFileDirectoryPath(moduleName);
		if (!fileDirPath.toFile().exists()) {
			LOG.info("File Store Directory {}/{}/{} not found, creating one", this.fileStoreBaseDir, "1016",
					moduleName);
			Files.createDirectories(fileDirPath);
			LOG.info("Created File Store Directory {}/{}/{}", this.fileStoreBaseDir, "1016", moduleName);
		}
		return this.getFilePath(fileDirPath, fileMapper.getFileStoreId());
	}

	private Path getFileDirectoryPath(String moduleName) {
		return Paths.get(new StringBuilder().append(this.fileStoreBaseDir).append(separator).append("1016")
				.append(separator).append(moduleName).toString());
	}

	private Path getFilePath(Path fileDirPath, String fileStoreId) {
		return Paths.get(fileDirPath + separator + fileStoreId);
	}

	public File fetch(FileStoreMapper fileMapper, String moduleName) {
		return this.fetch(fileMapper.getFileStoreId(), moduleName);
	}

	public Set<File> fetchAll(Set<FileStoreMapper> fileMappers, String moduleName) {
		return fileMappers.stream().map(fileMapper -> this.fetch(fileMapper.getFileStoreId(), moduleName))
				.collect(Collectors.toSet());
	}

	public File fetch(String fileStoreId, String moduleName) {
		Path fileDirPath = this.getFileDirectoryPath(moduleName);
		if (!fileDirPath.toFile().exists())
			throw new ApplicationRuntimeException(
					String.format("File Store does not exist at Path : %s/%s", this.fileStoreBaseDir, moduleName));

		return this.getFilePath(fileDirPath, fileStoreId).toFile();
	}

}
