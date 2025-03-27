package com.bix.imageprocessor.domain.image.service.impl;

import com.bix.imageprocessor.domain.image.service.ImageService;
import com.bix.imageprocessor.domain.image.model.Image;
import com.bix.imageprocessor.persistence.repository.ImageRepository;
import com.bix.imageprocessor.web.dto.image.ImageDto;
import com.bix.imageprocessor.web.mapper.ImageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageMapper imageMapper;
    private final ImageRepository imageRepository;

    @Override
    public Image save(ImageDto imageDto) {

        var image = imageMapper.convert(imageDto);
        return imageRepository.save(image);
    }
}
