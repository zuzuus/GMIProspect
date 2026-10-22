package com.gmi.prospect;

import java.util.ArrayList;

// Saved list of GMI courses (used when there is no internet)
public class CourseData {

    public static final String CAT_DIPLOMA = "DIPLOMA";
    public static final String CAT_GAPP = "GAPP";
    public static final String CAT_GUFP = "GUFP";

    public static final String REQ_DIPLOMA =
            "Credit in Mathematics + 1 Science subject + 1 other subject, and pass English";
    public static final String REQ_DESIGN =
            "Credit in any 3 subjects";
    public static final String REQ_GAPP =
            "Minimum C in English, Mathematics, Additional Mathematics, Physics and Chemistry";
    public static final String REQ_GUFP =
            "Minimum C in Bahasa Melayu, Mathematics, English, Additional Mathematics, Physics and Chemistry";

    private static final String[] DIPLOMA_NAMES = {
            "Diploma of Mechanical Engineering Technology (Manufacturing)",
            "Diploma of Mechanical Engineering Technology (CNC Precision)",
            "Diploma of Mechatronics Engineering Technology",
            "Diploma of Engineering Technology (Instrumentation and Control)",
            "Diploma in Engineering Technology (Sustainable Energy and Power Distribution)",
            "Diploma in Autotronics Engineering Technology",
            "Diploma in Precision Tooling Engineering Technology",
            "Diploma in Industrial Quality Engineering Technology",
            "Diploma in Innovative Product Design Engineering Technology",
            "Diploma in Cyber Security"
    };

    public static ArrayList<Course> getAllCourses() {
        ArrayList<Course> list = new ArrayList<>();

        // 10 diplomas with the same requirement
        for (String name : DIPLOMA_NAMES) {
            list.add(new Course(name, CAT_DIPLOMA, "3 years", REQ_DIPLOMA));
        }

        // Industrial Design has its own requirement
        list.add(new Course("Diploma in Engineering Technology (Industrial Design)",
                CAT_DIPLOMA, "3 years", REQ_DESIGN));

        // Pre-university programmes
        list.add(new Course("German A-Level Preparatory Programme (GAPP)",
                CAT_GAPP, "About 2 years", REQ_GAPP));
        list.add(new Course("GMI-UTP Foundation Programme (GUFP)",
                CAT_GUFP, "1 year", REQ_GUFP));

        return list;
    }
}
