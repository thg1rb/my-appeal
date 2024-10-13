package ku.cs.services.datasources;

import ku.cs.models.collections.UserList;
import ku.cs.models.persons.User;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class UserListDatasource implements Datasource<UserList> {
    private String directoryName;
    private String fileName;

    public UserListDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    public static UserList readAllUsers(){
        String adminFilePath = "data" + File.separator + "users" + File.separator +  "admin.csv";
        String facultyFilePath = "data" + File.separator + "users" + File.separator +  "facultyStaff.csv";
        String departmentFilePath = "data" + File.separator + "users" + File.separator +  "departmentStaff.csv";
        String advisorFilePath = "data" + File.separator + "users" + File.separator +  "advisor.csv";
        String studentFilePath = "data" + File.separator + "users" + File.separator +  "student.csv";

        String[] paths = {adminFilePath,facultyFilePath,departmentFilePath,advisorFilePath,studentFilePath};

        UserList userList = new UserList();
        for (String file : paths) {
            FileInputStream fileInputStream = null;

            try {
                fileInputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            InputStreamReader inputStreamReader = new InputStreamReader(
                    fileInputStream,
                    StandardCharsets.UTF_8
            );
            BufferedReader buffer = new BufferedReader(inputStreamReader);

            String line = "";
            try {
                // ใช้ while loop เพื่ออ่านข้อมูลรอบละบรรทัด
                while ( (line = buffer.readLine()) != null ){
                    // ถ้าเป็นบรรทัดว่าง ให้ข้าม
                    if (line.equals("")) continue;

                    // แยกสตริงด้วย ,
                    String[] data = line.split(",");

                    // เพิ่มข้อมูลลงใน list
                    userList.addUser(data);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return userList;
    }

    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }

        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public UserList readData() {
        UserList userList = new UserList();

        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(
                fileInputStream,
                StandardCharsets.UTF_8
        );
        BufferedReader buffer = new BufferedReader(inputStreamReader);

        String line = "";
        try {
            // ใช้ while loop เพื่ออ่านข้อมูลรอบละบรรทัด
            while ( (line = buffer.readLine()) != null ){
                // ถ้าเป็นบรรทัดว่าง ให้ข้าม
                if (line.equals("")) continue;

                // แยกสตริงด้วย ,
                String[] data = line.split(",");

                // เพิ่มข้อมูลลงใน list
                userList.addUser(data);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    @Override
    public void writeData(UserList userList) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        // เตรียม object ที่ใช้ในการเขียนไฟล์
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                fileOutputStream,
                StandardCharsets.UTF_8
        );
        BufferedWriter buffer = new BufferedWriter(outputStreamWriter);

        try {
            for (User user : userList.getUsers()) {
                String line = user.toString();
                buffer.append(line);
                buffer.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.flush();
                buffer.close();
            }
            catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

//    public static void main(String[] args) {
//        UserList userList = UserListDatasource.readAllUsers();
//
//        UserList admin = userList.getAdmins();
//        UserListDatasource adminDatasource = new UserListDatasource("data"+File.separator+"users", "admin.csv");
//        adminDatasource.writeData(admin);
//
//        UserList faculty = userList.getFacultyStaffs();
//        UserListDatasource facultyDatasource = new UserListDatasource("data"+File.separator+"users", "facultyStaff.csv");
//        facultyDatasource.writeData(faculty);
//
//        UserList department = userList.getDepartmentStaffs();
//        UserListDatasource DepartmentDatasource = new UserListDatasource("data"+File.separator+"users", "departmentStaff.csv");
//        departmentDatasource.writeData(department);
//
//        UserList advisor = userList.getAdvisors();
//        UserListDatasource advisorDatasource = new UserListDatasource("data"+File.separator+"users", "advisor.csv");
//        advisorDatasource.writeData(advisor);
//
//        UserList student = userList.getStudents();
//        UserListDatasource studentDatatsource = new UserListDatasource("data"+File.separator+"users", "student.csv");
//        studentDatatsource.writeData(student);
//    }

//    public static void main(String[] args) {
//        UserListDatasource datasource = new UserListDatasource("data/users", "students.csv");
//        UserList studentList = datasource.readData();
//        UserList userList = UserListDatasource.readAllUsers();
//        UserList facultyList = userList.getFacultyStaffs();
//        UserList departmentList = userList.getDepartmentStaffs();
//        UserList advisorList = userList.getAdvisors();
//
//        for (User user : userList.getUsers()) {
//            System.out.println(user);
//        }
//        System.out.println("=========");
//        for (User user : facultyList.getUsers()) {
//            System.out.println(user);
//        }
//        System.out.println("=========");
//        for (User user : departmentList.getUsers()) {
//            System.out.println(user);
//        }
//        System.out.println("=========");
//        for (User user : advisorList.getUsers()) {
//            System.out.println(user);
//        }
//        System.out.println("=========");
//        for (User user : studentList.getUsers()) {
//            System.out.println(user);
//        }
//        userList.addUser(new Student("โอ้ค", "นักเรียนคนเก่ง", "6610401985", "aock@email.com", "วิทยาศาสตร์" ,"วิทยาการคอมพิวเตอร์"));
//        datasource.writeData(userList.getStudents());
////        userList.addUser(new Student("ต้น", "จาวา", "6610401932", "ton@email.com", "วิทยาศาสตร์", "สถิติ", "D14033"));
////        datasource.writeSelectedUserType(userList.getStudents(), "นักศึกษา");
////        datasource.writeData(userList);
//    }
}
