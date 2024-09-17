package ku.cs.services;

import ku.cs.models.collections.UserList;
import ku.cs.models.persons.User;

public class UserListHardCodeDatasource implements Datasource<UserList> {
    UserList list;
    @Override
    public UserList readData() {
        list = new UserList();
//        list.addUser(new User("ผู้ดูแลระบบ", "admin", "admin123", "แอดมิน", "เองคับ"));
//        list.addUser(new User("เจ้าหน้าที่คณะ", "Karn", "Karnesque", "กานต์เอง", "นะคับ", "วิทยาศาสตร์"));
//        list.addUser(new User("เจ้าหน้าที่คณะ", "faculty1", "user1", "จนท.คณะ", "นะคับผม", "เศรษฐศาสตร์"));
//        list.addUser(new User("เจ้าหน้าที่คณะ", "faculty2", "user2", "จนท.คณะ", "นะคับผม", "วิศวกรรมศาสตร์"));
//        list.addUser(new User("เจ้าหน้าที่ภาควิชา", "Ton", "tonton1678", "ต้นเอง", "จ้า", "วิทยาศาสตร์", "วิทยาการคอมพิวเตอร์"));
//        list.addUser(new User("เจ้าหน้าที่ภาควิชา", "major1", "user1", "จนท.คณะ", "เองงับ", "วิทยาศาสตร์", "สถิติ"));
//        list.addUser(new User("เจ้าหน้าที่ภาควิชา", "major2", "user2", "จนท.คณะ", "เองงับ", "วิทยาศาสตร์", "เตรียมแพทย์ศาสตร์"));
//        list.addUser(new User("เจ้าหน้าที่ภาควิชา", "major3", "user3", "จนท.คณะ", "เองงับ", "วิศวกรรมศาสตร์", "วิศวกรรมไฟฟ้า"));
//        list.addUser(new User("เจ้าหน้าที่ภาควิชา", "major4", "user4", "จนท.คณะ", "เองงับ", "วิศวกรรมศาสตร์", "วิศวกรรมเครื่องกล"));
//        list.addUser(new User("เจ้าหน้าที่ภาควิชา", "major5", "user5", "จนท.คณะ", "เองงับ", "วิศวกรรมศาสตร์", "วิศวกรรมโยธา"));
//        list.addUser(new User("อาจารย์ที่ปรึกษา", "BrightPro", "professor", "ไบร้ง้าบ", "เป็นอาจารย์", "วิทยาศาสตร์", "วิทยาการคอมพิวเตอร์", "6610402132"));
//        list.addUser(new User("อาจารย์ที่ปรึกษา", "professor1", "test1", "อาจารย์", "ชอบสอน", "วิทยาศาสตร์", "สถิติ", "12345678"));
//        list.addUser(new User("อาจารย์ที่ปรึกษา", "professor2", "test2", "อาจารย์", "ชอบสอน", "วิศวกรรมศาสตร์", "วิศวกรรมไฟฟ้า", "12345678"));
//        list.addUser(new User("อาจารย์ที่ปรึกษา", "professor3", "test3", "อาจารย์", "ชอบสอน", "เศรษฐศาสตร์", "เศรษฐศาสตร์", "12345678"));
//        list.addUser(new User("นักศึกษา", "BrightStu", "student", "ไบร้", "เป็นนิสิต", "วิทยาศาสตร์", "วิทยาการคอมพิวเตอร์", "6610401985", "bright@email.test", "ไบร้ง้าบ เป็นอาจารย์"));
//        list.addUser(new User("นักศึกษา", "nisit1", "test1", "นิสิต", "สมมติ1", "วิทยาศาสตร์", "สถิติ", "6610401xxx", "nisit@email.test", "ไบร้ง้าบ เป็นอาจารย์"));
//        list.addUser(new User("นักศึกษา", "nisit2", "test2", "นิสิต", "สมมติ2", "วิทยาศาสตร์", "สถิติ", "6610401xxx", "nisit@email.test", "ไบร้ง้าบ เป็นอาจารย์"));
//        list.addUser(new User("นักศึกษา", "nisit3", "test3", "นิสิต", "สมมติ3", "วิทยาศาสตร์", "สถิติ", "6610401xxx", "nisit@email.test", "ไบร้ง้าบ เป็นอาจารย์"));
//        User bannedStudent = new User("นักศึกษา", "nisitban", "ban", "นิสิตโดนแบน", "ทดสอบ", "วิทยาศาสตร์", "สถิติ", "6610401xxx", "nisit@email.test", "ไบร้ง้าบ เป็นอาจารย์");
//        bannedStudent.banUser();
//        list.addUser(bannedStudent);
        return list;
    }

    @Override
    public void writeData(UserList data) {

    }
}
