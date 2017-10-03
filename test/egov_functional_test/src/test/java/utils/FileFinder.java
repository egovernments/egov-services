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
        this.allFiles = new ArrayList();
    }

    public static FileFinder fileFinder(String rootPath) {
        return new FileFinder(rootPath);
    }

    public static FileFinder fileFinder() {
        return new FileFinder(System.getProperty("user.dir"));
    }

    public List<File> find(FileExtension fileExtension) {
        this.collectFiles(new File(this.path), fileExtension.getFileExtension());
        return this.allFiles;
    }

    public File find(String fileName, FileExtension fileExtension) {
        this.collectFile(new File(this.path), fileName, fileExtension.getFileExtension());
        return this.fileToSearch;
    }

    private void collectFiles(File rootFile, String fileExtensionToSearch) {
        if (rootFile.exists() && rootFile.isDirectory()) {
            File[] files = rootFile.listFiles();

            assert files != null;

            File[] var4 = files;
            int var5 = files.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                File file = var4[var6];
                if (!file.isDirectory()) {
                    this.fileFound = file.getName().endsWith(fileExtensionToSearch);
                    if (this.fileFound) {
                        this.allFiles.add(file);
                    }
                } else {
                    this.collectFiles(file, fileExtensionToSearch);
                }
            }
        }

    }

    private void collectFile(File rootFile, String fileName, String fileExtensionToSearch) {
        if (rootFile.exists() && rootFile.isDirectory()) {
            File[] files = rootFile.listFiles();

            assert files != null;

            File[] var5 = files;
            int var6 = files.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                File file = var5[var7];
                if (!file.isDirectory()) {
                    this.fileFound = file.getName().equals(fileName + fileExtensionToSearch);
                    if (this.fileFound) {
                        this.fileToSearch = file;
                        break;
                    }
                } else {
                    this.collectFile(file, fileName, fileExtensionToSearch);
                }
            }
        }

    }

}
