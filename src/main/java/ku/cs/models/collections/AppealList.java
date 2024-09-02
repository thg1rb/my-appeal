package ku.cs.models.collections;

import ku.cs.models.appeal.Appeal;

import java.util.ArrayList;

public class AppealList {
    private ArrayList<Appeal> appeals;

    public AppealList(){
        appeals =  new ArrayList<>();
    }

    public boolean addNewAppeal(Appeal appeal){
        if(appeal != null){
            appeals.add(appeal);
            return true;
        }
        return false;
    }

    public ArrayList<Appeal> getAppeals(){
        return appeals;
    }
}
