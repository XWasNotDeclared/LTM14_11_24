package a;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import model.Room;
import model.RoomClient;
import model.User;

public class RoomManager {
	private static RoomManager instance;
	private List<Room> rooms;
	
	
	
	
	
	public RoomManager() {
		super();
		this.rooms = new ArrayList<>();
	}

	public static synchronized  RoomManager getInstance() {
		if (instance == null) {
			instance = new RoomManager();
		}
		return instance;
	}
	
	public synchronized Room createRoom (String name, int iD, int numPeople, int numSeed, int numType, int time_match, String date_play) {
		Room room = new Room(name,iD, numPeople, numSeed, numType, time_match,date_play);
		rooms.add(room);
		return room;
	}
	
	public synchronized void removeRoom (Room room) {
		rooms.remove(room);
	}
	public synchronized List<Room> getActiveRooms(){
		return new ArrayList<>(rooms);// tra ve ban sao danh sach phong
	}
	
    // Phương thức chuyển đổi Set<Room> sang List<RoomClient>
    public ArrayList<RoomClient> getActiveRoomClients() {   ///// **************CO THE LOI GI DO O DAY, TAM THOI CHUA THAY :))
        return rooms.stream()
                .map(room -> new RoomClient(
                    room.getName(),
                    room.getID(),
                    room.getNumPeople(),
                    room.getNumSeed(),
                    room.getNumType(),
                    room.getTime_match(),
                    room.getDate_play(),
                    new ArrayList<>(room.getParticipants().stream()
                        .map(SocketHandler::getCurrentUser) // Giả sử bạn muốn thêm người dùng vào RoomClient
                        .collect(Collectors.toList())
                    ))
                )
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
//    public synchronized Room getRoomByRoomClient(RoomClient roomClient) {
//    	 return rooms.stream()
//    		        .filter(room -> room.getID() == roomClient.getID())
//    		        .findFirst()
//    		        .orElse(null); // Trả về null nếu không tìm thấy
//    }
    public synchronized Room getRoomByRoomClient(RoomClient roomClient) {
        for (Room room : rooms) {
            if (room.getID() == roomClient.getID()) {
                return room; // Trả về phòng nếu tìm thấy
            }
        }
        return null; // Trả về null nếu không tìm thấy
    }
    public synchronized Room getRoomByID(Integer roomID) {
        for (Room room : rooms) {
            if (room.getID() == roomID) {
                return room; // Trả về phòng nếu tìm thấy
            }
        }
        return null; // Trả về null nếu không tìm thấy
    }
	
	
	

}
