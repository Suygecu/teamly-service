package com.suygecu.teamly_service.dto.jira;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fields {
    private String summary;
    private Status status;
    private UserDto assignee;
    private UserDto reporter;
    private List<IssueLink> issuelinks;
    private Priority priority;

    @JsonProperty("customfield_12905")
    private String targetEnd;

    @JsonProperty("customfield_12904")
    private String targetStart;

    @JsonProperty("customfield_11302")
    private CustomFieldOption contentType;

    @JsonProperty("customfield_10100")
    private String epicLink;

    @JsonProperty("customfield_10102")
    private String epicTitle;

	@JsonProperty("customfield_14700")
	private String leadTime;

}
