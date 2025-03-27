package com.bix.imageprocessor.web.dto.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public record ImageDto(
        String fileName,
        String contentType,
        byte[] data
) {

    public ImageDto(MultipartFile file) throws IOException {
        this(file.getOriginalFilename(), file.getContentType(), file.getBytes());
    }
}
