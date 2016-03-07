

import java.io.File;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.DirectoryChooser;

public class GuiController {


	@FXML private Label hecom_logo;
	@FXML protected DatePicker von_datePicker;
	@FXML protected DatePicker bis_datePicker;
	@FXML protected TextField ausgabeverzeichnissField;
	@FXML private Button durchsuchenButton;
	@FXML private RadioButton emailRadio;
	@FXML private ProgressBar fortschrittProgressBar;
	@FXML public TextArea infoTextArea;
	@FXML private Button startButton;
	@FXML private Button exitButton;
	
	LocalDate startDate;
	LocalDate endDate;
	String path;
	Boolean email; 
	
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
	 
	this.startDate = von_datePicker.getValue();
	this.endDate = bis_datePicker.getValue();
	this.path = ausgabeverzeichnissField.getText();
	this.email = emailRadio.isSelected();

	
	System.out.println("Startdate: "+startDate.toString());
	System.out.println("Enddate: "+endDate.toString());
	System.out.println("Path: "+path);
	System.out.println("email: "+email.toString());
	System.out.println(" -----  ");
	System.out.println();
	
	if(valuesValid(startDate, endDate, path)){
	    Settings settings = new Settings(startDate, endDate, path, email);
	    settings.startApp();
	    if(email){
		settings.sendMail();
	    }
	}
 }
 
 private boolean valuesValid(LocalDate startDate, LocalDate endDate, String path){
	 if(startDate.isAfter(endDate)){
		 infoTextArea.setText("Der Auswertungszeitraum ist Ungültig. \nBitte wählen Sie ein Anfangsdatum, welches vor dem Enddatum liegt.");
		 return false;
	 }else{
		 return true;
	 } 
 }
 //---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---//
 //-------------------------------------------------Mail-Controller ab hier---------------------------------------------//
 //---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---   ---//
 
	@FXML	TextField toAdress;
	@FXML	TextField title;
	@FXML	TextArea bodyText;
	@FXML	Label SuccessMessage;
	@FXML	Button closeButton;
	@FXML   Button mailTest;
	
	
public void mailTest(ActionEvent event){
    Settings settings = new Settings();
    settings.sendMail();
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
	
}
