package com.bix.imageprocessor.web.controller;

import com.bix.imageprocessor.domain.subscription.model.SubscriptionType;
import com.bix.imageprocessor.domain.user.service.UserService;
import com.bix.imageprocessor.web.dto.user.CreateUserDto;
import com.bix.imageprocessor.web.dto.user.UserDto;
import com.bix.imageprocessor.web.exception.model.ApiError;
import com.bix.imageprocessor.web.exception.model.InternalApplicationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{id}")
    @Operation(summary = "Get user information. A simple user can retrieve only his own information, while an admin can retrieve any user's information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User information retrieve successfully", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied. If current user isn't admin and it's retrieving information of another user.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized.", content = @Content)
    })
    public UserDto get(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {

        var currentUserId = Long.valueOf(jwt.getSubject());
        var currentUser = userService.findById(currentUserId)
                .orElseThrow(() -> new InternalApplicationException("Current user not found"));

        if (Objects.equals(id, currentUserId)) {
            return currentUser;
        }
        if (isFalse(currentUser.isAdmin())) {
            throw new AccessDeniedException("Access denied");
        }

        return userService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @PostMapping("/users")
    @Operation(summary = "Allows the creation of a new user with basic subscription.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation errors", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Void> create(@RequestBody @Validated CreateUserDto userDto) {

        var user = userService.create(userDto);

        var userUri = fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return created(userUri).build();
    }

    @PatchMapping("/users/{id}/subscriptionType/{subscriptionType}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @Operation(summary = "Allows an admin user to update the subscription type of the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "204", description = "User already subscribed to the subscription type requested", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation errors", content = @Content)
    })
    public UserDto update(@PathVariable Long id, @PathVariable SubscriptionType subscriptionType) {
        return userService.update(id, subscriptionType);
    }
}