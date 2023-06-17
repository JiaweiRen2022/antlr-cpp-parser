package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    private static Properties properties;

    public static String getConfigByKey(String key) throws IOException {
        if (properties == null){
            properties = new Properties();
            InputStream resourceAsStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("config.properties");
            properties.load(resourceAsStream);
        }
        return properties.getProperty(key);
    }



}
