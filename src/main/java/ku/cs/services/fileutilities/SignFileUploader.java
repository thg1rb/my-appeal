package ku.cs.services.fileutilities;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.models.appeals.Appeal;
import ku.cs.models.persons.DepartmentStaff;
import ku.cs.models.persons.FacultyStaff;
import ku.cs.models.persons.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class SignFileUploader implements FileUploader {
    private User staff;
    private Appeal selectedAppeal;
    private String directoryName;
    private String fullPath;
    private boolean success;

    @FXML private Button uploadButton;

    public SignFileUploader(User staff, Button uploadButton, Appeal selectedAppeal, String directoryName) {
        this.staff = staff;
        this.directoryName = directoryName;
        this.selectedAppeal = selectedAppeal;
        this.uploadButton = uploadButton;
        checkFileIsExisted();
    }
    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }

        String filePath = directoryName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void upload(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                String fileName = selectedAppeal.getUuid().toString();
                if (staff instanceof DepartmentStaff) {
                    fileName += "_department-sign";
                } else if (staff instanceof FacultyStaff) {
                    fileName += "_faculty-sign";
                }
                fileName += ".pdf";
                Path destination = new File(directoryName + File.separator + fileName).toPath();

                fullPath = destination.toString();

                Files.copy(file.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

                Platform.runLater(() -> {
                    uploadButton.setText(file.getName());
                });

                success = true;

            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            System.out.println("No PDF file selected");
            success = false;
        }
    }
    public boolean uploadSuccess(){
        return this.success;
    }

    public String getFullPath() {
        return fullPath;
    }
}
