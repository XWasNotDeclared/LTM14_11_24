package control;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Message;
import model.RoomClient;

public class CreateNewRoomController {
	private Communication commu;

    public Communication getCommu() {
		return commu;
	}


	public void setCommu(Communication commu) {
		this.commu = commu;
	}

	
	
	

//    @FXML
//    private Button createNewRoom;


    @FXML
    private TextField numSeedText;

    @FXML
    private TextField time;
    @FXML
    private ComboBox<String> comboBoxNumPeople;


    @FXML
    public void initialize() {
       
    	comboBoxNumPeople.getItems().addAll("2", "3", "4");
    	comboBoxNumPeople.setValue("4");
    }
    
//    @FXML
//    void creatRoomClick(ActionEvent event) {
//    	
//    	int numPeople = 2;
//    	int numSeed = Integer.valueOf(numSeedText.getText());
//    	int numTypeSeed = 2;
//    	int time_match = Integer.valueOf(time.getText());
//    	String date_play = "temp_fake_day";
//    	
//    	RoomClient newRoom = new RoomClient("name_room_here",numPeople,numSeed,numTypeSeed,time_match,date_play);
//    	
//    	Message msg = new Message("CREATE_ROOM",newRoom);
//    	try {
//			commu.sendMessage(msg);
//			System.out.println("Đã gui thong tin tao phong!!!");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
    @FXML
    void createRoom4Click(ActionEvent event) {
    	int numPeople = Integer.valueOf(comboBoxNumPeople.getValue());
    	int numSeed = Integer.valueOf(numSeedText.getText());
    	int numTypeSeed = 2;
    	int time_match = Integer.valueOf(time.getText());
    	String date_play = "temp_fake_day";
    	RoomClient newRoom = new RoomClient("name_room_here",numPeople,numSeed,numTypeSeed,time_match,date_play);
    	Message msg = new Message("CREATE_ROOM",newRoom);
    	try {
			commu.sendMessage(msg);
			System.out.println("Đã gui thong tin tao phong 4 nguoi!!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    
    @FXML
    void goBackButtonCick(ActionEvent event) {
    	commu.getNavigation().switchTo("Home.fxml");
    	
    	
    	
    }

}