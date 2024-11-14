package control;
import control.*;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.*;
import utils.*;

import java.io.IOException;
import java.net.Socket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LoginController {
	private Communication commu;
	private Navigation navigation;

	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField;

	public LoginController() throws ClassNotFoundException, IOException {
		super();
		this.commu = new Communication("localhost", 12345);
		commu.startReceiving();
		commu.startProcessing();
	}

	@FXML
	public void Login(ActionEvent e) {
		String username = usernameField.getText();
		String password = passwordField.getText();
		System.out.println("DangNhapClick" + username + password);
		User user = new User(username, password);
		sendLoginMessage(user);
	}

	@FXML
	public void Register(ActionEvent e) {
		if (commu.getNavigation()==null) {
			commu.setNavigation(navigation);
		}
		System.out.println("DangKyClick");
		System.out.println(commu.getNavigation());
		RegisterController registerController = commu.getNavigation().switchTo("Register.fxml");
		registerController.setCommu(commu);
	}

	public void sendLoginMessage(User u) {
		try {
			if (commu.getNavigation()==null) {
				commu.setNavigation(navigation);
			}
			Message msg = new Message("LOGIN", u);
//			commu.setNavigation(navigation);
			commu.sendMessage(msg);
			System.out.println("Gui yeu cau dang nhap");
		} catch (Exception e) {
			e.printStackTrace();
			Util.showError("Không thể kết nối đến server");
		}

	}

	public void closeCommunication() {
		try {
			commu.close();
			System.out.println("Đã đóng kết nối.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public Navigation getNavigation() {
		return navigation;
	}

	public void setNavigation(Navigation navigation) {
		this.navigation = navigation;
	}
	public void closeStage() {
		commu.getNavigation().getStage().setOnCloseRequest((WindowEvent event) -> {
            System.out.println("Stage is closing");
            // Thực hiện thêm hành động nếu cần, như hiển thị thông báo xác nhận
            // event.consume(); // Nếu muốn hủy việc đóng stage
        });
	}

}
