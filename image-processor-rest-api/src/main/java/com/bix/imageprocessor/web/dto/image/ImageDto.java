package com.bix.imageprocessor.web.dto.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public record ImageDto(String fileName, String description, String contentType, byte[] data) {

    public ImageDto(MultipartFile file, String description) throws IOException {
        this(file.getOriginalFilename(), description, file.getContentType(), file.getBytes());
    }
}
