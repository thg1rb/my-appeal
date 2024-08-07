package ku.cs.cs211671project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXRouter.bind(this, stage, "CS211 Project", 1440, 832);
        configRoutes();
        FXRouter.goTo("login");
    }

    private void configRoutes() {
        String viewPath = "ku/cs/views/";
        FXRouter.when("hello", viewPath + "hello-view.fxml");

        // General
        FXRouter.when("login", viewPath + "login.fxml");
        FXRouter.when("register-personal-data", viewPath + "register-personal-data.fxml");
        FXRouter.when("register-username-password", viewPath + "register-username-password.fxml");

        // Admin
        FXRouter.when("admin-user-manage", viewPath + "admin-user-management.fxml");
        FXRouter.when("admin-faculty-manage", viewPath + "admin-faculty-management.fxml");
        FXRouter.when("admin-staff-manage", viewPath + "admin-staff-management.fxml");
        FXRouter.when("admin-dashboard", viewPath + "admin-dashboard.fxml");

        // Student
        FXRouter.when("student-track-appeal", viewPath + "student-track-appeal.fxml");
        FXRouter.when("student-create-appeal", viewPath + "student-create-appeal.fxml");

        // Professor
        FXRouter.when("professor-student-list", viewPath + "professor-student-list.fxml");
        FXRouter.when("professor-student-appeal", viewPath + "professor-student-appeal.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}