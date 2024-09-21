package ku.cs.models.collections;

import ku.cs.models.Major;

import java.util.ArrayList;

public class MajorList {
    private ArrayList<Major> majors;

    public MajorList() {
        this.majors = new ArrayList<>();
    }

    public void addMajor(String name, String faculty, String id, FacultyList facultyList){
        name = name.trim();
        faculty = faculty.trim();
        id = id.trim();
        if (!name.isEmpty() && !faculty.isEmpty() && !id.isEmpty() && facultyList.isFacultyExist(faculty)){
            majors.add(new Major(name, faculty, id));
        }
    }

    public void addMajor(String name, String faculty, String id){
        name = name.trim();
        faculty = faculty.trim();
        id = id.trim();
        if (!name.isEmpty() && !faculty.isEmpty() && !id.isEmpty()){
            majors.add(new Major(name, faculty, id));
        }
    }
    public void addMajor(String uuid, String name, String faculty, String id){
        name = name.trim();
        faculty = faculty.trim();
        id = id.trim();
        if (!name.isEmpty() && !faculty.isEmpty() && !id.isEmpty()){
            majors.add(new Major(uuid, name, faculty, id));
        }
    }

    public void addMajor (Major obj){
        for (Major m : majors){
            if (m.getMajorName().equals(obj.getMajorName())){
                return;
            }
        }
        majors.add(obj);
    }

    public ArrayList<Major> findObjMajorsByFaculty(String faculty){
        ArrayList<Major> majorsInFaculty = new ArrayList<>();
        for (Major major : majors){
            if (major.getFaculty().equals(faculty)){
                majorsInFaculty.add(major);
            }
        }
        return majorsInFaculty;
    }

    public ArrayList<String> findMajorsByFaculty(String faculty){
        ArrayList<String> majorsInFaculty = new ArrayList<>();
        for (Major major : majors){
            if (major.getFaculty().equals(faculty)){
                majorsInFaculty.add(major.getMajorName());
            }
        }
        return majorsInFaculty;
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

    public void deleteAllMajorsOfFaculty(String faculty){
        majors.removeIf(major -> major.getFaculty().equals(faculty));
    }

    public void deleteMajor(Major major){
        majors.remove(major);
    }

    public ArrayList<Major> getMajors(){
        return majors;
    }
}
