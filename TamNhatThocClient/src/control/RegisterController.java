package control;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.scene.image.ImageView;

import model.Message;
import model.User;
import utils.Util;

public class RegisterController {

	private Communication commu;

	@FXML
	private PasswordField passwordText;
    @FXML
    private ComboBox<String> comboboxAvatar;
	@FXML
	private Button registerButton;

	@FXML
	private TextField userNameText;

	@FXML
	private Button chooseImage;
	@FXML
	private Label pathToImage;
	@FXML
	private ImageView avatarPreview;
	
    @FXML
    public void initialize() {
    
    	comboboxAvatar.getItems().addAll("01","02", "03", "04");
    	comboboxAvatar.setValue("01");
    }
	@FXML
	void NavToLogin(ActionEvent event) {
		commu.getNavigation().switchTo("Login.fxml");

	}

	@FXML
	void Register(ActionEvent event) {
		String username = userNameText.getText();
		String password = passwordText.getText();
		String avatar = "avatar"+comboboxAvatar.getValue();
		System.out.println("DangKyClick" + username + password + avatar);
		User user = new User(username, password, avatar, 0.0f);
		sendRegisterMessage(user);



	}

	public void sendRegisterMessage(User u) {
		try {
			Message msg = new Message("REGISTER", u);
			commu.sendMessage(msg);

			
			System.out.println("Gui yeu cau dang ki");
		} catch (Exception e) {
			e.printStackTrace();
			Util.showError("Không thể kết nối đến server");
		}

	}

	@FXML
	void chooseImageClick(ActionEvent event) {

	}
	

	public Communication getCommu() {
		return commu;
	}

	public void setCommu(Communication commu) {
		this.commu = commu;
	}


}
