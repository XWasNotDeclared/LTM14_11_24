package a;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import model.MatchHistory;
import model.Room;
import model.User;

import java.sql.ResultSet;

public class DAO {
	private String db = "tamnhatthoc5";
	

	public User Login(User user) {
		String sql = "SELECT * FROM " + db +".user_tbl WHERE username = ? and pw = ?";
		try (Connection conn = DBConnect.getConnect();

				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int uid = rs.getInt("uid");
				String name = rs.getString("username");
				String avatar = rs.getString("avatar");
				float score = rs.getFloat("score");
				String date_create = rs.getString("date_create");
				return new User(uid, name, avatar, score, date_create);
			} else {
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public User getUserByID(int getThisUid) {
		String sql = "SELECT * FROM " + db +".user_tbl WHERE uid = ?";
		try (Connection conn = DBConnect.getConnect();

				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, getThisUid);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int uid = rs.getInt("uid");
				String name = rs.getString("username");
				String avatar = rs.getString("avatar");
				float score = rs.getFloat("score");
				String date_create = rs.getString("date_create");
				return new User(uid, name, avatar, score, date_create);
			} else {
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

//	public User updateUserInfor(User user) {
//		String sql = "SELECT * FROM " + db +".user_tbl WHERE username = ? and pw = ?";
//		try (Connection conn = DBConnect.getConnect();
//				
//				PreparedStatement stmt = conn.prepareStatement(sql)){
//			stmt.setString(1, user.getUsername());
//			stmt.setString(2, user.getPassword());
//			
//			ResultSet rs = stmt.executeQuery();
//			if (rs.next()) {
//				int uid = rs.getInt("uid");
//				String name = rs.getString("username");
//				String avatar =  rs.getString("avatar");
//				float score = rs.getFloat("score");
//				String date_create = rs.getString("date_create");
//				return new User(uid, name, avatar, score, date_create);
//			}
//			else {
//				return null;
//			}
//			
//			
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}

	public String registerUser(User user) {

		String sql = "SELECT * FROM " + db +".user_tbl WHERE username = ?";

		String sql1 = "INSERT INTO " + db +".user_tbl (username, pw, avatar, score) values (?,?,?,?)";

		try (Connection conn = DBConnect.getConnect();
				PreparedStatement stmt = conn.prepareStatement(sql);
				PreparedStatement stmt1 = conn.prepareStatement(sql1)) {
			stmt.setString(1, user.getUsername());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				System.out.println("Flagres");
				return "DUPLICATE_USER_REGISTER_FALSE";
			}
			stmt1.setString(1, user.getUsername());
			stmt1.setString(2, user.getPassword());
			stmt1.setString(3, user.getAvatar());
			stmt1.setFloat(4, user.getScore());

			stmt1.executeUpdate();

			System.out.println("Add new user success!!!");
			return "REGISTER_TRUE";
		} catch (SQLException e) {
			e.printStackTrace();
			return "REGISTER_FALSE";
		}

	}

//	public User getUserInfor (User user) {
//		String sql = "SELECT * FROM tamnhatthoc1.users WHERE ?";
//		try (Connection conn = DBConnect.getConnect();
//				PreparedStatement stmt = conn.prepareStatement(sql)){
//			stmt.st
//			
//			
//		}catch(SQLException e) {
//			e.printStackTrace();
//		}
//		
//		
//		return user;
//	}

	public List<User> getAllUserInfor() {
		List<User> userList = new ArrayList<>();
		String sql = "SELECT * FROM " + db +".user_tbl";

		try (Connection conn = DBConnect.getConnect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int uid = rs.getInt("uid");
				String name = rs.getString("username");
				String avatar = rs.getString("avatar");
				float score = rs.getFloat("score");
				String date_create = rs.getString("date_create");
				userList.add(new User(uid, name, avatar, score, date_create));

			}
			return userList;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public List<User> getTop10Users() {
		List<User> userList = new ArrayList<>();
		String sql = "SELECT * FROM " + db +".user_tbl ORDER BY score DESC LIMIT 10";

		try (Connection conn = DBConnect.getConnect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int uid = rs.getInt("uid");
				String name = rs.getString("username");
				String avatar = rs.getString("avatar");
				float score = rs.getFloat("score");
				String date_create = rs.getString("date_create");
				userList.add(new User(uid, name, avatar, score, date_create));
			}
			return userList;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public int get_Match_ID(int room_id, String date_play) {
		String sql = "SELECT id FROM " + db +".match_tbl WHERE room_id = ? and date_play = ?";

		try (Connection conn = DBConnect.getConnect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, room_id);
			stmt.setString(2, date_play);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("id");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

//	public int get_user_match_ID(int room_id, String date_play, User user) {
//		
//		int match_id = get_Match_ID(room_id, date_play);
//		
//		String sql = "SELECT id FROM " + db +".user_match  WHERE match_tblid = ? and user_tbluid = ?";
//		
//		try (Connection conn = DBConnect.getConnect();
//				PreparedStatement stmt = conn.prepareStatement(sql)){
//			stmt.setInt(1,match_id);
//			stmt.setInt(2,user.getUid());
//			ResultSet rs = stmt.executeQuery();
//			if (rs.next()) {
//				return rs.getInt("id");
//			}
//			
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return -1;
//	}
	public List<User> getFriendsByUID(int uid) {
	    List<User> friendList = new ArrayList<>();
	    String sql = "SELECT u.* " +
	                 "FROM " + db +".user_tbl u " +
	                 "JOIN " + db +".friend_tbl f ON (u.uid = f.uid2 OR u.uid = f.uid) " +
	                 "WHERE (f.uid = ? OR f.uid2 = ?) AND u.uid != ?";

	    try (Connection conn = DBConnect.getConnect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, uid);
	        stmt.setInt(2, uid);
	        stmt.setInt(3, uid);

	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            int friendUid = rs.getInt("uid");
	            String name = rs.getString("username");
	            String avatar = rs.getString("avatar");
	            float score = rs.getFloat("score");
	            String date_create = rs.getString("date_create");

	            // Thêm đối tượng User vào danh sách bạn bè
	            friendList.add(new User(friendUid, name, avatar, score, date_create));
	        }

	        return friendList;

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return null; // Trả về null nếu có lỗi xảy ra
	}

	public void insertMatch(Room room) {
		String sql = "INSERT INTO " + db +".match_tbl (num_seed, num_type, time_match, date_play, room_id) values(?,?,?,?,?)";

		try (Connection conn = DBConnect.getConnect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, room.getNumSeed());
			stmt.setInt(2, room.getNumType());
			stmt.setInt(3, room.getTime_match());
			stmt.setString(4, room.getDate_play());
			stmt.setInt(5, room.getID());
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertUserMatch(User user, Room room) {
		String sql = "INSERT INTO " + db +".user_match (time_win, match_tblid, user_tbluid) values(?,?,?)";

		try (Connection conn = DBConnect.getConnect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, -2);// coi nhu mac dinh -2 la thoat khoi phong
			//, thang la so giay win, thua la -1, Hòa là -3
			// Luc thoat khoi can xu ly
			stmt.setInt(2, get_Match_ID(room.getID(), room.getDate_play()));
			stmt.setInt(3, user.getUid());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public boolean isFriendExist(User user1, User user2) {
	    String checkSql = "SELECT * FROM " + db +".friend_tbl WHERE (uid = ? AND uid2 = ?) OR (uid = ? AND uid2 = ?)";
	    try (Connection conn = DBConnect.getConnect();
	         PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

	        checkStmt.setInt(1, user1.getUid());
	        checkStmt.setInt(2, user2.getUid());
	        checkStmt.setInt(3, user2.getUid());
	        checkStmt.setInt(4, user1.getUid());

	        ResultSet rs = checkStmt.executeQuery();
	        return rs.next(); // Trả về true nếu có bản ghi tồn tại, false nếu không
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	public boolean insertFriend(User user1, User user2) {
		String sql = "INSERT INTO " + db +".friend_tbl (uid, uid2) values(?,?)";
		try (Connection conn = DBConnect.getConnect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, user1.getUid());
			stmt.setInt(2, user2.getUid());
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
	}

	public void updateUserMatch(User user, Room room, int time_win) {// time_win -2: thoat, -1: thua, >0: thang
		String sql = "UPDATE " + db +".user_match SET time_win = ? where match_tblid = ? and user_tbluid = ?";
		try (Connection conn = DBConnect.getConnect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, time_win);// coi nhu mac dinh -2 la thoat khoi phong, thang la so giay win, thua la -1.
										// Luc thoat khoi can xu ly
			stmt.setInt(2, get_Match_ID(room.getID(), room.getDate_play()));
			stmt.setInt(3, user.getUid());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateUserScore(User user, float newScore) {
		String sql = "UPDATE " + db +".user_tbl SET score = ? where uid = ?";
		try (Connection conn = DBConnect.getConnect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setFloat(1, newScore);// coi nhu mac dinh -2 la thoat khoi phong, thang la so giay win, thua la -1.
										// Luc thoat khoi can xu ly
			stmt.setInt(2, user.getUid());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Integer> getWinLoseDraw(int uid) {
	    List<Integer> matchResults = new ArrayList<>();
	    int wins = 0, losses = 0, draws = 0;

	    String sql = "SELECT time_win FROM " + db +".user_match WHERE user_tbluid = ?";

	    try (Connection conn = DBConnect.getConnect(); 
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, uid);
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            int timeWin = rs.getInt("time_win");
	            if (timeWin > 0) {
	                wins++;     // Thắng
	            } else if (timeWin == -1) {
	                losses++;   // Thua
	            } else if (timeWin == -3) {
	                draws++;    // Hòa
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    // Thêm các giá trị vào danh sách theo thứ tự thắng, thua, hòa
	    matchResults.add(wins);
	    matchResults.add(losses);
	    matchResults.add(draws);

	    return matchResults;
	}

	public List<MatchHistory> getMatchHistoryByUID(int uid) {
	    List<MatchHistory> matchHistoryList = new ArrayList<>();

	    String sql = "SELECT m.id AS match_id, m.num_seed, m.num_type, m.time_match, m.date_play, m.room_id, "
	               + "um.time_win "
	               + "FROM " + db +".match_tbl m "
	               + "JOIN " + db +".user_match um ON m.id = um.match_tblid "
	               + "WHERE um.user_tbluid = ?";

	    try (Connection conn = DBConnect.getConnect(); 
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, uid);
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            int matchId = rs.getInt("match_id");
	            int numSeed = rs.getInt("num_seed");
	            int numType = rs.getInt("num_type");
	            int timeMatch = rs.getInt("time_match");
	            String datePlay = rs.getString("date_play");
	            String roomId = rs.getString("room_id");
	            int timeWin = rs.getInt("time_win");

	            // Tạo một đối tượng MatchHistory với các thông tin đã lấy từ CSDL
	            MatchHistory matchHistory = new MatchHistory(matchId, numSeed, numType, timeMatch, datePlay, roomId, timeWin);
	            matchHistoryList.add(matchHistory);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return matchHistoryList;
	}
	
	

}
