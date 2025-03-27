package com.bix.imageprocessor.persistence;

import com.bix.imageprocessor.domain.image.model.ImageTransform;
import com.bix.imageprocessor.domain.image.model.ImageTransformParams;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.util.Arrays.asList;

public class ImageTransformRowMapperImpl implements RowMapper<ImageTransform> {

    @Override
    public ImageTransform mapRow(ResultSet rs, int rowNum) throws SQLException {

        var id = rs.getLong("id");
        var image = rs.getBytes("image");

        var resizeRatio = rs.getFloat("resize_ratio");
        var filtersStr = rs.getString("filters");
        var filters = filtersStr != null ? asList(filtersStr.split(",")) : new ArrayList<String>();

        var params = new ImageTransformParams(resizeRatio, filters);
        return new ImageTransform(id, image, params);
    }
}
