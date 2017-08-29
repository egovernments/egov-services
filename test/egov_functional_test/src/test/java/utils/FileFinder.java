package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileFinder {

    private String path;
    private boolean fileFound;
    private List<File> allFiles;
    private File fileToSearch;

    private FileFinder(String path) {
        this.path = path;
        allFiles = new ArrayList<>();
    }

    public static FileFinder fileFinder(String rootPath) {
        return new FileFinder(rootPath);
    }

    public static FileFinder fileFinder() {
        return new FileFinder(System.getProperty("user.dir"));
    }

    public List<File> find(FileExtension fileExtension) {
        collectFiles(new File(path), fileExtension.getFileExtension());
        return allFiles;
    }

    public File find(String fileName, FileExtension fileExtension) {
        collectFile(new File(path), fileName, fileExtension.getFileExtension());
        return fileToSearch;
    }

    private void collectFiles(File rootFile, String fileExtensionToSearch) {
        if (rootFile.exists() && rootFile.isDirectory()) {
            File[] files = rootFile.listFiles();
            assert files != null;
            for (File file : files) {
                if (!file.isDirectory()) {
                    fileFound = file.getName().endsWith(fileExtensionToSearch);
                    if (fileFound) {
                        allFiles.add(file);
                    }
                } else {
                    collectFiles(file, fileExtensionToSearch);
                }
            }
        }
    }

    private void collectFile(File rootFile, String fileName, String fileExtensionToSearch) {
        if (rootFile.exists() && rootFile.isDirectory()) {
            File[] files = rootFile.listFiles();
            assert files != null;
            for (File file : files) {
                if (!file.isDirectory()) {
                    fileFound = file.getName().matches(fileName + fileExtensionToSearch);
                    if (fileFound) {
                        fileToSearch = file;
                        break;
                    }
                } else {
                    collectFile(file, fileName, fileExtensionToSearch);
                }
            }
        }
    }
}
