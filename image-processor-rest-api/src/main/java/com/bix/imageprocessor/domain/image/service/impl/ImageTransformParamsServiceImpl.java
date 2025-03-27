package com.bix.imageprocessor.domain.image.service.impl;

import com.bix.imageprocessor.domain.image.model.ImageTransformParams;
import com.bix.imageprocessor.domain.image.service.ImageTransformParamsService;
import com.bix.imageprocessor.persistence.repository.ImageTransformParamsRepository;
import com.bix.imageprocessor.web.dto.image.ImageTransformParamsDto;
import com.bix.imageprocessor.web.mapper.ImageTransformParamsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageTransformParamsServiceImpl implements ImageTransformParamsService {

    private final ImageTransformParamsMapper imageTransformParamsMapper;
    private final ImageTransformParamsRepository imageTransformParamsRepository;

    @Override
    public ImageTransformParams save(ImageTransformParamsDto imageTransformIdParamsDto) {

        var imageTransformIdParams = imageTransformParamsMapper.convert(imageTransformIdParamsDto);
        return imageTransformParamsRepository.save(imageTransformIdParams);
    }
}
