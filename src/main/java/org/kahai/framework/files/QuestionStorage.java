package org.kahai.framework.files;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.kahai.framework.errors.FileError;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

import org.kahai.framework.models.questions.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class QuestionStorage {
    private static final Logger log = LoggerFactory.getLogger(QuestionStorage.class);

    protected Path getStorageFolder() throws FileError {
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

    protected String getFilename(
        UUID uuid
    ) {
        return uuid + ".json";
    };

    protected Path getPath(
        UUID uuid
    ) {
        Path path = this.getStorageFolder();
        return path.resolve(this.getFilename(uuid));
    };

    public void delete(
        UUID uuid,
        Path path
    ) throws FileError {
        try {
            Files.deleteIfExists(
                this.getPath(uuid)
            );
            log.info("Questão ({}) deletada!", uuid);
        } catch (Exception e) {
            throw new FileError();
        }
    };

    public void save(
        Question question
    ) throws FileError {
        try {
            UUID uuid = question.getRoot().getUuid();
            Path path = this.getPath(uuid);
            try (FileOutputStream outputStream = new FileOutputStream(path.toFile())) {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(question);
                outputStream.write(json.getBytes());
            };
            log.info("Questão ({}) salva!",uuid);
        } catch (Exception e) {
            throw new FileError();
        };
    };

    public Question load(UUID uuid) {
        try {
            //Path path = this.getPath(uuid);
            
            // TODO - Carregar o JSON da questão e converter para Question
            // e... claro, fazer usso desse método depois
            throw new FileError();
            //log.info("Questão ({}) carregada!",uuid);
        } catch (Exception e) {
            throw new FileError();
        }
    };
};