package ku.cs.services;

import ku.cs.models.users.StaffFaculty;

public class ApproverListHardCodeDatasource implements Datasource<ApproverList> {
    ApproverList list;
    @Override
    public ApproverList readData() {
        list = new ApproverList();
        StaffFaculty facultyStaff = new StaffFaculty("test","test", "จนท", "คณะ", "วิท");
        Approver facultyApproverTest = new Approver("อนุมัติระดับคณะ", "ทดลอง", "รักษาการณ์แทนคณบดี", facultyStaff);

        list.addApprover(new Approver("จิ๋ว" , "นิว" , "คณบดี" , facultyStaff));
        list.addApprover(new Approver("จอห์น" , "ชาวไร่" , "รักษาการณ์แทนคณบดี" , facultyStaff));
        list.addApprover(new Approver("แจ็ค" , "แฟนฉัน" , "รองคณบดีฝ่ายการเงิน" , facultyStaff));
        return list;
    }

    @Override
    public void writeData(ApproverList data) {

    }
}
