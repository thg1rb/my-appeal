package ku.cs.models.collections;

import ku.cs.models.dates.ModifyDate;

import java.util.ArrayList;
import java.util.UUID;

public class ModifyDateList {
    private ArrayList<ModifyDate> modifyDates;

    public ModifyDateList() {
        this.modifyDates = new ArrayList<>();
    }

    // add a new Modify Date (also add a new Appeal)
    public boolean addModifyDate(ModifyDate modifyDate) {
        if (modifyDate != null) {
            modifyDates.add(modifyDate);
            return true;
        }
        return false;
    }

    // Find Modify Date by UUID
    public ModifyDate findModifyDateByUuid(UUID uuid) {
        for (ModifyDate modifyDate : modifyDates) {
            if (modifyDate.getUuid().equals(uuid)) {
                return modifyDate;
            }
        }
        return null;
    }

    // Getter
    public ArrayList<ModifyDate> getModifyDates() {
        return modifyDates;
    }
}
