module ku.cs {
    requires javafx.controls;
    requires javafx.fxml;
    requires bcrypt;
    requires java.xml.crypto;
    requires java.desktop;
    requires java.sql;
    requires java.management;

    opens ku.cs.cs211671project to javafx.fxml;
    exports ku.cs.cs211671project;

    exports ku.cs.controllers.admin;
    opens ku.cs.controllers.admin to javafx.fxml;

    exports ku.cs.controllers.faculty;
    opens ku.cs.controllers.faculty to javafx.fxml;

    exports ku.cs.controllers.general;
    opens ku.cs.controllers.general to javafx.fxml;

    exports ku.cs.controllers.department;
    opens ku.cs.controllers.department to javafx.fxml;

    exports ku.cs.controllers.advisor;
    opens ku.cs.controllers.advisor to javafx.fxml;

    exports ku.cs.controllers.student;
    opens ku.cs.controllers.student to javafx.fxml;

    exports ku.cs.models;
    opens ku.cs.models to javafx.base;

    exports ku.cs.models.persons;
    opens ku.cs.models.persons to javafx.base;

    exports ku.cs.models.appeals;
    opens ku.cs.models.appeals to javafx.base;

    exports ku.cs.models.collections to javafx.fxml;
    opens ku.cs.models.collections to javafx.base;

    exports ku.cs.services.exceptions to javafx.fxml;
    opens ku.cs.services.exceptions to javafx.base;

    exports ku.cs.services.fileutilities to javafx.fxml;
    opens ku.cs.services.fileutilities to javafx.base;
    exports ku.cs.services.datasources to javafx.fxml;
    opens ku.cs.services.datasources to javafx.base;
    exports ku.cs.services;
    opens ku.cs.services to javafx.base, javafx.fxml;
}