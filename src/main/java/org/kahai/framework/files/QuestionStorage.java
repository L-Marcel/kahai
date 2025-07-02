package org.kahai.framework.files;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.kahai.framework.errors.FileError;
import org.kahai.framework.errors.HttpError;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

import org.kahai.framework.models.questions.Question;
import org.springframework.stereotype.Component;

@Component
public class QuestionStorage {
    public Path getStorageFolder() throws FileError {
        try {
            Path path = Paths.get("").toAbsolutePath();

            String lastSegment = path.getFileName().toString();
            if(lastSegment.equals("backend") || lastSegment.equals("src")) {
                path = path.getParent();
            }
        
            return path.resolve("data");
        } catch (Exception e) {
            throw new FileError();
        }
    };

    public String makeFilename(
        UUID uuid
    ) {
        return uuid + ".json";
    };

    public void delete(
        UUID uuid,
        Path path
    ) throws FileError {
        try {
            Path filePath = path.resolve(this.makeFilename(uuid));
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            throw new FileError();
        }
    };

    public void save(
        Question question
    ) throws FileError {
        try {
            Path destiny = this.getStorageFolder();
            String filename = this.makeFilename(question.getRoot().getUuid());
            try (FileOutputStream outputStream = new FileOutputStream(destiny.resolve(filename).toFile())) {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(question);
                outputStream.write(json.getBytes());
            };
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new FileError();
        };
    };
};