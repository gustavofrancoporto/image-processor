package com.bix.imageprocessor.domain.image.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import static jakarta.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@Entity
@Table(name = "image_transformation_params")
public class ImageTransformParams {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private BigDecimal resizeRatio;

    @Column
    private Integer sepiaIntensity;

    @Column
    private Boolean grayscale;

    @Column
    private Boolean invertColors;
}
