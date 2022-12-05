package com.example.demotest.service;

import com.example.demotest.shared.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto updateUser(Long id, UserDto userDto);
    UserDto getUser(Long id);
    List<UserDto> getUsers();
    void deleteUser(Long id);
}
