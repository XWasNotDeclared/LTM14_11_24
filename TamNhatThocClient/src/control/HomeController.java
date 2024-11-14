package control;



import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import model.*;

public class HomeController {
	private Communication commu;
    public Communication getCommu() {
		return commu;
	}
	public void setCommu(Communication commu) {
		this.commu = commu;
	}
	@FXML
    private ComboBox<?> comboBox;

    @FXML
    private ListView<UserActive>  listViewFriend;

    @FXML
    private ListView<User> listViewTopPlayer;

    @FXML
    private Button newRoom;
    @FXML
    private ImageView avatarImg;
    @FXML
    private Text scoreText;

    @FXML
    private Text userNameText;
    @FXML
    private Button logOutButton;
    private ObservableList<User> topPlayerList;
    private ObservableList<UserActive> friendList;

    @FXML
    void newRoomClick(ActionEvent event) {
    	CreateNewRoomController createNewRoomController = commu.getNavigation().switchTo("CreateNewRoom.fxml");
    	createNewRoomController.setCommu(commu);
    }
    @FXML
    void logOutButtonClick(ActionEvent event) {
    	commu.getNavigation().switchTo("Login.fxml");
    	try {
			commu.sendMessage(new Message("LOG_OUT",commu.getCurrentUser()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }   
    @FXML
    void findRoomButtonClick(ActionEvent event) {	
    	try {
			commu.sendMessage(new Message("GET_ROOM_LIST", null));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    
    @FXML
    void reLoadTopPlayer(ActionEvent event) {
    	sendReLoadTopPlayerRequest();
    }
    
    public void sendReLoadTopPlayerRequest() {
    	try {
			commu.sendMessage(new Message("GET_TOP_PLAYER_LIST", null));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @FXML
    void reLoadFriend(ActionEvent event) {
    	sendReLoadFriendRequest();
    }
    
    public void sendReLoadFriendRequest() {
    	try {
			commu.sendMessage(new Message("GET_FRIEND_LIST", null));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    
    public HomeController() {
		super();
		topPlayerList = FXCollections.observableArrayList();
		friendList = FXCollections.observableArrayList();
	}

	@FXML
    void initialize() {
    	listViewTopPlayer.setItems(topPlayerList);
    	 listViewTopPlayer.setCellFactory(lv -> new ListCell<User>() {
    	        @Override
    	        protected void updateItem(User user, boolean empty) {
    	            super.updateItem(user, empty);
    	            if (empty || user == null) {
    	                setText(null);
    	            } else {
    	                // Hiển thị tên và điểm số của người chơi
    	                setText(user.getUsername() + "   ||   " + user.getScore());
    	            }
    	        }
    	    });
     	listViewFriend.setItems(friendList);
     	listViewFriend.setCellFactory(lv -> new ListCell<UserActive>() {
   	        @Override
   	        protected void updateItem(UserActive user, boolean empty) {
   	            super.updateItem(user, empty);
   	            if (empty || user == null) {
   	                setText(null);
   	            } else {
   	                // Hiển thị tên và điểm số của người chơi
   	                setText(user.getUser().getUsername()+" || " + user.getStatus());
   	            }
   	        }
   	    });
     	
        // Thêm sự kiện click vào các mục trong ListView bạn bè
        listViewFriend.setOnMouseClicked(event -> {
            UserActive selectedFriend = listViewFriend.getSelectionModel().getSelectedItem();
            if (selectedFriend != null) {
                // Lấy ID của friend bị click
                int friendId = selectedFriend.getUser().getUid();
                handleFriendClick(friendId); // Gọi phương thức xử lý khi click
            }
        });
     	
//    	///fake data test//
//    	roomList.add("Fake room data 1");
//    	roomList.add("Fake room data 2");
//    	roomList.add("Fake room data 3");
    }
    public void setlistViewTopPlayer(List<User> topPlayerList) {
    	this.topPlayerList.setAll(topPlayerList);
    }
    public void setlistViewFriend(List<UserActive> friendList) {
    	this.friendList.setAll(friendList);
    }
	
	public void updateUserInfor (String userName, float score) {
		userNameText.setText(userName);
		scoreText.setText(String.valueOf(score));
	}
	public void setAvatarImage(String imageName) {
	    // Đường dẫn tuyệt đối từ thư mục gốc của dự án
	    String imagePath = "res/" + imageName+".jpg";

	    try {
	        // Tạo đối tượng File từ đường dẫn
	        File file = new File(imagePath);
	        
	        // Kiểm tra xem file có tồn tại không
	        if (file.exists()) {
	            // Tạo đối tượng Image từ tệp ảnh
	            Image image = new Image(file.toURI().toString());
	            avatarImg.setImage(image);
	        } else {
	            System.out.println("Không tìm thấy ảnh: " + imagePath);
	        }
	    } catch (Exception e) {
	        System.out.println("Lỗi khi lấy ảnh: " + e.getMessage());
	    }
	}

    // Phương thức xử lý khi click vào bạn bè
    private void handleFriendClick(int friendId) {
        System.out.println("ID của bạn bè đã click: " + friendId);
        // Xử lý thêm logic tại đây, ví dụ: gửi yêu cầu xem thông tin bạn bè
        try {
            commu.sendMessage(new Message("VIEW_PLAYER_INFOR", friendId));
            commu.Pre_sence = "Home.fxml";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void clickAvatar(MouseEvent event) {
    	try {
			commu.sendMessage(new Message("VIEW_PLAYER_INFOR", commu.getCurrentUser().getUid()));
			commu.Pre_sence = "Home.fxml";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
