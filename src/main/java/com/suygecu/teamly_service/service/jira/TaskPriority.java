package com.suygecu.teamly_service.service.jira;

import lombok.Getter;

public enum TaskPriority {
    HIGHEST("Highest", "https://jira.xexbo.ru/images/icons/priorities/highest.svg"),
    HIGH("High", "https://jira.xexbo.ru/images/icons/priorities/high.svg"),
    MEDIUM("Medium", "https://jira.xexbo.ru/images/icons/priorities/medium.svg"),
    LOW("Low", "https://jira.xexbo.ru/images/icons/priorities/low.svg"),
    LOWEST("Lowest", "https://jira.xexbo.ru/images/icons/priorities/lowest.svg"),
    NONE("None", "https://jira.xexbo.ru/images/icons/priorities/none.svg"); // Значение по умолчанию

    private final String name;

    @Getter
    private final String imageUrl;

    TaskPriority(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }



    public static TaskPriority fromString(String priority) {
        for (TaskPriority tp : values()) {
            if (tp.name.equalsIgnoreCase(priority.trim())) {
                return tp;
            }
        }
        return NONE; // По умолчанию - без иконки
    }
}
