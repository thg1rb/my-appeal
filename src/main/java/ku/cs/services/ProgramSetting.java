package ku.cs.services;

import javafx.scene.layout.AnchorPane;

public class ProgramSetting {
    private static ProgramSetting instance;
    private String theme = "สว่าง";
    private String fontSize = "ปกติ";
    private String font = "Kanit";
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

    public String getFont() {return font;}
    // Setters
    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }
    public void setFont(String font) {
        this.font = font;
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

        switch (getFont()) {
            case "Arial":
                anchorPane.getStylesheets().add(ProgramSetting.class.getResource("/ku/cs/styles/font-arial.css").toExternalForm());
                break;
            case "Kanit": // ฟอนต์เริ่มต้น
                anchorPane.getStylesheets().add(ProgramSetting.class.getResource("/ku/cs/styles/font-kanit.css").toExternalForm());
                break;
            default:
                anchorPane.getStylesheets().add(ProgramSetting.class.getResource("/ku/cs/styles/font-kanit.css").toExternalForm()); // Default to Kanit
                break;
        }
    }

}
