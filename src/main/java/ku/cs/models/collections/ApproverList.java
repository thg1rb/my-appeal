package ku.cs.models.collections;

import ku.cs.models.persons.Approver;
import ku.cs.models.persons.User;

import java.util.ArrayList;
import java.util.List;

public class ApproverList {
    private List<Approver> approvers;

    public ApproverList() {
        approvers = new ArrayList<>();
    }

    public void addApprover(String firstName, String lastName, String faculty, String major,String role) {
        firstName = firstName.trim();
        lastName = lastName.trim();
        role = role.trim();
        String fullName = firstName + " " + lastName;
        if (findApproverByFullName(fullName) != null) {
            approvers.add(new Approver(firstName, lastName, faculty, major, role));
        }
    }

    public void addApprover(String firstName, String lastName, String role, User adder) {
        approvers.add(new Approver(firstName, lastName, role, adder));
    }

    public Approver findApproverByFullName(String fullName) {
        for (Approver approver : approvers) {
            if (approver.getFullName().equals(fullName)) {
                return approver;
            }
        }
        return null;
    }

    public List<Approver> getApprovers() {
        return approvers;
    }
}
