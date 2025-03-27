package com.bix.imageprocessor.domain.image.model;

import com.bix.imageprocessor.persistence.converter.StringListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@Entity
@Table(name = "image_transform_params")
public class ImageTransformParams {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private Float resizeRatio;

    @Convert(converter = StringListConverter.class)
    private List<String> filters;
}
