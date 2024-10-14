package ku.cs.models.collections;

import ku.cs.models.Department;

import java.util.ArrayList;
import java.util.UUID;

public class DepartmentList {
    private ArrayList<Department> departments;

    public DepartmentList() {
        this.departments = new ArrayList<>();
    }

    public void addDepartment(String name, UUID faculty, String id, FacultyList facultyList){
        name = name.trim();
        id = id.trim();
        if (!name.isEmpty() && faculty != null && !id.isEmpty()){
            departments.add(new Department(name, faculty, id));
        }
    }

    public void addDepartment(String name, UUID faculty, String id){
        name = name.trim();
        id = id.trim();
        if (!name.isEmpty() && faculty != null && !id.isEmpty()){
            departments.add(new Department(name, faculty, id));
        }
    }
    public void addDepartment(String uuid, String name, String faculty, String id){
        name = name.trim();
        faculty = faculty.trim();
        id = id.trim();
        if (!name.isEmpty() && !faculty.isEmpty() && !id.isEmpty()){
            departments.add(new Department(uuid, name, faculty, id));
        }
    }

    public void addDepartment(Department obj){
        for (Department m : departments){
            if (m.getDepartmentName().equals(obj.getDepartmentName())){
                return;
            }
        }
        departments.add(obj);
    }


    public ArrayList<String> getDepartmentsNameByFaculty(UUID faculty){
        ArrayList<String> departmentsInFaculty = new ArrayList<>();
        for (Department department : departments){
            if (department.getFacultyUUID().equals(faculty)){
                departmentsInFaculty.add(department.getDepartmentName());
            }
        }
        return departmentsInFaculty;
    }

    public ArrayList<Department> getDepartmentsByFaculty(UUID faculty){
        ArrayList<Department> departmentsInFaculty = new ArrayList<>();
        for (Department department : departments){
            if (department.getFacultyUUID().equals(faculty)) {
                departmentsInFaculty.add(department);
            }
        }
        return departmentsInFaculty;
    }

    public Department findDepartmentByName(String name){
        for (Department department : departments){
            if (department.getDepartmentName().equals(name)){
                return department;
            }
        }
        return null;
    }

    public Department findDepartmentById(String id){
        for (Department department : departments){
            if (department.getDepartmentId().equals(id)){
                return department;
            }
        }
        return null;
    }

    public Department findDepartmentByUUID(UUID departmentUUID){
        for (Department department : departments){
            if (department.getUuid().equals(departmentUUID)){
                return department;
            }
        }
        return null;
    }

    public void deleteAllDepartmentsOfFaculty(UUID faculty){
        departments.removeIf(department -> department.getFacultyUUID().equals(faculty));
    }

    public void deleteDepartment(Department department){
        departments.remove(department);
    }

    public ArrayList<Department> getDepartments(){
        return departments;
    }
}
