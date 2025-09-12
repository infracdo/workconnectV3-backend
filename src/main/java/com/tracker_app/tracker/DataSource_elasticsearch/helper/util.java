package com.tracker_app.tracker.DataSource_elasticsearch.helper;

import java.io.File;
import java.nio.file.Files;

import org.springframework.core.io.ClassPathResource;

public class util {
    public static String loadAsString(final String path) {
        try {
            final File resource = new ClassPathResource(path).getFile();

            return new String(Files.readAllBytes(resource.toPath()));
        } catch (final Exception e) {
            return null;
        }
    }
}
