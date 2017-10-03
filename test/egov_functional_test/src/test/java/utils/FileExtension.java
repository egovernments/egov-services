package utils;

public enum  FileExtension {
    JAVA(".java"),
    TEXT(".txt"),
    SQL(".sql"),
    FEATURE(".feature"),
    EXCEL(".xls"),
    ELEMENTS(".elements"),
    YAML(".yaml");

    private String fileExtension;

    private FileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileExtension() {
        return this.fileExtension;
    }
}
