package model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class RoomClient implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private int ID;
	private int numPeople;
	private int numSeed;
	private int numType;
	private int time_match;
	private int time_win;
	private String date_play;
	private long randomSeed;
	public long getRandomSeed() {
		return randomSeed;
	} 
	public void setRandomSeed(long randomSeed) {
		this.randomSeed = randomSeed;
	}

	private ArrayList<User> users;
	
	

	

	public RoomClient(int numSeed, int numType, int time_match, long randomSeed) {
		super();
		this.numSeed = numSeed;
		this.numType = numType;
		this.time_match = time_match;
		this.randomSeed = randomSeed;
	}
	public RoomClient() {
		super();
		this.users = new ArrayList<>();
	}
	public RoomClient(String name, int iD, int numPeople, int numSeed, int numType) {
		super();
		this.name = name;
		ID = iD;
		this.numPeople = numPeople;
		this.numSeed = numSeed;
		this.numType = numType;
		this.users = new ArrayList<>();
	}
	public RoomClient(String name, int iD, int numPeople, int numSeed, int numType, ArrayList<User> users) {
		super();
		this.name = name;
		ID = iD;
		this.numPeople = numPeople;
		this.numSeed = numSeed;
		this.numType = numType;
		this.users = users;
		this.users = new ArrayList<>();
	}
	
	
	
	
	
	
	public RoomClient(String name, int numPeople, int numSeed, int numType, int time_match,String date_play) {
		super();
		this.name = name;
		this.numPeople = numPeople;
		this.numSeed = numSeed;
		this.numType = numType;
		this.time_match = time_match;
		this.date_play = date_play;
		this.users = new ArrayList<>();
	}
	
	
	public RoomClient(String name, int iD, int numPeople, int numSeed, int numType, int time_match, String date_play) {
		super();
		this.name = name;
		this.ID = iD;
		this.numPeople = numPeople;
		this.numSeed = numSeed;
		this.numType = numType;
		this.time_match = time_match;
		this.date_play = date_play;
		this.users = new ArrayList<>();
	}
	
	
	
	
	
	public RoomClient(String name, int iD, int numPeople, int numSeed, int numType, int time_match, int time_win,
			String date_play, ArrayList<User> users) {
		super();
		this.name = name;
		ID = iD;
		this.numPeople = numPeople;
		this.numSeed = numSeed;
		this.numType = numType;
		this.time_match = time_match;
		this.time_win = time_win;
		this.date_play = date_play;
		this.users = users;
	}
	
	
	
	
	
	
	
	
	
	public RoomClient(String name, int iD, int numPeople, int numSeed, int numType, int time_match, String date_play,
			ArrayList<User> users) {
		super();
		this.name = name;
		ID = iD;
		this.numPeople = numPeople;
		this.numSeed = numSeed;
		this.numType = numType;
		this.time_match = time_match;
		this.date_play = date_play;
		this.users = users;
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
	public void setID(int iD) {
		ID = iD;
	}
	public void addUser(User user) {
		users.add(user);
	}
	
	public void removeUser(User user) {
		users.remove(user);
	}
	
	
	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public int getID() {
		return ID;
	}






	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
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

	@Override
	public String toString() {
		return "RoomClient [name=" + name + ", ID=" + ID + ", numPeople=" + numPeople + ", numSeed=" + numSeed
				+ ", numType=" + numType + ", users=" + users + "]";
	}





	

}
