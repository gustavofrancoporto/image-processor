package com.bix.imageprocessor.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    private final static String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(List<String> values) {
        return values != null ? String.join(DELIMITER, values) : "";
    }

    @Override
    public List<String> convertToEntityAttribute(String value) {
        return value != null ? asList(value.split(DELIMITER)) : emptyList();
    }
}