package com.bix.imageprocessor.web.mapper;

import com.bix.imageprocessor.domain.image.model.Image;
import com.bix.imageprocessor.web.dto.image.ImageDto;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface ImageMapper extends Converter<Image, ImageDto> {
    Image convert(ImageDto imageDto);
}