package a;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import model.MatchHistory;
import model.Message;
import model.Pair;
import model.User;
import model.UserActive;

import java.io.*;
import model.Room;
import model.RoomClient;

public class SocketHandler implements Runnable {
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private volatile boolean running = true;
	private int currentUid = -1;
	private Room room;
	private boolean ready = false;
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCurrentUid() {
		return currentUid;
	}

	public void setCurrentUid(int currentUid) {
		this.currentUid = currentUid;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public User getCurrentUser() {
		DAO dao = new DAO();
		return dao.getUserByID(currentUid);
	}

	public SocketHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		// TODO Auto-generated method stub

		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());

			while (running) {
				Object rcvObj = ois.readObject();
//				Thread.sleep(100);
				System.out.println(rcvObj.getClass().getName());
				// kiem tra doi tuong rcvObj instance of User
				if (rcvObj instanceof Message) {
					Message rcvMsg = (Message) rcvObj; // cast ve Message
					String label = rcvMsg.getLabel();
					System.out.println("lbl:" + label);

					///////////////////////////////////
					switch (label) {
					case "LOGIN":
						User u = loginHandle(rcvMsg);
						if (u != null) {
							currentUid = u.getUid();
							status = "ON";
							sendTopPlayerList();
							sendFriendList();
							sendUpdateStatusToAllFriend(this.getCurrentUser(), "ON");
						}
						break;
					case "REGISTER":
						registerHandle(rcvMsg);
						break;
					case "GET_TOP_PLAYER_LIST":
						sendTopPlayerList();
						break;
					case "GET_FRIEND_LIST":
						sendFriendList();
						break;
					case "CREATE_ROOM":
						status = "BUSY";
						createRoom(rcvMsg);
						updateRoom();
						Server1.broadcastMessage(updateListRoom());
						sendUpdateStatusToAllFriend(this.getCurrentUser(), "BUSY");
						break;
					case "GET_ROOM_LIST":
						sendRoomList(rcvMsg);
						break;
					case "INVITE_TO_ROOM":
						inviteToRoom(rcvMsg);
						break;
					case "INVITE_ROOM_REFUSE":
						inviteToRoomRefuse(rcvMsg);
						break;
					case "JOIN_ROOM":
						status = "BUSY";
						if(joinRoom(rcvMsg)) {
							updateRoom();
							sendUpdateStatusToAllFriend(this.getCurrentUser(), "BUSY");	
						}
						Server1.broadcastMessage(updateListRoom());
						break;
					case "CHAT_TO_ROOM_MESSAGE":
						chatToRoom(rcvMsg);
						break;

					case "LEAVE_ROOM":
						status = "ON";
						leaveRoom(rcvMsg);
						updateRoom();
						Server1.broadcastMessage(updateListRoom());
						sendUpdateStatusToAllFriend(this.getCurrentUser(), "ON");
						break;
					case "UPDATE_ROOM_INFOR":
						updateRoom();
						break;
					case "SEND_FRIEND_REQUEST":
						sendFriendRequestHandle(rcvMsg);
						break;
					case "VIEW_PLAYER_INFOR":
						viewPlayerInfor(rcvMsg);
						break;
					case "FIREND_REQUEST_AGREE":
						friendRequestAgree(rcvMsg);
						sendFriendList();
						sendRsListFriendToAllFriend(currentUid);
						sendUpdateStatusToAllFriend(this.getCurrentUser(), "BUSY");
						break;
					case "FIREND_REQUEST_REFUSE":
						friendRequestRefuse(rcvMsg);
						break;

					case "LOG_OUT":
						logOut(rcvMsg);
						status = "OFF";
						break;
					case "START":
						start(rcvMsg);
						break;
					case "I_WIN":
						winProcess(rcvMsg);
						updateRoom();
						break;
					case "TIME_UP":
						drawProcess(rcvMsg);
						break;

					case "PING":
						sendPong();
						break;
					}
					////////////////////////////////////////
					System.out.println("now onl" + Server1.getUsers());

					/////////////////////////////////////////

				} else {
					System.out.println("Doi tuong nhan duoc khac voi msg");
				}
			}

		} catch (Exception e) {
			System.out.println("Ket noi bi ngat dot ngot");
			e.printStackTrace();
		} finally {
			// Đảm bảo socket được đóng
			try {

				if (room != null) {
					leaveRoom(new Message("holder message", null));
				}
				Server1.removeClient(this);
				Server1.broadcastMessage(updateListRoom());
				oos.close();
				ois.close();
				if (socket != null && !socket.isClosed()) {
					socket.close();
				}

			} catch (IOException e) {
				System.err.println("Lỗi khi đóng socket: " + e.getMessage());
			}
		}

	}

	private void registerHandle(Message msg) throws IOException {
		User rcvUser = (User) msg.getData();
		DAO userDao = new DAO();
		String status = userDao.registerUser(rcvUser);
		if (status == "REGISTER_TRUE") {
			System.out.println("dang ky thanh cong  " + rcvUser.getUsername());
			sendMessage(new Message("REGISTER_TRUE", "OK"));
		} else if (status == "DUPLICATE_USER_REGISTER_FALSE") {
			System.out.println("dang ky that bai,user da ton tai!!!  " + rcvUser.getUsername());
			sendMessage(new Message("DUPLICATE_USER_REGISTER_FALSE", "NOT_OK"));
		} else {
			System.out.println("dang ky that bai,khong ro nguyen nhan  " + rcvUser.getUsername());
			sendMessage(new Message("REGISTER_FALSE", "NOT_OK"));
		}
		System.out.println("Gui phan hoi");
	}

	private User loginHandle(Message msg) throws IOException {
		User rcvUser = (User) msg.getData();
		if (!Server1.getUsers().contains(rcvUser)) {
			DAO userDao = new DAO();
			User user = userDao.Login(rcvUser);
			if (user != null) {
				System.out.println("Login thanh cong  " + rcvUser.getUsername());
				sendMessage(new Message("LOGIN_TRUE", user));
				System.out.println("Gui phan hoi");
				return user;
			} else {
				System.out.println("Login that bai  " + rcvUser.getUsername());
				sendMessage(new Message("LOGIN_FALSE", user));
				System.out.println("Gui phan hoi");
				return null;
			}
		} else {
			System.out.println("Tai khoan da dang nhap  " + rcvUser.getUsername());
			sendMessage(new Message("ALREADY_LOGIN", null));
			System.out.println("Gui phan hoi");
			return null;
		}

	}

	private void sendTopPlayerList() {
		System.out.println("get room list request!!!");
		DAO dao = new DAO();
		List<User> listTopPlayer = dao.getTop10Users();
		try {
			sendMessage(new Message("UPDATE_LIST_TOP_PLAYER", listTopPlayer));
			System.out.println("gui list top player xong!!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendFriendList() {
		System.out.println("get friend list request!!!");
		DAO dao = new DAO();
		List<User> listFriend = dao.getFriendsByUID(this.currentUid);
		List<UserActive> listFriend2 = new ArrayList<UserActive>();
		for (User u : listFriend) {
			String status = "OFF";
			for (SocketHandler sh : Server1.getClients()) {
				if (u.getUid() == sh.getCurrentUid()) {
					status = sh.status;
				}
			}
			listFriend2.add(new UserActive(u, status));
		}

		try {
			sendMessage(new Message("UPDATE_LIST_FRIEND", listFriend2));
			System.out.println(listFriend2);
			System.out.println("gui list friend xong!!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createRoom(Message msg) throws IOException {
		RoomClient newRoomInfor = (RoomClient) msg.getData();
		int numPeople = newRoomInfor.getNumPeople();
		int numSeed = newRoomInfor.getNumSeed();
		int numType = newRoomInfor.getNumType();
		int time_match = newRoomInfor.getTime_match();
		String date_play = Utils.nowTime();

//    	int time_play = ;

		System.out.println(numPeople + " " + numSeed + " " + numType + " " + time_match + " " + date_play);
		this.room = RoomManager.getInstance().createRoom("test", (int) System.currentTimeMillis() % 1000, numPeople,
				numSeed, numType, time_match, date_play);
		room.addParticiant(this);
		room.setRoomOwner_Uid(currentUid);
		ready = true;
		System.out.println("OK" + room.getName());
		System.out.println(RoomManager.getInstance().getActiveRooms());
		Message resMsg = new Message("CREATE_ROOM_OK", Integer.valueOf(room.getID()));
		sendMessage(resMsg);
	}

	public void sendRoomList(Message rcvMsg) {
		System.out.println("get room list request!!!");
		List<RoomClient> listRoom = RoomManager.getInstance().getActiveRoomClients();
		try {
			sendMessage(new Message("LIST_ID_ROOM", listRoom));
			System.out.println("gui list room xong!!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean joinRoom(Message msg) {
		Integer roomID = (Integer) msg.getData();
		this.room = RoomManager.getInstance().getRoomByID(roomID);
		if (room.getNumPeople()-room.getParticipants().size()>0) {
			room.addParticiant(this);
			try {
				System.out.println("send" + room.getUsersInRoom());
				this.sendMessage(new Message("JOIN_ROOM_ACCEPT", room.getUsersInRoom()));
				room.broadcastRoom(
						new Message("RECEIVE_CHAT_IN_ROOM", getCurrentUser().getUsername() + ": Join the game!!!"));
				return true;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			try {
				this.sendMessage(new Message("ROOM_FULL", null));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return false;
	}

	private void leaveRoom(Message msg) {
		String userName = (String) msg.getData();
		room.broadcastRoom(new Message("RECEIVE_CHAT_IN_ROOM", userName + ": Leave the room!!!"));
		if (room != null) {
			this.room.removeParticiant(this);
		}
		ready = false;
	}

	private Message updateListRoom() {
		System.out.println("update room list because comeone leave or create");
		ArrayList<RoomClient> listRoom = RoomManager.getInstance().getActiveRoomClients();
		return new Message("UPDATE_LIST_ROOM", listRoom);
	}

	private void updateRoom() {// co nguoi thoa ra

		try {
			System.out.println("send updateRoom to" + room.getUsersInRoom());
			for (SocketHandler sh : room.getParticipants()) {
				sh.sendMessage(new Message("UPDATE_ROOM", room.getUsersInRoom()));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void logOut(Message msg) {
		User tempUser = this.getCurrentUser();
		if (currentUid != -1) {
			currentUid = -1;
			sendUpdateStatusToAllFriend(tempUser, "OFF");
		}
		try {
			sendMessage(new Message("LOG_OUT_OK", null));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void start(Message msg) {
		if (room.getRoomOwner_Uid() == currentUid) {
			if (room.gameStart(this)) {
				DAO dao = new DAO();
				this.room.setDate_play(Utils.nowTime());
				dao.insertMatch(this.room);
				System.out.println("insert match to db");
				System.out.println("insert user_match to db");
				System.out.println("send start msg to all handler in room");
				Random random = new Random();
				int randomNumber = random.nextInt(1000);
				for (SocketHandler sh : room.getParticipants()) {
					try {
						dao.insertUserMatch(sh.getCurrentUser(), this.room);
						sh.sendMessage(new Message("START_GAME_OK",
								new RoomClient(room.getNumSeed(), room.getNumType(), room.getTime_match(),randomNumber )));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			;
		} else {
			if (ready == false) {
				ready = true;
				String userName = (String) msg.getData();
				room.broadcastRoom(new Message("RECEIVE_CHAT_IN_ROOM", "<SERVER>: " + userName + " IS READY"));
			} else {
				ready = false;
				String userName = (String) msg.getData();
				room.broadcastRoom(new Message("RECEIVE_CHAT_IN_ROOM", "<SERVER>: " + userName + " IS NOT READY"));
			}

			// TODO send ready ve cho client//
		}

	}

	private void winProcess(Message msg) {
		Integer timeRemain = (Integer) msg.getData();
		System.out.println("some one win " + getCurrentUser().getUsername());
		this.room.processWin(this, timeRemain);
		this.room.setGameStarted(false);
	}

	private void drawProcess(Message msg) {
		System.out.println("het gio, hoa " + getCurrentUser().getUsername());
		if (this.room.isGameStarted() == true) {
			this.room.processDraw();
			this.room.setGameStarted(false);
		}

	}

	private void sendFriendRequestHandle(Message msg) {
		User fiendOnRequest = (User) msg.getData();
		DAO dao = new DAO();
		if (dao.isFriendExist(this.getCurrentUser(), fiendOnRequest)) {
			try {
				this.sendMessage(new Message("ALREADY_ARE_FRIEND", fiendOnRequest));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			for (SocketHandler sh : room.getParticipants()) {
				if (sh.getCurrentUid() == fiendOnRequest.getUid()) {
					try {
						sh.sendMessage(new Message("NEW_FRIEND_REQUEST", this.getCurrentUser()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

	private void viewPlayerInfor(Message msg) {
		Integer viewPlayerUid = (Integer) msg.getData();

		DAO dao = new DAO();
		User u = dao.getUserByID(viewPlayerUid);
		List <Integer> winInfor = dao.getWinLoseDraw(viewPlayerUid);
		List<MatchHistory> history = dao.getMatchHistoryByUID(viewPlayerUid);
		try {
			this.sendMessage(new Message("INFOR_WIN", winInfor));
			this.sendMessage(new Message("INFOR_U", u));
			this.sendMessage(new Message("INFOR_HISTORY", history));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void inviteToRoom(Message msg) {
		User getInviteU = (User) msg.getData();
		for (SocketHandler sh : Server1.getClients()) {
			if (sh.getCurrentUid() == getInviteU.getUid()) {
				try {
					sh.sendMessage(new Message("RECIVE_INVITE_ROOM",
							new Pair<String, Integer>(this.getCurrentUser().getUsername(), this.room.getID())));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void inviteToRoomRefuse(Message msg) {
		String nameRefuse = (String) msg.getData();
		for (SocketHandler sh : Server1.getClients()) {
			if (sh.getCurrentUser().getUsername().equals(nameRefuse)) {
				try {
					sh.sendMessage(new Message("RECIVE_INVITE_ROOM_REFUSE", this.getCurrentUser().getUsername()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void friendRequestAgree(Message msg) {
		DAO dao = new DAO();
		User fiendOnRequest = (User) msg.getData();
		if (dao.insertFriend(this.getCurrentUser(), fiendOnRequest)) {
			for (SocketHandler sh : room.getParticipants()) {
				if (sh.getCurrentUid() == fiendOnRequest.getUid()) {
					try {
						sh.sendMessage(new Message("NEW_FRIEND_DONE", null));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			try {
				this.sendMessage(new Message("NEW_FRIEND_DONE", null));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("Loi o phan ket ban");
		}
	}

	private void friendRequestRefuse(Message msg) {
		User fiendOnRequest = (User) msg.getData();
		for (SocketHandler sh : room.getParticipants()) {
			if (sh.getCurrentUid() == fiendOnRequest.getUid()) {
				try {
					sh.sendMessage(new Message("NEW_FRIEND_REFUSE", null));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void chatToRoom(Message msg) {
		String chatMsg = (String) msg.getData();
		this.room.broadcastRoom(new Message("RECEIVE_CHAT_IN_ROOM", chatMsg));
	}

	private void sendRsListFriendToAllFriend(int uid) {
		// to do: send refresh friend list to all user
		System.out.println("send refreah to all  friend onl");
		DAO dao = new DAO();
		List<User> listFriend = dao.getFriendsByUID(uid);
		for (User u : listFriend) {
			for (SocketHandler sh : Server1.getClients()) {
				if (u.getUid() == sh.getCurrentUid()) {
					sh.sendFriendList();
				}
			}
		}

	}

	private void sendUpdateStatusToAllFriend(User userSend, String status) {
		// to do: send refresh friend list to all user
		System.out.println("send update status to all  friend onl");
		DAO dao = new DAO();
		List<User> listFriend = dao.getFriendsByUID(userSend.getUid());
		UserActive userActive = new UserActive(userSend, status);
		Message msg = new Message("FRIEND_UPDATE_STATUS", userActive);
		for (User u : listFriend) {
			for (SocketHandler sh : Server1.getClients()) {
				if (u.getUid() == sh.getCurrentUid()) {
					try {
						sh.sendMessage(msg);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

	public synchronized void sendMessage(Message message) throws IOException {
		oos.writeObject(message);
		oos.flush();
	}

	private void sendPong() throws IOException {
		System.out.println("get PING!!!");
		sendMessage(new Message("PONG", "B"));
		System.out.println("send Pong!!!");
	}

	private void stop() {
		running = false;
		leaveRoom(null);
	}
}
