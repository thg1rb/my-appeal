package ku.cs.models.collections;

import ku.cs.models.Major;

import java.util.ArrayList;

public class MajorList {
    private ArrayList<Major> majors;

    public MajorList() {
        this.majors = new ArrayList<>();
    }

    public void addMajor(String name, String faculty, String id){
        name = name.trim();
        faculty = faculty.trim();
        id = id.trim();
        if (!name.isEmpty() && !faculty.isEmpty() && !id.isEmpty() && FacultyList.isFacultyExist(faculty)){
            majors.add(new Major(name, faculty, id));
        }
    }

    public Major findMajorByName(String name){
        for (Major major : majors){
            if (major.getMajorName().equals(name)){
                return major;
            }
        }
        return null;
    }

    public Major findMajorById(String id){
        for (Major major : majors){
            if (major.getMajorId().equals(id)){
                return major;
            }
        }
        return null;
    }

    public ArrayList<Major> getMajors(){
        return majors;
    }
}
