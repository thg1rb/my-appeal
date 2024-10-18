package ku.cs.controllers.general;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import ku.cs.models.persons.User;
import ku.cs.services.FXRouter;
import ku.cs.services.ProgramSetting;

import java.awt.image.ImagingOpException;
import java.io.IOException;

public class ProgramSettingController {
    @FXML private AnchorPane mainPane;

    @FXML private Pane navbarAnchorPane;

    @FXML private ImageView themeImageView;
    @FXML private ImageView fontSizeImageView;
    @FXML private ImageView fontChangeImageView;

    @FXML private ChoiceBox<String> themeChoiceBox;
    @FXML private ChoiceBox<String> fontSizeChoiceBox;
    @FXML private ChoiceBox<String> fontChoiceBox;
    private User user;

    private final String[] themes = {"สว่าง", "มืด"};
    private String selectedTheme;

    private final String[] fontSizes = {"เล็ก", "ปกติ", "ใหญ่"};
    private String selectedFontSize;

    private final String[] fonts = {"Kanit", "Arial"};
    private String selectedFont;

    @FXML
    public void initialize() {
        user = (User) FXRouter.getData();

        selectedTheme = ProgramSetting.getInstance().getTheme();
        selectedFontSize = ProgramSetting.getInstance().getFontSize();
        selectedFont = ProgramSetting.getInstance().getFont();

        onApplyButtonClick();

        //NavBar Component
        String role = user.getRoleInEnglish();
        FXMLLoader navbarComponentLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/general/" + role + "-navbar.fxml"));
        try {
            Pane navbarComponent = navbarComponentLoader.load();
            navbarAnchorPane.getChildren().add(navbarComponent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        initializeChoiceBox();
    }

    //
    private void initializeChoiceBox() {
        // ธีมสีของโปรแกรม
        themeChoiceBox.getItems().addAll(themes);
        themeChoiceBox.setOnAction((ActionEvent event) -> {
            selectedTheme = themeChoiceBox.getSelectionModel().getSelectedItem();
        });
        themeChoiceBox.setValue(selectedTheme);

        // ขนาดฟ้อนของโปรแกรม
        fontSizeChoiceBox.getItems().addAll(fontSizes);
        fontSizeChoiceBox.setOnAction((ActionEvent event) -> {
            selectedFontSize = fontSizeChoiceBox.getSelectionModel().getSelectedItem();
        });
        fontSizeChoiceBox.setValue(selectedFontSize);
        // ฟอนต์ของโปรแกรม
        fontChoiceBox.getItems().addAll(fonts);
        fontChoiceBox.setOnAction((ActionEvent event) -> {
            selectedFont = fontChoiceBox.getSelectionModel().getSelectedItem();
        });
        fontChoiceBox.setValue(selectedFont);
    }

    //
    @FXML
    public void onApplyButtonClick() {
        if (selectedTheme != null) {
            ProgramSetting.getInstance().setTheme(selectedTheme);

            if (ProgramSetting.getInstance().getTheme().equals("สว่าง")) {
                themeImageView.setImage(new Image(getClass().getResource("/icons/theme-program.png").toString()));
                fontSizeImageView.setImage(new Image(getClass().getResource("/icons/font-program.png").toString()));
                fontChangeImageView.setImage(new Image(getClass().getResource("/icons/font-change.png").toString()));
            } else {
                themeImageView.setImage(new Image(getClass().getResource("/icons/theme-program-white.png").toString()));
                fontSizeImageView.setImage(new Image(getClass().getResource("/icons/font-program-white.png").toString()));
                fontChangeImageView.setImage(new Image(getClass().getResource("/icons/font-change-white.png").toString()));
            }
        }

        if (selectedFontSize != null) {
            ProgramSetting.getInstance().setFontSize(selectedFontSize);
        }
        if (selectedFont != null) {
            ProgramSetting.getInstance().setFont(selectedFont);
        }
        ProgramSetting.getInstance().applyStyles(mainPane);
    }

    @FXML
    public void onAboutUsButtonClick() {
        try {
            FXRouter.goTo("about-us", user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


