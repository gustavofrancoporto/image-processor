package com.bix.imageprocessor.domain.imagetransformers.impl;

import com.bix.imageprocessor.domain.image.model.SepiaImageTransformParams;
import com.bix.imageprocessor.domain.imagetransformers.MarvinImageTransformer;
import lombok.extern.slf4j.Slf4j;
import marvin.image.MarvinImage;
import org.springframework.stereotype.Component;

import static marvinplugins.MarvinPluginCollection.sepia;

@Slf4j
@Component
public class SepiaFilterTransformerImpl extends MarvinImageTransformer<SepiaImageTransformParams> {

    @Override
    protected void transform(MarvinImage marvinImage, SepiaImageTransformParams params) {

        log.info("Applying sepia filter with intensity {}", params.sepiaIntensity());

        sepia(marvinImage, params.sepiaIntensity());
    }

    @Override
    public boolean apply(SepiaImageTransformParams params) {
        return params.sepiaIntensity() != null && params.sepiaIntensity() > 0;
    }
}
