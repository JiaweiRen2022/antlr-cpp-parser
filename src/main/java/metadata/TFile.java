package metadata;

import java.io.File;
import java.util.List;

public class TFile {


    public TFile(File file){
        this.fileName = file.getName();
        this.path = file.getAbsolutePath();
    }

    public TFile(String relPath){
        this.path = relPath;
        this.fileName = relPath.substring(relPath.lastIndexOf("/"));
    }

    public enum AnalyzeType{
        CLAZZ,
        FUNCTION,
        LOGICAL_NODE
        ;
    }

    private String fileName;
    private String path;
    private List<TFunction> functions;
    private List<TClass> classes;
    private AnalyzeType type;
    private List<String> errorList;


    public List<TFunction> getFunctions() {
        return functions;
    }

    public void setFunctions(List<TFunction> functions) {
        this.functions = functions;
    }

    public List<TClass> getClasses() {
        return classes;
    }

    public void setClasses(List<TClass> classes) {
        this.classes = classes;
    }

    public AnalyzeType getType() {
        return type;
    }

    public void setType(AnalyzeType type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<String> errorList) {
        this.errorList = errorList;
    }
}
