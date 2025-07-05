package org.kahai.framework.services;

import org.kahai.framework.auth.JwtUtil;
import org.kahai.framework.dtos.request.RegisterRequest;
import org.kahai.framework.errors.InvalidCredentials;
import org.kahai.framework.errors.ValidationsError;
import org.kahai.framework.models.User;
import org.kahai.framework.repositories.UserRepository;
import org.kahai.framework.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    public User createUser(
        RegisterRequest body
    ) throws ValidationsError {
        String email = body.getEmail();
        String password = body.getPassword();
        String name = body.getName();

        Validator validator = new Validator();

        validator.validate("body", body);
        
        validator.validate("name", name)
            .nonempty("O nome é obrigatório!")
            .max(45, "O nome não pode ter mais de 45 caracteres!");
        
        validator.validate("email", email)
            .nonempty("O e-mail é obrigatório!")
            .email("O e-mail deve ser válido!")
            .verify(
                !this.repository.findByEmail(email).isPresent(), 
                "O e-mail já se encontra em uso!"
            );
        
        validator.validate("password", password)
            .nonempty("A senha é obrigatória!")
            .min(6, "A senha deve ter no mínimo 6 caracteres!")
            .max(20, "A senha deve ter no máximo 6 caracteres!");
        
        validator.run();

        User user = new User(email, encoder.encode(password), name);
        repository.save(user);

        log.info("Novo usuário cadastrado!");

        return user;
    };

    public String login(
        String email, 
        String password
    ) throws InvalidCredentials {
        User user = repository.findByEmail(email)
            .orElseThrow(InvalidCredentials::new);

        if(!encoder.matches(password, user.getPassword()))
            throw new InvalidCredentials();

        return jwtUtil.generateToken(user.getUuid());
    };
};
