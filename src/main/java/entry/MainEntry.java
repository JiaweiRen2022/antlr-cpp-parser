package entry;

import metadata.TFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AntlrUtils;
import utils.PropertiesUtil;

import java.io.File;
import java.io.IOException;


/**
 * Tools Entry point
 */

public class MainEntry {

    private static final Logger logger = LoggerFactory.getLogger(MainEntry.class);

    private static final String repoPathKey = "source_repo_path";

    public static String source_repo_path;

    static {
        try {
            //repo root path
            source_repo_path = PropertiesUtil.getConfigByKey(repoPathKey);
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
            TFile tFile = AntlrUtils.defaultTypeParseFile(f);
            long parseEnd = System.currentTimeMillis();
            logger.info("parse time {} ", (parseEnd - parseStart) / 1000);
        } catch (Exception | Error e) {
            logger.error("FILE ({})PARSE FAILED!　", f.getAbsolutePath(), e);
        }
    }


    public static String getRelativePath(String path) {
        path = path.replaceAll("\\\\", "/");
        if (path.contains(MainEntry.source_repo_path))
            return path.split(MainEntry.source_repo_path)[1];
        return path;
    }

    public static String getAbsPath(String path) {
        return source_repo_path + path;
    }


}
