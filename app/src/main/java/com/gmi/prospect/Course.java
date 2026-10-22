package com.gmi.prospect;

// Model class for one course
public class Course {

    private String name;
    private String category;   // DIPLOMA, GAPP or GUFP
    private String duration;
    private String requirement;

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