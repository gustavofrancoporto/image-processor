package com.bix.imageprocessor.domain.imagetransformers;

import com.bix.imageprocessor.domain.image.model.ImageTransformParams;
import lombok.SneakyThrows;
import marvin.image.MarvinImage;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;

public abstract class MarvinImageTransformer<T extends ImageTransformParams> implements ImageTransformer<T> {

    @Override
    @SneakyThrows
    public byte[] transform(byte[] image, T params) {

        var sourceBufferedImage = ImageIO.read(new ByteArrayInputStream(image));
        var marvinImage = new MarvinImage(sourceBufferedImage);

        transform(marvinImage, params);

        var finalBufferedImage = marvinImage.getBufferedImageNoAlpha();

        return toByteArray(finalBufferedImage);
    }

    protected abstract void transform(MarvinImage marvinImage, T params);
}
