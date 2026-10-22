package com.gmi.prospect;

import java.util.ArrayList;

// Stores the list of courses offered in GMI
public class CourseData {

    public static final String REQ_DIPLOMA = "Credit in Mathematics + 1 Science subject + 1 other subject, and pass English";
    public static final String REQ_DESIGN = "Credit in any 3 subjects";
    public static final String REQ_GAPP = "Minimum C in English, Mathematics, Additional Mathematics, Physics and Chemistry";
    public static final String REQ_GUFP = "Minimum C in Bahasa Melayu, Mathematics, English, Additional Mathematics, Physics and Chemistry";

    public static ArrayList<Course> getAllCourses() {
        ArrayList<Course> list = new ArrayList<>();

        // Diploma courses (3 years)
        list.add(new Course("Diploma of Mechanical Engineering Technology (Manufacturing)", "DIPLOMA", "3 years", REQ_DIPLOMA));
        list.add(new Course("Diploma of Mechanical Engineering Technology (CNC Precision)", "DIPLOMA", "3 years", REQ_DIPLOMA));
        list.add(new Course("Diploma of Mechatronics Engineering Technology", "DIPLOMA", "3 years", REQ_DIPLOMA));
        list.add(new Course("Diploma of Engineering Technology (Instrumentation and Control)", "DIPLOMA", "3 years", REQ_DIPLOMA));
        list.add(new Course("Diploma in Engineering Technology (Sustainable Energy and Power Distribution)", "DIPLOMA", "3 years", REQ_DIPLOMA));
        list.add(new Course("Diploma in Autotronics Engineering Technology", "DIPLOMA", "3 years", REQ_DIPLOMA));
        list.add(new Course("Diploma in Precision Tooling Engineering Technology", "DIPLOMA", "3 years", REQ_DIPLOMA));
        list.add(new Course("Diploma in Industrial Quality Engineering Technology", "DIPLOMA", "3 years", REQ_DIPLOMA));
        list.add(new Course("Diploma in Innovative Product Design Engineering Technology", "DIPLOMA", "3 years", REQ_DIPLOMA));
        list.add(new Course("Diploma in Cyber Security", "DIPLOMA", "3 years", REQ_DIPLOMA));
        list.add(new Course("Diploma in Engineering Technology (Industrial Design)", "DIPLOMA", "3 years", REQ_DESIGN));

        // Pre-university
        list.add(new Course("German A-Level Preparatory Programme (GAPP)", "GAPP", "About 2 years", REQ_GAPP));
        list.add(new Course("GMI-UTP Foundation Programme (GUFP)", "GUFP", "1 year", REQ_GUFP));

        return list;
    }
}