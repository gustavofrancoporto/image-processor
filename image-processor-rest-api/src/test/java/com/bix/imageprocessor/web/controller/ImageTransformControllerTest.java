package com.bix.imageprocessor.web.controller;

import com.bix.imageprocessor.config.SecurityConfiguration;
import com.bix.imageprocessor.domain.image.service.ImageTransformStarterService;
import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.security.service.JwtTokenService;
import com.bix.imageprocessor.web.dto.image.ImageDto;
import com.bix.imageprocessor.web.dto.image.ImageTransformParamsDto;
import com.bix.imageprocessor.web.exception.model.ApiError;
import com.bix.imageprocessor.web.exception.model.ApiValidationError;
import com.bix.imageprocessor.web.exception.service.impl.DefaultApiValidationErrorFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockPart;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageTransformController.class)
@Import({SecurityConfiguration.class, DefaultApiValidationErrorFactory.class})
@ActiveProfiles("test")
public class ImageTransformControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean ImageTransformStarterService imageTransformService;
    @MockitoBean JwtTokenService jwtTokenService;

    @Mock User user;
    @Mock Jwt jwt;

    @Captor ArgumentCaptor<ImageDto> image;

    @Test
    void shouldReturnUnauthorizedWhenUserIsNotAuthenticated() throws Exception {

        mockMvc.perform(get("/api/v1/image-transform"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnValidationErrorWhenAllTheParametersAreNull() throws Exception {

        var parametersJson = objectMapper.writeValueAsString(new ImageTransformParamsDto(null, null, null, null));

        var filePart = new MockPart("image", "logo.jpg", "***".getBytes());
        var jsonPart = new MockPart("parameters", null, parametersJson.getBytes(), APPLICATION_JSON);

        mockMvc.perform(multipart(POST, "/api/v1/image-transform")
                        .part(filePart, jsonPart)
                        .with(jwt())
                        .contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("at least one image transformation parameter must be provided")));
    }

    @ParameterizedTest
    @CsvSource({
            "0  , 0",
            "201, 101"
    })
    void shouldReturnValidationErrorsForInvalidRangeValues(Integer resizePercentage, Integer sepiaIntensity) throws Exception {

        var parametersJson = objectMapper.writeValueAsString(new ImageTransformParamsDto(resizePercentage, sepiaIntensity, null, null));

        var expectedValidationErrors = List.of(
                new ApiValidationError("parameters", "resizePercentage", resizePercentage, "Range", "must be between 1 and 200"),
                new ApiValidationError("parameters", "sepiaIntensity", sepiaIntensity, "Range", "must be between 1 and 100")
        );

        var filePart = new MockPart("image", "logo.jpg", "***".getBytes());
        var jsonPart = new MockPart("parameters", null, parametersJson.getBytes(), APPLICATION_JSON);

        var responseJson = mockMvc.perform(multipart(POST, "/api/v1/image-transform")
                        .part(filePart, jsonPart)
                        .with(jwt())
                        .contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")))
                .andReturn().getResponse().getContentAsString();

        var apiError = objectMapper.readValue(responseJson, ApiError.class);

        assertThat(apiError.getValidationErrors()).containsExactlyInAnyOrderElementsOf(expectedValidationErrors);
    }

    @ParameterizedTest
    @CsvSource({
            "1  , 1",
            "200, 100"
    })
    void shouldReturnSuccessForValidTransformImageRequest(Integer resizePercentage, Integer sepiaIntensity) throws Exception {

        when(jwtTokenService.getUser(jwt)).thenReturn(user);

        var data = "***".getBytes();
        var filename = "logo.jpg";
        var contentType = IMAGE_JPEG;
        var imageTransformParams = new ImageTransformParamsDto(resizePercentage, sepiaIntensity, false, true);
        var parametersJson = objectMapper.writeValueAsString(imageTransformParams);

        var filePart = new MockPart("image", filename, data, contentType);
        var jsonPart = new MockPart("parameters", null, parametersJson.getBytes(), APPLICATION_JSON);

        mockMvc.perform(multipart(POST, "/api/v1/image-transform")
                        .part(filePart, jsonPart)
                        .with(jwt().jwt(jwt))
                        .contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        verify(imageTransformService, only()).startProcess(image.capture(), eq(imageTransformParams), eq(user));

        assertThat(image.getValue().fileName()).isEqualTo(filename);
        assertThat(image.getValue().data()).isEqualTo(data);
        assertThat(image.getValue().contentType()).isEqualTo(contentType.toString());
    }
}
