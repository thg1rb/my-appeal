package ku.cs.services;

import ku.cs.models.collections.ModifyDateList;
import ku.cs.models.dates.ModifyDate;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ModifyDateListFileDatasource implements Datasource<ModifyDateList> {
    private String directoryName;
    private String fileName;

    // Constructor
    public ModifyDateListFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
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

    @Override
    public ModifyDateList readData() {
        ModifyDateList modifyDateList = new ModifyDateList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
        BufferedReader buffer = new BufferedReader(inputStreamReader);

        String line = "";
        try {
            // ใช้ while loop เพื่ออ่านข้อมูลรอบละบรรทัด
            while ((line = buffer.readLine()) != null) {
                // ถ้าเป็นบรรทัดว่าง ให้ข้าม
                if (line.equals("")) continue;

                // แยกสตริงด้วย ,
                String[] data = line.split(",");

                String uuid = data[0];
                String createDate = data[1];
                String advisorApproveDate = data[2];
                String departmentApproveDate = data[3];
                String deanApproveDate = data[4];

                modifyDateList.addModifyDate(new ModifyDate(uuid, createDate, advisorApproveDate, departmentApproveDate, deanApproveDate));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return modifyDateList;
    }

    @Override
    public void writeData(ModifyDateList data) {
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
            // สร้าง csv ของ modifyDate และเขียนลงในไฟล์ทีละบรรทัด
            for (ModifyDate modifyDate : data.getModifyDates()) {
                String line = modifyDate.toString();
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
