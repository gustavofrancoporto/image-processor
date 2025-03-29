package com.bix.imageprocessor.web.mapper;

import com.bix.imageprocessor.domain.image.model.ImageTransformParams;
import com.bix.imageprocessor.web.dto.image.ImageTransformParamsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_EVEN;

@Mapper(componentModel = "spring")
public interface ImageTransformParamsMapper extends Converter<ImageTransformParams, ImageTransformParamsDto> {

    @Mapping(source = "resizePercentage", target = "resizeRatio", qualifiedByName = "percentToDecimal")
    ImageTransformParams convert(ImageTransformParamsDto paramsDto);

    @Named("percentToDecimal")
    static BigDecimal percentToDecimal(Integer resizePercentage) {
        return resizePercentage == null ? null : new BigDecimal(resizePercentage).divide(new BigDecimal(100), 2, HALF_EVEN);
    }
}