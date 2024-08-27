package ku.cs.services;

import ku.cs.models.User;
import ku.cs.models.collections.UserList;

public class UserListHardCodeDatasource implements Datasource<UserList> {
    UserList list;
    @Override
    public UserList readData() {
        list = new UserList();
        list.addUser(new User("admin", "admin123", "ไชยวัตน์", "หนูวัฒนา", "ผู้ดูแลระบบ"));
        list.addUser(new User("Bright", "thgirb", "บวรัตน์", "รุ่งรัตนไชย", "อาจารย์ที่ปรึกษา"));
        list.addUser(new User("Ton", "tonnam1678", "เกิดสิริ", "ศรีเจริญ", "เจ้าหน้าที่ภาควิชา"));
        list.addUser(new User("Karn", "karnesque", "คมชาญ", "กานเอง", "เจ้าหน้าที่คณะ"));
        list.addUser(new User("Somsak", "test-password", "สมมุติ", "ขึ้นมา", "นิสิต"));
        list.addUser(new User("jib", "test-password", "จิ๊บ", "ขึ้นมา", "นิสิต"));
        list.addUser(new User("advice", "test-password", "แอดไวซ์", "ขึ้นมา", "นิสิต"));
        list.addUser(new User("malee", "test-password", "มาลี", "ขึ้นมา", "นิสิต"));
        list.addUser(new User("macbook", "test-password", "แม้คบุ้ค", "ขึ้นมา", "นิสิต"));
        list.addUser(new User("windows", "test-password", "วินโดว์", "ขึ้นมา", "นิสิต"));
        return list;
    }

    @Override
    public void writeData(UserList data) {

    }
}
