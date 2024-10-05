package ku.cs.models.persons;

import ku.cs.models.collections.MajorList;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.MajorListDatasource;

import java.util.UUID;

public class DepartmentStaff extends FacultyStaff {
//    private String department;
    private UUID departmentUUID;

    // Constructor
    public DepartmentStaff(String role, String username, String password, String firstName, String lastName, UUID faculty, UUID department) {
        super(role, username, password, firstName, lastName, faculty);
//        this.department = department;
        this.departmentUUID = department;
    }
    // Constructor for reading file
    public DepartmentStaff(String uuid, String role, String username, String password, String firstName, String lastName, boolean access, String loginDate, String profileImg, String initialPasswordText, String initialPasswordHashed, String faculty, String department) {
        super(uuid, role, username, password, firstName, lastName, access, loginDate, profileImg, initialPasswordText, initialPasswordHashed, faculty);
//        this.department = department;
        this.departmentUUID = UUID.fromString(department);
    }

    //Getter
    public String getDepartment() {
        Datasource<MajorList> majorListDatasource = new MajorListDatasource("data", "majors.csv");
        String department = majorListDatasource.readData().findMajorByUUID(this.departmentUUID).getMajorName();
        return department;
    }
    public UUID getDepartmentUUID() {
        return departmentUUID;
    }
    //Setter
    public void setDepartmentUUID(UUID departmentUUID) {
        this.departmentUUID = departmentUUID;
    }

    @Override
    public String toString() {
        return super.toString() + "," + departmentUUID;
    }

    @Override
    public String getRoleInEnglish() {
        return "majorStaff";
    }
}
