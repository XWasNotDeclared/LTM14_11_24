package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import a.DAO;
import a.RoomManager;
import a.SocketHandler;

public class Room {

	private String name;
	private int ID;
	private int numPeople;
	private int numSeed;
	private int numType;
	private int time_match;
	private int time_win;
	private String date_play;

	private int roomOwner_Uid;

	private List<SocketHandler> participants;
	private boolean gameStarted = false;

	public int getRoomOwner_Uid() {
		return roomOwner_Uid;
	}

	public void setRoomOwner_Uid(int roomOwner_Uid) {
		this.roomOwner_Uid = roomOwner_Uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getNumPeople() {
		return numPeople;
	}

	public void setNumPeople(int numPeople) {
		this.numPeople = numPeople;
	}

	public int getNumSeed() {
		return numSeed;
	}

	public void setNumSeed(int numSeed) {
		this.numSeed = numSeed;
	}

	public int getNumType() {
		return numType;
	}

	public void setNumType(int numType) {
		this.numType = numType;
	}

	public int getTime_match() {
		return time_match;
	}

	public void setTime_match(int time_match) {
		this.time_match = time_match;
	}

	public int getTime_win() {
		return time_win;
	}

	public void setTime_win(int time_win) {
		this.time_win = time_win;
	}

	public String getDate_play() {
		return date_play;
	}

	public void setDate_play(String date_play) {
		this.date_play = date_play;
	}

	public void setParticipants(List<SocketHandler> participants) {
		this.participants = participants;
	}

	public Room(String name, int iD, int numPeople, int numSeed, int numType, int time_match, String date_play) {
		super();
		this.name = name;
		ID = iD;
		this.numPeople = numPeople;
		this.numSeed = numSeed;
		this.numType = numType;
		this.time_match = time_match;
		this.date_play = date_play;
		this.participants = new ArrayList<SocketHandler>();
	}

//	public Room(String name, int iD, int numPeople, int numSeed, int numType, int time_match, String date_play) {
//		this.participants = new ArrayList<SocketHandler>();
//	}

	public synchronized void addParticiant(SocketHandler handler) {
		participants.add(handler);
//		System.out.println("flagadd"+handler.getCurrentUser());
	}

	public synchronized void removeParticiant(SocketHandler handler) {
		System.out.println(handler.getCurrentUser());
		participants.remove(handler);

		if (participants.isEmpty()) {
			RoomManager.getInstance().removeRoom(this);
		} else {
			if (handler.getCurrentUid() == roomOwner_Uid) {// set chu phong moi
				SocketHandler newRoomOwner = participants.get(0);
				roomOwner_Uid = newRoomOwner.getCurrentUid();
				newRoomOwner.setReady(true);
				broadcastRoom(new Message("RECEIVE_CHAT_IN_ROOM",
						"<SERVER>: NOW \n" + newRoomOwner.getCurrentUser().getUsername() + " IS A NEW OWNER ROOM"));
				try {
					newRoomOwner.sendMessage(new Message("YOU_ARE_NEW_HOST", null));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	public synchronized List<SocketHandler> getParticipants() {
		return participants;
	}

	public synchronized boolean gameStart(SocketHandler handler) {
		if (!gameStarted && allUserReady()) {
			gameStarted = true;
			System.out.println(handler.getCurrentUser() + " start the game!!!");
			return true;
		} else {
			System.out.println("co nguoi chua san sang");
			return false;
		}
	}

	public synchronized void processWin(SocketHandler winnersh, Integer timeRemain) {
		for (SocketHandler sh : participants) {
			User u = sh.getCurrentUser();
			if (sh.getCurrentUid() != roomOwner_Uid) {
				sh.setReady(false);
			}
			if (sh == winnersh) {
				System.out.println("CO NHEEEEEEEEEEEE");
				broadcastRoom(new Message("RECEIVE_CHAT_IN_ROOM",
						"<SERVER>: " + sh.getCurrentUser().getUsername() + " IS WINNER!!!"));
				try {
					DAO dao = new DAO();
					dao.updateUserMatch(u, this, timeRemain);
					dao.updateUserScore(u, u.getScore() + 5);// thang +5
					u.setScore(u.getScore() + 5);
					System.out.println(u.getScore());
					sh.sendMessage(new Message("YOU_WIN", u));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					DAO dao = new DAO();
					dao.updateUserMatch(u, this, -1);
					dao.updateUserScore(u, u.getScore() - 5);// thua -5
					u.setScore(u.getScore() - 5);
					sh.sendMessage(new Message("YOU_LOSE", u));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("FLAGG" + this.getUsersInRoom());
	}

	public synchronized void processDraw() {
		broadcastRoom(new Message("RECEIVE_CHAT_IN_ROOM", "<SERVER>: " + "DRAW"));
		for (SocketHandler sh : participants) {
			User u = sh.getCurrentUser();
			if (sh.getCurrentUid() != roomOwner_Uid) {
				sh.setReady(false);
			}
			try {
				DAO dao = new DAO();
				dao.updateUserMatch(u, this, -3);
				dao.updateUserScore(u, u.getScore());// Hoa +0
				u.setScore(u.getScore());
				System.out.println(u.getScore());
				sh.sendMessage(new Message("DRAW", u));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("FLAGG" + this.getUsersInRoom());
	}

	public synchronized boolean isGameStarted() {
		return gameStarted;
	}

	public synchronized void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}

	public ArrayList<User> getUsersInRoom() {

		ArrayList<User> users = new ArrayList<User>();
		for (SocketHandler sh : participants) {
			User u = sh.getCurrentUser();
			if (u != null) {
				users.add(u);
			}
		}
		return users;
	}

	public boolean allUserReady() {
		for (SocketHandler sh : participants) {
			if (!sh.isReady()) {
				broadcastRoom(new Message("RECEIVE_CHAT_IN_ROOM",
						"<SERVER>: CAN NOT START \n" + sh.getCurrentUser().getUsername() + " IS NOT READY"));
				return false;
			}
		}

		return true;
	}

	public void broadcastRoom(Message msg) {
		for (SocketHandler sh : participants) {
			try {
				sh.sendMessage(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
