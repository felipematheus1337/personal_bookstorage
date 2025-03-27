package com.bookstorage.app.service;


import com.bookstorage.app.config.AMQPConfiguration;
import com.bookstorage.app.dto.BookCreateDTO;
import com.bookstorage.app.dto.BookProcessingMessage;
import com.bookstorage.app.dto.ResponseDTO;
import com.bookstorage.app.dto.UserCreateDTO;
import com.bookstorage.app.models.User;
import com.bookstorage.app.repository.UserRepository;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class BusinessServiceImpl implements BusinessService {

    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    public BusinessServiceImpl(UserRepository userRepository, RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public ResponseDTO createUser(UserCreateDTO dto) {
         var userAlreadyCreated = userRepository.findByEmail(dto.email());

         if (userAlreadyCreated.isPresent()) return new ResponseDTO(409, "An user already created with this email.");

         String hashedPassword = BCrypt.hashpw(dto.password(),BCrypt.gensalt());

         userRepository.save(new User(dto.name(), dto.email(), hashedPassword));

         return new ResponseDTO(201, "User created sucessfully.");

    }

    @Override
    public void registerBook(File file, Long userId, BookCreateDTO dto) throws IOException {
        var byteFile = Files.readAllBytes(file.toPath());
        BookProcessingMessage message = new BookProcessingMessage(userId.toString(),dto, byteFile);
        this.rabbitTemplate.convertAndSend(AMQPConfiguration.bookStorageQueue, message);
    }


}
