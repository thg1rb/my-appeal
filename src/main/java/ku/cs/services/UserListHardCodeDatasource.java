package ku.cs.services;

import ku.cs.models.*;
import ku.cs.models.collections.UserList;

public class UserListHardCodeDatasource implements Datasource<UserList> {
    UserList list;
    @Override
    public UserList readData() {
        list = new UserList();
        list.addUser(new StaffAdmin("admin", "admin123", "ไชยวัตน์", "หนูวัฒนา"));
        list.addUser(new Professor("Bright", "thgirb", "บวรัตน์", "รุ่งรัตนไชย", "Science", "Computer", "6610101010"));
        list.addUser(new StaffMajor("Ton", "tonnam1678", "เกิดสิริ", "ศรีเจริญ", "Science", "Computer"));
        list.addUser(new StaffFaculty("Karn", "karnesque", "คมชาญ", "กานเอง", "Science"));
        list.addUser(new Nisit("Somsak", "test-password", "สมมุติ", "ขึ้นมา", "test@email.com", "วิทยาศาสตร์", "คอมพิวเตอร์", "6610101010"));
        list.addUser(new Nisit("jib", "test-password", "จิ๊บ", "ขึ้นมา", "test@email.com", "วิทยาศาสตร์", "คอมพิวเตอร์", "6610101010"));
        list.addUser(new Nisit("advice", "test-password", "แอดไวซ์", "ขึ้นมา", "test@email.com", "วิทยาศาสตร์", "คอมพิวเตอร์", "6610101010"));
        list.addUser(new Nisit("malee", "test-password", "มาลี", "ขึ้นมา", "test@email.com", "วิทยาศาสตร์", "คอมพิวเตอร์", "6610101010"));
        list.addUser(new Nisit("macbook", "test-password", "แม้คบุ้ค", "ขึ้นมา", "test@email.com", "วิทยาศาสตร์", "คอมพิวเตอร์", "6610101010"));
        list.addUser(new Nisit("windows", "test-password", "วินโดว์", "ขึ้นมา", "test@email.com", "วิทยาศาสตร์", "คอมพิวเตอร์", "6610101010"));
        return list;
    }

    @Override
    public void writeData(UserList data) {

    }
}
