package org.kahai.framework.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.kahai.framework.errors.FileError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

public abstract class Storage<T> {
    private static final Logger log = LoggerFactory.getLogger(Storage.class);

    @Autowired
    @Getter
    private ObjectMapper objectMapper;

    protected Path getStorageFolder(String relativePath) throws FileError {
        try {
            Path path = Paths.get("").toAbsolutePath();

            String lastSegment = path.getFileName().toString();
            if (lastSegment.equals("backend") || lastSegment.equals("src")) {
                path = path.getParent();
            };

            return path.resolve(relativePath);
        } catch (Exception e) {
            log.error(
                "Erro ao montar caminho ({}) para a pasta!\n\n{}\n",
                relativePath,
                e.getMessage()
            );
            throw new FileError();
        }
    };

    protected Boolean delete(Path path) throws IOException, SecurityException, DirectoryNotEmptyException {
        return Files.deleteIfExists(path);
    };

    protected void write(Path path, T object) throws FileNotFoundException, IOException {
        try (FileOutputStream outputStream = new FileOutputStream(path.toFile())) {
            String json = objectMapper.writeValueAsString(object);
            outputStream.write(json.getBytes());
        };
    };

    protected T read(Path path, Class<T> objectClass) throws FileNotFoundException, IOException, StreamReadException, DatabindException {
        File file = path.toFile();

        if (!file.exists()) {
            log.error("Arquivo ({}) não existe!", path.toAbsolutePath());
            throw new FileNotFoundException();
        };

        return objectMapper.readValue(file, objectClass);
    };
};
