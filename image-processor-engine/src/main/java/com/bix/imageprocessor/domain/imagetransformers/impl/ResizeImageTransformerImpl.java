package com.bix.imageprocessor.domain.imagetransformers.impl;

import com.bix.imageprocessor.domain.image.model.ResizeImageTransformParams;
import com.bix.imageprocessor.domain.imagetransformers.MarvinImageTransformer;
import lombok.extern.slf4j.Slf4j;
import marvin.image.MarvinImage;
import org.springframework.stereotype.Component;

import static java.math.BigDecimal.ZERO;
import static marvinplugins.MarvinPluginCollection.scale;

@Slf4j
@Component
public class ResizeImageTransformerImpl extends MarvinImageTransformer<ResizeImageTransformParams> {

    @Override
    protected void transform(MarvinImage marvinImage, ResizeImageTransformParams params) {

        log.info("Applying resize filter with resize ratio of {}", params.resizeRatio());

        var targetWidth = (int) (marvinImage.getWidth() * params.resizeRatio().doubleValue());
        scale(marvinImage.clone(), marvinImage, targetWidth);
    }

    @Override
    public boolean apply(ResizeImageTransformParams params) {
        return params.resizeRatio() != null && params.resizeRatio().compareTo(ZERO) > 0;
    }
}
