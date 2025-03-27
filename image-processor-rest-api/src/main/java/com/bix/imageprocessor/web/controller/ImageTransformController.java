package com.bix.imageprocessor.web.controller;

import com.bix.imageprocessor.domain.image.service.ImageTransformStarterService;
import com.bix.imageprocessor.security.service.JwtTokenService;
import com.bix.imageprocessor.web.dto.image.ImageDto;
import com.bix.imageprocessor.web.dto.image.ImageTransformParamsDto;
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

@RestController
@RequestMapping("/image-transform")
@RequiredArgsConstructor
public class ImageTransformController {

    private final ImageTransformStarterService imageTransformService;
    private final JwtTokenService jwtTokenService;

    @PostMapping
    public void processImage(
            @RequestPart("transformParams")@Validated ImageTransformParamsDto processingParams,
            @RequestPart("image") MultipartFile imageMultipartFile,
            @AuthenticationPrincipal Jwt jwt) throws IOException {

        var user = jwtTokenService.getUser(jwt);
        var image = new ImageDto(imageMultipartFile);

        imageTransformService.startProcess(image, processingParams, user);

    }
}