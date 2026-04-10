package com.suygecu.teamly_service.service.jira;

public enum TaskStatus {
    TO_DO("Grey", "TO DO"),
    IN_DEVELOPMENT("Blue", "IN PROGRESS"),
    DONE("Green", "DONE"),
    BACKLOG("Grey", "BACKLOG"),
    PAUSE("Yellow", "PAUSE"),
    PREPARING("Gray", "PREPARING"),
    IN_REVIEW("Blue", "IN REVIEW"),
    IN_TEST("Blue", "IN TEST"),
    IN_ASSETS("Green", "IN ASSETS"),
    CANCELED("Red", "CANCELED"),;




    private final String color;
    private final String title;

    TaskStatus(String color, String title) {
        this.color = color;
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public String getTitle() {
        return title;
    }

    public static TaskStatus fromString(String status) {
        for (TaskStatus ts : values()) {
            if (ts.name().equalsIgnoreCase(status.replace(" ", "_"))) {
                return ts;
            }
        }
        return DONE;
    }
}
