package com.example.demotest.service.impl;

import com.example.demotest.entities.User;
import com.example.demotest.repositories.UserRepository;
import com.example.demotest.service.UserService;
import com.example.demotest.shared.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        Boolean exists = userRepository.existsByContact(userDto.getContact());
        if (exists) throw new RuntimeException("User already exist");

        User user = new User();
        BeanUtils.copyProperties(userDto, user);

        User userCreated = userRepository.save(user);

        UserDto createdUserDto = new UserDto();
        BeanUtils.copyProperties(userCreated, createdUserDto);

        return createdUserDto;
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        boolean exists = userRepository.existsById(id);
        if (!exists) throw new RuntimeException("User does not exist");

        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setId(id);

        User userUpdated = userRepository.save(user);

        UserDto updatedUserDto = new UserDto();
        BeanUtils.copyProperties(userUpdated, updatedUserDto);

        return updatedUserDto;
    }

    @Override
    public UserDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);

        return userDto;
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();

        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            userDtos.add(userDto);
        }

        return userDtos;
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        userRepository.delete(user);
    }
}
