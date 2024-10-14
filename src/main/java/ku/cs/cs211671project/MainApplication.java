package ku.cs.cs211671project;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class MainApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        String fontPath = "/fonts/";
        Font.loadFont(getClass().getResourceAsStream(fontPath + "Kanit-Regular.ttf"), 20);
        Font.loadFont(getClass().getResourceAsStream(fontPath + "Kanit-Bold.ttf"), 20);

        FXRouter.bind(this, stage, "CS211 Project", 1440, 832);
        configRoutes();
        FXRouter.goTo("login");
    }

    private void configRoutes() {
        String viewPath = "ku/cs/views/";
        FXRouter.when("hello", viewPath + "hello-view.fxml");

        // General
        FXRouter.when("login", viewPath + "general/" + "login.fxml");
        FXRouter.when("register-personal-data", viewPath + "general/" + "register-personal-data.fxml");
        FXRouter.when("register-username-password", viewPath + "general/" + "register-username-password.fxml");
        FXRouter.when("profile-setting", viewPath + "general/" + "profile-setting.fxml");
        FXRouter.when("program-setting", viewPath + "general/" + "program-setting.fxml");
        FXRouter.when("about-us", viewPath + "general/" + "about-us.fxml");

        // Admin
        FXRouter.when("admin-user-manage", viewPath + "admin/" + "admin-user-management.fxml");
        FXRouter.when("admin-faculty-manage", viewPath + "admin/" + "admin-faculty-management.fxml");
        FXRouter.when("admin-staff-manage", viewPath + "admin/" + "admin-staff-management.fxml");
        FXRouter.when("admin-dashboard", viewPath + "admin/" + "admin-dashboard.fxml");

        // Student
        FXRouter.when("student-track-appeal", viewPath + "student/" + "student-track-appeal.fxml");
        FXRouter.when("student-create-appeal", viewPath + "student/" + "student-create-appeal.fxml");

        // Advisor
        FXRouter.when("advisor-student-list", viewPath + "advisor/" + "advisor-student-list.fxml");
        FXRouter.when("advisor-student-appeal", viewPath + "advisor/" + "advisor-student-appeal.fxml");

        // Faculty
        FXRouter.when("faculty-appeal-manage", viewPath + "faculty/" + "faculty-appeal-manage.fxml");
        FXRouter.when("faculty-approver-manage", viewPath + "faculty/" + "faculty-approver-manage.fxml");

        // Department
        FXRouter.when("department-appeal-manage", viewPath + "department/" + "department-appeal-manage.fxml");
        FXRouter.when("department-approver-manage", viewPath + "department/" + "department-approver-manage.fxml");
        FXRouter.when("department-nisit-manage", viewPath + "department/" + "department-nisit-manage.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}