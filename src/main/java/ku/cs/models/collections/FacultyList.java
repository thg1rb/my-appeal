package ku.cs.models.collections;

import ku.cs.models.Faculty;

import java.util.ArrayList;
import java.util.List;

public class FacultyList {
    private static ArrayList<Faculty> faculties;

    static {
        faculties = new ArrayList<>();
    }

    public static boolean isFacultyExist(String facultyName) {
        for (Faculty faculty : faculties) {
            if (faculty.getFacultyName().equals(facultyName)) {
                return true;
            }
        }
        return false;
    }

    public static void addFaculty(Faculty obj) {
        for (Faculty faculty : faculties) {
            if (faculty.getFacultyName().equals(obj.getFacultyName())) {
                return;
            }
        }
        faculties.add(obj);
    }

    public static ArrayList<String> getAllFacultiesName() {
        ArrayList<String> facultiesName = new ArrayList<>();
        for (Faculty faculty : faculties) {
            facultiesName.add(faculty.getFacultyName());
        }
        return facultiesName;
    }

    public static void addFaculty(String name, String id){
        name = name.trim();
        id = id.trim();
        if ( !id.isEmpty() && !name.isEmpty() ){
            Faculty exist = findFacultyById(id);
            if ( exist == null ){
                faculties.add(new Faculty(name, id));
            }
        }
    }

    public static boolean removeFaculty(Faculty faculty){
        if (faculties.contains(faculty) && faculty != null){
            faculties.remove(faculty);
            return true;
        }
        return false;
    }

    public static Faculty findFacultyByName(String facultyName){
        for (Faculty faculty : faculties){
            if (faculty.getFacultyName().equals(facultyName)){
                return faculty;
            }
        }
        return null;
    }

    public static Faculty findFacultyById(String facultyId){
        for (Faculty faculty : faculties){
            if (faculty.getFacultyId().equals(facultyId)){
                return faculty;
            }
        }
        return null;
    }

    public static ArrayList<Faculty> getFaculties() { return faculties; }
}
