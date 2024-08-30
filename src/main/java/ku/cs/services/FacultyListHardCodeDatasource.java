package ku.cs.services;

import ku.cs.models.collections.FacultyList;

public class FacultyListHardCodeDatasource implements Datasource<FacultyList>{
    FacultyList facultyList;
    @Override
    public FacultyList readData() {
        facultyList = new FacultyList();
        facultyList.addFaculty("วิศวกรรมศาสตร์", "E");
        facultyList.addFaculty("มนุษยศาสตร์", "L");
        facultyList.addFaculty("วิทยาศาสตร์", "D");
        facultyList.addFaculty("เศรษฐศาสตร์", "G");
        return facultyList;
    }

    @Override
    public void writeData(FacultyList data) {

    }
}
