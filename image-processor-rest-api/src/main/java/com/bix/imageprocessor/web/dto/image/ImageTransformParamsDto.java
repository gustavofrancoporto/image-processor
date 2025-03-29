package com.bix.imageprocessor.web.dto.image;

import com.bix.imageprocessor.web.validation.ImageTransformParamsRequired;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Range;

@ImageTransformParamsRequired
@Schema(name = "Image Transformation Parameters", description = "At least one parameter must be specified.")
public record ImageTransformParamsDto(
        @Range(min = 1, max = 100)
        @Schema(description = "If present, the image will be resized by this percentage. If not present, no resizing will be performed.",
                example = "50", nullable = true, minimum = "1", maximum = "100")
        Integer resizePercentage,

        @Range(min = 1, max = 100)
        @Schema(description = "If present, the sepia filter will be applied with this intensity. If not present, the sepia filter will not be applied.",
                example = "25", nullable = true, minimum = "1", maximum = "100")
        Integer sepiaIntensity,

        @Schema(description = "Whether the grayscale filter should be applied or not", example = "false", nullable = true)
        Boolean grayscale,

        @Schema(description = "Whether the invert colors filter should be applied or not", example = "true", nullable = true)
        Boolean invertColors
) {
}
