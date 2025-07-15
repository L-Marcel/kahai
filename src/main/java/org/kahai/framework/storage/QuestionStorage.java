package org.kahai.framework.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.kahai.framework.errors.FileError;
import org.kahai.framework.questions.Question;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class QuestionStorage extends Storage<Question> {
    private static final Logger log = LoggerFactory.getLogger(QuestionStorage.class);
    
    protected String getFilename(
        UUID uuid
    ) {
        return uuid + ".json";
    };

    protected Path getPath(
        UUID uuid
    ) {
        Path path = this.getStorageFolder("data");
        return path.resolve(this.getFilename(uuid));
    };

    public void delete(
        Question question
    ) throws FileError {
        UUID uuid = question.getRoot().getUuid();
        
        try {
            Path path = this.getPath(uuid);
            this.delete(path);
            log.info("Questão ({}) deletada!", uuid);
        } catch (Exception e) {
            log.error(
                "Erro ao apagar arquivo da questão ({})!\n\n{}\n",
                uuid,
                e.getMessage()
            );
            throw new FileError();
        }
    };

    public void save(
        Question question
    ) throws FileError {
        UUID uuid = question.getRoot().getUuid();

        try {
            Path path = this.getPath(uuid);
            this.write(path, question);
            log.info("Arquivo da questão ({}) escrito!", uuid);
        } catch (Exception e) {
            log.error(
                "Erro ao escrever arquivo da questão ({})!\n\n{}\n", 
                uuid,
                e.getMessage()
            );
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
            Question question = this.read(path, Question.class);
            log.info("Questão ({}) carregada!", uuid);
            return question;
        } catch (IOException e) {
            log.error(
                "Falha ao carregar ou parsear o arquivo da questão ({})!\n\n{}\n", 
                uuid, 
                e.getMessage()
            );
            throw new FileError();
        }
    };
};