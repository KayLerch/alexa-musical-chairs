package io.klerch.alexa.musicalchairs;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Encapsulates access to application-wide property values
 */
public class SkillConfig {
    private static Properties properties = new Properties();
    private static final String defaultPropertiesFile = "app.properties";
    private static final String customPropertiesFile = "my.app.properties";

    /**
     * Static block does the bootstrapping of all configuration properties with
     * reading out values from different resource files
     */
    static {
        final String propertiesFile =
                SkillConfig.class.getClassLoader().getResource(customPropertiesFile) != null ?
                        customPropertiesFile :
                        SkillConfig.class.getClassLoader().getResource(defaultPropertiesFile) != null ?
                        defaultPropertiesFile : null;

        if (propertiesFile != null) {
            final InputStream propertiesStream = SkillConfig.class.getClassLoader().getResourceAsStream(propertiesFile);
            try {
                properties.load(propertiesStream);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (propertiesStream != null) {
                    try {
                        propertiesStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static String getProperty(final String key) {
        return Optional.ofNullable(properties).map(p -> p.getProperty(key)).orElse(System.getenv(key));
    }

    static String getAlexaAppId() {
        return getProperty("AlexaAppId");
    }

    public static String getS3BucketUrl() {
        return getProperty("S3BucketUrl");
    }

    public static String[] getSongs() {
        return getProperty("Songs").split(",");
    }

    public static Integer getInterruptProbabilityPercent() {
        return Integer.valueOf(getProperty("InterruptProbabilityPercent"));
    }
}
