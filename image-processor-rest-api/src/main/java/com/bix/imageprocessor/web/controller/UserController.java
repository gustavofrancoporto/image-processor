package com.bix.imageprocessor.web.controller;

import com.bix.imageprocessor.domain.user.service.UserService;
import com.bix.imageprocessor.web.dto.user.CreateUserDto;
import com.bix.imageprocessor.web.dto.user.ViewUserDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{id}")
    public ResponseEntity<ViewUserDto> get(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {

        var currentUserId = Long.valueOf(jwt.getSubject());
        if (!Objects.equals(id, currentUserId)) {
            throw new AccessDeniedException("Access denied");
        }

        var viewUserDto = userService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return ResponseEntity.ok(viewUserDto);
    }

    @PostMapping("/admin/users")
    public ResponseEntity<Void> create(@RequestBody @Validated CreateUserDto createUserDto) {

        var user = userService.create(createUserDto);

        var userUri = fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(userUri).build();
    }
}