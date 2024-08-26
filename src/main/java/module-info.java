module ku.cs {
    requires javafx.controls;
    requires javafx.fxml;
    requires bcrypt;

    opens ku.cs.cs211671project to javafx.fxml;
    exports ku.cs.cs211671project;

    exports ku.cs.controllers.admin;
    opens ku.cs.controllers.admin to javafx.fxml;

    exports ku.cs.controllers.faculty;
    opens ku.cs.controllers.faculty to javafx.fxml;

    exports ku.cs.controllers.general;
    opens ku.cs.controllers.general to javafx.fxml;

    exports ku.cs.controllers.major;
    opens ku.cs.controllers.major to javafx.fxml;

    exports ku.cs.controllers.professor;
    opens ku.cs.controllers.professor to javafx.fxml;

    exports ku.cs.controllers.student;
    opens ku.cs.controllers.student to javafx.fxml;
}