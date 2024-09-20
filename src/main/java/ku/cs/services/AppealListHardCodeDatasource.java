package ku.cs.services;

import ku.cs.models.appeals.Appeal;
import ku.cs.models.appeals.BreakAppeal;
import ku.cs.models.appeals.GeneralAppeal;
import ku.cs.models.appeals.SuspendAppeal;
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
        appealList.addAppeal(appeal);
    }

    @Override
    public AppealList readData() {
//        appealList.addNewAppeal(new Appeal(DateTimeService.detailedDateToString(new Date()), "คำร้องทั่วไป", "ใบคำร้องใหม่ | คำร้องส่งต่อให้อาจารย์ที่ปรึกษา", "6610401934", "เกิดสิริ ศรีเจริญ", "ขอเปิดหมู่รายวิชา 112 เพิ่ม", "เนื่องจากกระผมต้องการลงทะเบียนเรียนในวิชา 112 เพราะจำเป็นต้องใช้ในรายวิชาถัดไป", UUID.randomUUID().toString()));
//        appealList.addNewAppeal(new Appeal(DateTimeService.detailedDateToString(new Date()),"คำร้องขอลากิจหรือลาป่วย", "ใบคำร้องใหม่ | คำร้องส่งต่อให้อาจารย์ที่ปรึกษา", "6610402132","Bright Kun", "ผมติดงานแต่งพี่สาวที่จังหวัดเชียงใหม่", "ลากิจ", "112,233,C PLUS PLUS", "startDate", "endDate", UUID.randomUUID().toString()));
//        appealList.addNewAppeal(new Appeal(DateTimeService.detailedDateToString(new Date()),"คำร้องขอพักการศึกษา", "ใบคำร้องใหม่ | คำร้องส่งต่อให้อาจารย์ที่ปรึกษา", "6610401985","Aock taepun", "เกรดเฉลี่ยน้อยเกินไป", "เทอมปลาย", "2567", "112,234,A PLUS PLUS", UUID.randomUUID().toString()));
        String modifyDate = DateTimeService.detailedDateToString(new Date());
        String newAppealStatus = "ใบคำร้องใหม่ | คำร้องส่งต่อให้อาจารย์ที่ปรึกษา";
        appealList.addAppeal(new GeneralAppeal(modifyDate, UUID.randomUUID().toString(), "คำร้องทั่วไป", newAppealStatus, "6610401934", "Ton KerdSiri", "วิศวกรรมคอมพิวเตอร์", "วิศวกรรมศาสตร์", "อาจารย์เคยกินข้าวแกงกะหรี่ฝีมือผมมั้ยยย?? ผมอยากให้อาจารย์ลองสักครั้ง มันอร่อยนะครับ", "อือออ ผมว่าข้าวแกงกะหรี่ฝีมือผมก็ไม่แย่นะ"));
        appealList.addAppeal(new SuspendAppeal(modifyDate, UUID.randomUUID().toString(), "คำร้องขอพักการศึกษา", newAppealStatus, "6610402132", "Bright Bowornrat", "สถิติศาสตร์", "วิทยาศาสตร์","เกิดมา 19 ปียังไม่เคยออกไปทำงานหาเงินด้วยตัวเองเลย ผมขอออกไปหาประสบการณ์เป็นเวลา 1 ปี", "01418211-Software Construction-1/01418235-UNIX-and-OS-2", "ภาคต้น", "2566"));
        appealList.addAppeal(new BreakAppeal(modifyDate, UUID.randomUUID().toString(), "คำร้องขอลาป่วยหรือลากิจ", newAppealStatus, "6610401985", "Aock Chaiyawat", "วิทยาการคอมพิวเตอร์", "วิทยาศาสตร์", "แค่ก แค่ก รู้สึกเหมือนจะไม่สบาย ปวดหัว ตัวร้อน ไปหมด ผมว่าผมอาจจะเป็นโควิด", "01418262-Machine Learning System-1/01418231-Data Structure-1", "ลาป่วย", "2024/09/20", "2024/09/23"));
        return appealList;
    }

    @Override
    public void writeData(AppealList data) {

    }

}
