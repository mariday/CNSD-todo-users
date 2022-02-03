package com.mariday.todo.users.mapper;

import com.mariday.todo.users.dto.UserDto;
import com.mariday.todo.users.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(UserDto dto);
    UserDto toDto(UserEntity entity);
}
