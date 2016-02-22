
import java.io.File;
import java.io.IOException;
import java.util.Date;

import auswertung.Settings;
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
 	directoryChooser.setTitle("Durchsuchen");
 	directoryChooser.showDialog(new Stage());
 	File dir = directoryChooser.showDialog(null);
 	if(dir!=null){
 		ausgabeverzeichnissField.setText(dir.getPath());
 	}
 }
 
public void startProgramm(ActionEvent event) throws Exception{
	 	

	
	Settings settings = new Settings();
 } 
}

