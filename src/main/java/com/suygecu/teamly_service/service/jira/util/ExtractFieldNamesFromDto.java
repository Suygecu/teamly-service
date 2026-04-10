package com.suygecu.teamly_service.service.jira.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExtractFieldNamesFromDto {
    public static List<String> extractFieldNamesFromDto(Class<?> dtoClass) {
        List<String> fieldNames = new ArrayList<>();
        for (Field field : dtoClass.getDeclaredFields()) {
            JsonProperty jsonProp = field.getAnnotation(JsonProperty.class);
            if (jsonProp != null) {
                fieldNames.add(jsonProp.value());
            } else {
                fieldNames.add(field.getName());
            }
        }
        return fieldNames;
    }

}
