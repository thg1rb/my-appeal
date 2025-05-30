package ku.cs.models.collections;

import ku.cs.models.Faculty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FacultyList {
    private ArrayList<Faculty> faculties;

    public FacultyList() {
        faculties = new ArrayList<>();
    }

    public ArrayList<String> getAllFacultiesName() {
        ArrayList<String> facultiesName = new ArrayList<>();
        for (Faculty faculty : faculties) {
            facultiesName.add(faculty.getFacultyName());
        }
        return facultiesName;
    }

    public void addFaculty(String name, String id){
        name = name.trim();
        id = id.trim();
        if ( !id.isEmpty() && !name.isEmpty() ){
            Faculty exist = findFacultyById(id);
            if ( exist == null ){
                faculties.add(new Faculty(name, id));
            }
        }
    }
    public void addFaculty(String uuid, String name, String id){
        name = name.trim();
        id = id.trim();
        if ( !id.isEmpty() && !name.isEmpty() ){
            Faculty exist = findFacultyById(id);
            if ( exist == null ){
                faculties.add(new Faculty(uuid, name, id));
            }
        }
    }

    public Faculty findFacultyByName(String facultyName){
        for (Faculty faculty : faculties){
            if (faculty.getFacultyName().equals(facultyName)){
                return faculty;
            }
        }
        return null;
    }

    public Faculty findFacultyByUUID(UUID facultyUUID){
        for (Faculty faculty : faculties){
            if (faculty.getUuid().equals(facultyUUID)){
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

    public void deleteFaculty(Faculty faculty){
        faculties.remove(faculty);
    }

    public ArrayList<Faculty> getFaculties() { return faculties; }
}
