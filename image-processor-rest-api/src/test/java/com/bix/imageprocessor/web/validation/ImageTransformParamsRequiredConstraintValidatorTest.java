package com.bix.imageprocessor.web.validation;

import com.bix.imageprocessor.web.dto.image.ImageTransformParamsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageTransformParamsRequiredConstraintValidatorTest {

    ImageTransformParamsRequiredConstraintValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new ImageTransformParamsRequiredConstraintValidator();
    }

    @ParameterizedTest
    @CsvSource({
            "  ,   ,      ,      , false",
            "  ,   , false,      , false",
            "  ,   ,      , false, false",
            "10,   ,      ,      , true",
            "10,   , false, false, true",
            "  , 10,      ,      , true",
            "  ,   , true ,      , true",
            "  ,   ,      , true , true",
    })
    void shouldValidateImageTransformParams(Integer resizePercentage, Integer sepiaIntensity, Boolean grayscale, Boolean invertColors, boolean expected) {

        var params = new ImageTransformParamsDto(resizePercentage, sepiaIntensity, grayscale, invertColors);

        var isValid = validator.isValid(params, null);

        assertThat(isValid).isEqualTo(expected);

    }
}
