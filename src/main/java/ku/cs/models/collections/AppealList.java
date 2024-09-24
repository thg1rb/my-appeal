package ku.cs.models.collections;

import ku.cs.models.appeals.Appeal;

import java.util.ArrayList;
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
    public int countByDepartment(String department) {
        int departmentCount = 0;
        for (Appeal appeal : appeals) {
            if (appeal.getOwnerDepartment().equals(department)) {
                departmentCount++;
            }
        }
        return departmentCount;
    }

    public int countByFaculty(String faculty) {
        int facultyCount = 0;
        for (Appeal appeal : appeals) {
            if (appeal.getOwnerFaculty().equals(faculty)) {
                facultyCount++;
            }
        }
        return facultyCount;
    }

    // Getter
    public ArrayList<Appeal> getAppeals(){
        return appeals;
    }
    public AppealList getAppealByDepartment(String department){
        AppealList appealList = new AppealList();
        for (Appeal appeal : appeals) {
            if (appeal.getOwnerDepartment().equals(department)) {
                appealList.addAppeal(appeal);
            }
        }
        return appealList;
    }
}
