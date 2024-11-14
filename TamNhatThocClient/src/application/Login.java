package application;

import control.*;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.*;
import model.*;

public class Login  extends Application{
	
	private LoginController loginController;
	
	public void start (String args[]) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		try {
			primaryStage.setTitle("Tấm nhặt thóc");
			Navigation navigation = new Navigation(primaryStage);
			loginController = navigation.switchTo("Login.fxml");
			loginController.setNavigation(navigation);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		super.stop();
		if(loginController != null) {
			loginController.closeCommunication();
			loginController.closeStage();
			
		}
		
	}
	

}
