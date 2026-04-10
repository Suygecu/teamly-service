package com.suygecu.teamly_service.dto.jira;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFieldOption {
    private String value;

    public String getValue() {
        return value;
    }
}
