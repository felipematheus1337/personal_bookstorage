package com.bookstorage.app.service;


import com.bookstorage.app.dto.ResponseDTO;
import com.bookstorage.app.dto.UserCreateDTO;
import com.bookstorage.app.models.User;
import com.bookstorage.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class BusinessServiceImpl implements BusinessService {

    private final UserRepository userRepository;

    public BusinessServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseDTO createUser(UserCreateDTO dto) {
         var userAlreadyCreated = userRepository.findByEmail(dto.email());

         if (userAlreadyCreated.isPresent()) return new ResponseDTO(409, "An user already created with this email.");

         String hashedPassword = BCrypt.hashpw(dto.password(),BCrypt.gensalt());

         userRepository.save(new User(dto.name(), dto.email(), hashedPassword));

         return new ResponseDTO(201, "User created sucessfully.");

    }


}
