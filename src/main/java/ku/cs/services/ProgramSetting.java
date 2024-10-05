package ku.cs.services;

import javafx.scene.layout.AnchorPane;

public class ProgramSetting {
    private static ProgramSetting instance;
    private String theme = "สว่าง";
    private String fontSize = "ปกติ";

    // A Constructor
    private ProgramSetting() {}

    // Getters
    public static ProgramSetting getInstance() {
        if (instance == null)
            instance = new ProgramSetting();
        return instance;
    }

    public String getTheme() {
        return theme;
    }

    public String getFontSize() {
        return fontSize;
    }

    // Setters
    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    // Apply Style (Initialize & Program Setting)
    public void applyStyles(AnchorPane anchorPane) {
        anchorPane.getStylesheets().clear(); // Clear existing styles

        // Apply theme-specific styles
        switch (getTheme()) {
            case "สว่าง":
                anchorPane.getStylesheets().add(ProgramSetting.class.getResource("/ku/cs/styles/light-theme.css").toExternalForm());
                break;
            case "มืด":
                anchorPane.getStylesheets().add(ProgramSetting.class.getResource("/ku/cs/styles/dark-theme.css").toExternalForm());
                break;
        }

        // Apply font size styles
        switch (getFontSize()) {
            case "เล็ก":
                anchorPane.getStylesheets().add(ProgramSetting.class.getResource("/ku/cs/styles/font-small.css").toExternalForm());
                break;
            case "ปกติ":
                anchorPane.getStylesheets().add(ProgramSetting.class.getResource("/ku/cs/styles/font-normal.css").toExternalForm());
                break;
            case "ใหญ่":
                anchorPane.getStylesheets().add(ProgramSetting.class.getResource("/ku/cs/styles/font-large.css").toExternalForm());
                break;
        }
    }
}
