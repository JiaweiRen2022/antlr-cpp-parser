package metadata;

import java.io.File;
import java.util.List;
import java.util.Set;

public class TFile {


    public TFile(File file){
        this.fileName = file.getName();
        this.path = file.getAbsolutePath();
    }


    private String fileName;
    private String path;
    private Set<TFunction> globalFunctions;
    private Set<TClass> classes;
    private List<String> errorList;

    public Set<TFunction> getGlobalFunctions() {
        return globalFunctions;
    }

    public void setGlobalFunctions(Set<TFunction> globalFunctions) {
        this.globalFunctions = globalFunctions;
    }

    public Set<TClass> getClasses() {
        return classes;
    }

    public void setClasses(Set<TClass> classes) {
        this.classes = classes;
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
