package com.bix.imageprocessor.web.validation;

import com.bix.imageprocessor.web.dto.image.ImageTransformParamsDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.BooleanUtils.isNotTrue;
import static org.apache.commons.lang3.ObjectUtils.allNull;
import static org.apache.commons.lang3.ObjectUtils.anyNull;

@Component
public class ImageTransformParamsRequiredConstraintValidator implements ConstraintValidator<ImageTransformParamsRequired, ImageTransformParamsDto> {

    @Override
    public boolean isValid(ImageTransformParamsDto params, ConstraintValidatorContext context) {

        if (isNotTrue(params.invertColors()) && isNotTrue(params.grayscale()) && params.sepiaIntensity() == null && params.resizeRatio() == null) {
            return false;
        }
        return true;
    }
}
