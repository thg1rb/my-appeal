package ku.cs.models.collections;

import ku.cs.models.Approver;

import java.util.ArrayList;

public class ApproverList {
    private ArrayList<Approver> approvers;

    public ApproverList() {
        approvers = new ArrayList<>();
    }

    public boolean addApprover(Approver approver) {
        if (approver != null){
            approvers.add(approver);
            return true;
        }
        return false;
    }



    public ArrayList<Approver> getApprovers() {
        return approvers;
    }
}


