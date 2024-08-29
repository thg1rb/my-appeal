package ku.cs.services;

import ku.cs.models.collections.UserList;
import ku.cs.models.persons.User;

public class UserListHardCodeDatasource implements Datasource<UserList> {
    UserList list;
    @Override
    public UserList readData() {
        list = new UserList();
        list.addUser(new User("ผู้ดูแลระบบ", "admin", "admin123", "แอดมิน", "เองคับ"));
        list.addUser(new User("เจ้าหน้าที่คณะ", "Karn", "Karnesque", "กานต์เอง", "นะคับ", "วิทยาศาสตร์"));
        list.addUser(new User("เจ้าหน้าที่คณะ", "faculty", "user", "จนท.คณะ", "นะคับผม", "เศรษฐศาสตร์"));
        list.addUser(new User("เจ้าหน้าที่คณะ", "faculty", "user", "จนท.คณะ", "นะคับผม", "วิศวกรรมศาสตร์"));
        list.addUser(new User("เจ้าหน้าที่ภาควิชา", "Ton", "tonton1678", "ต้นเอง", "จ้า", "วิทยาศาสตร์", "วิทยาการคอมพิวเตอร์"));
        list.addUser(new User("เจ้าหน้าที่ภาควิชา", "major", "user", "จนท.คณะ", "เองงับ", "วิทยาศาสตร์", "สถิติ"));
        list.addUser(new User("เจ้าหน้าที่ภาควิชา", "major", "user", "จนท.คณะ", "เองงับ", "วิทยาศาสตร์", "เตรียมแพทย์ศาสตร์"));
        list.addUser(new User("เจ้าหน้าที่ภาควิชา", "major", "user", "จนท.คณะ", "เองงับ", "วิศวกรรมศาสตร์", "วิศวกรรมไฟฟ้า"));
        list.addUser(new User("เจ้าหน้าที่ภาควิชา", "major", "user", "จนท.คณะ", "เองงับ", "วิศวกรรมศาสตร์", "วิศวกรรมเครื่องกล"));
        list.addUser(new User("เจ้าหน้าที่ภาควิชา", "major", "user", "จนท.คณะ", "เองงับ", "วิศวกรรมศาสตร์", "วิศวกรรมโยธา"));
        list.addUser(new User("อาจารย์ที่ปรึกษา", "Bright", "professor", "ไบร้ง้าบ", "เป็นอาจารย์", "วิทยาศาสตร์", "วิทยาการคอมพิวเตอร์", "6610402132"));
        list.addUser(new User("อาจารย์ที่ปรึกษา", "professor", "1234", "อาจารย์", "ชอบสอน", "วิทยาศาสตร์", "สถิติ", "12345678"));
        list.addUser(new User("อาจารย์ที่ปรึกษา", "professor", "1234", "อาจารย์", "ชอบสอน", "วิศวกรรมศาสตร์", "วิศวกรรมไฟฟ้า", "12345678"));
        list.addUser(new User("อาจารย์ที่ปรึกษา", "professor", "1234", "อาจารย์", "ชอบสอน", "เศรษฐศาสตร์", "เศรษฐศาสตร์", "12345678"));
        list.addUser(new User("นักศึกษา", "Bright", "student", "ไบร้", "เป็นนิสิต", "วิทยาศาสตร์", "วิทยาการคอมพิวเตอร์", "6610401985", "bright@email.test"));
        list.addUser(new User("นักศึกษา", "nisit", "test", "นิสิต", "สมมติ1", "วิทยาศาสตร์", "สถิติ", "6610401xxx", "nisit@email.test"));
        list.addUser(new User("นักศึกษา", "nisit", "test", "นิสิต", "สมมติ2", "วิทยาศาสตร์", "สถิติ", "6610401xxx", "nisit@email.test"));
        list.addUser(new User("นักศึกษา", "nisit", "test", "นิสิต", "สมมติ3", "วิทยาศาสตร์", "สถิติ", "6610401xxx", "nisit@email.test"));
        return list;
    }

    @Override
    public void writeData(UserList data) {

    }
}
