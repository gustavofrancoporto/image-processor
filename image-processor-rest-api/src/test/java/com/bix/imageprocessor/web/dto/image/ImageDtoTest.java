package com.bix.imageprocessor.web.dto.image;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ImageDtoTest {

    @Mock
    private MultipartFile multipartFile;

    @Test
    void testConstructor() throws IOException {

        var fileName = "test-image.jpg";
        var contentType = "image/jpeg";
        var data = "***".getBytes();

        when(multipartFile.getOriginalFilename()).thenReturn(fileName);
        when(multipartFile.getContentType()).thenReturn(contentType);
        when(multipartFile.getBytes()).thenReturn(data);

        var imageDto = new ImageDto(multipartFile);

        assertThat(imageDto.fileName()).isEqualTo(fileName);
        assertThat(imageDto.contentType()).isEqualTo(contentType);
        assertArrayEquals(data, imageDto.data());
    }
}
