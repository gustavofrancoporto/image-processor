package com.bix.imageprocessor.domain.notification.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public record Notification(
        String imageCode,
        String imageFileName,
        String email,
        boolean success
) {


    public static void main(String[] args) throws JsonProcessingException {
        var n = new Notification("dbae537d0d2749ccab2a3ca8f0112c9c", "test.png", "gustavofrancoporto@gmail.com", true);
        var json = new ObjectMapper().writeValueAsString(n);

        System.out.println(json);
    }
}
