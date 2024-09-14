package ku.cs.models.collections;

import ku.cs.models.dates.ModifyDate;

import java.util.ArrayList;

public class ModifyDateList {
    private ArrayList<ModifyDate> modifyDates;

    public ModifyDateList() {
        this.modifyDates = new ArrayList<>();
    }

    // Getter
    public ArrayList<ModifyDate> getModifyDates() {
        return modifyDates;
    }

    //
    public boolean addModifyDate(ModifyDate modifyDate) {
        if (modifyDate != null) {
            modifyDates.add(modifyDate);
            return true;
        }
        return false;
    }

    // Find modify date by UUID
    public ModifyDate findModifyDateByUuid(String uuid) {
        for (ModifyDate modifyDate : modifyDates) {
            if (modifyDate.getUuid().equals(uuid)) {
                return modifyDate;
            }
        }
        return null;
    }
}
