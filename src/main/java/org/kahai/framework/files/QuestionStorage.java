package org.kahai.framework.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.kahai.framework.errors.FileError;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

import org.kahai.framework.models.questions.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class QuestionStorage {
    private static final Logger log = LoggerFactory.getLogger(QuestionStorage.class);

    @Autowired
<<<<<<< Updated upstream
    private ObjectMapper objectMapper;
=======
    ObjectMapper objectMapper;
>>>>>>> Stashed changes

    protected Path getStorageFolder() throws FileError {
        try {
            Path path = Paths.get("").toAbsolutePath();

            String lastSegment = path.getFileName().toString();
            if (lastSegment.equals("backend") || lastSegment.equals("src")) {
                path = path.getParent();
            }

            return path.resolve("data");
        } catch (Exception e) {
            throw new FileError();
        }
    };

    protected String getFilename(
            UUID uuid) {
        return uuid + ".json";
    };

    protected Path getPath(
            UUID uuid) {
        Path path = this.getStorageFolder();
        return path.resolve(this.getFilename(uuid));
    };

    public void delete(
            UUID uuid,
            Path path) throws FileError {
        try {
            Files.deleteIfExists(
                    this.getPath(uuid));
            log.info("Questão ({}) deletada!", uuid);
        } catch (Exception e) {
            throw new FileError();
        }
    };

    public void save(
            Question question) throws FileError {
        try {
            UUID uuid = question.getRoot().getUuid();
            Path path = this.getPath(uuid);
            try (FileOutputStream outputStream = new FileOutputStream(path.toFile())) {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(question);
                outputStream.write(json.getBytes());
            }
            ;
            log.info("Questão ({}) salva!", uuid);
        } catch (Exception e) {
            throw new FileError();
        }
        ;
    };

    public Question load(UUID uuid) throws FileError {
        Path path = this.getPath(uuid);
        File file = path.toFile();

        if (!file.exists()) {
            log.warn("Tentativa de carregar questão não existente: {}", uuid);
            throw new FileError("Arquivo da questão com UUID " + uuid + " não encontrado.", HttpStatus.NOT_FOUND);
        }

        try {
            Question question = objectMapper.readValue(file, Question.class);
            log.info("Questão ({}) carregada!", uuid);
            return question;
        } catch (IOException e) {
            log.error("Falha ao carregar ou parsear o arquivo da questão: {}", uuid, e);
            throw new FileError("Erro ao ler o arquivo da questão: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
};