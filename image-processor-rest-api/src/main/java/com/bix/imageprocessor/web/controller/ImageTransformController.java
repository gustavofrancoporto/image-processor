package com.bix.imageprocessor.web.controller;

import com.bix.imageprocessor.domain.image.service.ImageTransformStarterService;
import com.bix.imageprocessor.security.service.JwtTokenService;
import com.bix.imageprocessor.web.dto.image.ImageDto;
import com.bix.imageprocessor.web.dto.image.ImageTransformParamsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Image Transformation")
public class ImageTransformController {

    private final ImageTransformStarterService imageTransformService;
    private final JwtTokenService jwtTokenService;

    @PostMapping(path = "/image-transform", consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Performs image transformations.",
            requestBody = @RequestBody(content = @Content(encoding = @Encoding(name = "parameters", contentType = "application/json"))
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image transformation requested successfully. An e-mail is sent when the task is completed.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized.", content = @Content)
    })
    public void processImage(
            @RequestPart("parameters")
            @Validated
            ImageTransformParamsDto transformationParams,

            @RequestPart("image")
            MultipartFile imageMultipartFile,

            @AuthenticationPrincipal
            Jwt jwt
    ) throws IOException {

        var user = jwtTokenService.getUser(jwt);
        var image = new ImageDto(imageMultipartFile);

        imageTransformService.startProcess(image, transformationParams, user);

    }
}