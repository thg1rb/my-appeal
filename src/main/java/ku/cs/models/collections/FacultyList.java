package ku.cs.models.collections;

import ku.cs.models.Faculty;

import java.util.ArrayList;
import java.util.List;

public class FacultyList {
    private ArrayList<Faculty> faculties;
    private static ArrayList<String> allFaculties;

    static {
        allFaculties = new ArrayList<>();
    }

    public FacultyList() {
        faculties = new ArrayList<>();
    }

    public static boolean isFacultyExist(String facultyName) {
        return allFaculties.contains(facultyName);
    }

    public void addFaculty(String name, String id){
        name = name.trim();
        id = id.trim();
        if ( !id.isEmpty() && !name.isEmpty() ){
            Faculty exist = findFacultyById(id);
            if ( exist == null ){
                faculties.add(new Faculty(name, id));
                allFaculties.add(name);
            }
        }
    }

    public boolean removeFaculty(Faculty faculty){
        if (faculties.contains(faculty) && faculty != null){
            faculties.remove(faculty);
            return true;
        }
        return false;
    }

    public Faculty findFacultyByName(String facultyName){
        for (Faculty faculty : faculties){
            if (faculty.getFacultyName().equals(facultyName)){
                return faculty;
            }
        }
        return null;
    }

    public Faculty findFacultyById(String facultyId){
        for (Faculty faculty : faculties){
            if (faculty.getFacultyId().equals(facultyId)){
                return faculty;
            }
        }
        return null;
    }

    public ArrayList<Faculty> getFaculties() { return faculties; }
}
