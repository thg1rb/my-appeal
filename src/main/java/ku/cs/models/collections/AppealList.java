package ku.cs.models.collections;

import ku.cs.models.appeals.Appeal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class AppealList {
    private ArrayList<Appeal> appeals;

    // Constructor
    public AppealList(){
        appeals =  new ArrayList<>();
    }

    // Add a new appeal
    public boolean addAppeal(Appeal appeal){
        if(appeal != null){
            appeals.add(appeal);
            return true;
        }
        return false;
    }

    // Count by ...
    public int countSuccessAppealByDepartmentUUID(String department) {
        int departmentCount = 0;
        for (Appeal appeal : appeals) {
            if (appeal.getOwnerDepartmentUuid().equals(department) && appeal.getStatus().contains("ดำเนินการครบถ้วน")) {
                departmentCount++;
            }
        }
        return departmentCount;
    }

    public int countSuccessAppealByFacultyUUID(String faculty) {
        int facultyCount = 0;
        for (Appeal appeal : appeals) {
            if (appeal.getOwnerFacultyUuid().equals(faculty) && appeal.getStatus().contains("ดำเนินการครบถ้วน")) {
                facultyCount++;
            }
        }
        return facultyCount;
    }

    public HashMap<String, Integer> getStatusAppealsCount(){
        HashMap<String, Integer> appealsCount = new HashMap<>();
        for (Appeal appeal : appeals) {
            String status = appeal.getStatus();
            if (appealsCount.containsKey(status)) {
                appealsCount.put(status, appealsCount.get(status) + 1);
            } else {
                appealsCount.put(status, 1);
            }
        }
        return appealsCount;
    }

    // Getters
    public ArrayList<Appeal> getAppeals(){
        return appeals;
    }

    public AppealList getAppealByDepartment(String department){
        AppealList appealList = new AppealList();
        for (Appeal appeal : appeals) {
            if (appeal.getOwnerDepartmentUuid().equals(department)) {
                appealList.addAppeal(appeal);
            }
        }
        return appealList;
    }

    public AppealList getAppealByFaculty(String faculty){
        AppealList appealList = new AppealList();
        for (Appeal appeal : appeals) {
            if (appeal.getOwnerFacultyUuid().equals(faculty)) {
                appealList.addAppeal(appeal);
            }
        }
        return appealList;
    }
}
