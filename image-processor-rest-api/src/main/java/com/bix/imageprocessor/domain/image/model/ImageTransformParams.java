package com.bix.imageprocessor.domain.image.model;

import com.bix.imageprocessor.persistence.converter.StringListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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
    BigDecimal resizePercentage;

    @Convert(converter = StringListConverter.class)
    List<String> filters;
}
