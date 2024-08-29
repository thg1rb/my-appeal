package ku.cs.services;

import ku.cs.models.BreakAppeal;
import ku.cs.models.collections.AppealList;

public class AppealListHardCodeDatasource implements Datasource<AppealList> {
    AppealList appealList;
    @Override
    public AppealList readData() {
        appealList = new AppealList();


        return appealList;
    }

    @Override
    public void writeData(AppealList data) {

    }
}
