package ku.cs.models.collections;

import java.util.ArrayList;

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
    public ArrayList<Appeal> getAppeals(){
        return appeals;
    }
}
