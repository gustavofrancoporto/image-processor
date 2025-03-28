package com.bix.imageprocessor.domain.image.model;

import com.bix.imageprocessor.domain.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "image_transformations")
public class ImageTransform {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(STRING)
    private ImageTransformStatus status;

    @Column
    private byte[] transformedData;

    @Column(nullable = false)
    private String downloadCode;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Image image;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ImageTransformParams transformationParams;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User requestedBy;

    @Column(nullable = false)
    private Instant requestedAt;

    @Column
    private Instant completedAt;
}
