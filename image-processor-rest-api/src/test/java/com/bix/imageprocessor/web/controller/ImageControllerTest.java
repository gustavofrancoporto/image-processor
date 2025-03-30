package com.bix.imageprocessor.web.controller;

import com.bix.imageprocessor.config.SecurityConfiguration;
import com.bix.imageprocessor.domain.image.model.Image;
import com.bix.imageprocessor.domain.image.model.ImageTransform;
import com.bix.imageprocessor.persistence.repository.ImageTransformRepository;
import com.bix.imageprocessor.web.exception.service.impl.DefaultApiValidationErrorFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ImageController.class)
@Import({SecurityConfiguration.class, DefaultApiValidationErrorFactory.class})
@ActiveProfiles("test")
public class ImageControllerTest {

    @Autowired MockMvc mockMvc;

    @MockitoBean ImageTransformRepository imageTransformRepository;

    @Mock ImageTransform imageTransform;
    @Mock Image image;

    String code = "XYZ";
    String filename = "test.png";
    byte[] transformedData = "a".getBytes();

    @Test
    void shouldReturnImageAsAttachmentWhenFound() throws Exception {

        when(imageTransformRepository.findByDownloadCode(code)).thenReturn(Optional.of(imageTransform));
        when(imageTransform.getImage()).thenReturn(image);
        when(imageTransform.getTransformedData()).thenReturn(transformedData);
        when(image.getFileName()).thenReturn(filename);

        mockMvc.perform(get("/api/v1/images/" + code))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", equalTo("application/octet-stream")))
                .andExpect(header().string("Content-Disposition", equalTo("attachment; filename=\"" + filename + "\"")))
                .andExpect(content().bytes(transformedData));
    }

    @Test
    void shouldReturnNotFoundWhenImageIsMissing() throws Exception {

        when(imageTransformRepository.findByDownloadCode(code)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/images/" + code))
                .andExpect(status().isNotFound());
    }
}
