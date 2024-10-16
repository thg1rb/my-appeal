package ku.cs.services.fileutilities;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.models.appeals.Appeal;
import ku.cs.models.persons.DepartmentStaff;
import ku.cs.models.persons.FacultyStaff;
import ku.cs.models.persons.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class SignFileDownloader {
    private Appeal selectedAppeal;

    public SignFileDownloader(Appeal selectedAppeal) {
        this.selectedAppeal = selectedAppeal;
    }

    public void download(Stage stage, boolean isDepartment) {
//        File file = new File(selectedAppeal.getSignature()); //  ถ้าจะทำไฟล์เดียว

        File file = null;
        // choose file base on their tier
        if (isDepartment)
            file = new File(selectedAppeal.getDepartmentSignature());
        else
            file = new File(selectedAppeal.getFacultySignature());

        if (file.exists()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName(file.getName());
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

            File destinationFile = fileChooser.showSaveDialog(stage);

            if (destinationFile != null) {
                try {
                    Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
