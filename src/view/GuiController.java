package view;


import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import controller.SendEmail;
import controller.Settings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.DirectoryChooser;

public class GuiController {

	@FXML private Label hecom_logo = new Label();
	@FXML protected DatePicker von_datePicker = new DatePicker();
	@FXML protected DatePicker bis_datePicker = new DatePicker();
	@FXML protected TextField ausgabeverzeichnissField = new TextField();
	@FXML private Button durchsuchenButton = new Button();
	@FXML private RadioButton emailRadio = new RadioButton();
	@FXML public static TextArea infoTextArea = new TextArea();
	@FXML private Button startButton = new Button();
	@FXML private Button exitButton = new Button();
	@FXML private static ProgressBar fortschrittProgressBar = new ProgressBar(0);
	@FXML private static ProgressIndicator pind = new ProgressIndicator(0);
			
	LocalDate startDate;
	LocalDate endDate;
	String path;
	Boolean email; 
	
public static void setProgressbar(double stat){
    	System.out.println("Progress: "+(fortschrittProgressBar.getProgress() + stat));
    	fortschrittProgressBar.setProgress((fortschrittProgressBar.getProgress() + stat));
    	if(fortschrittProgressBar.getProgress() >= 1){
    	    fortschrittProgressBar.setProgress(0);
    	}
}

public static void setInfoText(String text){
    	infoTextArea.setText(text);
}

public void closeWindow(ActionEvent event){
    	Stage stage = (Stage) exitButton.getScene().getWindow();
    	stage.close();
 }
 
public void chooseFile(ActionEvent event){ 
 	DirectoryChooser directoryChooser = new DirectoryChooser();
 	directoryChooser.setTitle("Ausgabeverzeichniss wählen");
 	File dir = directoryChooser.showDialog(null);
 	if(dir!=null){
 		ausgabeverzeichnissField.setText(dir.getPath());
 	}
 }
 
public void startProgramm(ActionEvent event) throws Exception{
	 
	startDate = von_datePicker.getValue();
	endDate = bis_datePicker.getValue();
	path = ausgabeverzeichnissField.getText();
	email = emailRadio.isSelected();
	
	Settings settings = new Settings(startDate,endDate, path, email);
	
	if(valuesValid(startDate, endDate)){
	    settings.startApp();
	    if(email){
		sendMail();
	    }
	}else{
	    von_datePicker.setValue(null);
	    bis_datePicker.setValue(null);
	}
 }
 
 private boolean valuesValid(LocalDate startDate, LocalDate endDate){
	 if(startDate.isAfter(endDate)){
		 infoTextArea.setText("Der Auswertungszeitraum ist Ungültig. \nBitte wählen Sie ein Anfangsdatum, welches vor dem Enddatum liegt.");
		 return false;
	 }else{
		 return true;
	 } 
 }
 
//---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---//
//-------------------------------------------------Mail-Controller ----------------------------------------------------//
//---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---//
 
 	@FXML	TextField toAdress;
 	@FXML	TextField title;
 	@FXML	TextArea bodyText;
 	@FXML	Label SuccessMessage;
	@FXML	Button closeButton;
	@FXML   Button mailTest;	
	
public void sendMail(){
    	try{
    	    Parent root = FXMLLoader.load(getClass().getResource("Mail_GUI.fxml"));
    	    Stage stage = new Stage();
    	    Scene scene = new Scene(root);
    	    stage.setScene(scene);
    	    stage.show();
	
    	}catch(Exception e) {
	e.printStackTrace();
    	}
}

public void sendMail(ActionEvent event){
	
    	String adress = null;
    	String betreff = null;
    	String body= null;
    	Boolean dataCorrect = true;
    
    	if(toAdress.getText() != null){ adress = toAdress.getText(); }
    	else{ SuccessMessage.setText("Bitte tragen Sie eine Email Adresse ein");dataCorrect = false;}
    	if(title.getText() != null){ betreff = title.getText();	}
    	else{ SuccessMessage.setText("Bitte geben Sie einen Titel an");dataCorrect = false;}
    	if(bodyText.getText() != null){ body = bodyText.getText(); }
    	else{ SuccessMessage.setText("Bitte tragen sie einen Text ein");dataCorrect = false;}
		
    	if(dataCorrect){
    	    SendEmail.send(adress, betreff, body); 
    	    Stage stage = (Stage) closeButton.getScene().getWindow();	
    	    try {
    		Thread.sleep(2000);
    	    }catch (InterruptedException e) {
    		e.printStackTrace();
    	    }
    	    stage.close();
    	}		
}
	
public void closeMailWindow(ActionEvent event){
    	Stage stage = (Stage) closeButton.getScene().getWindow();
    	stage.close();
}

//---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---//
//-------------------------------------------------Password-Controller ------------------------------------------------//
//---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---//

@FXML Button loginButton;
@FXML PasswordField passwordField;
@FXML Label meldungLabel;
	
public void login(ActionEvent event){
	try {
	    String pw = passwordField.getText();
	    System.out.println(pw);
	    if(pw.equals(Settings.getPassword())){
		//öffne haupt Fenster
		Parent root;
		root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
		Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		//Schließe Login Fenster
		Stage stage2 = (Stage) loginButton.getScene().getWindow();
		stage2.close();
	    }else{
		    meldungLabel.setText("Das Passwort war Ungültig");
	    }    
	}catch (IOException e) {
	    System.out.println("Fehler beim Überprüfen des Passworts");
	    e.printStackTrace();
	}
    
}    

}
