package entry;

import metadata.TClass;
import metadata.TFile;
import metadata.TFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AntlrUtils;
import utils.PropertiesUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;


/**
 * Tools Entry point
 */

public class MainEntry {

    private static final Logger logger = LoggerFactory.getLogger(MainEntry.class);

    private static final String repoPathKey = "source_repo_path";

    public static String source_repo_path;

    public static Boolean log_node = false;

    static {
        try {
            //repo root path
            source_repo_path = PropertiesUtil.getConfigByKey(repoPathKey);
            log_node = Boolean.valueOf(PropertiesUtil.getConfigByKey("log_node_info"));
        } catch (IOException e) {
            logger.error("config file error!", e);
        }
    }


    public static void main(String[] args) {
        try {
            MainEntry entry = new MainEntry();
            entry.navigateFile(new File(source_repo_path));
        } catch (Exception e) {
            logger.error("Some exceptions have occurred!", e);
        }
    }

    public void navigateFile(File projectRootDir) {
        if (projectRootDir.isDirectory()) {
            for (File file : projectRootDir.listFiles()) {
                navigateFile(file);
            }
        } else {
            String name = projectRootDir.getName();
            if (name.endsWith("cpp")) {
                logger.info("==> excute file : " + projectRootDir.getAbsolutePath());
                excuteFile(projectRootDir);
            }
        }
    }


    private void excuteFile(File f) {
        try {
            long parseStart = System.currentTimeMillis();
            TFile tFile = AntlrUtils.defaultTypeParseFile(f, log_node);
            long parseEnd = System.currentTimeMillis();
            //summary
            logger.info("======================= Summary: =================================");
            logger.info("File : {}", tFile.getPath());
            logger.info("Parse time {} ", (parseEnd - parseStart) / 1000);
            summaryResults(tFile);
        } catch (Exception | Error e) {
            logger.error("file ({}) parse failed!　", f.getAbsolutePath(), e);
        }
    }


    private void summaryResults(TFile tFile) {

        logger.info("======================= Global =======================");
        Set<TFunction> globalFunctions = tFile.getGlobalFunctions();
        for (TFunction func : globalFunctions) {
            logger.info("#" + func.getName());
            logger.info("params: {}",func.getParamsAsString());
            logger.info("-----------------------");
        }

        logger.info("======================= Classes =======================");
        Set<TClass> classes = tFile.getClasses();
        for (TClass klass : classes) {
            logger.info(klass.toString());
            Set<TFunction> functions = klass.getFunctions();
            for (TFunction tFunction : functions) {
                logger.info("# TFunction: {}",tFunction.toString());
            }
            logger.info("-----------------------");
        }
        List<String> errorList = tFile.getErrorList();
        if (errorList.size()>0){
            logger.info("======================== Parse error info =======================");
            errorList.forEach(s->{
                logger.info("# : {}",s);
            });
        }
        logger.info("FINISHED");

    }





}
