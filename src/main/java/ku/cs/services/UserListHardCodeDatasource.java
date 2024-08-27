package ku.cs.services;

import ku.cs.models.User;
import ku.cs.models.collections.UserList;

public class UserListHardCodeDatasource implements Datasource<UserList> {
    @Override
    public UserList readData() {
        UserList list = new UserList();
        User user1 = new User("admin", "admin123", "ไชยวัตน์", "หนูวัฒนา", "ผู้ดูแลระบบ");
        User user2 = new User("Bright", "thgirb", "บวรัตน์", "รุ่งรัตนไชย", "อาจารย์ที่ปรึกษา");
        User user3 = new User("Ton", "tonnam1678", "เกิดสิริ", "ศรีเจริญ", "เจ้าหน้าที่ภาควิชา");
        User user4 = new User("Karn", "karnesque", "คมชาญ", "กานเอง", "เจ้าหน้าที่คณะ");
        User user5 = new User("Somsak", "test-password", "สมมุติ", "ขึ้นมา", "นิสิต");
        User user6 = new User("jib", "test-password", "จิ๊บ", "ขึ้นมา", "นิสิต");
        User user7 = new User("advice", "test-password", "แอดไวซ์", "ขึ้นมา", "นิสิต");
        User user8 = new User("malee", "test-password", "มาลี", "ขึ้นมา", "นิสิต");
        User user9 = new User("macbook", "test-password", "แม้คบุ้ค", "ขึ้นมา", "นิสิต");
        User user10 = new User("windows", "test-password", "วินโดว์", "ขึ้นมา", "นิสิต");
        list.addUser(user1);
        list.addUser(user2);
        list.addUser(user3);
        list.addUser(user4);
        list.addUser(user5);
        list.addUser(user6);
        list.addUser(user7);
        list.addUser(user8);
        list.addUser(user9);
        list.addUser(user10);
        return list;
    }

    @Override
    public void writeData(UserList data) {

    }
}
