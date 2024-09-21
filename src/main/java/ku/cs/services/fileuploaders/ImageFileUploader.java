package ku.cs.services.fileuploaders;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.models.persons.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ImageFileUploader implements FileUploader{
    private User user;
    private String directoryName;

    @FXML private Circle profileImageCircle;

    public ImageFileUploader(Circle profileImageCircle, User user, String directoryName) {
        this.user = user;
        this.profileImageCircle = profileImageCircle;
        this.directoryName = directoryName;
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
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try{
                String fileName = user.getUuid() + "_profile." + file.getName().split("\\.")[file.getName().split("\\.").length - 1];
                Path destination = new File(directoryName + File.separator + fileName).toPath();

                Files.copy(file.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
                user.setProfile(fileName);

                Image image = new Image("file:"+ directoryName + File.separator + user.getProfileUrl());
                profileImageCircle.setFill(new ImagePattern(image));
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }else{
            System.out.println("No image file selected.");
        }
    }
}
