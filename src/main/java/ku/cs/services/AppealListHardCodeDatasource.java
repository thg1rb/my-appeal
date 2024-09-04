package ku.cs.services;

import ku.cs.models.appeal.Appeal;
import ku.cs.models.collections.AppealList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

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
        appealList.addNewAppeal(new Appeal(DateTimeService.detailedDateToString(new Date()), "คำร้องทั่วไป", "ใบคำร้องใหม่ | คำร้องส่งต่อให้อาจารย์ที่ปรึกษา", "6610401934", "เกิดสิริ ศรีเจริญ", "ขอเปิดหมู่รายวิชา 112 เพิ่ม", "เนื่องจากกระผมต้องการลงทะเบียนเรียนในวิชา 112 เพราะจำเป็นต้องใช้ในรายวิชาถัดไป", UUID.randomUUID().toString()));
        appealList.addNewAppeal(new Appeal(DateTimeService.detailedDateToString(new Date()),"คำร้องขอลากิจหรือลาป่วย", "ใบคำร้องใหม่ | คำร้องส่งต่อให้อาจารย์ที่ปรึกษา", "6610402132","Bright Kun", "ผมติดงานแต่งพี่สาวที่จังหวัดเชียงใหม่", "ลากิจ", "112,233,C PLUS PLUS", "startDate", "endDate", UUID.randomUUID().toString()));
        appealList.addNewAppeal(new Appeal(DateTimeService.detailedDateToString(new Date()),"คำร้องขอพักการศึกษา", "ใบคำร้องใหม่ | คำร้องส่งต่อให้อาจารย์ที่ปรึกษา", "6610401985","Aock taepun", "เกรดเฉลี่ยน้อยเกินไป", "เทอมปลาย", "2567", "112,234,A PLUS PLUS", UUID.randomUUID().toString()));
        return appealList;
    }

    @Override
    public void writeData(AppealList data) {

    }

    public static void main(String[] args) {
        System.out.println(appealList.getAppeals().get(1).getStartDate());
    }
}
