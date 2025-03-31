package com.bix.imageprocessor.domain.image.service.impl;

import com.bix.imageprocessor.domain.image.model.Image;
import com.bix.imageprocessor.persistence.repository.ImageRepository;
import com.bix.imageprocessor.web.dto.image.ImageDto;
import com.bix.imageprocessor.web.mapper.ImageMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @Mock ImageMapper imageMapper;
    @Mock ImageRepository imageRepository;

    @InjectMocks ImageServiceImpl imageService;

    @Mock ImageDto imageDto;
    @Mock Image imageMapped, imageSaved;


    @Test
    void shouldSaveImageSuccessfully() {

        when(imageMapper.convert(imageDto)).thenReturn(imageMapped);
        when(imageRepository.save(imageMapped)).thenReturn(imageSaved);

        var result = imageService.save(imageDto);

        assertThat(result).isEqualTo(imageSaved);
    }
}
