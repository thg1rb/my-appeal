package ku.cs.models.collections;

import ku.cs.models.Major;

import java.util.ArrayList;

public class MajorList {
    private static ArrayList<Major> majors;

    static {
        majors = new ArrayList<>();
    }

    public static void addMajor(String name, String faculty, String id){
        name = name.trim();
        faculty = faculty.trim();
        id = id.trim();
        if (!name.isEmpty() && !faculty.isEmpty() && !id.isEmpty() && FacultyList.isFacultyExist(faculty)){
            majors.add(new Major(name, faculty, id));
        }
    }

    public static ArrayList<Major> findObjMajorsByFaculty(String faculty){
        ArrayList<Major> majorsInFaculty = new ArrayList<>();
        for (Major major : majors){
            if (major.getFaculty().equals(faculty)){
                majorsInFaculty.add(major);
            }
        }
        return majorsInFaculty;
    }

    public static ArrayList<String> findMajorsByFaculty(String faculty){
        ArrayList<String> majorsInFaculty = new ArrayList<>();
        for (Major major : majors){
            if (major.getFaculty().equals(faculty)){
                majorsInFaculty.add(major.getMajorName());
            }
        }
        return majorsInFaculty;
    }

    public static Major findMajorByName(String name){
        for (Major major : majors){
            if (major.getMajorName().equals(name)){
                return major;
            }
        }
        return null;
    }

    public static Major findMajorById(String id){
        for (Major major : majors){
            if (major.getMajorId().equals(id)){
                return major;
            }
        }
        return null;
    }

    public static ArrayList<Major> getMajors(){
        return majors;
    }
}
