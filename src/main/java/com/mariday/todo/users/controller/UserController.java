package com.mariday.todo.users.controller;

import com.mariday.todo.users.dto.UserDto;
import com.mariday.todo.users.dto.UserRegisterDto;
import com.mariday.todo.users.entity.UserEntity;
import com.mariday.todo.users.mapper.UserMapper;
import com.mariday.todo.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(
        UserService userService,
        UserMapper userMapper
    ) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@RequestBody UserRegisterDto userDetails) {
        UserEntity userEntity = userService.createUser(userDetails);
        if (userEntity == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(userMapper.toDto(userEntity), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserEntity> userEntities = userService.getAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (UserEntity entity : userEntities) {
            userDtos.add(userMapper.toDto(entity));
        }
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping(path = "/{username}")
    public ResponseEntity<String> verifyUser (@PathVariable String username) {
        UserEntity userEntity = userService.getUserByUsername(username);
        if (userEntity == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(userEntity.getUsername(), HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<UserDto> loginUser (@RequestBody UserRegisterDto userDetails) {
        UserEntity userEntity = userService.loginUser(userDetails);
        if (userEntity == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(userMapper.toDto(userEntity), HttpStatus.OK);
    }
}
