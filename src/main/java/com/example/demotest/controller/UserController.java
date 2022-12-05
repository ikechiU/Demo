package com.example.demotest.controller;

import com.example.demotest.model.UserRequest;
import com.example.demotest.model.UserRest;
import com.example.demotest.service.UserService;
import com.example.demotest.shared.UserDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserRest> createUser(@RequestBody @Valid UserRequest userRequest) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userRequest, userDto);

        UserDto user = userService.createUser(userDto);

        UserRest userRest = new UserRest();
        BeanUtils.copyProperties(user, userRest);

        return new ResponseEntity<>(userRest, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRest> updateUser(@PathVariable Long id, @RequestBody @Valid UserRequest userRequest) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userRequest, userDto);

        UserDto user = userService.updateUser(id, userDto);

        UserRest userRest = new UserRest();
        BeanUtils.copyProperties(user, userRest);

        return new ResponseEntity<>(userRest, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRest> getUser(@PathVariable Long id) {
        UserDto user = userService.getUser(id);

        UserRest userRest = new UserRest();
        BeanUtils.copyProperties(user, userRest);

        return new ResponseEntity<>(userRest, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserRest>> getUsers() {
        List<UserDto> users = userService.getUsers();

//        imperativePattern(users);

        Function<UserDto, UserRest> mapper = userDto -> {
            UserRest userRest = new UserRest();
            BeanUtils.copyProperties(userDto, userRest);
            return userRest;
        };

        List<UserRest> userRests = users.stream()
                .map(mapper)
                .collect(Collectors.toList());

        return new ResponseEntity<>(userRests, HttpStatus.OK);
    }

    private List<UserRest> imperativePattern(List<UserDto> users) {
        List<UserRest> userRests = new ArrayList<>();

        for (UserDto userDto : users) {
            UserRest userRest = new UserRest();
            BeanUtils.copyProperties(userDto, userRest);
            userRests.add(userRest);
        }

        return userRests;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
