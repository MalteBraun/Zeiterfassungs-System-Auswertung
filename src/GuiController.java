

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

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
import javafx.stage.Window;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class GuiController {

	@FXML private Label hecom_logo;
	@FXML private DatePicker von_datePicker;
	@FXML private DatePicker bis_datePicker;
	@FXML private TextField ausgabeverzeichnissField;
	@FXML private Button durchsuchenButton;
	@FXML private RadioButton emailRadio;
	@FXML private ProgressBar fortschrittProgressBar;
	@FXML private TextArea infoTextArea;
	@FXML private Button startButton;
	@FXML private Button exitButton;

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
	 	
	LocalDate startDate = von_datePicker.getValue();
	LocalDate endDate = bis_datePicker.getValue();
	String path = ausgabeverzeichnissField.getText();
	Boolean email = emailRadio.isSelected();
	
	System.out.println("Startdate: "+startDate.toString());
	System.out.println("Enddate: "+endDate.toString());
	System.out.println("Path: "+path);
	System.out.println("email: "+email.toString());
	
	if(valuesValid(startDate, endDate, path)){
		Settings settings = new Settings(startDate, endDate, path, email);
		settings.startApp();
		if(email){
			settings.sendMail(path);
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


}

