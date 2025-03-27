package com.bix.imageprocessor.domain.imagetransformers.impl;

import com.bix.imageprocessor.domain.image.model.ImageTransformParams;
import com.bix.imageprocessor.domain.imagetransformers.ImageTransformer;
import lombok.SneakyThrows;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Component
public class ResizeImageTransformerImpl implements ImageTransformer {

    @Override
    @SneakyThrows
    public byte[] transform(byte[] image, ImageTransformParams params) {

        var sourceBufferedImage = ImageIO.read(new ByteArrayInputStream(image));

        var targetWidth = (int) (sourceBufferedImage.getWidth() * params.resizeRatio());

        var finalBufferedImage = Scalr.resize(sourceBufferedImage, targetWidth);

        try (var outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(finalBufferedImage, "jpg", outputStream);
            return outputStream.toByteArray();
        }
    }

    @Override
    public boolean apply(ImageTransformParams params) {
        return params.resizeRatio() != null && params.resizeRatio() > 0;
    }
}
