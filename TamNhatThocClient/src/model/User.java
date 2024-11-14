package model;

import java.io.Serializable;
import java.util.Objects;
/**
 *
 * @author PC
 */
public class User implements Serializable {
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int uid;
    private String username;
    private String password;
    private String avatar;
    private float score;
    private String date_create;
    

    public User(int uid, String username, String password, String avatar, float score, String date_create) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.score = score;
        this.date_create = date_create;
    }

    public User(String username, String password, String avatar, float score) {
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.score = score;
    }

    

    public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
    
    
    

	public User(int uid, String username, String avatar, float score, String date_create) {
		super();
		this.uid = uid;
		this.username = username;
		this.avatar = avatar;
		this.score = score;
		this.date_create = date_create;
	}

	public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getDate_create() {
        return date_create;
    }

    public void setDate_create(String date_create) {
        this.date_create = date_create;
    }
    
    

	@Override
	public int hashCode() {
		return Objects.hash(avatar, date_create, password, score, uid, username);
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals( user.username);
    }

	@Override
	public String toString() {
		return "User [uid=" + uid + ", username=" + username + ", password=" + password + ", avatar=" + avatar
				+ ", score=" + score + ", date_create=" + date_create + "]";
	}
    

    
}
