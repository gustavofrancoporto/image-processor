package com.bix.imageprocessor.web.controller;

import com.bix.imageprocessor.domain.user.service.UserService;
import com.bix.imageprocessor.web.dto.user.CreateUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateUserDto user) {
        userService.create(user.email(), user.password());
        return ResponseEntity.ok().build();
    }
}