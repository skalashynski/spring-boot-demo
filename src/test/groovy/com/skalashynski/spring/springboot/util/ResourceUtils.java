package com.skalashynski.spring.springboot.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Objects.requireNonNull;

/**
 * Utility class containing common methods around bundled resources in support of testing.
 */
public class ResourceUtils {

    /**
     * Constructs a file based on a resource name. Resources are loaded from directory `/src/test/resources/`.
     *
     * @param fileName Name of the resource. For example, `dir/file.ext` will point to a resource file at `/src/test/resources/dir/file.ext`.
     *
     * @return {@link File} representation if found.
     * @throws IllegalArgumentException when file not found.
     */
    public static File getResourceAsFile(String fileName) {
        ClassLoader classLoader = ResourceUtils.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            var errorMessage = String.format("File `%s` is not found!", fileName);
            throw new IllegalArgumentException(errorMessage);
        } else {
            return new File(resource.getFile());
        }
    }

    /**
     * Reads the contents of a bundled resource and returns it as a UTF-8 string.
     *
     * @param resourceName The name of the bundled resource.
     *
     * @return The contents of the resource as a UTF-8 string, or null if the resource is not found.
     * @throws NullPointerException If the resource is not found.
     * @throws RuntimeException     If the resource cannot be read.
     */
    public static String getResourceContents(String resourceName) {

        var resourceUrl = requireNonNull(
            ResourceUtils.class.getClassLoader().getResource(resourceName)
        );

        try {
            var path = Path.of(resourceUrl.toURI());
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException("Failed to read resource '" + resourceName + "'.", e);
        }
    }
}
