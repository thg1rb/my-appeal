package ku.cs.services.datasources;

import ku.cs.models.appeals.Appeal;
import ku.cs.models.appeals.BreakAppeal;
import ku.cs.models.appeals.GeneralAppeal;
import ku.cs.models.appeals.SuspendAppeal;
import ku.cs.models.collections.AppealList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class AppealListFileDatasource implements Datasource<AppealList> {
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
        AppealList appealList = new AppealList();
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
                String modifyDate = data[0];
                String uuid = data[1].trim();
                String type = data[2].trim();
                String status = data[3].trim();
                String rejectedReason = data[4].trim();
                String ownerId = data[5].trim();
                String ownerFullName = data[6].trim();
                String ownerDepartmentUuid = data[7].trim();
                String ownerFacultyUuid = data[8].trim();
                String departmentSignature = data[9].trim();
                String facultySignature = data[10].trim();
                String reason = data[11].trim();
                String subjects = data[12].trim();

                // เพิ่มข้อมูลลงใน list
                if (type.equals("คำร้องทั่วไป")) {
                    String topic = data[13].trim();
                    appealList.addAppeal(new GeneralAppeal(modifyDate, UUID.fromString(uuid), type, status, rejectedReason, ownerId, ownerFullName, UUID.fromString(ownerDepartmentUuid), UUID.fromString(ownerFacultyUuid), departmentSignature, facultySignature, reason, subjects, topic));
                }
                else if (type.equals("คำร้องขอพักการศึกษา")) {
                    String semester = data[13].trim();
                    String year = data[14].trim();
                    appealList.addAppeal(new SuspendAppeal(modifyDate, UUID.fromString(uuid), type, status, rejectedReason, ownerId, ownerFullName, UUID.fromString(ownerDepartmentUuid), UUID.fromString(ownerFacultyUuid), departmentSignature, facultySignature, reason, subjects, semester, year));
                }
                else if (type.equals("คำร้องขอลาป่วยหรือลากิจ")) {
                    String purpose = data[13].trim();
                    String startDate = data[14].trim();
                    String endDate = data[15].trim();
                    appealList.addAppeal(new BreakAppeal(modifyDate, UUID.fromString(uuid), type, status, rejectedReason, ownerId, ownerFullName, UUID.fromString(ownerDepartmentUuid), UUID.fromString(ownerFacultyUuid), departmentSignature, facultySignature, reason, subjects, purpose, startDate, endDate));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return appealList;
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
            // สร้าง csv ของ appeal และเขียนลงในไฟล์ทีละบรรทัด
            for (Appeal appeal : data.getAppeals()) {
                String line = "";
                switch (appeal.getType()) {
                    case "คำร้องทั่วไป" -> {
                        GeneralAppeal generalAppeal = (GeneralAppeal) appeal;
                        line = generalAppeal.toString();
                    }
                    case "คำร้องขอพักการศึกษา" -> {
                        SuspendAppeal suspendAppeal = (SuspendAppeal) appeal;
                        line = suspendAppeal.toString();
                    }
                    case "คำร้องขอลาป่วยหรือลากิจ" -> {
                        BreakAppeal breakAppeal = (BreakAppeal) appeal;
                        line = breakAppeal.toString();
                    }
                }

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
}
