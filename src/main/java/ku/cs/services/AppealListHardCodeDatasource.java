package ku.cs.services;

import ku.cs.models.appeal.Appeal;
import ku.cs.models.collections.AppealList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class AppealListHardCodeDatasource implements Datasource<AppealList> {
    static AppealList appealList;
    SimpleDateFormat sdf;
    String start;
    String end;
    Date startDate;
    Date endDate;

    public AppealListHardCodeDatasource() {
        appealList = new AppealList();
        sdf = new SimpleDateFormat("yyyy/MM/dd");
        start = "2556/05/23";
        end = "2556/05/25";

        try {
            startDate = sdf.parse(start);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        try {
            endDate = sdf.parse(end);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void getAppeal(Appeal appeal) {
        appealList.addNewAppeal(appeal);
    }

    @Override
    public AppealList readData() {
        appealList.addNewAppeal(new Appeal(DateTimeService.detailedDateToString(new Date()), "คำร้องทั่วไป", "เกิดสิริ ศรีเจริญ", "ขอเปิดหมู่รายวิชา 112 เพิ่ม", "เนื่องจากกระผมต้องการลงทะเบียนเรียนในวิชา 112 เพราะจำเป็นต้องใช้ในรายวิชาถัดไป"));
        appealList.addNewAppeal(new Appeal(DateTimeService.detailedDateToString(new Date()),"คำร้องขอลากิจหรือลาป่วย", "Bright Kun", "ผมติดงานแต่งพี่สาวที่จังหวัดเชียงใหม่", "ขอลากิจเป็นเวลา 2 วัน", "112,233,C PLUS PLUS", "startDate", "endDate"));
        appealList.addNewAppeal(new Appeal(DateTimeService.detailedDateToString(new Date()),"คำร้องขอพักการศึกษา", "Aock taepun", "เกรดเฉลี่ยน้อยเกินไป", "เทอมปลาย", "2567", "112,234,A PLUS PLUS"));
        return appealList;
    }

    @Override
    public void writeData(AppealList data) {

    }
}
