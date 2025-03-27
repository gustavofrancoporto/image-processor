package com.bix.imageprocessor.domain.imagetransformers.impl;

import com.bix.imageprocessor.domain.image.model.GrayscaleImageTransformParams;
import com.bix.imageprocessor.domain.imagetransformers.MarvinImageTransformer;
import lombok.extern.slf4j.Slf4j;
import marvin.image.MarvinImage;
import org.springframework.stereotype.Component;

import static marvinplugins.MarvinPluginCollection.grayScale;
import static org.apache.commons.lang3.BooleanUtils.isTrue;

@Slf4j
@Component
public class GrayscaleFilterTransformerImpl extends MarvinImageTransformer<GrayscaleImageTransformParams> {

    @Override
    protected void transform(MarvinImage marvinImage, GrayscaleImageTransformParams params) {
        log.info("Applying grayscale filter");
        grayScale(marvinImage);
    }

    @Override
    public boolean apply(GrayscaleImageTransformParams params) {
        return isTrue(params.grayscale());
    }
}
