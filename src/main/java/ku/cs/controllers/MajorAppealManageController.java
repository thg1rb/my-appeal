package ku.cs.controllers;

import javafx.fxml.FXML;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class MajorAppealManageController {
    @FXML
    protected void onApproverManageButtonClick() {
        try {
            FXRouter.goTo("major-approver-manage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onNisitManageButtonClick() {
        try {
            FXRouter.goTo("major-nisit-manage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onAppealManageButtonClick() {
        try {
            FXRouter.goTo("major-appeal-manage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onLogoutButtonClick(){
        try{
            FXRouter.goTo("login");
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }

}
