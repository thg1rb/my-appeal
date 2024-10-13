package ku.cs.models.persons;

import ku.cs.models.collections.DepartmentList;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.DepartmentListDatasource;

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
        Datasource<DepartmentList> departmentListDatasource = new DepartmentListDatasource("data", "departments.csv");
        String department = departmentListDatasource.readData().findDepartmentByUUID(this.departmentUUID).getDepartmentName();
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
        return "departmentStaff";
    }
}
