package ku.cs.models.collections;

import java.util.ArrayList;
import java.util.List;

import ku.cs.models.persons.User;
import ku.cs.models.persons.Approver;

public class ApproverList {
    private List<Approver> approvers;

    public ApproverList() {
        approvers = new ArrayList<>();
    }

    //Add approver from file reading
    public void addApprover(String tier, String firstName, String lastName, String role) {
        approvers.add(new Approver(tier, firstName, lastName, role));
    }

    //Add approver from Staff
    public void addApprover(String firstName, String lastName, String role, User adder){
        firstName = firstName.trim();
        lastName = lastName.trim();
        role = role.trim();
        approvers.add(new Approver(firstName, lastName, role, adder));
    }

    public void addApprover(Approver approver){
        approvers.add(approver);
    }

    //Getter
    public List<Approver> getApprovers() {
        return approvers;
    }
    public ApproverList getFacultyTierApprovers(String faculty) {
        ApproverList approverList = new ApproverList();
        for(Approver approver : approvers) {
            if (approver.getTier().equals("ผู้อนุมัติคำร้องระดับคณะ") && approver.getRole().contains(faculty)){
                approverList.addApprover(approver);
            }
        }
        return approverList;
    }
    public ApproverList getDepartmentTierApprovers(String department) {
        ApproverList approverList = new ApproverList();
        for(Approver approver : approvers) {
            if (approver.getTier().equals("ผู้อนุมัติคำร้องระดับภาควิชา") && approver.getRole().contains(department)){
                approverList.addApprover(approver);
            }
        }
        return approverList;
    }
}
