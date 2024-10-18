package ku.cs.models.persons;

import at.favre.lib.crypto.bcrypt.BCrypt;
import ku.cs.models.collections.FacultyList;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.FacultyListDatasource;

import java.util.UUID;

public class FacultyStaff extends User {
    private String initialPasswordText;
    private String initialPasswordHashed;

    private UUID facultyUUID;

    //Constructor
    public FacultyStaff(String role, String username, String password, String firstName, String lastName, UUID facultyUUID) {
        super(role, username, password, firstName, lastName);
        this.facultyUUID = facultyUUID;
        this.initialPasswordText = password;
        setInitPassword(password);
    }
    // Constructor for reading file
    public FacultyStaff(String uuid,String role, String username, String password, String firstName, String lastName, boolean access, String loginDate, String profileImg, String initialPasswordText, String initialPasswordHashed,String faculty) {
        super(uuid, role, username, password, firstName, lastName, access, loginDate, profileImg);
        this.initialPasswordText = initialPasswordText;
        this.initialPasswordHashed = initialPasswordHashed;
        this.facultyUUID = UUID.fromString(faculty);
    }

    //Authentication
    private void setInitPassword(String password) {
        if (super.validatePassword(initialPasswordText)){
            super.setPasswordHash(password);
        }
        this.initialPasswordHashed = BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    //Getter
    public String getInitialPasswordText() {
        return initialPasswordText;
    }
    public String getFaculty() {
        Datasource<FacultyList> datasource = new FacultyListDatasource("data", "faculties.csv");
        String faculty = datasource.readData().findFacultyByUUID(this.facultyUUID).getFacultyName();
        return faculty;
    }
    public UUID getFacultyUUID() {
        return facultyUUID;
    }

    //Setter
    public void setInitialPassword(String password){
        setInitPassword(password);
        this.initialPasswordText = password;
    }
    public void setFacultyUUID(UUID facultyUUID) {
        this.facultyUUID = facultyUUID;
    }

    @Override
    public String toString() {
        return super.toString() + "," + initialPasswordText + "," + initialPasswordHashed + "," + facultyUUID;
    }

    @Override
    public String getRoleInEnglish() {
        return "facultyStaff";
    }
}
