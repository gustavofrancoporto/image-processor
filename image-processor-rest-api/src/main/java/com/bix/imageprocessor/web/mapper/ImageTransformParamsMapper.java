package com.bix.imageprocessor.web.mapper;

import com.bix.imageprocessor.domain.image.model.ImageTransformParams;
import com.bix.imageprocessor.web.dto.image.ImageTransformParamsDto;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface ImageTransformParamsMapper extends Converter<ImageTransformParams, ImageTransformParamsDto> {

    ImageTransformParams convert(ImageTransformParamsDto paramsDto);
}