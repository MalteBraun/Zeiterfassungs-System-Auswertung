
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MailController {
	
	@FXML	TextField toAdress;
	@FXML	TextField title;
	@FXML	TextArea bodyText;
	@FXML	Label SuccessMessage;
	@FXML	Button closeButton;
	
	
	public void sendMail(ActionEvent event){
		
		String adress;
		String betreff;
		String body;
		
		if(toAdress.getText() != null){ adress = toAdress.getText(); }
		else{ adress = "M.Braun@hecom-shop.de"; }
		if(title.getText() != null){ betreff = title.getText();	}
		else{ betreff = "Ging nicht"; }
		if(bodyText.getText() != null){ body = bodyText.getText(); }
		else{body = "Ging nicht"; }	
		
		SendEmail.send(adress, betreff, body);

		Stage stage = (Stage) closeButton.getScene().getWindow();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stage.close();
	}
	
	public void closeWindow(ActionEvent event){
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	}
	
}
