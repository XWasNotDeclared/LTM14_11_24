package control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.util.Duration;
import model.Message;
import model.User;
import model.UserActive;
import utils.Util;

public class RoomController {
	

	
    @FXML
    private ListView<UserActive>  listViewFriend;
    private ObservableList<UserActive> friendList;
    
	private List<User> u;
    private PauseTransition popupDelay;

	public List<User> getU() {
		return u;
	}

	public void setU(List<User> u) {
		this.u = u;
	}

	private Communication commu;

	public Communication getCommu() {
		return commu;
	}

	public void setCommu(Communication commu) {
		this.commu = commu;
	}

	@FXML
	private Button StartButton;

	@FXML
	private Label UserName1, UserName2, UserName3, UserName4;

	@FXML
	private Text score1, score2, score3, score4;
	@FXML
	private Text textCoTam01, textCoTam02, textCoTam03, textCoTam04;
	@FXML
	private ImageView avatar1, avatar2, avatar3, avatar4;
	@FXML
	private ListView<String> chatList;
	@FXML
	private TextField chatTextField;
	@FXML
	private Button sendChatBtn;
	private Popup popup;


	@FXML
	private void showPopup(MouseEvent event) {
		System.out.println("inRoom"+u);
	    if (event.getSource() instanceof ImageView) {
	        ImageView avatar = (ImageView) event.getSource();
	        String avatarId = avatar.getId();
	        int userIndex = Character.getNumericValue(avatarId.charAt(6))-1;
	        System.out.println("avaClick "+ String.valueOf(userIndex));
	        if (userIndex >= 0 && userIndex < u.size()) {
	            User selectedUser = u.get(userIndex);

	            // Đóng popup hiện tại nếu có
	            if (popup != null && popup.isShowing()) {
	                popup.hide();
	            }

	            // Tạo mới popup nếu cần
//	            if (popup == null) {
	                popup = new Popup();
	                VBox vbox = new VBox(10);
		            System.out.println("----------"+selectedUser.getUsername());
	                Button btnFriendRequest = new Button("Kết bạn");
	                btnFriendRequest.setOnAction(e -> sendFriendRequest(selectedUser));

	                Button btnViewInfo = new Button("Xem thông tin");
	                btnViewInfo.setOnAction(e -> viewUserInfo(selectedUser));

	                vbox.getChildren().addAll(btnFriendRequest, btnViewInfo);
	                vbox.setAlignment(Pos.CENTER);
	                vbox.setStyle("-fx-background-color: #fff; -fx-padding: 10px; -fx-border-radius: 5px; -fx-border-color: #ccc;");
	                
	                popup.getContent().add(vbox);
//	            }

	            // Hiển thị popup tại vị trí chuột
	            popup.show(((Node) event.getSource()).getScene().getWindow(), event.getScreenX(), event.getScreenY());

	            // Đóng popup khi click ra ngoài
	            popup.setAutoHide(true);
	        }
	    }
	}

	@FXML
	private void hidePopup(MouseEvent event) {
//	    if (popup != null) {
//	        // Nếu popup đang hiển thị và chuột rời khỏi, chờ 1 giây để ẩn popup
//	        PauseTransition hideDelay = new PauseTransition(Duration.seconds(1));
//	        hideDelay.setOnFinished(e -> {
//	            if (popup != null) {
//	                popup.hide(); // Ẩn popup sau khi hết thời gian 1 giây
//	            }
//	        });
//	        hideDelay.playFromStart(); // Bắt đầu bộ đếm thời gian
//	    }
	}

	
	
	
//    @FXML
//    void sendFriendRequest(ActionEvent event) {
//    	if(u.size()>1) {
//    		User sendUser = new User(null, null);
//    		for (User user: u) {
//    			if (user.getUid() != commu.getCurrentUser().getUid()) {
//    				sendUser = user;
//    				break;
//    			}
//    		}
//    		
//    		
//        	if (sendUser != null) {
//            	try {
//        			commu.sendMessage(new Message("SEND_FRIEND_REQUEST", sendUser));
//        			Util.showError("Đã gửi lời mời tới " + sendUser.getUsername());
//        		} catch (IOException e) {
//        			// TODO Auto-generated catch block
//        			e.printStackTrace();
//        		}
//        	}
//    	}
//
//    }

	void sendFriendRequest(User u) {
		try {
			commu.sendMessage(new Message("SEND_FRIEND_REQUEST", u));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void viewUserInfo(User u) {
		System.out.println("Xem infor" + u);
		try {
			System.out.println("playerInfor" + Integer.valueOf(u.getUid()));
			commu.sendMessage(new Message("VIEW_PLAYER_INFOR", Integer.valueOf(u.getUid())));
			commu.Pre_sence = "Room.fxml";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		PlayerInforController playerInforController = commu.getNavigation().switchTo("PlayerInfor.fxml");
//		playerInforController.setCommu(commu);
//		if (commu.getNavigation().getController("PlayerInfor.fxml")!=null) {
//			PlayerInforController playerInforController = commu.getNavigation().getController("PlayerInfor.fxml");
//			playerInforController.setCommu(commu);
//		}
	}

	private ObservableList<String> chatListObsl;

	public RoomController() {
		super();
		chatListObsl = FXCollections.observableArrayList();
		friendList = FXCollections.observableArrayList();
        // Khởi tạo PauseTransition để trì hoãn đóng popup 3 giây
        popupDelay = new PauseTransition(Duration.seconds(1));
        popupDelay.setOnFinished(event -> {
            if (popup != null) {
                popup.hide();
            }
        });
	}

	@FXML
	void initialize() {
		chatList.setItems(chatListObsl);
     	listViewFriend.setItems(friendList);
     	
     	  // Đăng ký sự kiện nhấp cho mỗi phần tử trong ListView
        listViewFriend.setOnMouseClicked(event -> {
            UserActive selectedUser = listViewFriend.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                invite(selectedUser); // Gọi hàm invite khi một phần tử được nhấp
            }
        });
     	
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
	}
	
	private void invite(UserActive user) {
	    
	    if (user.getStatus().equals("ON")) {
	    	System.out.println("Inviting user: " + user.getUser().getUsername());
	    	try {
				commu.sendMessage(new Message("INVITE_TO_ROOM", user.getUser()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    }
	    else {
	    	Util.showError("Không thể mời, "+user.getUser().getUsername()+" đang " + user.getStatus());
	    }
	    
	    
	}
	
    public void setlistViewFriend(List<UserActive> friendList) {
    	this.friendList.setAll(friendList);
    }
	public void addChatRoomMsg(String chatMsg) {
		chatListObsl.add(0, chatMsg);
	}

	@FXML
	void startButtonClick(ActionEvent event) {
		start();
	}

	@FXML
	void goBackButtunClick(ActionEvent event) {
		leaveRoom();
		HomeController homeController = commu.getNavigation().switchTo("Home.fxml");
		homeController.updateUserInfor(commu.getCurrentUser().getUsername(), commu.getCurrentUser().getScore());
		UserName1.setText("Waiting1");
		UserName2.setText("Waiting2");
		UserName3.setText("Waiting3");
		UserName4.setText("Waiting4");
		homeController.sendReLoadTopPlayerRequest();
		commu.getNavigation().resetScene("Room.fxml");
	}

	public void setUserName1(String userName1) {
		UserName1.setText(userName1);
	}

	public void setUserName2(String userName2) {
		UserName2.setText(userName2);
	}

	private void leaveRoom() {
		try {
			commu.sendMessage(new Message("LEAVE_ROOM", commu.getCurrentUser().getUsername()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setUserText() {
		// Đặt danh sách
		ImageView[] avatars = { avatar1, avatar2, avatar3, avatar4 };
		Text[] coTamTexts = { textCoTam01, textCoTam02, textCoTam03, textCoTam04 };
		Label[] userLabels = { UserName1, UserName2, UserName3, UserName4 };
		Text[] scoreTexts = { score1, score2, score3, score4 };
		System.out.println(u);
		// Hiển thị thông tin cho từng người dùng trong danh sách `u`
		for (int i = 0; i < userLabels.length; i++) {
			if (i < u.size()) {
				User user = u.get(i);
				if (user.getUsername().equals(commu.getCurrentUser().getUsername())) {
					userLabels[i].setStyle("-fx-text-fill: red;"); 
				}else {
					userLabels[i].setStyle("-fx-text-fill: black;"); 
				}
				userLabels[i].setText(user.getUsername());
				scoreTexts[i].setText(String.valueOf(user.getScore()));
				if (user.getAvatar() !=null) {
					setAvatarImage(avatars[i], user.getAvatar());
				}
				// Hiển thị nhãn và điểm của người dùng
				avatars[i].setVisible(true);
				coTamTexts[i].setVisible(true);
				userLabels[i].setVisible(true);
				scoreTexts[i].setVisible(true);
			} else {
				// Ẩn nhãn và điểm nếu không có người dùng
				avatars[i].setVisible(false);
				coTamTexts[i].setVisible(false);
				userLabels[i].setVisible(false);
				scoreTexts[i].setVisible(false);
			}
		}
	}

	public void start() {
		if (u.size() <= 1) {
			Platform.runLater(() -> {
				Util.showError("Phong chua du nguoi");
			});
		} else {
			try {
				commu.sendMessage(new Message("START", commu.getCurrentUser().getUsername()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void setButtonStartText(String txt) {
		StartButton.setText(txt);
	}

	@FXML
	void sendChatBtnClick(ActionEvent event) {
		String chatMessage = commu.getCurrentUser().getUsername() + ": " + chatTextField.getText().trim(); // Lấy và
																											// loại bỏ
																											// khoảng
																											// trắng

		if (!chatMessage.isEmpty()) {
			try {
				// Tạo thông điệp chat để gửi đến server
				Message message = new Message("CHAT_TO_ROOM_MESSAGE", chatMessage);

				// Gửi thông điệp qua lớp Communication
				commu.sendMessage(message);

				// Thêm tin nhắn vào ListView để hiển thị
//                chatList.getItems().add("You: " + chatMessage);

				// Xóa nội dung trong chatTextField sau khi gửi
				chatTextField.clear();
			} catch (IOException e) {
				e.printStackTrace();
				Util.showError("Không thể gửi tin nhắn. Vui lòng thử lại.");
			}
		}
	}
	
	///////////////////////////////////
//	public void setAvatars() {
//	    // Mảng các ImageView tương ứng với các avatar trong giao diện
//	    ImageView[] avatars = { avatar1, avatar2, avatar3, avatar4 };
//	    
//	    // Duyệt qua danh sách người dùng
//	    for (int i = 0; i < u.size(); i++) {
//	        User user = u.get(i);
//	        
//	        // Kiểm tra nếu ảnh của người chơi có tên trong hệ thống
//	        String avatarName = user.getAvatar();
//	        
//	        // Gọi hàm setAvatarImage với tên tệp ảnh để thay đổi ảnh đại diện
//	        setAvatarImage(avatars[i], avatarName);
//	    }
//	}

	private void setAvatarImage(ImageView avatar, String imageName) {
	    // Đường dẫn tuyệt đối từ thư mục gốc của dự án
	    String imagePath = "res/" + imageName + ".jpg";
	    
	    try {
	        // Tạo đối tượng File từ đường dẫn
	        File file = new File(imagePath);
	        
	        // Kiểm tra xem file có tồn tại không
	        if (file.exists()) {
	            // Tạo đối tượng Image từ tệp ảnh
	            Image image = new Image(file.toURI().toString());
	            avatar.setImage(image);  // Thiết lập ảnh vào ImageView
	        } else {
	            System.out.println("Không tìm thấy ảnh: " + imagePath);
	        }
	    } catch (Exception e) {
	        System.out.println("Lỗi khi lấy ảnh: " + e.getMessage());
	    }
	}

	///////////////////////////////////

}
