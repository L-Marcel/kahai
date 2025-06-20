package org.kahai.framework.services;

import org.kahai.framework.auth.JwtUtil;
import org.kahai.framework.dtos.request.RegisterRequestBody;
import org.kahai.framework.errors.EmailAlreadyInUse;
import org.kahai.framework.errors.InvalidCredentials;
import org.kahai.framework.errors.InvalidEmail;
import org.kahai.framework.errors.MissingFields;
import org.kahai.framework.errors.WeakPassword;
import org.kahai.framework.models.User;
import org.kahai.framework.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public final class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    public User createUser(
        RegisterRequestBody body
    ) throws MissingFields, InvalidEmail, WeakPassword, EmailAlreadyInUse {
        String email = body.getEmail();
        String password = body.getPassword();
        String name = body.getName();
        
        if(
            email == null || email.isBlank() ||
            password == null || password.isBlank() ||
            name == null || name.isBlank()
        ) throw new MissingFields();

        if(!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
            throw new InvalidEmail();

        if(password.length() < 6)
            throw new WeakPassword();

        if(repository.findByEmail(email).isPresent())
            throw new EmailAlreadyInUse();

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
