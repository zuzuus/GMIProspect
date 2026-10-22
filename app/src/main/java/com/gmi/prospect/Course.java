package com.gmi.prospect;

// Model class: holds the data for one course
public class Course {

    private final String name;
    private final String category;     // DIPLOMA, GAPP or GUFP
    private final String duration;
    private final String requirement;

    public Course(String name, String category, String duration, String requirement) {
        this.name = name;
        this.category = category;
        this.duration = duration;
        this.requirement = requirement;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDuration() {
        return duration;
    }

    public String getRequirement() {
        return requirement;
    }
}
