package org.kahai.framework.questions.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.kahai.framework.errors.FileError;
import org.kahai.framework.questions.Question;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuestionStorage {
    private static final Logger log = LoggerFactory.getLogger(QuestionStorage.class);

    @Autowired
    private ObjectMapper objectMapper;

    protected Path getStorageFolder() throws FileError {
        try {
            Path path = Paths.get("").toAbsolutePath();

            String lastSegment = path.getFileName().toString();
            if (lastSegment.equals("backend") || lastSegment.equals("src")) {
                path = path.getParent();
            };

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
        Question question
    ) throws FileError {
        UUID uuid = question.getRoot().getUuid();
        
        try {
            Path path = this.getPath(uuid);
            Files.deleteIfExists(path);
            log.info("Questão ({}) deletada!", uuid);
        } catch (Exception e) {
            log.error("Erro ao apagar arquivo da questão ({})!", uuid);
            throw new FileError();
        }
    };

    public void save(
        Question question
    ) throws FileError {
        UUID uuid = question.getRoot().getUuid();

        try {
            Path path = this.getPath(uuid);
            try (FileOutputStream outputStream = new FileOutputStream(path.toFile())) {
                String json = objectMapper.writeValueAsString(question);
                outputStream.write(json.getBytes());
            };

            log.info("Arquivo da questão ({}) escrito!", uuid);
        } catch (Exception e) {
            log.info("Erro ao escrever arquivo da questão ({})!", uuid);
            throw new FileError();
        };
    };

    public Question load(UUID uuid) throws FileError {
        Path path = this.getPath(uuid);
        File file = path.toFile();

        if (!file.exists()) {
            log.error("Arquivo da questão ({}) não existe!", uuid);
            throw new FileError();
        }
        
        try {
            Question question = objectMapper.readValue(file, Question.class);
            log.info("Questão ({}) carregada!", uuid);
            return question;
        } catch (IOException e) {
            log.error("Falha ao carregar ou parsear o arquivo da questão ({})!", uuid, e);
            throw new FileError();
        }
    };
};