package ku.cs.models.collections;

import ku.cs.models.appeals.Appeal;

import java.util.ArrayList;
import java.util.UUID;

public class AppealList {
    private ArrayList<Appeal> appeals;

    public AppealList(){
        appeals =  new ArrayList<>();
    }

    public boolean addAppeal(Appeal appeal){
        if(appeal != null){
            appeals.add(appeal);
            return true;
        }
        return false;
    }

//    public AppealList getAppealByMajor(String major){
//        for(Appeal appeal : appeals){
//            if (appeal.getUuid()
//        }
//    }

    public ArrayList<Appeal> getAppeals(){
        return appeals;
    }
}
