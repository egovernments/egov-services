package utils;

public enum FileExtension {
    JAVA(".java"),
    TEXT(".txt"),
    SQL(".sql"),
    FEATURE(".feature"),
    ELEMENTS(".elements"),
    EXCEL(".xls");

    private String fileExtension;

    FileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileExtension() {
        return fileExtension;
    }
}
