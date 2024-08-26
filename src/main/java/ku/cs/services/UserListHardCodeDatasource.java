package ku.cs.services;

import ku.cs.models.User;
import ku.cs.models.collections.UserList;

public class UserListHardCodeDatasource implements Datasource<UserList> {
    @Override
    public UserList readData() {
        UserList list = new UserList();
        User user1 = new User("Aock", "Aockaock_2547", "ไชยวัตน์", "หนูวัฒนา", "ผู้ดูแลระบบ");
        User user2 = new User("Bright", "thgirb", "บวรัตน์", "รุ่งรัตนไชย", "อาจารย์ที่ปรึกษา");
        User user3 = new User("Ton", "tonnam1678", "เกิดสิริ", "ศรีเจริญ", "เจ้าหน้าที่ภาควิชา");
        User user4 = new User("Karn", "karnesque", "คมชาญ", "กานเอง", "เจ้าหน้าที่คณะ");
        User user5 = new User("Somsak", "test-password", "สมมุติ", "ขึ้นมา", "นิสิต");
        list.addUser(user1);
        list.addUser(user2);
        list.addUser(user3);
        list.addUser(user4);
        list.addUser(user5);
        return list;
    }

    @Override
    public void writeData(UserList data) {

    }
}
