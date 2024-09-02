package ku.cs.services;

import ku.cs.models.appeal.Appeal;
import ku.cs.models.collections.AppealList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class AppealListFileDatasource implements Datasource<AppealList>{
    private String directoryName;
    private String fileName;

    // Constructor
    public AppealListFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    // ตรวจสอบไฟล์ว่ามีจริงหรือไม่?
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

    // Read data
    @Override
    public AppealList readData() {
        AppealList appeals = new AppealList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        // เตรียม object ที่ใช้ในการอ่านไฟล์
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
            while ((line = buffer.readLine()) != null ){
                // ถ้าเป็นบรรทัดว่าง ให้ข้าม
                if (line.equals("")) continue;

                // แยกสตริงด้วย ,
                String[] data = line.split(",");

                // อ่านข้อมูลตาม index แล้วจัดการประเภทของข้อมูลให้เหมาะสม
                String createDate = data[0].trim();
                String ownerId = data[1].trim();
                String type = data[2].trim();
                String status = data[3].trim();
                String topic = data[4].trim();
                String reason = data[5].trim();
                String purpose = data[6].trim();
                String subjects = data[7].trim();
                String startDate = data[8].trim();
                String endDate = data[9].trim();
                String semester = data[10].trim();
                String year = data[11].trim();

                // เพิ่มข้อมูลลงใน list
                if (type.equals("คำร้องทั่วไป")) {
                    appeals.addNewAppeal(new Appeal(createDate, type, ownerId, topic, reason));
                }
                else if (type.equals("คำร้องขอลาป่วยหรือลากิจ")) {
                    appeals.addNewAppeal(new Appeal(createDate, type, ownerId, reason, purpose, subjects, startDate, endDate));
                }
                else if (type.equals("คำร้องขอพักการศึกษา")) {
                    appeals.addNewAppeal(new Appeal(createDate, type, ownerId, reason, semester, year, subjects));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return appeals;
    }

    // Write data
    @Override
    public void writeData(AppealList data) {
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
            // สร้าง csv ของ Student และเขียนลงในไฟล์ทีละบรรทัด
            for (Appeal appeal : data.getAppeals()) {
                String line = appeal.toString();

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

    // ทดสอบการอ่านไฟล์
//    public static void main(String[] args) {
//        AppealListHardCodeDatasource data = new AppealListHardCodeDatasource();
//        AppealList appeals = data.readData();
//
//        AppealListFileDatasource w = new AppealListFileDatasource("data", "appeal-list.csv");
//        w.writeData(appeals);
//    }
}
