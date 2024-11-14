package control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.application.Platform;
import javafx.stage.Stage;
import model.*;
import utils.*;

public class Communication {
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Socket socket;
	private volatile boolean running = true;
	private Navigation navigation;
	private User currentUser;
	private List<UserActive> listFriend = new ArrayList<UserActive>();
	public String Pre_sence;

	private final BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<Message>();

	public Communication(String ip, int port) throws IOException, ClassNotFoundException {
		this.socket = connectToServer(ip, port);
		this.oos = new ObjectOutputStream(socket.getOutputStream());
		sendMessage(new Message("PING", "a"));
		this.ois = new ObjectInputStream(socket.getInputStream());
		ois.readObject();
	}

	public void startReceiving() {
		new Thread(() -> {
			try {
				while (running) {
					System.out.println("flag1");
					Message message = receiveMessage();
					messageQueue.offer(message);
				}
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
	}

	public void startProcessing() {
		new Thread(() -> {
			while (running) {
				try {
					Message message = messageQueue.take();
					processMessage(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void processMessage(Message message) throws IOException {
		

		
		System.out.println(message.getLabel());
		switch (message.getLabel()) {
		case "LOGIN_TRUE":
			currentUser = (User) message.getData();
			System.out.println(currentUser);
			Platform.runLater(() -> {
				HomeController homeController = navigation.switchTo("Home.fxml");
				homeController.updateUserInfor(currentUser.getUsername(), currentUser.getScore());
				homeController.setAvatarImage(currentUser.getAvatar());
				homeController.setCommu(this);
			});
			break;
		case "LOGIN_FALSE":
			Platform.runLater(() -> {
				Util.showError("Đăng nhập thất bại, sai tên hoặc mật khẩu!");
			});
			break;
		case "ALREADY_LOGIN":
			Platform.runLater(() -> {
				Util.showError("Đăng nhập thất bại, Tai khoan nay da dăng nhap");
			});
			break;
		case "REGISTER_FALSE":
			Platform.runLater(() -> {
				Util.showError("Đăng ký thất bại");
			});
			break;
		case "REGISTER_TRUE":
			Platform.runLater(() -> {
				Util.showError("Đăng ký thành công, hãy quay lại đăng nhập");
			});
			break;
		case "DUPLICATE_USER_REGISTER_FALSE":
			Platform.runLater(() -> {
				Util.showError("Đăng ký thất bại, username đã tồn tại");
			});
			break;
		case "UPDATE_LIST_TOP_PLAYER":
			List<User> listTopPlayer = (ArrayList<User>) message.getData();
			Platform.runLater(() -> {
				if (this.getNavigation().getController("Home.fxml") != null) {
					HomeController homeController = this.getNavigation().getController("Home.fxml");
					homeController.setlistViewTopPlayer(listTopPlayer);
				}
			});
		break;
		case "UPDATE_LIST_FRIEND":
			listFriend = (ArrayList<UserActive>) message.getData();
			Platform.runLater(() -> {
				if (this.getNavigation().getController("Home.fxml") != null) {
					HomeController homeController = this.getNavigation().getController("Home.fxml");
					homeController.setlistViewFriend(listFriend);
				}
			});
			Platform.runLater(() -> {
				if (this.getNavigation().getController("Room.fxml") != null) {
					RoomController roomController = this.getNavigation().getController("Room.fxml");
					roomController.setlistViewFriend(listFriend);
				}
			});
		break;
		
		case "FRIEND_UPDATE_STATUS":
			UserActive friend = (UserActive)message.getData();
			Platform.runLater(() -> {
				if (this.getNavigation().getController("Home.fxml") != null) {
					HomeController homeController = this.getNavigation().getController("Home.fxml");
					for (UserActive ua: listFriend) {
						if (ua.getUser().getUid() == friend.getUser().getUid()) {
							ua.setStatus(friend.getStatus());
						}
					}
					homeController.setlistViewFriend(listFriend);
				}
			});
			Platform.runLater(() -> {
				if (this.getNavigation().getController("Room.fxml") != null) {
					RoomController roomController = this.getNavigation().getController("Room.fxml");
					for (UserActive ua: listFriend) {
						if (ua.getUser().getUid() == friend.getUser().getUid()) {
							ua.setStatus(friend.getStatus());
						}
					}
					roomController.setlistViewFriend(listFriend);
				}
			});
		break;
		
		case "INFOR_WIN":
			List <Integer> winInfor = (ArrayList<Integer>) message.getData();
			System.out.println(winInfor);
			Platform.runLater(() -> {
				PlayerInforController playerInforController = this.getNavigation().switchTo("PlayerInfor.fxml");
				playerInforController.setCommu(this);
				playerInforController.setPieChartData(winInfor.get(0), winInfor.get(1), winInfor.get(2));
//				if (this.getNavigation().getController("Home.fxml") != null) {
//					HomeController homeController = this.getNavigation().getController("Home.fxml");
//					homeController.setlistViewFriend(listFriend);
//				}
			});
		break;
		case "INFOR_HISTORY":
			List<MatchHistory> hisList = (List<MatchHistory>) message.getData();
			Platform.runLater(() -> {
				if (this.getNavigation().getController("PlayerInfor.fxml") != null) {
					PlayerInforController playerInforController = this.getNavigation().getController("PlayerInfor.fxml");
					playerInforController.setListViewData(hisList);
				}
			});
			Platform.runLater(() -> {
				if (this.getNavigation().getController("Room.fxml") != null) {
					RoomController roomController = this.getNavigation().getController("Room.fxml");
					roomController.setlistViewFriend(listFriend);
				}
			});
		break;
		case "INFOR_U":
			User u1 = (User) message.getData();
			Platform.runLater(() -> {
				if (this.getNavigation().getController("PlayerInfor.fxml") != null) {
					PlayerInforController playerInforController = this.getNavigation().getController("PlayerInfor.fxml");
					playerInforController.setPlayerInfo(u1.getUsername(), u1.getScore());
					playerInforController.setAvatarImage(u1.getAvatar());
				}
			});
			Platform.runLater(() -> {
				if (this.getNavigation().getController("Room.fxml") != null) {
					RoomController roomController = this.getNavigation().getController("Room.fxml");
					roomController.setlistViewFriend(listFriend);
				}
			});
		break;
		case "CREATE_ROOM_OK":
			Platform.runLater(() -> {
				Util.showError("tạo thành công, id room: " + String.valueOf(message.getData()));
				RoomController roomController = navigation.switchTo("Room.fxml");
				roomController.setButtonStartText("START!");
				roomController.setU(new ArrayList<User>(Arrays.asList(currentUser)));
				roomController.setCommu(this);
				roomController.setUserText();
				roomController.setlistViewFriend(listFriend);
				
			});
			break;
//
		case "LIST_ID_ROOM":
			List<RoomClient> listRoom = (ArrayList<RoomClient>) message.getData();
			System.out.println(listRoom);
			Platform.runLater(() -> {
				FindRoomController findRoomController = this.getNavigation().switchTo("FindRoom.fxml");
				findRoomController.setRoomList(listRoom);
		    	findRoomController.setCommu(this);
			});
			break;
			
		case "UPDATE_LIST_ROOM":
			List<RoomClient> listRoom2 = (ArrayList<RoomClient>) message.getData();
			Platform.runLater(() -> {
				if (this.getNavigation().getController("FindRoom.fxml") != null) {
					FindRoomController findRoomController = this.getNavigation().getController("FindRoom.fxml");
					findRoomController.setRoomList(listRoom2);
				}
			});
		break;

		case "JOIN_ROOM_ACCEPT":
			ArrayList<User> users = (ArrayList<User>) message.getData();
			System.out.println("setuserFlag" + users);
			Platform.runLater(() -> {
				RoomController roomController = navigation.switchTo("Room.fxml");
				roomController.setButtonStartText("READY");
				roomController.setCommu(this);
				roomController.setU(users);
				roomController.setUserText();
				roomController.setlistViewFriend(listFriend);
			});
			break;
		case "UPDATE_ROOM"://co nguoi roi hoac moi vao
			users = (ArrayList<User>) message.getData();
			Platform.runLater(() -> {
				if (this.getNavigation().getController("Room.fxml") != null) {
					RoomController roomController = this.getNavigation().getController("Room.fxml");
					roomController.setU(users);
					roomController.setUserText();
				}
			});
			break;
		case "RECEIVE_CHAT_IN_ROOM"://nhan duoc chat
			String rcvChat = (String) message.getData();
			Platform.runLater(() -> {
				if (this.getNavigation().getController("Room.fxml") != null) {
					RoomController roomController = this.getNavigation().getController("Room.fxml");
					roomController.addChatRoomMsg(rcvChat);
				}
			});	
		break;
		case "LOG_OUT_OK":
			System.out.println("dang xuat thanh cong "+currentUser);
			break;
		case "START_GAME_OK":
			RoomClient room = (RoomClient) message.getData();
			Platform.runLater(() -> {
				GameController gameController =  this.getNavigation().switchTo("Game.fxml");
				gameController.setCommu(this);
				gameController.createGame(room.getNumSeed(),room.getNumType(),room.getTime_match(),room.getRandomSeed());
				
			});
		break;
		case "NEW_FRIEND_REQUEST":
			User u = (User) message.getData();
			Platform.runLater(() -> {
				 // Danh sách các nhãn nút
		        List<String> options = Arrays.asList("XÁC NHẬN", "TỪ CHỐI");

		        // Gọi phương thức showOptionAlert
		        String selectedOption = Util.showOptionAlert(u.getUsername()+" yêu cầu kết bạn", options);

		        // Xử lý kết quả trả về
		        if (selectedOption != null) {
		            System.out.println("Bạn đã chọn: " + selectedOption);
		            if (selectedOption.equals("XÁC NHẬN")) {
		               // do something
		            	try {
							this.sendMessage(new Message("FIREND_REQUEST_AGREE",u));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }
		            if (selectedOption.equals("TỪ CHỐI")) {
		               // do something
		            	try {
							this.sendMessage(new Message("FIREND_REQUEST_REFUSE",u));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }
		        } else {
		            System.out.println("Không có lựa chọn nào được chọn.");
		        }
			});
		break;
		case "RECIVE_INVITE_ROOM":
			Pair<String, Integer> uidSendAndRoomID = (Pair<String, Integer>) message.getData();
			String nameSender = uidSendAndRoomID.getKey();
			Integer roomID = uidSendAndRoomID.getValue();
			Platform.runLater(() -> {
				 // Danh sách các nhãn nút
		        List<String> options = Arrays.asList("XÁC NHẬN", "TỪ CHỐI");

		        // Gọi phương thức showOptionAlert
		        String selectedOption = Util.showOptionAlert(nameSender+" mời bạn chơi !!!", options);

		        // Xử lý kết quả trả về
		        if (selectedOption != null) {
		            System.out.println("Bạn đã chọn: " + selectedOption);
		            if (selectedOption.equals("XÁC NHẬN")) {
		               // do something
		            	try {
							this.sendMessage(new Message("JOIN_ROOM",roomID));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }
		            if (selectedOption.equals("TỪ CHỐI")) {
		               // do something
		            	try {
							this.sendMessage(new Message("INVITE_ROOM_REFUSE",nameSender));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }
		        } else {
		            System.out.println("Không có lựa chọn nào được chọn.");
		        }
			});
		break;
		case "RECIVE_INVITE_ROOM_REFUSE":
			String nameRefuse = (String) message.getData();
			Platform.runLater(() -> {
				Util.showError(nameRefuse+ " từ chối vào phòng !!!");
			});
			break;
		case "ROOM_FULL":
			Platform.runLater(() -> {
				Util.showError("Phòng đã đầy !!!");
			});
			break;
		case "ALREADY_ARE_FRIEND":
			Platform.runLater(() -> {
				Util.showError("Các bạn đã là bạn bè rồi!");
			});
			break;
		case "NEW_FRIEND_DONE":
			Platform.runLater(() -> {
				Util.showError("Kết bạn thành công!");
			});
			break;
		case "NEW_FRIEND_REFUSE":
			Platform.runLater(() -> {
				Util.showError("Kết bạn thất bại do bị từ chối!");
			});
			break;
		case "YOU_WIN":
			Platform.runLater(() -> {
				GameController controller = this.getNavigation().getController("Game.fxml");
				controller.getCountdownTimeline().stop();
//				if (Util.showConfirmation("YOU WIN! " + this.currentUser.getUsername()) == 1) {
//					this.getNavigation().switchTo("Room.fxml");
//					this.getNavigation().resetScene("Game.fxml");
//					this.currentUser = (User) message.getData();
//				}
				this.currentUser = (User) message.getData();
				WinLoseController winLoseController = this.getNavigation().switchTo("WinLose.fxml");
				winLoseController.setCommu(this);
				winLoseController.setWinLoseText("Bạn thắng, điểm +5");
			});
			break;
			
			
		case "YOU_LOSE":
			Platform.runLater(() -> {
				GameController controller = this.getNavigation().getController("Game.fxml");
				controller.getCountdownTimeline().stop();
				
//				if (Util.showConfirmation("YOU LOSE! " + this.currentUser.getUsername()) == 1) {//chon choi tiep
//					this.getNavigation().switchTo("Room.fxml");
//					this.getNavigation().resetScene("Game.fxml");
//					this.currentUser = (User) message.getData();
//				}
				this.currentUser = (User) message.getData();
				WinLoseController winLoseController = this.getNavigation().switchTo("WinLose.fxml");
				winLoseController.setCommu(this);
				winLoseController.setWinLoseText("Bạn thua, điểm -5");
			});
			break;
		case "DRAW":
			Platform.runLater(() -> {
				GameController controller = this.getNavigation().getController("Game.fxml");
				controller.getCountdownTimeline().stop();
//				if (Util.showConfirmation("Van Dau Hoa " + this.currentUser.getUsername()) == 1) {
//					this.getNavigation().switchTo("Room.fxml");
//					this.getNavigation().resetScene("Game.fxml");
//					this.currentUser = (User) message.getData();
//				}
				this.currentUser = (User) message.getData();
				WinLoseController winLoseController = this.getNavigation().switchTo("WinLose.fxml");
				winLoseController.setCommu(this);
				winLoseController.setWinLoseText("Hòa, điểm +0");
			});
			break;
		case "YOU_ARE_NEW_HOST":
			Platform.runLater(() -> {
				RoomController controller = this.getNavigation().getController("Room.fxml");
				controller.setButtonStartText("START!");
			});
			break;
//			
//			
//
		default:
			Platform.runLater(() -> {
				Util.showError("Lối bất định");
			});

			break;

		}

	}

	public void checkConnection() throws IOException, ClassNotFoundException {
		if (socket.isClosed() || !socket.isConnected()) {
			System.out.println("Socket không còn kết nối!");
		} else {
			long startTime = System.currentTimeMillis();
			try {
				sendMessage(new Message("PING", "a"));
				Message respoMessage = receiveMessage();
				if (respoMessage != null && respoMessage.getLabel().equals("PONG")) {
					long endTime = System.currentTimeMillis();
					System.out.println("Co ket noi");
					System.out.println(endTime - startTime);
				}

			} catch (IOException e) {
				System.out.println("Socket không còn kết nối!(K ping dc)");
				close();
			}
		}
	}

//	private void checkConnectionLoop() throws IOException, ClassNotFoundException {
//
//		while (running) {
//			checkConnection();
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				Thread.currentThread().interrupt();
//				break;
//			}
//		}
//	}

	public synchronized void sendMessage(Message message) throws IOException {
		oos.writeObject(message);
		oos.flush();
	}

	public Message receiveMessage() throws IOException, ClassNotFoundException {
		return (Message) ois.readObject();
	}

	
	public void close() throws IOException {

		try {
			this.sendMessage(new Message("LEAVE_ROOM",this.getCurrentUser().getUsername()));
			this.sendMessage(new Message("LOG_OUT",this.getCurrentUser()));
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//    	try {
//			this.sendMessage(new Message("LOG_OUT",this.getCurrentUser()));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		running = false;
		ois.close();
		oos.close();
		socket.close();
	}

	public Socket connectToServer(String host, int port) {

		try {
			Socket socket = new Socket(host, port);
			System.out.println("Ket noi thanh cong!!!");
			return socket;

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("k kn sv");

		return null;
	}

	public Navigation getNavigation() {
		return navigation;
	}

	public void setNavigation(Navigation navigation) {
		this.navigation = navigation;
	}

	public User getCurrentUser() {
		return currentUser;
	}
}
