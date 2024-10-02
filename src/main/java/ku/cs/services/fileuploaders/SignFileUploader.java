package ku.cs.services.fileuploaders;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
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

    @FXML private Rectangle imageRectangle;

    public SignFileUploader(User staff, Rectangle imageRectangle, Appeal selectedAppeal, String directoryName) {
        this.staff = staff;
        this.directoryName = directoryName;
        this.imageRectangle = imageRectangle;
        this.selectedAppeal = selectedAppeal;
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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                String fileName = selectedAppeal.getUuid();
                if (staff instanceof DepartmentStaff) {
                    fileName += "_department-sign";
                } else if (staff instanceof FacultyStaff) {
                    fileName += "_faculty-sign";
                }
                fileName += "." +file.getName().split("\\.")[file.getName().split("\\.").length - 1];
                Path destination = new File(directoryName + File.separator + fileName).toPath();

                fullPath = destination.toString();

                Files.copy(file.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

                Image image = new Image("file:" + directoryName + File.separator + fileName);
                imageRectangle.setFill(new ImagePattern(image));
                success = true;

            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            System.out.println("No image file selected");
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
