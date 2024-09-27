package ku.cs.models.collections;

import java.util.ArrayList;
import java.util.List;

import ku.cs.models.persons.User;
import ku.cs.models.persons.Approver;

public class NewApproverList {
    private List<Approver> approvers;

    public NewApproverList() {
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
    public ApproverList getFacultyTierApprovers() {
        ApproverList approverList = new ApproverList();
        for(Approver approver : approvers) {
            if (approver.getTier().equals("ผู้อนุมัติคำร้องระดับคณะ")){
                approverList.addApprover(approver);
            }
        }
        return approverList;
    }
    public ApproverList getDepartmentTierApprovers() {
        ApproverList approverList = new ApproverList();
        for(Approver approver : approvers) {
            if (approver.getTier().equals("ผู้อนุมัติคำร้องระดับภาควิชา")){
                approverList.addApprover(approver);
            }
        }
        return approverList;
    }
}
