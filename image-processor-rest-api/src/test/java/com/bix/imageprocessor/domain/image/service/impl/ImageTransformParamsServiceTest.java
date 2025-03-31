package com.bix.imageprocessor.domain.image.service.impl;

import com.bix.imageprocessor.domain.image.model.ImageTransformParams;
import com.bix.imageprocessor.persistence.repository.ImageTransformParamsRepository;
import com.bix.imageprocessor.web.dto.image.ImageTransformParamsDto;
import com.bix.imageprocessor.web.mapper.ImageTransformParamsMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageTransformParamsServiceTest {

    @Mock ImageTransformParamsMapper imageTransformParamsMapper;
    @Mock ImageTransformParamsRepository imageTransformParamsRepository;

    @InjectMocks ImageTransformParamsServiceImpl imageService;

    @Mock ImageTransformParamsDto paramsDto;
    @Mock ImageTransformParams paramsMapped, paramsSaved;


    @Test
    void shouldSaveParamsSuccessfully() {

        when(imageTransformParamsMapper.convert(paramsDto)).thenReturn(paramsMapped);
        when(imageTransformParamsRepository.save(paramsMapped)).thenReturn(paramsSaved);

        var result = imageService.save(paramsDto);

        assertThat(result).isEqualTo(paramsSaved);
    }
}
