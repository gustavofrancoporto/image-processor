package com.bix.imageprocessor.domain.imagetransformers.impl;

import com.bix.imageprocessor.domain.image.model.InvertColorsImageTransformParams;
import com.bix.imageprocessor.domain.imagetransformers.MarvinImageTransformer;
import lombok.extern.slf4j.Slf4j;
import marvin.image.MarvinImage;
import org.springframework.stereotype.Component;

import static marvinplugins.MarvinPluginCollection.invertColors;
import static org.apache.commons.lang3.BooleanUtils.isTrue;

@Slf4j
@Component
public class InvertColorsFilterTransformerImpl extends MarvinImageTransformer<InvertColorsImageTransformParams> {

    @Override
    protected void transform(MarvinImage marvinImage, InvertColorsImageTransformParams params) {
        log.info("Applying invert colors filter");
        invertColors(marvinImage);
    }

    @Override
    public boolean apply(InvertColorsImageTransformParams params) {
        return isTrue(params.invertColors());
    }
}
