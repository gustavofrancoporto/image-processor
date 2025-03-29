package com.bix.imageprocessor.web.controller;

import com.bix.imageprocessor.persistence.repository.ImageTransformRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ContentDisposition.attachment;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Image")
public class ImageController {

    private final ImageTransformRepository imageTransformRepository;

    @GetMapping("/images/{code}")
    @Operation(summary = "Download transformed image.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image transformed", content = @Content),
            @ApiResponse(responseCode = "404", description = "Image not found", content = @Content),
    })
    public ResponseEntity<byte[]> get(@PathVariable String code) {

        var imageTransformation = imageTransformRepository.findByDownloadCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Image not found."));

        var filename = imageTransformation.getImage().getFileName();

        var httpHeaders = new HttpHeaders();
        httpHeaders.set(CONTENT_TYPE, APPLICATION_OCTET_STREAM_VALUE);
        httpHeaders.set(CONTENT_DISPOSITION, attachment().filename(filename).build().toString());

        return ok()
                .headers(httpHeaders)
                .body(imageTransformation.getTransformedData());
    }
}
