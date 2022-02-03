package com.mariday.todo.users.service;

import com.mariday.todo.users.dto.UserRegisterDto;
import com.mariday.todo.users.entity.UserEntity;
import com.mariday.todo.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity createUser(UserRegisterDto userDetails) {
        if (!isValid(userDetails)) return null;
        Optional<UserEntity> existingUser = userRepository.findByUsername(userDetails.getUsername());
        if (existingUser.isPresent()) return null;
        UserEntity newUser = new UserEntity();
        newUser.setUsername(userDetails.getUsername());
        userRepository.save(newUser);
        return newUser;
    }

    public List<UserEntity> getAll() {
        return (List<UserEntity>) userRepository.findAll();
    }

    public UserEntity getUserByUsername(String username) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        if (userEntity.isEmpty()) return null;
        return userEntity.get();
    }

    public UserEntity loginUser(UserRegisterDto userDetails) {
        if (!isValid(userDetails)) return null;
        Optional<UserEntity> userEntity = userRepository.findByUsername(userDetails.getUsername());
        if (userEntity.isEmpty()) return null;
        return userEntity.get();
    }

    public boolean isValid(UserRegisterDto userDetails) {
        return userDetails.getUsername() != null && !userDetails.getUsername().equals("");
    }
}
