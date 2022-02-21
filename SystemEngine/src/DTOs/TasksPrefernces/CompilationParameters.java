package DTOs.TasksPrefernces;

public class CompilationParameters {

    private final String sourcePath;
    private final String destinationPath;
    private String ExtraData;

    public CompilationParameters(String sourcePath, String destinationPath) {
        this.sourcePath = sourcePath;
        this.destinationPath = destinationPath;
    }

    public String getSourcePath() {
        return this.sourcePath;
    }

    public String getDestinationPath() {
        return this.destinationPath;
    }

    public String getExtraData() {
        return ExtraData;
    }

    public void setExtraData(String extraData) {
        ExtraData = extraData;
    }

    @Override
    public String toString() {
        return
                "source Path='" + sourcePath + "\n" +
                " destination Path='" + destinationPath + "\n" +
                " Extra Data='" + ExtraData + "\n";
    }
}
