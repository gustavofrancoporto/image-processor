package com.bix.imageprocessor.persistence;

import com.bix.imageprocessor.domain.image.model.ImageTransform;
import com.bix.imageprocessor.domain.image.model.ImageTransformParams;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageTransformRowMapperImpl implements RowMapper<ImageTransform> {

    @Override
    public ImageTransform mapRow(ResultSet rs, int rowNum) throws SQLException {

//        var id = rs.getLong("id");
//        var image = rs.getBytes("image");
//
//        var resizeRatio = rs.getBigDecimal("resize_ratio");
//        var sepiaIntensity = rs.getInt("sepia_intensity");
//        var grayscale = rs.getBoolean("grayscale");
//        var invertColors = rs.getBoolean("invert_colors");
//
//        var params = new ImageTransformParams(resizeRatio, sepiaIntensity, grayscale, invertColors);
//        return new ImageTransform(id, image, params);
        return null;
    }
}
