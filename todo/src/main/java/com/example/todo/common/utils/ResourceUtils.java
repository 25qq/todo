package com.example.todo.common.utils;

import com.google.common.base.Strings;
import lombok.NonNull;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ResourceUtils {

    public static Optional<Path> findFirstFilesystemResource(Collection<String> filenames, Collection<Path> systemPaths) {
        return findFilesystemResources(filenames, systemPaths).stream().findFirst();
    }

    public static List<Path> findFilesystemResources(Collection<String> filenames, Collection<Path> systemPaths) {
        List<Path> paths = new ArrayList<>();
        for (Path path : systemPaths) {
            for (String filename : filenames) {
                final Path file = path.resolve(filename);
                if (Files.exists(file)) {
                    paths.add(file);
                }
            }
        }
        return paths;
    }


    public static Resource findNamedResource(@NonNull final Environment env, @NonNull final String propertyName, @NonNull final String environmentName, @NonNull final String fileName) {
        return findNamedResource(env, propertyName)
                .or(() -> findNamedResource(env, environmentName))
                .orElse(findNamedResource(fileName));
    }

    public static Optional<Resource> findNamedResource(@NonNull final Environment env, @NonNull final String propertyName) {
        final String filename = env.getProperty(propertyName, "");

        if (!Strings.isNullOrEmpty(filename))
            return Optional.of(findNamedResource(filename));

        return Optional.empty();
    }

    public static Resource findNamedResource(@NonNull final String fileName) {
        return new FileSystemResource(fileName);
    }

    public static String readToString(Path path, Consumer<Throwable> onError) {
        try {
            return readToString(path);
        } catch (IOException e) {
            onError.accept(e);
            return null;
        }
    }

    public static String readToString(Path path) throws IOException {
        return Files.readString(path);
    }


}
