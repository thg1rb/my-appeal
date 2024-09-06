package ku.cs.services;

import ku.cs.models.collections.MajorList;

public class MajorListHardCodedatasource implements Datasource<MajorList> {
    MajorList majorList;
    @Override
    public MajorList readData() {
        majorList = new MajorList();
//        majorList.addMajor("วิทยาการคอมพิวเตอร์", "วิทยาศาสตร์", "D14");
//        majorList.addMajor("เตรียมแพทย์ศาสตร์", "วิทยาศาสตร์", "D32");
//        majorList.addMajor("สถิติ", "วิทยาศาสตร์", "D04");
//        majorList.addMajor("ภาษาอังกฤษ", "มนุษยศาสตร์", "L32");
//        majorList.addMajor("ภาษาเยอรมัน", "มนุษยศาสตร์", "L34");
//        majorList.addMajor("ภาษาฝรั่งเศส", "มนุษยศาสตร์", "L33");
//        majorList.addMajor("วิศวกรรมเครื่องกล", "วิศวกรรมศาสตร์", "E03");
//        majorList.addMajor("วิศวกรรมไฟฟ้า", "วิศวกรรมศาสตร์", "E05");
//        majorList.addMajor("วิศวกรรมโยธา", "วิศวกรรมศาสตร์", "E06");
//        majorList.addMajor("เศรษฐศาสตร์", "เศรษฐศาสตร์", "G01");
//        majorList.addMajor("เศรษฐศาสตร์สหกรณ์", "เศรษฐศาสตร์", "G03");
//        majorList.addMajor("เศรษฐศาสตร์การประกอบการ", "เศรษฐศาสตร์", "G11");
        return majorList;
    }

    @Override
    public void writeData(MajorList data) {

    }
}
