package com.bix.imageprocessor.domain.imagetransformers;

import com.bix.imageprocessor.domain.image.model.ImageTransformParams;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface ImageTransformer<T extends ImageTransformParams> {

    byte[] transform(byte[] image, T params);

    boolean apply(T params);

    default byte[] toByteArray(BufferedImage image) throws IOException {
        try (var outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", outputStream);
            return outputStream.toByteArray();
        }
    }
}
